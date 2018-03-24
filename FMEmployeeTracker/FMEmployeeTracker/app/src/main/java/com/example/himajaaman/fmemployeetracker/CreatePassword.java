package com.example.himajaaman.fmemployeetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreatePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);


        final Button cPasswordLink = (Button) findViewById(R.id.cPassword);
        final EditText emplID = (EditText)findViewById(R.id.emplID);
        final EditText emplEmail = (EditText)findViewById(R.id.emplEmail);
        final EditText emplMobile = (EditText)findViewById(R.id.emplMobile);
        final EditText emplPass = (EditText)findViewById(R.id.emplPass);




        cPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = emplID.getText().toString();
                String Email = emplEmail.getText().toString();
                String mobile = emplMobile.getText().toString();
                String pass = emplPass.getText().toString();
                new JSONAdminTask().execute("http://147.97.156.222/regemp?id="+ID+"&email="+Email+"&phone="+mobile+"&pass="+pass);
                /*Intent cPasswordIntent = new Intent(CreatePassword.this, MainActivity.class);
                CreatePassword.this.startActivity(cPasswordIntent);*/
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
                String resultName = parentObject.getString("id");
                return resultType + " - " + resultName + " ID Created";

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
                Toast.makeText(CreatePassword.this,result, Toast.LENGTH_SHORT).show();
                Intent admLogIntent = new Intent(CreatePassword.this,MainActivity.class);
                CreatePassword.this.startActivity(admLogIntent);
            }else{
                Toast.makeText(CreatePassword.this,R.string.error_login_msg, Toast.LENGTH_SHORT).show();
            }

        }
    }

}
