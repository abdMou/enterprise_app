package com.example.ik.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
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

import java.io.*;
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
    // MD5 PART
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
    // END OF MD5 PART

    //IMEI PART
    String imei;
    public static boolean isMultiSimEnabled = false;
    public static List<SubscriptionInfo> subInfoList;
    public static ArrayList<String> numbers;
    private SubscriptionManager subscriptionManager;
    static final Integer PHONESTATS = 0x1;
    private final String TAG = MainActivity.class.getSimpleName();
    // END IMEI PART

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] mode_offline = {"0"};
        setContentView(R.layout.connecting);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    Class.forName("com.mysql.jdbc.Driver");
                    dbconnect Dbconnect=null;
                    Dbconnect.dbconnection();
                    mode_offline[0] = ".";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.activity_main);
                            login(mode_offline[0]);
                        }
                    });
                    } catch (SQLException e) {
                        mode_offline[0] =e.getSQLState();
                        Log.e("sqlstate",mode_offline[0]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mode_offline[0].equals("08001") || mode_offline[0].equals("08S01")){
                                    Toast.makeText(MainActivity.this,"Could not create connection to database server, maybe you don't have internet , or server is offline ,try sign-in and we will pass you to offline mode if that possible",Toast.LENGTH_LONG).show();
                                    setContentView(R.layout.activity_main);
                                    login(mode_offline[0]);
                                }
                            }
                        });

                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();









    }
    public void login(String mode_offline){
        askForPermission(Manifest.permission.READ_PHONE_STATE, PHONESTATS);
        // OFFLINE MODE START
        if(mode_offline.equals("08001") ||mode_offline.equals("08S01") ){
            final Sqlite sqlite=new Sqlite(getApplicationContext());
            final EditText input_username = findViewById(R.id.input_username);
            final EditText input_password = findViewById(R.id.input_password);
            final Button login = findViewById(R.id.login_button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String password_sqlite=sqlite.GETUSERNAME_PASSWORD_TYPE(input_username.getText().toString()).first;
                    String type_sqlite=sqlite.GETUSERNAME_PASSWORD_TYPE(input_username.getText().toString()).second;
                    if(password_sqlite == null){
                        Toast.makeText(MainActivity.this,"You can't access to this account without internet \nBecause to access to OFFLINE mode , you need to access with this account in ONLINE mode at least one time ",Toast.LENGTH_LONG).show();
                    }else{

                        try {
                            if(password_sqlite.equals(md5(input_password.getText().toString()))){
                                if(type_sqlite.equals("U")){
                                    Intent intent = new Intent(getApplicationContext(), UsersPage.class);
                                    Toast.makeText(MainActivity.this, "Welcome to your page " + input_username.getText().toString(), Toast.LENGTH_LONG).show();
                                    intent.putExtra("offline_online","OFFLINE");
                                    intent.putExtra("from_file","false");
                                    intent.putExtra("username", input_username.getText().toString().toLowerCase());
                                    intent.putExtra("salary", sqlite.GETSTATUE_SALARY(input_username.getText().toString()).first);
                                    intent.putExtra("statue", sqlite.GETSTATUE_SALARY(input_username.getText().toString()).second);
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                    String date = sdf.format(new Date());
                                    intent.putExtra("STORING_DATE",date);
                                    intent.putExtra("STORING_IMEI",imei);
                                    intent.putExtra("STORING_IP","OFFLINE");
                                    intent.putExtra("STORING_UUID",sqlite.UUID(input_username.getText().toString()));
                                    startActivity(intent);
                                    finish();
                                }
                                if(type_sqlite.equals("A")){
                                    Log.e("Admin","Confirmed");
                                    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                                    Toast.makeText(MainActivity.this, "Welcome to your page " + input_username.getText().toString(), Toast.LENGTH_LONG).show();
                                    intent.putExtra("offline_online","OFFLINE");
                                    intent.putExtra("from_file","false");
                                    intent.putExtra("admin_username", input_username.getText().toString().toLowerCase());
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                    String date = sdf.format(new Date());
                                    intent.putExtra("STORING_DATE",date);
                                    intent.putExtra("STORING_IMEI",imei);
                                    intent.putExtra("STORING_IP","OFFLINE");
                                    intent.putExtra("STORING_UUID",sqlite.UUID(input_username.getText().toString()));
                                    startActivity(intent);
                                    finish();
                                }

                            }else{
                                Toast.makeText(MainActivity.this, "Please check your login informations", Toast.LENGTH_LONG).show();

                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }




                    }

                }
            });
            // OFFLINE MODE END

            Button forgotten = findViewById(R.id.forgot);
            forgotten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this,"Sorry, you can't do that in offline mode",Toast.LENGTH_LONG).show();
                }
            });

            // ONLINE MODE START
        }if(mode_offline.equals(".")){
            final Sqlite sqlite=new Sqlite(getApplicationContext());
            ArrayList<Stored> send_mysql=new ArrayList<>();
            send_mysql=sqlite.send_to_mysql_uuid();
            // TODO : DELETE THIS AFTER TESTING
            final ArrayList<Stored> finalSend_mysql = send_mysql;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        dbconnect dbconnect=null;
                        int i=0;
                        if(finalSend_mysql.size()>0){
                            while (i< finalSend_mysql.size()){
                                String sqlquery=("{CALL from_sqlite_to_mysql(?,?,?,?,?)}");
                                CallableStatement callableStatement=dbconnect.dbconnection().prepareCall(sqlquery);
                                String uuid=finalSend_mysql.get(i).getUuid();
                                String imei=finalSend_mysql.get(i).getImei();
                                String ip=finalSend_mysql.get(i).getIp();
                                String login_date=finalSend_mysql.get(i).getLogin_date();
                                String logout_date=finalSend_mysql.get(i).getLogout_date();
                                callableStatement.setString(1,uuid);
                                callableStatement.setString(2,imei);
                                callableStatement.setString(3,ip);
                                callableStatement.setString(4,login_date);
                                callableStatement.setString(5,logout_date);
                                callableStatement.execute();
                                i++;
                            }
                            sqlite.delete_after_login_in_onlinemode();
                        }


                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            String uuid = ".";
            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new
                        File(getFilesDir()+File.separator+"Uuid.txt")));
                int i=0;
                while (i<1){
                    uuid=bufferedReader.readLine();
                    i++;
                }
                bufferedReader.close();
            } catch (IOException e){
                e.getCause();
            }
            final String finalUuid = uuid;
            if(!uuid.equals(".")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String Type_account = "";
                        String username="";
                        String salary="";
                        String statue="";
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            dbconnect Dbconnect=null;
                            String sqlquery="{CALL re_login(?,?,?,?,?)}";
                            CallableStatement cs=Dbconnect.dbconnection().prepareCall(sqlquery);
                            cs.setString(1,finalUuid);
                            cs.registerOutParameter(2,Types.VARCHAR);
                            cs.registerOutParameter(3,Types.VARCHAR);
                            cs.registerOutParameter(4,Types.VARCHAR);
                            cs.registerOutParameter(5,Types.VARCHAR);
                            cs.execute();
                            Type_account=cs.getString(2);
                            username=cs.getString(3);
                            salary=cs.getString(4);
                            statue=cs.getString(5);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            Log.e("SQLERROR", String.valueOf(e.getMessage()));
                        }
                        final String finalType_account = Type_account;
                        final String finalUsername = username;
                        final String finalSalary = salary;
                        final String finalStatue = statue;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(finalType_account.equals("A")){
                                    Intent intent=new Intent(MainActivity.this,AdminPage.class);
                                    intent.putExtra("from_file","true");
                                    intent.putExtra("admin_username", finalUsername);
                                    startActivity(intent);
                                    finish();
                                }if(finalType_account.equals("U")){
                                    Intent intent=new Intent(MainActivity.this,UsersPage.class);
                                    intent.putExtra("from_file","true");
                                    intent.putExtra("uuid", finalUuid);
                                    intent.putExtra("username", finalUsername);
                                    intent.putExtra("salary", finalSalary);
                                    intent.putExtra("statue", finalStatue);

                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });
                    }
                }).start();
            }else{
                final int[] Counter = {0};

                setContentView(R.layout.activity_main);
                final EditText input_username = findViewById(R.id.input_username);
                final EditText input_password = findViewById(R.id.input_password);
                final Button login = findViewById(R.id.login_button);
                login.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        askForPermission(Manifest.permission.READ_PHONE_STATE, PHONESTATS);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Class.forName("com.mysql.jdbc.Driver");
                                    final dbconnect dbconnect = null;
                                    String query = "{CALL login_user(?,?,?,?,?)}";
                                    CallableStatement cs = com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(query);
                                    cs.setString(1, input_username.getText().toString());
                                    cs.setString(2, md5(input_password.getText().toString()));
                                    cs.registerOutParameter(3, Types.VARCHAR);
                                    cs.registerOutParameter(4, Types.INTEGER);
                                    cs.registerOutParameter(5, Types.INTEGER);
                                    cs.execute();
                                    String block = "{CALL block_user(?,?,?)}";
                                    CallableStatement callableStatement = com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(block);
                                    callableStatement.setString(1, input_username.getText().toString());
                                    callableStatement.setInt(2,0);
                                    callableStatement.registerOutParameter(3, Types.VARCHAR);
                                    callableStatement.execute();

                                    if(callableStatement.getInt(3)==1 && Counter[0]<3){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MainActivity.this,"You can't Access With This Blocked Account , Please Contact the admin to unblock your account",Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }else{
                                        Counter[0]++;

                                        if(Counter[0]>=3){
                                            callableStatement.setString(1, input_username.getText().toString());
                                            callableStatement.setString(2,"Block");
                                            callableStatement.registerOutParameter(3, Types.VARCHAR);
                                            callableStatement.execute();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MainActivity.this,"You can't Access With This Account , Because You entered Wrong Password 3 Times And got blocked , Please Wait Until the admin unblock your Account or Contact the admin",Toast.LENGTH_LONG).show();

                                                }
                                            });
                                        }else{
                                            if(cs.getInt(5)==1){

                                                if (cs.getInt(4) == 1) {
                                                    String Usertype=cs.getString(3);
                                                    if (Usertype.equals("A")) {
                                                        String uuid = null;
                                                        int Allow = 0;

                                                        String gettinginfosquery = "{CALL user_page(?,?,?,?)}";
                                                        cs = com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(gettinginfosquery);
                                                        cs.setString(1, input_username.getText().toString());
                                                        cs.registerOutParameter(2, Types.VARCHAR);
                                                        cs.execute();
                                                        uuid = cs.getString(2);
                                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                                        // GET DATE AND STORE IT IN DATABASE
                                                        String date = sdf.format(new Date());
                                                        String allowlogin_query = "{CALL sessions_add(?,?,?,?,?)}";
                                                        CallableStatement callableStatement1 = com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(allowlogin_query);
                                                        callableStatement1.setString(1, uuid);
                                                        callableStatement1.setString(2, imei);
                                                        callableStatement1.setString(3, IP());
                                                        callableStatement1.setString(4, date);
                                                        callableStatement1.registerOutParameter(5, Types.INTEGER);
                                                        callableStatement1.execute();
                                                        Allow = callableStatement1.getInt(5);
                                                        final int finalAllow = Allow;
                                                        if(finalAllow==1){
                                                            Sqlite sqlite=new Sqlite(getApplicationContext());
                                                            sqlite.user_insert_infos(uuid,input_username.getText().toString(),md5(input_password.getText().toString()),null,null,"A");
                                                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
                                                                    File(getFilesDir()+File.separator+"Uuid.txt")));
                                                            bufferedWriter.write(uuid);
                                                            bufferedWriter.close();
                                                        }
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                if (finalAllow == 1) {
                                                                    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                                                                    Toast.makeText(MainActivity.this, "WELCOME TO ADMIN PAGE", Toast.LENGTH_LONG).show();
                                                                    intent.putExtra("admin_username", input_username.getText().toString().toLowerCase());
                                                                    intent.putExtra("from_file","false");
                                                                    intent.putExtra("offline_online","ONLINE");
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(MainActivity.this, "There is someone else logged-in with this Account , You can't Log-in Until the other one logged-out !", Toast.LENGTH_LONG).show();

                                                                }

                                                            }
                                                        });
                                                    }


                                                    if (Usertype.equals("U")) {
                                                        String uuid = null;
                                                        String salary = null;
                                                        String statue = null;
                                                        int Allow = 0;
                                                        String gettinginfos_query = "CALL user_page(?,?,?,?)";
                                                        cs = com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(gettinginfos_query);
                                                        cs.setString(1, (input_username.getText().toString()).toLowerCase());
                                                        cs.registerOutParameter(2, Types.VARCHAR);
                                                        cs.registerOutParameter(3, Types.VARCHAR);
                                                        cs.registerOutParameter(4, Types.VARCHAR);
                                                        cs.execute();
                                                        uuid = cs.getString(2);
                                                        salary = cs.getString(3);
                                                        statue = cs.getString(4);
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

                                                        // GET DATE AND STORE IT IN DATABASE
                                                        String date = sdf.format(new Date());
                                                        String allow_login_query = "{CALL sessions_add(?,?,?,?,?)}";
                                                        cs = com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(allow_login_query);
                                                        cs.setString(1, uuid);
                                                        cs.setString(2, imei);
                                                        cs.setString(3, IP());
                                                        cs.setString(4, date);
                                                        cs.registerOutParameter(5, Types.INTEGER);
                                                        cs.execute();
                                                        Allow = cs.getInt(5);

                                                        final String finalSalary = salary;
                                                        final String finalStatue = statue;
                                                        final int finalAllow = Allow;
                                                        if (finalAllow == 1) {
                                                            Sqlite sqlite=new Sqlite(getApplicationContext());
                                                            sqlite.user_insert_infos(uuid,input_username.getText().toString(),md5(input_password.getText().toString()),salary,statue,"U");
                                                        }
                                                        final String finalUuid1 = uuid;
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                BufferedWriter bufferedWriter = null;
                                                                try {
                                                                    bufferedWriter = new BufferedWriter(new FileWriter(new
                                                                            File(getFilesDir()+File.separator+"Uuid.txt")));
                                                                    bufferedWriter.write(finalUuid1);
                                                                    bufferedWriter.close();
                                                                }
                                                                catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                if (finalAllow == 1) {
                                                                    Intent intent = new Intent(getApplicationContext(), UsersPage.class);
                                                                    Toast.makeText(MainActivity.this, "Welcome to your page " + input_username.getText().toString(), Toast.LENGTH_LONG).show();
                                                                    // get informations
                                                                    intent.putExtra("from_file","false");
                                                                    intent.putExtra("offline_online","ONLINE");
                                                                    intent.putExtra("uuid", finalUuid1);
                                                                    intent.putExtra("username", input_username.getText().toString().toLowerCase());
                                                                    intent.putExtra("salary", finalSalary);
                                                                    intent.putExtra("statue", finalStatue);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(MainActivity.this, "There is someone else logged-in with this Account , You can't Log-in Until the other one logged-out !", Toast.LENGTH_LONG).show();

                                                                }


                                                            }


                                                        });
                                                    }
                                                }else{

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            if(Counter[0]<3){
                                                                Toast.makeText(MainActivity.this,"Please Check Your Username or Password",Toast.LENGTH_LONG).show();

                                                            }


                                                        }
                                                    });
                                                }
                                            }else{
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        if(Counter[0]<3){
                                                            Toast.makeText(MainActivity.this,"Please Check Your Username or Password",Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });
                                            }
                                        }



                                    }




                                } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException | SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                });
                // ONLINE MODE END


            }
            Button forgotten = findViewById(R.id.forgot);
            forgotten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent forgotten = new Intent(getApplicationContext(), ForgottenPasswordActivity.class);
                    startActivity(forgotten);
                    finish();

                }
            });
        }
    }
    public String IP() throws IOException {
       // URL whatismyip = new URL("http://checkip.amazonaws.com");
      //  BufferedReader in = new BufferedReader(new InputStreamReader(
       //         whatismyip.openStream()));

       // String ip = in.readLine();
        return "0";
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







}

