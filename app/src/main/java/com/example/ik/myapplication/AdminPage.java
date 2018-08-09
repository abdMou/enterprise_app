package com.example.ik.myapplication;

// DONE


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdminPage extends AppCompatActivity {
    Spinner statue;
    //IMEI PART
    public static boolean isMultiSimEnabled = false;
    public static List<SubscriptionInfo> subInfoList;
    public static ArrayList<String> numbers;
    private static final Integer PHONESTATS = 0x1;
    private SubscriptionManager subscriptionManager;
    private final String TAG = MainActivity.class.getSimpleName();
    String imei;
    // END EMEI PART
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        numbers = new ArrayList<String>();
        askForPermission(Manifest.permission.READ_PHONE_STATE, PHONESTATS);
        Intent intent = getIntent();
        final String uuid = intent.getExtras().getString("uuid");
        final EditText new_username_put=(EditText) findViewById(R.id.input_username);
        final EditText new_password_put=(EditText) findViewById(R.id.input_password);
        final EditText new_password2_put=(EditText) findViewById(R.id.input_password2);
        final EditText new_email_put=(EditText) findViewById(R.id.input_email);
        final EditText new_salary_put=findViewById(R.id.put_salary);
        Button signup=(Button) findViewById(R.id.put_new_user);
        ArrayList<String> statuelist=new ArrayList<>();
        statuelist.add("Active");
        statuelist.add("Abandoned");
        statuelist.add("Suspended");
        statuelist.add("Not yet accepted");
        statue=findViewById(R.id.statue);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,statuelist);
       statue.setAdapter(adapter);
        int index=0;
        //ADD USERNAME & PASSWORD & E-MAIL & ...
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                     @Override
                     public void run() {
                         try {
                             Class.forName("com.mysql.jdbc.Driver");
                             dbconnect dbconnect = null;
                             String sql_check_email_password="CALL check_username_email_exits(?,?,?,?)";
                             CallableStatement statement=dbconnect.dbconnection().prepareCall(sql_check_email_password);
                             statement.setString(1,new_username_put.getText().toString().toLowerCase());
                             statement.setString(2,new_email_put.getText().toString().toLowerCase());
                             statement.registerOutParameter(3, Types.INTEGER);
                             statement.registerOutParameter(4, Types.INTEGER);
                             statement.execute();
                             final int check_email=statement.getInt(4);
                             final int check_username=statement.getInt(3);
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {

                                         if(new_username_put.getText().toString().trim().length() <= 0){
                                             new_username_put.setError("Username Cannot be empty");
                                         }
                                         if(new_password_put.getText().toString().trim().length() <= 0){
                                             new_password_put.setError("Password Cannot be empty");
                                         }
                                         if(new_password2_put.getText().toString().trim().length() <= 0){
                                             new_password2_put.setError("Password Cannot be empty");
                                         }
                                         if(new_email_put.getText().toString().trim().length() <= 0){
                                             new_email_put.setError("E-mail Cannot be empty");
                                         }
                                         if(new_salary_put.getText().toString().trim().length() <= 0){
                                             new_salary_put.setError("Salary Cannot be empty");
                                         }if(!(new_password_put.getText().toString().equals(new_password2_put.getText().toString()))) {
                                             new_password2_put.setError("2nd Password mismatch 1st Password");
                                         }


                                         if(check_email==0){
                                             new_email_put.setError("There is already this E-mail");

                                         }
                                         if(check_username==0){
                                             new_username_put.setError("There is already this Username");

                                         }

                                         if(check_username==1 && check_email==1 &&new_username_put.getText().toString().trim().length() > 0 && new_password_put.getText().toString().trim().length() > 0 && new_password2_put.getText().toString().trim().length() > 0 && new_email_put.getText().toString().trim().length() > 0 &&  new_salary_put.getText().toString().trim().length() > 0 && new_password_put.getText().toString().equals(new_password2_put.getText().toString()) ){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    char[] checkemail = new_email_put.getText().toString().toCharArray();
                                                    int i= 0;
                                                    boolean testingemail = false;
                                                    while (i < checkemail.length) {
                                                        if (checkemail[i] == '@') {
                                                            int j = i;
                                                            while (j < checkemail.length) {
                                                                if (checkemail[j] == '.') {
                                                                    testingemail = true;
                                                                    break;
                                                                }
                                                                j++;
                                                            }
                                                            if (testingemail) {
                                                                break;
                                                            }
                                                        }
                                                        i++;
                                                    }
                                                    // CHECKING IF THERE IS AN ACCCOUNT WITH SAME EMAIL AND USERNAME IS MISSING !

                                                    if (testingemail) {
                                                        CNIL password=new CNIL(new_password_put.getText().toString());
                                                        if(password.check_CNIL()){
                                                            Intent check = new Intent(getApplicationContext(), CheckAdmin.class);
                                                            check.putExtra("username_modify", new_username_put.getText().toString());

                                                            check.putExtra("password_modify", new_password_put.getText().toString());

                                                            check.putExtra("email_modify", new_email_put.getText().toString().toLowerCase());
                                                            check.putExtra("salary_modify", new_salary_put.getText().toString());
                                                            check.putExtra("statue_modify", statue.getSelectedItem().toString());

                                                            startActivity(check);
                                                        }else{
                                                            new_password_put.setError("Check the password please ,\n Password should has : \n - more than 8 characters \n - One capital letter at least \n - One number at least \n - One Special character at least ");
                                                        }
                                                    } else {
                                                        new_email_put.setError("That's not an E-mail");
                                                    }

                                                }
                                            });


                                         }

                                 }

                             });


                         } catch (ClassNotFoundException | SQLException e) {
                             e.printStackTrace();
                         }

                     }
                 }).start();



                }




        });
        Button AdminLogout=(Button) findViewById(R.id.admin_logout );
        // LOGOUT ADMIN
            AdminLogout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
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
                    Intent logout=new Intent(AdminPage.this,MainActivity.class);
                    startActivity(logout);
                    finish();
                }
            });
        Button settings=(Button) findViewById(R.id.EditUsers);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AccountSettings.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public String IP() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine();

        return ip;
}
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(AdminPage.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(AdminPage.this, permission)) {

                ActivityCompat.requestPermissions(AdminPage.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(AdminPage.this, new String[]{permission}, requestCode);
            }
        } else {
            imei = getImeiNumber();
            getClientPhoneNumber();
            Toast.makeText(this, permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    imei = getImeiNumber();
                    getClientPhoneNumber();

                } else {

                    Toast.makeText(AdminPage.this, "You have Denied the Permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private String getImeiNumber() {
        final TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //getDeviceId() is Deprecated so for android O we can use getImei() method
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                // return getImeiNumber();
            }
            return telephonyManager.getImei();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            return telephonyManager.getDeviceId();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void getClientPhoneNumber() {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                subInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            }
            //check whether the phone is of Multi sim or Not
            if (subInfoList.size() > 1)
            {
                isMultiSimEnabled = true;
            }
            for (SubscriptionInfo subscriptionInfo : subInfoList)
            //add all sim number into arraylist
            {
                numbers.add(subscriptionInfo.getNumber());
            }
            Log.e(TAG,"Sim 1:- "+numbers.get(0));
            Log.e(TAG,"Sim 2:- "+ numbers.get(1));
        }catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }


    }



}

