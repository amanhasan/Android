package com.example.himajaaman.fmemployeetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class EmployeeList extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    FloatingActionButton addEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.ListView);
        addEmployee = (FloatingActionButton) findViewById(R.id.addEmployee);

        final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(EmployeeList.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.employeenames));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(EmployeeList.this, EmployeeData.class);
                String name = mAdapter.getItem(i);
                intent.putExtra("employeename", name);

                startActivity(intent);

            }
        });
        listView.setAdapter(mAdapter);

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addEmpBuilder = new AlertDialog.Builder(EmployeeList.this);
                View addEmpView = getLayoutInflater().inflate(R.layout.activity_add_employee, null);
                final EditText addEmpId = (EditText) addEmpView.findViewById(R.id.addEmpId);
                final EditText addEmpName = (EditText) addEmpView.findViewById(R.id.addEmpName);
                final EditText addEmpEmail = (EditText) addEmpView.findViewById(R.id.addEmpEmail);
                final EditText addEmpMobile = (EditText) addEmpView.findViewById(R.id.addEmpMobile);
                final Button submitLink = (Button) addEmpView.findViewById(R.id.submit);

                submitLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String empName = addEmpName.getText().toString();
                        String empID = addEmpId.getText().toString();
                        String empEmail = addEmpEmail.getText().toString();
                        String empMobile = addEmpMobile.getText().toString();
                        new JSONAddEmpTask().execute("http://147.97.156.222/addemp?id="+empID+"&name="+empName+"&email="+empEmail+"&phone="+empMobile);

                    }
                });
                addEmpBuilder.setView(addEmpView);
                AlertDialog dialog = addEmpBuilder.create();
                dialog.show();
            }
        });

        final Button admLogoutLink = (Button) findViewById(R.id.admLogout);


        admLogoutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admLogoutIntent = new Intent(EmployeeList.this, MainActivity.class);
                EmployeeList.this.startActivity(admLogoutIntent);
            }
        });
    }
    public class JSONAddEmpTask extends AsyncTask<String, String, String> {

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
                String resultID = parentObject.getString("id");
                String resultName = parentObject.getString("name");
                String resultEmail = parentObject.getString("email");
                String resultPhone = parentObject.getString("phone");
                return resultType + " - " + resultName + " - " + resultID + " - " + resultEmail + " - " + resultPhone;

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
                Toast.makeText(EmployeeList.this, result, Toast.LENGTH_SHORT).show();
                Intent addEmpIntent = new Intent(EmployeeList.this,EmployeeList.class);
                EmployeeList.this.startActivity(addEmpIntent);
            }else{
                Toast.makeText(EmployeeList.this, "Invalid Credentials/Employee exists", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
