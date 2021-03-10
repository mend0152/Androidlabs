 package com.cst2335.mend0152;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton mImageButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageButton = findViewById(R.id.imageButton2);
        mImageButton.setOnClickListener(click -> dispatchTakePictureIntent() );

        EditText emailTypeField = findViewById(R.id.edit1);

        Intent fromMain = getIntent();
        emailTypeField.setText(fromMain.getStringExtra("typed"));

        Button chatButton = findViewById(R.id.gotochat);

        //this creates a transition to load ChatRoomActivity.java:
        Intent gotoChatRoomAct = new Intent(ProfileActivity.this, ChatRoomActivity.class);

        chatButton.setOnClickListener( click ->
        {

            startActivity(gotoChatRoomAct);

        });

        Button weatherButton = findViewById(R.id.gotoWeather);

        //this creates a transition to load WeatherForecast.java:
        Intent gotoWeatherForecast = new Intent(ProfileActivity.this, WeatherForecast.class);

        weatherButton.setOnClickListener( click ->
        {

            startActivity(gotoWeatherForecast);

        });


        Log.e(ACTIVITY_NAME,"In Function" + " onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME,"In Function" + " onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME,"In Function" + " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME,"In Function" + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME,"In Function" + " onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME,"In Function" + " onDestroy");
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME,"In Function" + "onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

}