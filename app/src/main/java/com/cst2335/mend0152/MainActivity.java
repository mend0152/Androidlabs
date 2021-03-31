package com.cst2335.mend0152;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);

        EditText typeEmail = findViewById(R.id.lab3_email);
        typeEmail.setText(prefs.getString("email", ""));

        Button loginButton = findViewById(R.id.lab3_login);

        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
        loginButton.setOnClickListener(bt -> {
            saveSharedPrefs(typeEmail.getText().toString());
            goToProfile.putExtra("email", typeEmail.getText().toString());
            startActivity(goToProfile);
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);

        String savedEmail = prefs.getString("email", "");
        EditText typeEmail = findViewById(R.id.lab3_email);
        typeEmail.setText(savedEmail);


    }

    private void saveSharedPrefs(String emailToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", emailToSave);
        editor.commit();
    }
}