package com.example.weight_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// imports for SMS
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    // UI Components
    private TextView tvGoalWeight, tvCurrentWeight, tvDate;
    private RecyclerView rvWeightHistory;
    private ArrayList<WeightData> weightList;
    private WeightAdapter adapter;

    // User + Data
    private double goalWeight = 0.0;
    private double currentWeight = 0.0;
    private static final int SMS_PERMISSION_CODE = 101;
    private String username; //Store the current user’s username
    private DatabaseHelper dbHelper; //initialize dbHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get username passed from LoginActivity
        username = getIntent().getStringExtra("username");
        if (username == null) {
            Toast.makeText(this, "No user info received!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize DB
        dbHelper = new DatabaseHelper(this);

        // Initialize UI components FIRST before using them
        tvGoalWeight = findViewById(R.id.tvGoalWeight);
        tvCurrentWeight = findViewById(R.id.tvCurrentWeight);
        tvDate = findViewById(R.id.tvDate);
        rvWeightHistory = findViewById(R.id.rvWeightHistory);

        // Load goal weight from database
        goalWeight = dbHelper.getGoalWeight(username);
        tvGoalWeight.setText("Goal Weight: " + goalWeight + " lbs");

        // Check SMS permission
        checkSmsPermission();

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set up weight history list
        weightList = dbHelper.getWeightHistoryForUser(username);
        adapter = new WeightAdapter(weightList, data -> showWeightOptionsDialog(data));
        rvWeightHistory.setLayoutManager(new LinearLayoutManager(this));
        rvWeightHistory.setAdapter(adapter);

        // Goal weight click listener
        LinearLayout gridGoalWeight = findViewById(R.id.gridGoalWeight);
        gridGoalWeight.setOnClickListener(v -> updateGoalWeight());

        // Current weight click listener
        LinearLayout gridCurrentWeight = findViewById(R.id.gridCurrentWeight);
        gridCurrentWeight.setOnClickListener(v -> updateCurrentWeight());
    }


    // Step 1: SMS permission logic
    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            showSmsPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSmsPermissionGranted();
            } else {
                showSmsPermissionDenied();
            }
        }
    }

    private void showSmsPermissionGranted() {
        Toast.makeText(this, "SMS permission granted. You will receive notifications.", Toast.LENGTH_SHORT).show();
    }

    private void showSmsPermissionDenied() {
        Toast.makeText(this, "SMS permission denied. Notifications will not be sent.", Toast.LENGTH_SHORT).show();
    }

    // Step 2: Update Goal Weight
    private void updateGoalWeight() {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Set Goal Weight")
                .setMessage("Enter your goal weight:")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    goalWeight = Double.parseDouble(input.getText().toString());
                    tvGoalWeight.setText("Goal Weight: " + goalWeight + " lbs");
                    dbHelper.saveGoalWeight(username, goalWeight);
                    Toast.makeText(this, "Goal weight updated!", Toast.LENGTH_SHORT).show();


                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Step 3: Update Current Weight
    private void updateCurrentWeight() {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Set Current Weight")
                .setMessage("Enter your current weight:")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    currentWeight = Double.parseDouble(input.getText().toString());
                    //grab the date from the device for the entry
                    String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                    tvCurrentWeight.setText("Current Weight: " + currentWeight + " lbs");
                    tvDate.setText("Date: " + date);

                    //Save this to the DB
                    dbHelper.insertWeightEntry(username, currentWeight, date);

                    // Add to weight history
                    weightList.add(0, new WeightData(String.valueOf(currentWeight), date));
                    adapter.notifyDataSetChanged();

                    // Check goal achievement
                    if (currentWeight <= goalWeight) {
                        Toast.makeText(this, "Congratulations! You have reached your goal weight!", Toast.LENGTH_LONG).show();
                    }


                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showWeightOptionsDialog(WeightData data) {
        String[] options = {"Edit", "Delete"};

        new AlertDialog.Builder(this)
                .setTitle("Select Action")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditDialog(data);
                    } else if (which == 1) {
                        dbHelper.deleteWeightEntry(data.getId());
                        weightList.remove(data);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
    private void showEditDialog(WeightData data) {
        final EditText input = new EditText(this);
        input.setText(data.getWeight());

        new AlertDialog.Builder(this)
                .setTitle("Edit Weight Entry")
                .setMessage("Enter new weight:")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newWeight = input.getText().toString();
                    String newDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

                    data.setWeight(newWeight);
                    data.setDate(newDate);

                    dbHelper.updateWeightEntry(data.getId(), Double.parseDouble(newWeight), newDate);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Entry updated!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
