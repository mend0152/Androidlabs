package com.cst2335.mend0152;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery req = new ForecastQuery();
        req.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");  //Type 1

    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {

        double uvRating;
        String iconName = "";
        String min = "";
        String max = "";
        String value = "";
        String fName = "";
        String urlString = "";
        Bitmap image;
        Bitmap bm;

        //Type3                Type1
        public String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                //From part 3, slide 20
                //String parameter = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <temperature> start tag
                            min = xpp.getAttributeValue(null,    "min");
                            publishProgress(25);
                            max = xpp.getAttributeValue(null, "max");
                            value = xpp.getAttributeValue(null, "value");
                            publishProgress(50);
                        }

                        else if(xpp.getName().equals("weather"))
                        {
                            iconName = xpp.getAttributeValue(null, "icon"); // this will run for <weather icon="parameter" >
                            publishProgress(75);
                        }


                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                //build the string to download the Bitmap image
                fName = iconName + ".png";
                urlString = "https://openweathermap.org/img/w/" + fName;


                //the code below is to download the Bitmap code
                if( !fileExistance(fName) ) {
                    Log.i("download", "The file will be downloaded " + fName);
                    URL urlBitmap = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) urlBitmap.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        publishProgress(100);

                        //Save the Bitmap object to the local application storage with the following code:
                        FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        File directory = getFilesDir();
                        outputStream.close();
                    }
                } else {
                    //If the Image file exists, then you don’t need to re-download it, just read it from your disk
                    Log.i("found", "The file has found " + fName);
                    FileInputStream fis = null;
                    try {    fis = openFileInput(fName);   }
                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                    bm = BitmapFactory.decodeStream(fis);

                }

                //create a URL object of what server to contact:
                URL urlUV = new URL("https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

                //open the connection
                HttpURLConnection urlUvConnection = (HttpURLConnection) urlUV.openConnection();

                //wait for data:
                InputStream responseUV = urlUvConnection.getInputStream();
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseUV, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                uvRating = uvReport.getDouble("value");

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            ProgressBar someProgressBar = findViewById(R.id.progressBar);
            someProgressBar.setVisibility(View.VISIBLE);
            someProgressBar.setProgress(25);
            someProgressBar.setProgress(50);
            someProgressBar.setProgress(75);
            someProgressBar.setProgress(100);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            TextView minTemp = findViewById(R.id.minTemperature);
            minTemp.setText("The min temperature is: " + min + " Celcius");
            TextView maxTemp = findViewById(R.id.maxTemperature);
            maxTemp.setText("The max temperature is: " + max + " Celcius");
            TextView curTemp = findViewById(R.id.currentTemperature);
            curTemp.setText("The current temperature is: " + value + " Celcius");
            TextView uvRat = findViewById(R.id.uvRating);
            uvRat.setText("The UV Rating is: " + String.valueOf(uvRating));
            ImageView view = findViewById(R.id.currentWeather);
            if ( image == null) {
                //ImageView view = findViewById(R.id.currentWeather);
                view.setImageBitmap(bm);
            }else
                view.setImageBitmap(image);

            Log.i("HTTP", fromDoInBackground);
            ProgressBar someProgressBar = findViewById(R.id.progressBar);
            someProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public boolean fileExistance (String fname){
        File[] allFiles = getFilesDir().listFiles();
        Log.i("looking", "looking for the file named: " + fname);
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();

    }

}