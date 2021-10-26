package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Credentials;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button signup;
    private DatabaseHelper myDB;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        myDB = new DatabaseHelper(this);
        loginUser();

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
//                String strUsername = credentials.getString("Username", null);
//                String strPassword = credentials.getString("Password", null);
//                String userName = username.getText().toString();
//                String passWord = password.getText().toString();
//
//                if(strUsername != null && userName != null && strUsername.equals(userName)) {
//                    if(strPassword != null && passWord != null && strPassword.equals(passWord)) {
//                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
//                        startActivity(intent);
//                    }
//                    else {
//                        username.getText().clear();
//                        password.getText().clear();
//                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else {
//                    username.getText().clear();
//                    password.getText().clear();
//                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void loginUser(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean var = myDB.checkUser(username.getText().toString(), password.getText().toString());
                if(var){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    username.getText().clear();
                    password.getText().clear();
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}