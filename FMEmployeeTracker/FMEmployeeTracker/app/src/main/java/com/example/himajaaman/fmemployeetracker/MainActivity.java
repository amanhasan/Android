package com.example.himajaaman.fmemployeetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final ImageView admLogLink = (ImageView) findViewById(R.id.admLog);
        final ImageView empLogLink = (ImageView) findViewById(R.id.empLog);


        admLogLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder admLogBuilder = new AlertDialog.Builder(MainActivity.this);
                View admLogView = getLayoutInflater().inflate(R.layout.activity_admin_login, null);
                final EditText admId = (EditText) admLogView.findViewById(R.id.admId);
                final EditText admPassword = (EditText) admLogView.findViewById(R.id.admPassword);
                Button admLoginLink = (Button) admLogView.findViewById(R.id.admLogin);

                admLoginLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = admId.getText().toString();
                        String pass = admPassword.getText().toString();

                        new JSONAdminTask().execute("http://147.97.156.222/login?name="+name+"&pass="+pass);

                    }
                });

                admLogBuilder.setView(admLogView);
                AlertDialog dialog = admLogBuilder.create();
                dialog.show();


            }
        });

        empLogLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder empLogBuilder = new AlertDialog.Builder(MainActivity.this);
                View empLogView = getLayoutInflater().inflate(R.layout.activity_employee_login, null);
                final EditText empId = (EditText) empLogView.findViewById(R.id.empID);
                final EditText empPassword = (EditText) empLogView.findViewById(R.id.empPassword);
                Button empLoginLink = (Button) empLogView.findViewById(R.id.empLogin);
                TextView fcPasswordLink = (TextView) empLogView.findViewById(R.id.fcPassword);

                empLoginLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = empId.getText().toString();
                        String pass = empPassword.getText().toString();
                        new JSONEmpTask().execute("http://147.97.156.222/logemp?id="+name+"&pw="+pass);

                    }
                });

                fcPasswordLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent fcPasswordIntent = new Intent(MainActivity.this, CreatePassword.class);
                        MainActivity.this.startActivity(fcPasswordIntent);
                    }
                });



                empLogBuilder.setView(empLogView);
                AlertDialog dialog = empLogBuilder.create();
                dialog.show();

            }
        });




    }
    public class JSONAdminTask extends AsyncTask<String, String, String> {

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

                JSONObject parentObject = new JSONObject(finalJson);
                String resultType = parentObject.getString("success");
                String resultName = parentObject.getString("name");
                return resultType + " - " + resultName;

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
            if(result.contains("true")) {
                Toast.makeText(MainActivity.this, R.string.success_login_msg, Toast.LENGTH_SHORT).show();
                Intent admLogIntent = new Intent(MainActivity.this,EmployeeList.class);
                MainActivity.this.startActivity(admLogIntent);
            }else{
                Toast.makeText(MainActivity.this, R.string.error_login_msg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class JSONEmpTask extends AsyncTask<String, String, String> {

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

                JSONObject parentObject = new JSONObject(finalJson);
                String resultType = parentObject.getString("success");
                String resultName = parentObject.getString("id");
                return resultType + " - " + resultName;

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
            if(result.contains("true")) {
                Toast.makeText(MainActivity.this, R.string.success_login_msg, Toast.LENGTH_SHORT).show();
                Intent admLogIntent = new Intent(MainActivity.this,TrackingGpsLocation.class);
                MainActivity.this.startActivity(admLogIntent);
            }else{
                Toast.makeText(MainActivity.this, R.string.error_login_msg, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
