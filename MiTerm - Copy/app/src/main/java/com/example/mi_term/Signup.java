package com.example.mi_term;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Signup extends AppCompatActivity {
    //username
    public EditText editText3;
    //password
    public EditText editText4;
    //confirm password
    public EditText editText5;
    //hse number
    public EditText editText17;
    //hse type
    private Switch switch1;
    //sms
    private EditText editText6;
    Button button;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editText3 = findViewById(R.id.editTextText3);
        editText4 = findViewById(R.id.editTextText4);
        editText5 = findViewById(R.id.editTextText5);
        editText17 =findViewById(R.id.editTextText17);
        switch1 = findViewById(R.id.switch1);
        editText6 = findViewById(R.id.editTextText6);
        button = findViewById(R.id.button);
        DB=new DBHelper(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText3.getText().toString();
                String Password = editText4.getText().toString();
                String con_Password = editText5.getText().toString();
                String accountnumber = editText17.getText().toString();
                String housetype;
                String Phonenumber = editText6.getText().toString().trim();


                if (email.isEmpty() || Password.isEmpty() || con_Password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
                }if(!Patterns.EMAIL_ADDRESS.matcher(editText3.getText().toString().trim()).matches()){
                    editText3.setError("Enter a valid email address!!");
                    return;
                } else {
                    if (Password.equals(con_Password)) {
                        if(switch1.isChecked()==true){
                            housetype="M";
                        }else{
                            housetype="A";
                        }
                        System.out.println("Here we are instead");

                        Boolean checkinsertdata = DB.insertUserdata(accountnumber,Password,housetype,email,Phonenumber);

                        if (checkinsertdata == true)
                            Toast.makeText(Signup.this, "User successfully created", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Signup.this, "Error in user creation", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Signup.this, "Passwords are not identical", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    }
