package com.example.ik.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
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
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public String md5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(text.getBytes());
        byte[] digest = messageDigest.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return (hashtext);
    }
    //IMEI PART
    String imei;
    public static boolean isMultiSimEnabled = false;
    public static List<SubscriptionInfo> subInfoList;
    public static ArrayList<String> numbers;
    private SubscriptionManager subscriptionManager;
    static final Integer PHONESTATS = 0x1;
    private final String TAG = MainActivity.class.getSimpleName();
    // END EMEI PART
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final EditText input_username = findViewById(R.id.input_username);
        final EditText input_password = findViewById(R.id.input_password);
        final Button login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                askForPermission(Manifest.permission.READ_PHONE_STATE, PHONESTATS);
                if (input_username.getText().toString().equals("admin")) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Class.forName("com.mysql.jdbc.Driver");
                                final dbconnect dbconnect = null;
                                final Statement statement = com.example.ik.myapplication.dbconnect.dbconnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                String query = "{CALL loginadmin(?,?,?)}";
                                CallableStatement cs= com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(query);
                                cs.setString(1,input_username.getText().toString());
                                cs.setString(2,md5(input_password.getText().toString()));
                                cs.registerOutParameter(3,Types.INTEGER);
                                cs.execute();
                                final int logging_admin =cs.getInt(3);
                                String uuid = null;
                                int Allow = 0;
                                if(logging_admin==1){
                                    String gettinginfosquery="{CALL admin_page(?,?)}";
                                    cs= com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(gettinginfosquery);
                                    cs.setString(1,input_username.getText().toString());
                                    cs.registerOutParameter(2,Types.VARCHAR);
                                    cs.execute();
                                    uuid=cs.getString(2);
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss a");
                                    // GET DATE AND STORE IT IN DATABASE
                                    String date = sdf.format(new Date());
                                    String allowlogin_query="{CALL sessions_add(?,?,?,?,?)}";
                                    CallableStatement callableStatement=com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(allowlogin_query);

                                    callableStatement.setString(1,uuid);
                                    callableStatement.setString(2,imei);
                                    callableStatement.setString(3,IP());
                                    callableStatement.setString(4,date);
                                    callableStatement.registerOutParameter(5,Types.INTEGER);
                                    callableStatement.execute();
                                    Allow=callableStatement.getInt(5);
                                }
                                final int finalAllow = Allow;
                                final String finalUuid = uuid;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                            if(logging_admin==1){
                                                if(finalAllow ==1){
                                                    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                                                    Toast.makeText(MainActivity.this, "WELCOME TO ADMIN PAGE", Toast.LENGTH_LONG).show();
                                                    intent.putExtra("admin_username",input_username.getText().toString().toLowerCase());
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(MainActivity.this, "There is someone else logged-in with this Account , You can't Log-in Until the other one logged-out !", Toast.LENGTH_LONG).show();

                                                }

                                            } else {
                                                Toast.makeText(MainActivity.this, "Check Your Informations , Please", Toast.LENGTH_LONG).show();

                                            }

                                    }
                                });
                            } catch (SQLException e) {
                                Log.e("+====/>",e.getSQLState());
                                Log.e("+====/>",e.getMessage());
                                e.printStackTrace();
                            } catch (ClassNotFoundException | NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();


                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Class.forName("com.mysql.jdbc.Driver");
                                final dbconnect dbconnect = null;
                                final Statement statement = com.example.ik.myapplication.dbconnect.dbconnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                String query ="{CALL login_user(?,?,?)}";
                                CallableStatement callableStatement= com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(query);
                                callableStatement.setString(1,input_username.getText().toString());
                                callableStatement.setString(2,md5(input_password.getText().toString()));
                                callableStatement.registerOutParameter(3,Types.INTEGER);
                                callableStatement.execute();
                                final int confirm_login=callableStatement.getInt(3);
                                String uuid = null;
                                String salary=null;
                                String statue=null;
                                int Allow = 0;
                                if(confirm_login==1){
                                    String gettinginfos_query="CALL user_page(?,?,?,?)";
                                    callableStatement= com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(gettinginfos_query);
                                    callableStatement.setString(1,(input_username.getText().toString()).toLowerCase());
                                    callableStatement.registerOutParameter(2,Types.VARCHAR);
                                    callableStatement.registerOutParameter(3,Types.VARCHAR);
                                    callableStatement.registerOutParameter(4,Types.VARCHAR);
                                    callableStatement.execute();
                                    uuid=callableStatement.getString(2);
                                    salary=callableStatement.getString(3);
                                    statue=callableStatement.getString(4);
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss a");

                                    // GET DATE AND STORE IT IN DATABASE
                                    String date = sdf.format(new Date());
                                    String allow_login_query="{CALL sessions_add(?,?,?,?,?)}";
                                    callableStatement= com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(allow_login_query);
                                    callableStatement.setString(1,uuid);
                                    callableStatement.setString(2,imei);
                                    callableStatement.setString(3,IP());
                                    callableStatement.setString(4,date);
                                    callableStatement.registerOutParameter(5,Types.INTEGER);
                                    callableStatement.execute();
                                    Allow=callableStatement.getInt(5);

                                }

                                final String finalSalary = salary;
                                final String finalStatue = statue;
                                final int finalAllow = Allow;
                                final String finalUuid = uuid;
                                Log.e("uuid : ",uuid+" ... "+finalUuid);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                                if (confirm_login==1) {
                                                    if(finalAllow ==1) {
                                                        Intent intent = new Intent(getApplicationContext(), UsersPage.class);
                                                        Toast.makeText(MainActivity.this, "Welcome to your page " + input_username.getText().toString(), Toast.LENGTH_LONG).show();
                                                        // get informations
                                                        intent.putExtra("uuid", finalUuid);
                                                        intent.putExtra("username",input_username.getText().toString().toLowerCase() );
                                                        intent.putExtra("salary", finalSalary);
                                                        intent.putExtra("statue", finalStatue);
                                                        startActivity(intent);
                                                        finish();
                                                    }else {
                                                        Toast.makeText(MainActivity.this, "There is someone else logged-in with this Account , You can't Log-in Until the other one logged-out !", Toast.LENGTH_LONG).show();

                                                    }


                                                }else{
                                                    Toast.makeText(MainActivity.this, "Check your informations please", Toast.LENGTH_SHORT).show();

                                                }


                                    }
                                });
                            } catch (SQLException | ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();


                }
            }
        });
        Button forgotten = findViewById(R.id.forgot);
        forgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotten = new Intent(getApplicationContext(), ForgottenPassword.class);
                startActivity(forgotten);
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
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
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

                    Toast.makeText(MainActivity.this, "You have Denied the Permission", Toast.LENGTH_SHORT).show();
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

