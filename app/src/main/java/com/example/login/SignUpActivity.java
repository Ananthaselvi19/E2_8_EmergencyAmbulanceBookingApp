package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    private EditText Username;
    private EditText Password;
    private EditText confirm_password;
    private EditText contact_no;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button Signup;
    private DatabaseHelper myDB;
    String strPassword;
    String strconfirm_password;
    String strUsername;
    String strcontact_no;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        confirm_password = findViewById(R.id.confirm_password);
        contact_no = findViewById(R.id.contact_no);
        radioGroup = (RadioGroup)findViewById(R.id.radio);
        Signup = findViewById(R.id.Signup);

        myDB = new DatabaseHelper(this);
        insertUser();

//        Signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String strPassword = Password.getText().toString();
//                String strconfirm_password = confirm_password.getText().toString();
//                String strUsername = Username.getText().toString();
//                String strcontact_no = contact_no.getText().toString();
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                radioButton = (RadioButton) findViewById(selectedId);
//                String strRadioButton = radioButton.getText().toString();
//
//                boolean check = validateInfo(strUsername, strPassword, strcontact_no);
//
//                if(check == true){
//                    if(strPassword != null && strconfirm_password != null && strPassword.equals(strconfirm_password)) {
//                        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = credentials.edit();
//                        editor.putString("Password", strPassword);
//                        editor.putString("Username", strUsername);
//                        editor.putString("contact_no", strcontact_no);
//                        editor.putString("radio", strRadioButton);
//                        editor.commit();
//                        Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
//                        SignUpActivity.this.finish();
//
//                    }
//                    else if(strPassword != strconfirm_password) {
//                        Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
    }

    private void insertUser(){
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPassword = Password.getText().toString();
                strconfirm_password = confirm_password.getText().toString();
                strUsername = Username.getText().toString();
                strcontact_no = contact_no.getText().toString();
                boolean check = validateInfo(strUsername, strPassword, strcontact_no);

                if(check == true){
                    if(strPassword != null && strconfirm_password != null && strPassword.equals(strconfirm_password)) {
                        boolean var = myDB.registerUser(Username.getText().toString(), Password.getText().toString(), contact_no.getText().toString());
                        if(var){
                            Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                        SignUpActivity.this.finish();
                    }
                    else if(strPassword != strconfirm_password) {
                        Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
            }
        }

            private boolean validateInfo(String strUsername, String strPassword, String strcontact_no) {
                if(strUsername.length()==0){
                    Username.requestFocus();
                    Username.setError("Username cannot be empty");
                    return false;
                }
                else if(!strUsername.matches("[a-zA-Z]{5,}+")){
                    Username.requestFocus();
                    Username.setError("Enter only alphabetical letters and 5 or more characters");
                    return false;
                }
                else if(strcontact_no.length()==0){
                    contact_no.requestFocus();
                    contact_no.setError("Contact number cannot be empty");
                    return false;
                }
                else if(!strcontact_no.matches("^[+][0-9]{10,13}$")){
                    contact_no.requestFocus();
                    contact_no.setError("Correct Format: +91XXXXXXXXXX");
                    return false;
                }
                else if(strPassword.length()==0){
                    Password.requestFocus();
                    Password.setError("Password cannot be empty");
                    return false;
                }
                else if(!strPassword.matches("^"+"(?=.*[@#$%&+=])"+"(?=\\" +
                        "S+$)"+".{5,}"+"$")){
                    Password.requestFocus();
                    Password.setError("Password is too weak");
                    return false;
                }
                else{
                    return true;
                }
            }
            });
    }

//    private boolean validateInfo(String strUsername, String strPassword, String strcontact_no) {
//        if(strUsername.length()==0){
//            Username.requestFocus();
//            Username.setError("Username cannot be empty");
//            return false;
//        }
//        else if(!strUsername.matches("[a-zA-Z]{5,}+")){
//            Username.requestFocus();
//            Username.setError("Enter only alphabetical letters and 5 or more characters");
//            return false;
//        }
//        else if(strcontact_no.length()==0){
//            contact_no.requestFocus();
//            contact_no.setError("Contact number cannot be empty");
//            return false;
//        }
//        else if(!strcontact_no.matches("^[+][0-9]{10,13}$")){
//            contact_no.requestFocus();
//            contact_no.setError("Correct Format: +91XXXXXXXXXX");
//            return false;
//        }
//        else if(strPassword.length()==0){
//            Password.requestFocus();
//            Password.setError("Password cannot be empty");
//            return false;
//        }
//        else if(!strPassword.matches("^"+"(?=.*[@#$%&+=])"+"(?=\\" +
//                "S+$)"+".{5,}"+"$")){
//            Password.requestFocus();
//            Password.setError("Password is too weak");
//            return false;
//        }
//        else{
//            return true;
//        }
//    }
}