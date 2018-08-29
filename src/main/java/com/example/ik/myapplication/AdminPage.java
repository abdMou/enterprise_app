package com.example.ik.myapplication;

// DONE



import android.annotation.SuppressLint;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.*;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class AdminPage extends AppCompatActivity {
    Spinner statue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        Intent intent = getIntent();
        final String username = intent.getExtras().getString("admin_username");
        final String[] uuid = {""};
        final String[] uuids = {intent.getExtras().getString("uuid")};
        String offline_or_online = "ONLINE";
        if(!intent.getExtras().getString("from_file").equals("true")){
            offline_or_online = intent.getExtras().getString("offline_online");
        }
        if (offline_or_online.equals("OFFLINE")) {
            final EditText new_username_put = (EditText) findViewById(R.id.input_username);
            final EditText new_password_put = (EditText) findViewById(R.id.input_password);
            final EditText new_password2_put = (EditText) findViewById(R.id.input_password2);
            final EditText new_email_put = (EditText) findViewById(R.id.input_email);
            final EditText new_salary_put = findViewById(R.id.put_salary);
            Button signup = (Button) findViewById(R.id.put_new_user);
            ArrayList<String> statuelist = new ArrayList<>();
            statuelist.add("Active");
            statuelist.add("Abandoned");
            statuelist.add("Suspended");
            statuelist.add("Not yet accepted");
            statue = findViewById(R.id.statue);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statuelist);
            statue.setAdapter(adapter);
            int index = 0;
            signup.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int check_email=1;
                    int check_username=1;
                    if (new_username_put.getText().toString().trim().length() <= 0) {
                        new_username_put.setError("Username Cannot be empty");
                    }
                    if (new_password_put.getText().toString().trim().length() <= 0) {
                        new_password_put.setError("Password Cannot be empty");
                    }
                    if (new_password2_put.getText().toString().trim().length() <= 0) {
                        new_password2_put.setError("Password Cannot be empty");
                    }
                    if (new_email_put.getText().toString().trim().length() <= 0) {
                        new_email_put.setError("E-mail Cannot be empty");
                    }
                    if (new_salary_put.getText().toString().trim().length() <= 0) {
                        new_salary_put.setError("Salary Cannot be empty");
                    }
                    if (!(new_password_put.getText().toString().equals(new_password2_put.getText().toString()))) {
                        new_password2_put.setError("2nd Password mismatch 1st Password");
                    }

                    Sqlite sqlite=new Sqlite(AdminPage.this);
                    if(new_email_put.getText().toString().equals(sqlite.CHECK_USERNAME_EMAIL_IF_EXISTS(new_username_put.getText().toString()).second)){
                        check_email=0;
                    }
                    if(new_email_put.getText().toString().equals(sqlite.CHECK_USERNAME_EMAIL_IF_EXISTS(new_username_put.getText().toString()).second)){
                        check_email=0;
                    }
                    if(check_email==0){
                        new_email_put.setError("There is already this E-mail");

                    }
                    if(check_username==0){
                        new_username_put.setError("There is already this Username");

                    }

                    if (check_username==1 && check_email==1 &&new_username_put.getText().toString().trim().length() > 0 && new_password_put.getText().toString().trim().length() > 0 && new_password2_put.getText().toString().trim().length() > 0 && new_email_put.getText().toString().trim().length() > 0 && new_salary_put.getText().toString().trim().length() > 0 && new_password_put.getText().toString().equals(new_password2_put.getText().toString())) {

                        char[] checkemail = new_email_put.getText().toString().toCharArray();
                        int i = 0;
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
                            CNIL password = new CNIL(new_password_put.getText().toString());
                            if (password.check_CNIL()) {
                                Intent check = new Intent(getApplicationContext(), CheckAdmin.class);
                                check.putExtra("admin_username", username);
                                check.putExtra("username_modify", new_username_put.getText().toString());

                                check.putExtra("password_modify", new_password_put.getText().toString());
                                check.putExtra("offline_online","OFFLINE");
                                check.putExtra("email_modify", new_email_put.getText().toString().toLowerCase());
                                check.putExtra("salary_modify", new_salary_put.getText().toString());
                                check.putExtra("statue_modify", statue.getSelectedItem().toString());

                                startActivity(check);
                            } else {
                                new_password_put.setError("Check the password please ,\n Password should has : \n - more than 8 characters \n - One capital letter at least \n - One number at least \n - One Special character at least ");
                            }
                        } else {
                            new_email_put.setError("That's not an E-mail");
                        }
                    }

                }
            });
            final String STORING_DATE = intent.getExtras().getString("STORING_DATE");
            final String STORING_IMEI = intent.getExtras().getString("STORING_IMEI");
            final String STORING_IP = intent.getExtras().getString("STORING_IP");
            final String STORING_UUID = intent.getExtras().getString("STORING_UUID");
            Button AdminLogout = (Button) findViewById(R.id.admin_logout);
            AdminLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sqlite sqlite=new Sqlite(getApplicationContext());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String date = sdf.format(new Date());
                    sqlite.insert_logout_user_data(STORING_UUID,STORING_IMEI,STORING_IP,STORING_DATE,date);
                    Intent logout = new Intent(AdminPage.this, MainActivity.class);
                    startActivity(logout);
                    finish();
                }
            });
            Button settings = (Button) findViewById(R.id.EditUsers);

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Sorry ,you can't do that in offline mode",Toast.LENGTH_LONG).show();

                }
            });
        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        Class.forName("com.mysql.jdbc.Driver");

                        String gettinginfosquery = "{CALL user_page(?,?,?,?)}";
                        CallableStatement cs = com.example.ik.myapplication.dbconnect.dbconnection().prepareCall(gettinginfosquery);
                        cs.setString(1, username);
                        cs.registerOutParameter(2, Types.VARCHAR);
                        cs.registerOutParameter(2, Types.VARCHAR);
                        cs.registerOutParameter(3, Types.VARCHAR);
                        cs.registerOutParameter(4, Types.VARCHAR);

                        cs.execute();
                        uuid[0] = cs.getString(2);
                        Sqlite sqlite=new Sqlite(AdminPage.this);
                        ArrayList<Account> users=sqlite.send_to_mysql_users();
                        int i=0;
                        String send_user_query="{CALL insert_user_from_sqlite(?,?,?,?,?,?,?)}";
                        cs=dbconnect.dbconnection().prepareCall(send_user_query);
                        while (i<users.size()){
                            cs.setString(1,users.get(i).getUuid());
                            cs.setString(2,users.get(i).getUsername());
                            cs.setString(3,users.get(i).getPassword());
                            cs.setString(4,users.get(i).getEmail());
                            cs.setString(5,users.get(i).getSalary());
                            cs.setString(6,users.get(i).getStatue());
                            cs.setString(7,users.get(i).getRegistrationdate());
                            cs.execute();
                            i++;
                        }sqlite.delete_users_in_sqlite_after_send_it_to_mysql();

                        } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            final EditText new_username_put = (EditText) findViewById(R.id.input_username);
            final EditText new_password_put = (EditText) findViewById(R.id.input_password);
            final EditText new_password2_put = (EditText) findViewById(R.id.input_password2);
            final EditText new_email_put = (EditText) findViewById(R.id.input_email);
            final EditText new_salary_put = findViewById(R.id.put_salary);
            Button signup = (Button) findViewById(R.id.put_new_user);
            ArrayList<String> statuelist = new ArrayList<>();
            statuelist.add("Active");
            statuelist.add("Abandoned");
            statuelist.add("Suspended");
            statuelist.add("Not yet accepted ☺");
            statue = findViewById(R.id.statue);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminPage.this, android.R.layout.simple_spinner_dropdown_item, statuelist);
            statue.setAdapter(adapter);
            final int index = 0;
            //ADD USERNAME & PASSWORD & E-MAIL & ...
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Class.forName("com.mysql.jdbc.Driver");
                                dbconnect dbconnect = null;
                                String sql_check_email_password = "CALL check_username_email_exits(?,?,?,?)";
                                CallableStatement statement = dbconnect.dbconnection().prepareCall(sql_check_email_password);
                                statement.setString(1, new_username_put.getText().toString().toLowerCase());
                                statement.setString(2, new_email_put.getText().toString().toLowerCase());
                                statement.registerOutParameter(3, Types.INTEGER);
                                statement.registerOutParameter(4, Types.INTEGER);
                                statement.execute();
                                final int check_email = statement.getInt(4);
                                final int check_username = statement.getInt(3);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (new_username_put.getText().toString().trim().length() <= 0) {
                                            new_username_put.setError("Username Cannot be empty");
                                        }
                                        if (new_password_put.getText().toString().trim().length() <= 0) {
                                            new_password_put.setError("Password Cannot be empty");
                                        }
                                        if (new_password2_put.getText().toString().trim().length() <= 0) {
                                            new_password2_put.setError("Password Cannot be empty");
                                        }
                                        if (new_email_put.getText().toString().trim().length() <= 0) {
                                            new_email_put.setError("E-mail Cannot be empty");
                                        }
                                        if (new_salary_put.getText().toString().trim().length() <= 0) {
                                            new_salary_put.setError("Salary Cannot be empty");
                                        }
                                        if (!(new_password_put.getText().toString().equals(new_password2_put.getText().toString()))) {
                                            new_password2_put.setError("2nd Password mismatch 1st Password");
                                        }


                                        if (check_email == 0) {
                                            new_email_put.setError("There is already this E-mail");

                                        }
                                        if (check_username == 0) {
                                            new_username_put.setError("There is already this Username");

                                        }

                                        if (check_username == 1 && check_email == 1 && new_username_put.getText().toString().trim().length() > 0 && new_password_put.getText().toString().trim().length() > 0 && new_password2_put.getText().toString().trim().length() > 0 && new_email_put.getText().toString().trim().length() > 0 && new_salary_put.getText().toString().trim().length() > 0 && new_password_put.getText().toString().equals(new_password2_put.getText().toString())) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    char[] checkemail = new_email_put.getText().toString().toCharArray();
                                                    int i = 0;
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
                                                        CNIL password = new CNIL(new_password_put.getText().toString());
                                                        if (password.check_CNIL()) {
                                                            Intent check = new Intent(getApplicationContext(), CheckAdmin.class);
                                                            check.putExtra("uuid",uuid[0]);
                                                            check.putExtra("admin_username", username);
                                                            check.putExtra("username_modify", new_username_put.getText().toString());
                                                            check.putExtra("offline_online","ONLINE");

                                                            check.putExtra("password_modify", new_password_put.getText().toString());

                                                            check.putExtra("email_modify", new_email_put.getText().toString().toLowerCase());
                                                            check.putExtra("salary_modify", new_salary_put.getText().toString());
                                                            check.putExtra("statue_modify", statue.getSelectedItem().toString());

                                                            startActivity(check);
                                                        } else {
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
            Button AdminLogout = (Button) findViewById(R.id.admin_logout);
            // LOGOUT ADMIN
            AdminLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new
                                File(getFilesDir() + File.separator + "Uuid.txt")));
                        bufferedWriter.write(".");
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.e("uuid",String.valueOf(uuid[0]));
                                String sqlquery = "{Call drop_session(?)}";
                                CallableStatement callableStatement = dbconnect.dbconnection().prepareCall(sqlquery);
                                callableStatement.setString(1, uuids[0]);
                                callableStatement.execute();
                              } catch (SQLException e) {
                                e.printStackTrace();
                            }try {
                                Log.e("uuid",String.valueOf(uuid[0]));
                                String sqlquery = "{Call drop_session(?)}";
                                CallableStatement callableStatement = dbconnect.dbconnection().prepareCall(sqlquery);
                                callableStatement.setString(1, uuid[0]);
                                callableStatement.execute();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Intent logout = new Intent(AdminPage.this, MainActivity.class);
                    startActivity(logout);
                    finish();
                }
            });
            Button settings = (Button) findViewById(R.id.EditUsers);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AccountSettings.class);
                    Log.e("uuid ", String.valueOf(uuid[0]));
                    intent.putExtra("uuid_admin", uuid[0]);
                    startActivity(intent);
                    finish();

                }
            });
        }

    }
}


