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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        final Intent intent=getIntent();
        final ArrayList<String> username = new ArrayList<>();
        final ArrayList<Integer> state=new ArrayList<>();
        final ListView userslist=findViewById(R.id.userslist) ;
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
                    int i=last_id+1;
                    sqlquery="{CALL get_users(?,?,?)}";
                    callableStatement= com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(sqlquery);
                 while(i!=0){

                     callableStatement.setInt(1,i);
                     callableStatement.registerOutParameter(2,Types.VARCHAR);
                     callableStatement.registerOutParameter(3,Types.VARCHAR);

                     callableStatement.execute();
                     if(callableStatement.getString(2)!=null){
                         username.add(callableStatement.getString(2));
                         state.add(callableStatement.getInt(3));
                     }

                     i--;
                 }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final String uuid_admin=intent.getExtras().getString("uuid_admin");
                        Account_state_adapter adapter=new Account_state_adapter(AccountSettings.this,username,state,uuid_admin);
                        userslist.setAdapter(adapter);

                    }
                });

            }
        }).start();




    }

}
