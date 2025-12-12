package com.example.weight_tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private String username;        // Store the current user’s username
    private DatabaseHelper dbHelper; // initialize dbHelper

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

        // Stats button click listener
        Button btnStats = findViewById(R.id.btnStats);
        if (btnStats != null) { // in case it's not in this layout yet
            btnStats.setOnClickListener(v -> showStatsDialog());
        }
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
                    // grab the date from the device for the entry
                    String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                    tvCurrentWeight.setText("Current Weight: " + currentWeight + " lbs");
                    tvDate.setText("Date: " + date);

                    // Save this to the DB
                    dbHelper.insertWeightEntry(username, currentWeight, date);

                    // Add to weight history (id is assigned by DB, so 0 is placeholder here)
                    weightList.add(0, new WeightData(0, String.valueOf(currentWeight), date));
                    adapter.notifyDataSetChanged();

                    // Check goal achievement
                    if (goalWeight > 0 && currentWeight <= goalWeight) {
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

    // ------------------ STATS / ALGORITHMS ------------------

    private void showStatsDialog() {
        if (weightList == null || weightList.isEmpty()) {
            Toast.makeText(this, "No weight entries to analyze yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<WeightEntryCalc> calcList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        // Build calculation list from WeightData
        for (WeightData data : weightList) {
            try {
                String wStr = data.getWeight();
                if (wStr == null || wStr.trim().isEmpty()) continue;

                double w = Double.parseDouble(wStr);
                String dateStr = data.getDate();
                if (dateStr == null || dateStr.trim().isEmpty()) continue;

                Date d = sdf.parse(dateStr);
                if (d != null) {
                    calcList.add(new WeightEntryCalc(d, w));
                }
            } catch (Exception e) {
                // Skip malformed rows
            }
        }

        if (calcList.isEmpty()) {
            Toast.makeText(this, "No valid weight data to analyze.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sort by date (oldest → newest)
        Collections.sort(calcList, new Comparator<WeightEntryCalc>() {
            @Override
            public int compare(WeightEntryCalc o1, WeightEntryCalc o2) {
                return o1.date.compareTo(o2.date);
            }
        });

        int n = calcList.size();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0.0;

        for (WeightEntryCalc e : calcList) {
            min = Math.min(min, e.weight);
            max = Math.max(max, e.weight);
            sum += e.weight;
        }

        double avg = sum / n;

        // Trend over most recent entries
        int window = Math.min(6, n);          // up to last 6 entries
        int startIdx = n - window;
        int half = window / 2;

        double firstHalfSum = 0.0;
        double secondHalfSum = 0.0;

        for (int i = 0; i < window; i++) {
            double w = calcList.get(startIdx + i).weight;
            if (i < half) {
                firstHalfSum += w;
            } else {
                secondHalfSum += w;
            }
        }

        double firstAvg = firstHalfSum / Math.max(half, 1);
        double secondAvg = secondHalfSum / Math.max(window - half, 1);

        String trend;
        if (secondAvg > firstAvg + 0.5) {
            trend = "Upward";
        } else if (secondAvg < firstAvg - 0.5) {
            trend = "Downward";
        } else {
            trend = "Stable";
        }

        // Logging streak: consecutive days ending at most recent date
        int streak = 1;
        Date lastDate = calcList.get(n - 1).date;
        for (int i = n - 2; i >= 0; i--) {
            Date d = calcList.get(i).date;
            long diffMs = lastDate.getTime() - d.getTime();
            long days = diffMs / (1000L * 60L * 60L * 24L);

            if (days == 1) {
                streak++;
                lastDate = d;
            } else if (days == 0) {
                // multiple entries in the same day → ignore for streak break
                continue;
            } else {
                break;
            }
        }

        String message =
                "Entries analyzed: " + n +
                        "\n\nMin weight: " + String.format(Locale.getDefault(), "%.1f", min) + " lbs" +
                        "\nMax weight: " + String.format(Locale.getDefault(), "%.1f", max) + " lbs" +
                        "\nAverage weight: " + String.format(Locale.getDefault(), "%.1f", avg) + " lbs" +
                        "\nTrend (recent): " + trend +
                        "\nLogging streak: " + streak + " day(s)";

        new AlertDialog.Builder(this)
                .setTitle("Weight Statistics")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Helper class used only for calculations
    private static class WeightEntryCalc {
        Date date;
        double weight;

        WeightEntryCalc(Date date, double weight) {
            this.date = date;
            this.weight = weight;
        }
    }
}
