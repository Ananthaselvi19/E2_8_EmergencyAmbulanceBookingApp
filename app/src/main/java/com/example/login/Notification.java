package com.example.login;

import static com.example.login.R.layout.activity_notification;
import static com.example.login.R.layout.book_dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Notification extends AppCompatActivity {
    Layout drawerlayout;
    TextView acceptdecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        acceptdecline = findViewById(R.id.acceptdecline);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
        if(getIntent().hasExtra("Accept")){
            displayAlert();
        }
        else if (getIntent().hasExtra("View")){

            Intent intent = new Intent(Notification.this, HomePage.class);
            startActivity(intent);
//            acceptdecline.setText("Request is declined");
//            acceptdecline.setTextColor(Color.RED);
        }
    }

    @SuppressLint("ResourceType")
    private void displayAlert() {
        final AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = Notification.this.getLayoutInflater();
        View dialogView = inflater.inflate(activity_notification, null);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Notification.this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Notification.this.startActivity(intent);
            }
        });
        alert.setCancelable(false);
        alert.setInverseBackgroundForced(true);
        alert.setView(dialogView);
        alert.show();
    }
}