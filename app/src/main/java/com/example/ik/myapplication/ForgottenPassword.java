package com.example.ik.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.*;
import java.util.ArrayList;

public class ForgottenPassword extends AppCompatActivity {
    EditText username,email;
    TextView wrong;
    Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);
        username=findViewById(R.id.username_getback);
        email=findViewById(R.id.email_getback);
        reset=findViewById(R.id.Reset);
        wrong=findViewById(R.id.wrong);
        reset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int check_username = 0;
                        int check_email = 0;
                        try {
                         Class.forName("com.mysql.jdbc.Driver");
                        dbconnect dbconnect = null;
                        String sqlquery="{CALL check_username_email_exits(?,?,?,?)}";
                        CallableStatement callableStatement=dbconnect.dbconnection().prepareCall(sqlquery);
                        callableStatement.setString(1,username.getText().toString().toLowerCase());
                        callableStatement.setString(2,email.getText().toString().toLowerCase());
                        callableStatement.registerOutParameter(3, Types.INTEGER);
                        callableStatement.registerOutParameter(4,Types.INTEGER);
                        callableStatement.execute();
                         check_username=callableStatement.getInt(3);
                         check_email=callableStatement.getInt(4);

                        } catch (SQLException e) {
                            e.printStackTrace();
                            Log.e("/==+>", String.valueOf(e.getCause()));
                            Log.e("/==+>", String.valueOf(e.getNextException()));
                            Log.e("/==+>", e.getSQLState());
                            Log.e("/==+>", String.valueOf(e.getErrorCode()));

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        final int finalCheck_username = check_username;
                        final int finalCheck_email = check_email;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if(finalCheck_username ==0 && finalCheck_email ==0){
                                    Intent intent=new Intent(ForgottenPassword.this,PutNewPassword.class);
                                    intent.putExtra("username",username.getText().toString().toLowerCase());
                                    startActivity(intent);
                                    finish();
                                }else{
                                    wrong.setTextColor(Color.RED);
                                    wrong.setText("Please , check Your E-mail or Username");
                                }

                            }
                        });

                    }

                }).start();



        }
    });
}
}
