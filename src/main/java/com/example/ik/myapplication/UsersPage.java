package com.example.ik.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UsersPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_page);
        Intent intent = getIntent();
        String offline_or_online="ONLINE";
        if(!intent.getExtras().getString("from_file").equals("true")){
            offline_or_online = intent.getExtras().getString("offline_online");
        }
        final String uuid = intent.getExtras().getString("uuid");
        final String username = intent.getExtras().getString("username");
        String salary = intent.getExtras().getString("salary");
        String statue = intent.getExtras().getString("statue");

        TextView user_page_hello_username = (TextView) findViewById(R.id.user_page_hello_username);
        user_page_hello_username.setText("Hello " + username);
        TextView username_salary = (TextView) findViewById(R.id.username_salary);
        username_salary.setText("Your Salary is : " + salary);
        TextView username_statue = (TextView) findViewById(R.id.username_statue);
        username_statue.setText("Your Statue is : " + statue);
        Button logout = (Button) findViewById(R.id.logout_normal_user);
        if(offline_or_online.equals("OFFLINE")) {
            final String STORING_DATE = intent.getExtras().getString("STORING_DATE");
            final String STORING_IMEI = intent.getExtras().getString("STORING_IMEI");
            final String STORING_IP = intent.getExtras().getString("STORING_IP");
            final String STORING_UUID = intent.getExtras().getString("STORING_UUID");
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sqlite sqlite=new Sqlite(getApplicationContext());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String date = sdf.format(new Date());
                    sqlite.insert_logout_user_data(STORING_UUID,STORING_IMEI,STORING_IP,STORING_DATE,date);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Logging out ..", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                }
            });
        }else{
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
                                File(getFilesDir()+File.separator+"Uuid.txt")));
                        bufferedWriter.write(".");
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String sqlquery="{Call drop_session(?)}";
                                CallableStatement callableStatement=dbconnect.dbconnection().prepareCall(sqlquery);
                                callableStatement.setString(1,uuid);
                                callableStatement.execute();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Logging out ..", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();

                }
            });
        }


    }
}
