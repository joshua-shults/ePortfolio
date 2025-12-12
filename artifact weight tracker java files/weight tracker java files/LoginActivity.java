package com.example.weight_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //remove the stupid action bar that keeps appearing
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //initialize the database for users
        db = new DatabaseHelper(this);

        // UI Components
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        TextView tvStatus = findViewById(R.id.tvStatus);

        // Handle Login Button Click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Temporary login logic (for testing)
                if (db.checkUser(username, password)){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    //send the username to HomeActivity to be saved in db
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("username", username); //attatched logged-in username to be saved there
                    startActivity(intent);
                    finish();
                } else{
                    tvStatus.setText("Invalid username or password");
                }
            }
        });

        // Handle Create Account Button Click
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter both a username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (db.checkUsernameExists(username)){
                    Toast.makeText(LoginActivity.this, "That username already exists", Toast.LENGTH_SHORT).show();
                } else{
                    boolean insertSuccess = db.insertUser(username, password);
                    if (insertSuccess){
                        Toast.makeText(LoginActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(LoginActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
