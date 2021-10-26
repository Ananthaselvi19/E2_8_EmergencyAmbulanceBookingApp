package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DloginPageActivity extends AppCompatActivity {
    EditText Username;
    EditText Password;
    Button Login, Signup;
    private DatabaseHelper myDB;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlogin_page);

        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.login);
        Signup = findViewById(R.id.signup);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DloginPageActivity.this, DsignupPageActivity.class);
                startActivity(intent);
            }
        });

        myDB = new DatabaseHelper(this);
        loginUser();

    }

    private void loginUser(){
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean var = myDB.checkUser(Username.getText().toString(), Password.getText().toString());
                if(var){
                    Toast.makeText(DloginPageActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DloginPageActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Username.getText().clear();
                    Password.getText().clear();
                    Toast.makeText(DloginPageActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
