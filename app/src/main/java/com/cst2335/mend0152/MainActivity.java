package com.cst2335.mend0152;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    @Override
    protected void onPause() {
        super.onPause();

        //loads FileName.xml
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);

        EditText typeField = findViewById(R.id.edit);

        typeField.setText(prefs.getString("email", ""));

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView loads objects onto the screen

        setContentView(R.layout.activity_main_lab3);

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);


        EditText typeField = findViewById(R.id.edit);
        typeField.setText(prefs.getString("email", ""));

        Button loginButton = findViewById(R.id.button_Login);

        //this creates a transition to load ProfileActivity.java:
        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);

        loginButton.setOnClickListener( click ->
                {
                    goToProfile.putExtra("typed", typeField.getText().toString());
                    startActivity(goToProfile);
                    saveSharedPrefs(typeField.getText().toString());
                });



    }

    private void saveSharedPrefs(String EmailToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", EmailToSave);
        editor.commit();
    }
}