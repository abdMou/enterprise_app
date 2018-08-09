package com.example.ik.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.sql.*;
import java.util.ArrayList;

// COMPLETED

public class AccountSettings extends AppCompatActivity {

    final String[] user_choose = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        final ArrayList<String> username = new ArrayList<String>();
        final ListView userslist=(ListView) findViewById(R.id.userslist) ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                 dbconnect dbconnect=null;
                 String sqlquery="{CALL get_accounts_last_id(?)}";
                 CallableStatement callableStatement=dbconnect.dbconnection().prepareCall(sqlquery);
                 callableStatement.registerOutParameter(1,Types.INTEGER);
                 callableStatement.execute();
                 int last_id=callableStatement.getInt(1);
                    int i=last_id;
                    sqlquery="{CALL get_users(?,?)}";
                    callableStatement= com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(sqlquery);
                 while(i!=0){

                     callableStatement.setInt(1,i);
                     callableStatement.registerOutParameter(2,Types.VARCHAR);
                     callableStatement.execute();
                     if(callableStatement.getString(2)!=null){
                         username.add(callableStatement.getString(2));
                     }

                     i--;
                 }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(AccountSettings.this,android.R.layout.simple_list_item_1,android.R.id.text1,username);
                        userslist.setAdapter(adapter);

                    }
                });

            }
        }).start();

        userslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                user_choose[0] = (String) userslist.getItemAtPosition(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbconnect dbconnect=null;
                        try {

                            String sqlquery=("{CALL account_username(?,?,?,?)}");
                            CallableStatement statement=dbconnect.dbconnection().prepareCall(sqlquery);
                            statement.setString(1,user_choose[0]);
                            statement.registerOutParameter(2, Types.VARCHAR);
                            statement.registerOutParameter(3, Types.VARCHAR);
                            statement.registerOutParameter(4, Types.VARCHAR);
                            statement.execute();
                            final String email=statement.getString(2);
                            final String salary=statement.getString(3);
                            final String statue=statement.getString(4);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent editusers=new Intent(getApplicationContext(),CheckAdmin.class);
                                    editusers.putExtra("username",user_choose[0]);
                                    editusers.putExtra("email",email);
                                    editusers.putExtra("salary",salary);
                                    editusers.putExtra("statue",statue);
                                    editusers.putExtra("fromaccountlist",1);
                                    startActivity(editusers);
                                    finish();

                                }

                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();


            }
        });



    }

}
