package com.example.himajaaman.fmemployeetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EmployeeData extends AppCompatActivity {

    Toolbar mToolbar;
    TextView flag;
    public double lat;
    public double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_data);

        mToolbar = (Toolbar) findViewById(R.id.toolbar1);
        flag = (TextView) findViewById(R.id.textView);
        final ImageButton calButtonLink = (ImageButton) findViewById(R.id.calButton);
        final Button admlogoutLink = (Button) findViewById(R.id.admlogout);
        final Button recentLocLink = (Button) findViewById(R.id.recentLoc);



        admlogoutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admlogoutIntent = new Intent(EmployeeData.this, MainActivity.class);
                EmployeeData.this.startActivity(admlogoutIntent);
            }
        });

        recentLocLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JSONLocTask().execute("http://147.97.156.222/locemp?id=123&long=9999&lat=9999");

                /*Intent recentLocIntent = new Intent(EmployeeData.this, MapsActivity.class);
                EmployeeData.this.startActivity(recentLocIntent);*/
            }
        });
        calButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder calBuilder = new AlertDialog.Builder(EmployeeData.this);
                View calView = getLayoutInflater().inflate(R.layout.activity_date_view, null);
                final ImageButton calButtonLink = (ImageButton) findViewById(R.id.calButton);

                calButtonLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent recentLocIntent = new Intent(EmployeeData.this, EmployeeData.class);
                        EmployeeData.this.startActivity(recentLocIntent);

                    }
                });


                calBuilder.setView(calView);
                AlertDialog dialog = calBuilder.create();
                dialog.show();

            }
        });

        Bundle bundle = getIntent().getExtras();//holds the data that comes from MAIN ACTIVITY
        if (bundle != null){
            mToolbar.setTitle(bundle.getString("employeename"));
        }

    }

    public class JSONLocTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader( new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line ="";

                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();

                    /*{"success":true,"id":"123","locations":
                            {"0":{"latitude":35,"longitude":90}
                            }
                      }*/
                JSONObject parentObject = new JSONObject(finalJson);
                String resultType = parentObject.getString("success");
                String resultName = parentObject.getString("id");
                JSONObject childObject = parentObject.getJSONObject("locations");
                JSONObject babyObject = childObject.getJSONObject("0");

                lat = babyObject.getDouble("latitude");
                lng = babyObject.getDouble("longitude");
                return resultName + " is at " + lat + " - " + lng;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(EmployeeData.this, result, Toast.LENGTH_SHORT).show();
            Toast.makeText(EmployeeData.this,result, Toast.LENGTH_SHORT).show();
                Intent admLogIntent = new Intent(EmployeeData.this,MapsActivity.class);
            EmployeeData.this.startActivity(admLogIntent);

        }
    }

}
