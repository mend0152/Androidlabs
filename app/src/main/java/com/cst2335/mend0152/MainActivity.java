package com.cst2335.mend0152;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_relative);
        //setContentView(R.layout.activity_main_grid);
        setContentView(R.layout.activity_main_linear);

        //TextView myText = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> toastMsg());
        Switch s = findViewById(R.id.switch1);
        s.setOnCheckedChangeListener((cb, isChecked) -> {
            String snack_message = MainActivity.this.getResources().getString(R.string.snack_message);
            Snackbar.make(cb, snack_message + " " + isChecked, Snackbar.LENGTH_LONG)
            .setAction("Undo", click -> cb.setChecked(!isChecked)).show();
        });


    }
    public void toastMsg() {

        String toast_message = MainActivity.this.getResources().getString(R.string.toast_message);
        Toast.makeText(MainActivity.this, toast_message, Toast.LENGTH_LONG).show();
    }

}