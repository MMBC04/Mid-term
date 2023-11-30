package com.example.mi_term;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    public EditText editText;//Username
    public EditText editText2;//password
    public Button button3;//login
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.editTextText);
        editText2 = findViewById(R.id.editTextText2);
        button3 = findViewById(R.id.button3);
        DB = new DBHelper(this);


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=editText.getText().toString();
                String Password=editText2.getText().toString();

                if (username.isEmpty() || Password.isEmpty()){
                    Toast.makeText(Login.this,"Please fill the fields",Toast.LENGTH_SHORT).show();
                }if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches()){
                    editText.setError("Enter a valid email address!!");
                    return;
                } else {
                    Boolean checkCred = DB.checkpassword(username,Password);
                    {
                        if (checkCred == true) {
                            DB.addSession(username);
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);

                        }
                    }
                }
            }
        });
    }
}