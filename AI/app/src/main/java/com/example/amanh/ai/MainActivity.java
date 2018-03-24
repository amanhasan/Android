package com.example.amanh.ai;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    TextView resultTEXT;
    //TTS object
    private TextToSpeech t1;
    private static final int REQUEST_CALL = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTEXT = (TextView) findViewById(R.id.tvResult);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.micButton) {
            promptSpeechInput();
        }

    }

    public void promptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //i.putExtra(RecognizerIntent.EXTRA_PROMPT, "say something");
        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "sorry your device doesnot support speech language", Toast.LENGTH_LONG).show();

        }

    }



    @SuppressLint("Range")
    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);

        switch (request_code) {
            case 100:
                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultTEXT.setText(result.get(0));

                    String intro = "Hi, my name is Arween. I am your personal A I assistant. Nice to meet you.";
                    if (result.toString().contains("hi") || result.toString().contains("who are you") || result.toString().contains("your name") || result.toString().contains("hello") || result.toString().contains("hey")) {
                        String toSpeak = intro.toString();
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    } else if (result.toString().contains("call")) {
                        String calling = "calling";
                        String toSpeak = calling.toString();
                        //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        makephonecall();


                }
                else {
                    String sorry ="I am Sorry! I can not do that. I am still learning.";
                    String toSpeak = sorry.toString();
                    //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }

            break;
        }

    }

    private void makephonecall() {

        Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
        callIntent.setData(Uri.parse("tel:8702199102"));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try {
                startActivity(callIntent);  //call activity and make phone call
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
            }

        }


    }
}
