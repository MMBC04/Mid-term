package com.example.mi_term;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    DBHelper dbHelper;
    Button water, electricity;
    TextView result;
    String typeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        water = findViewById(R.id.Electricity);
        electricity = findViewById(R.id.Water);
        result = findViewById(R.id.result_text);

        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeValue = "W";
                retrieveUserHistory();
            }
        });

        electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeValue = "E";
                retrieveUserHistory();
            }
        });

// Function to retrieve user history based on the selected typeValue

        }
    private void retrieveUserHistory() {
        dbHelper = new DBHelper(getApplicationContext());
        String username = dbHelper.getSession();
        if (username != null) {
            ArrayList<String> arrayList = dbHelper.getUserHistory(username, typeValue);
            if (arrayList != null) {
                Log.d("TAG", "User history retrieved: " + arrayList.size() + " items");
                ListView listView = findViewById(R.id.listView);
                if (listView != null) {
                    // Create an ArrayAdapter to bind the data to the ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_list_item_1,
                            arrayList
                    );

                    // Set the adapter to the ListView
                    listView.setAdapter(adapter);
                } else {
                    Log.e("TAG", "ListView is null");
                }
            } else {
                Log.e("TAG", "User history arrayList is null");
            }
        } else {
            Log.e("TAG", "Username is null");
        }

    }
}