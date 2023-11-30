package com.example.mi_term;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    //Add new bill
    public Button button13;
    //History
    public Button button14;
    //Send message
    public Button button15;

    public EditText editText23;
    public EditText editText24;
    public EditText editText25;
    public EditText editText26;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0 ;
    private String username;
    private String templateMessage;
    DBHelper DB = new DBHelper(this);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        button13 = findViewById(R.id.button13);
        button14 = findViewById(R.id.history);
        button15 = findViewById(R.id.button15);
        editText23 = findViewById(R.id.amount_elec);//eamount
        editText24 = findViewById(R.id.date_elec);//edate
        editText25 = findViewById(R.id.amount_water);//wamount
        editText26 = findViewById(R.id.date_water);//wdate


        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = DB.getSession();
                int eamount = Integer.parseInt(editText23.getText().toString());
                String edate = editText24.getText().toString();
                int wamount = Integer.parseInt(editText25.getText().toString());
                String wdate = editText26.getText().toString();

                templateMessage = "The total amount for Water paid on " + wdate + " is: \n" + wamount + "\n The total amount of Electricity paid on " + edate + " is: \n" + eamount + ".";

                boolean success = DB.insertBills( eamount,edate,wamount,wdate, username);
                if(success){
                    Toast.makeText(Home.this, "Bill added successfully", Toast.LENGTH_SHORT).show();
                    sendSMSMessage(username,templateMessage);
                }
                else{
                    Toast.makeText(Home.this, "Failed adding the bill", Toast.LENGTH_SHORT).show();
                }

            }
        });
        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
            }
        });
    }

    protected void sendSMSMessage(String username,String templateMessage) {

        String phoneNo = DB.getPhoneNo(username);
        if(phoneNo == null){
            Toast.makeText(this, "No phone number", Toast.LENGTH_SHORT).show();
        }
        else{
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.SEND_SMS)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
            else{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, templateMessage, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSMessage(username, templateMessage);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}