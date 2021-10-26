package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DsignupPageActivity extends AppCompatActivity {

    EditText Username, Email, Contact, Password,ConfirmPassword;
    Button Signup;
    private DatabaseHelper myDB;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsignup_page);

        Username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        Contact = findViewById(R.id.contact);
        Password = findViewById(R.id.password);
        ConfirmPassword = findViewById(R.id.confirm_password);
        Signup = findViewById(R.id.signup);

        myDB = new DatabaseHelper(this);
        insertUser();


    }

    private void insertUser(){
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Username.getText().toString();
                String email = Email.getText().toString();
                String contact = Contact.getText().toString();
                String password = Password.getText().toString();
                String confirm_password = ConfirmPassword.getText().toString();

                boolean check = validateInfo(username,password,email,contact);

                if(check == true){
                    if(Password != null && confirm_password != null && Password.equals(confirm_password)) {
                        boolean var = myDB.registerUser(Username.getText().toString(), Password.getText().toString(), Contact.getText().toString());
                        if(var){
                            Toast.makeText(DsignupPageActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(DsignupPageActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                        DsignupPageActivity.this.finish();
                    }
                    else if(Password != ConfirmPassword) {
                        Toast.makeText(DsignupPageActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean validateInfo(String username, String password, String email, String contact) {
                if (username.length() == 0) {
                    Username.requestFocus();
                    Username.setError("Username cannot be empty");
                    return false;
                } else if (!username.matches("[a-zA-Z]{5,}+")) {
                    Username.requestFocus();
                    Username.setError("Enter only alphabetical letters and 5 or more characters");
                    return false;
                } else if (email.length() == 0) {
                    Email.requestFocus();
                    Email.setError("Contact number cannot be empty");
                    return false;
                } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    Email.requestFocus();
                    Email.setError("Enter valid email id");
                    return false;
                } else if (contact.length() == 0) {
                    Contact.requestFocus();
                    Contact.setError("Contact number cannot be empty");
                    return false;
                } else if (!contact.matches("[0-9]{10}")) {
                    Contact.requestFocus();
                    Contact.setError("Enter valid mobile number");
                    return false;
                } else if (password.length() == 0) {
                    Password.requestFocus();
                    Password.setError("Password cannot be empty");
                    return false;
                } else if (!password.matches("^" + "(?=.*[@#$%&+=])" + "(?=\\" +
                        "S+$)" + ".{5,}" + "$")) {
                    Password.requestFocus();
                    Password.setError("Password is too weak");
                    return false;
                } else {
                    return true;
                }

            }
        });

    }
}