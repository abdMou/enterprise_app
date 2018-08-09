package com.example.ik.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//JUST ONE PART DONE !


public class CheckAdmin extends AppCompatActivity {
    String username;
    String password;
    String email;
    String salary;
    String statue;

    EditText usernameview, passwordview,password2view, emailview, salaryview;
    Spinner statueview;
    TextView confim_update;
    Button modify;

    private String md5(String text) throws NoSuchAlgorithmException {
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_admin);
        modify = (Button) findViewById(R.id.modify);
        Intent intent = getIntent();
        int fromAccount = getIntent().getExtras().getInt("fromaccountlist");
        //-------------------------------------------------------------------------------------------------------
        if (fromAccount == 1) {
            final String admin_username=getIntent().getExtras().getString("admin_username");
            username = getIntent().getExtras().getString("username");
            email = getIntent().getExtras().getString("email");
            statue = getIntent().getExtras().getString("statue");
            salary = getIntent().getExtras().getString("salary");
            confim_update = findViewById(R.id.confim_update);
            confim_update.setText("Edit Username : " + username);
            usernameview = (EditText) findViewById(R.id.usernameview);
            usernameview.setEnabled(false);
            passwordview = (EditText) findViewById(R.id.passwordview);
            passwordview.setEnabled(false);
            password2view=findViewById(R.id.passwordview2);
            password2view.setEnabled(false);
            emailview = (EditText) findViewById(R.id.emailview);
            emailview.setEnabled(false);
            salaryview = (EditText) findViewById(R.id.salaryview);
            salaryview.setEnabled(false);
            statueview = (Spinner) findViewById(R.id.statue);
            ArrayList<String> statuelist=new ArrayList<>();
            statuelist.add("Active");
            statuelist.add("Abandoned");
            statuelist.add("Suspended");
            statuelist.add("Not yet accepted");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,statuelist);
            statueview.setAdapter(adapter);
            statueview.setEnabled(false);
            statueview.setSelection(adapter.getPosition(statue));
            usernameview.setText(username);
            passwordview.setText(password);
            emailview.setText(email);
            salaryview.setText(salary);
            final Button logout = (Button) findViewById(R.id.admin_logout);
            logout.setText("Register");

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    logout.setEnabled(false);
                    passwordview.setEnabled(true);
                    password2view.setEnabled(true);
                    emailview.setEnabled(true);
                    salaryview.setEnabled(true);
                    statueview.setEnabled(true);
                    modify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (usernameview.getText().toString().trim().length() <= 0) {
                                usernameview.setError("Username Cannot be empty");
                            }
                            if (passwordview.getText().toString().trim().length() <= 0) {
                                passwordview.setError("Password Cannot be empty");
                            }
                            if (password2view.getText().toString().trim().length() <= 0) {
                                password2view.setError("Password Cannot be empty");
                            }
                            if (emailview.getText().toString().trim().length() <= 0) {
                                emailview.setError("E-mail Cannot be empty");
                            }
                            if (salaryview.getText().toString().trim().length() <= 0) {
                                salaryview.setError("Salary Cannot be empty");
                            }
                            if (!(passwordview.getText().toString().equals(password2view.getText().toString()))) {
                                password2view.setError("2nd Password mismatch 1st Password");
                            }
                            if (usernameview.getText().toString().trim().length() > 0 && passwordview.getText().toString().trim().length() > 0 && password2view.getText().toString().trim().length() > 0 && emailview.getText().toString().trim().length() > 0 && salaryview.getText().toString().trim().length() > 0 && passwordview.getText().toString().equals(password2view.getText().toString())) {

                                char[] checkemail = emailview.getText().toString().toCharArray();
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
                                if (testingemail) {
                                    CNIL PASSWORD = new CNIL(passwordview.getText().toString());
                                    if (PASSWORD.check_CNIL()) {


                                        password = passwordview.getText().toString();
                                        email = emailview.getText().toString();
                                        salary = salaryview.getText().toString();
                                        statue = statueview.getSelectedItem().toString();

                                        Intent check = new Intent(CheckAdmin.this, CheckAdmin.class);
                                        check.putExtra("username_admin_modify2", username.toLowerCase());
                                        check.putExtra("password_admin_modify2", password);
                                        check.putExtra("email_admin_modify2", email.toLowerCase());
                                        check.putExtra("salary_admin_modify2", salary);
                                        check.putExtra("statue_admin_modify2", statue);
                                        check.putExtra("fromaccountlist", 2);
                                        startActivity(check);
                                        finish();


                                    } else {
                                        passwordview.setError("Check the password please ,\n Password should has : \n - more than 8 characters \n - One capital letter at least \n - One number at least \n - One Special character at least ");
                                    }
                                } else {
                                    emailview.setError("That's not an E-mail");
                                }
                            }


                        }


                    });
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                                    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                                    Toast.makeText(getApplicationContext(), "Edited", Toast.LENGTH_LONG).show();
                                    intent.putExtra("admin_username",admin_username);   
                                    startActivity(intent);
                                    finish();
                                }
                            });







        }
        // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        if (fromAccount == 2) {
            Toast.makeText(CheckAdmin.this, "2", Toast.LENGTH_SHORT).show();
            final String admin_username=getIntent().getExtras().getString("admin_username");

            username = getIntent().getExtras().getString("username_admin_modify2");
            password = getIntent().getExtras().getString("password_admin_modify2");
            email = getIntent().getExtras().getString("email_admin_modify2");
            salary = getIntent().getExtras().getString("salary_admin_modify2");
            statue = getIntent().getExtras().getString("statue_admin_modify2");
            confim_update = findViewById(R.id.confim_update);
            confim_update.setText("Edit Username : " + username);

            usernameview = (EditText) findViewById(R.id.usernameview);
            usernameview.setEnabled(false);

            passwordview = (EditText) findViewById(R.id.passwordview);
            passwordview.setEnabled(false);

            emailview = (EditText) findViewById(R.id.emailview);
            emailview.setEnabled(false);

            password2view=findViewById(R.id.passwordview2);
            password2view.setEnabled(false);

            salaryview = (EditText) findViewById(R.id.salaryview);
            salaryview.setEnabled(false);

            statueview = (Spinner) findViewById(R.id.statue);
            ArrayList<String> statuelist=new ArrayList<>();
            statuelist.add("Active");
            statuelist.add("Abandoned");
            statuelist.add("Suspended");
            statuelist.add("Not yet accepted");
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,statuelist);
            statueview.setAdapter(adapter);
            statueview.setSelection(adapter.getPosition(statue));
            statueview.setEnabled(false);

            usernameview.setText(username);
            passwordview.setText(password);
            password2view.setText(password);
            usernameview.setText(username);

            emailview.setText(email);
            salaryview.setText(salary);

            final Button logout =findViewById(R.id.admin_logout);
            logout.setText("Register");

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logout.setEnabled(false);
                    passwordview.setEnabled(true);
                    password2view.setEnabled(true);
                    emailview.setEnabled(true);
                    salaryview.setEnabled(true);
                    statueview.setEnabled(true);
                    modify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(usernameview.getText().toString().trim().length() <= 0){
                                usernameview.setError("Username Cannot be empty");
                            }
                            if(passwordview.getText().toString().trim().length() <= 0){
                                passwordview.setError("Password Cannot be empty");
                            }
                            if(password2view.getText().toString().trim().length() <= 0){
                                password2view.setError("Password Cannot be empty");
                            }
                            if(emailview.getText().toString().trim().length() <= 0){
                                emailview.setError("E-mail Cannot be empty");
                            }
                            if(salaryview.getText().toString().trim().length() <= 0){
                                salaryview.setError("Salary Cannot be empty");
                            }if(!(passwordview.getText().toString().equals(password2view.getText().toString()))) {
                                password2view.setError("2nd Password mismatch 1st Password");
                            }
                            if(usernameview.getText().toString().trim().length() > 0 && passwordview.getText().toString().trim().length() > 0 && password2view.getText().toString().trim().length() > 0 && emailview.getText().toString().trim().length() > 0 &&  salaryview.getText().toString().trim().length() > 0 && passwordview.getText().toString().equals(password2view.getText().toString()) ){

                                char[] checkemail = emailview.getText().toString().toCharArray();
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
                                // CHECKING MISSING
                                if (testingemail) {
                                    CNIL password=new CNIL(passwordview.getText().toString());
                                    if(password.check_CNIL()){
                                        Intent check = new Intent(getApplicationContext(), CheckAdmin.class);
                                        check.putExtra("username_admin_modify2", username);

                                        check.putExtra("password_admin_modify2", passwordview.getText().toString());

                                        check.putExtra("email_admin_modify2", emailview.getText().toString());
                                        check.putExtra("salary_admin_modify2", salaryview.getText().toString());
                                        check.putExtra("statue_admin_modify2", statueview.getSelectedItem().toString());
                                        check.putExtra("fromaccountlist",2);

                                        startActivity(check);
                                    }else{
                                        passwordview.setError("Check the password please ,\n Password should has : \n - more than 8 characters \n - One capital letter at least \n - One number at least \n - One Special character at least ");
                                    }
                                } else {
                                    emailview.setError("That's not an E-mail");
                                }

                            }
                        }
                    });

                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dbconnect dbconnect=null;

                                String SQLQUERY="{CALL edit_user(?,?,?,?,?)}";
                                CallableStatement statement=dbconnect.dbconnection().prepareCall(SQLQUERY);
                                statement.setString(1,username.toLowerCase());
                                statement.setString(2,md5(password));
                                statement.setString(3,email.toLowerCase());
                                statement.setString(4,salary);
                                statement.setString(5,statue);
                                statement.execute();
                            } catch (NoSuchAlgorithmException | SQLException e) {

                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CheckAdmin.this, "Successful Update", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                                    intent.putExtra("admin_username",admin_username);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }).start();
                }

            });
        }
        // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        if (fromAccount != 1 && fromAccount != 2) {
            final String admin_username=getIntent().getExtras().getString("admin_username");
            username = getIntent().getExtras().getString("username_modify");
            password = getIntent().getExtras().getString("password_modify");
            email = getIntent().getExtras().getString("email_modify");
            salary = getIntent().getExtras().getString("salary_modify");
            statue = getIntent().getExtras().getString("statue_modify");
            confim_update = findViewById(R.id.confim_update);
            usernameview = (EditText) findViewById(R.id.usernameview);
            usernameview.setEnabled(false);
            passwordview = (EditText) findViewById(R.id.passwordview);
            passwordview.setEnabled(false);
            password2view = (EditText) findViewById(R.id.passwordview2);
            password2view.setEnabled(false);
            emailview = (EditText) findViewById(R.id.emailview);
            emailview.setEnabled(false);
            salaryview = (EditText) findViewById(R.id.salaryview);
            salaryview.setEnabled(false);
            statueview = (Spinner) findViewById(R.id.statue);
            ArrayList<String> statuelist=new ArrayList<>();
            statuelist.add("Active");
            statuelist.add("Abandoned");
            statuelist.add("Suspended");
            statuelist.add("Not yet accepted");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,statuelist);
            statueview.setAdapter(adapter);
            statueview.setSelection(adapter.getPosition(statue));
            statueview.setEnabled(false);
            usernameview.setText(usernameview.getText().toString() + username);
            emailview.setText(emailview.getText().toString() + email);
            salaryview.setText(salaryview.getText().toString() + salary);
            final Button logout = (Button) findViewById(R.id.admin_logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss a");
                            String date = sdf.format(new Date());
                            try {
                                dbconnect dbconnect=null;
                                String insert_sqlquery="{CALL insert_newuser(?,?,?,?,?,?)}";
                                CallableStatement statement=dbconnect.dbconnection().prepareCall(insert_sqlquery);
                                statement.setString(1,username.toLowerCase());
                                statement.setString(2,md5(password));
                                statement.setString(3,email.toLowerCase());
                                statement.setString(4,salary);
                                statement.setString(5,statue);
                                statement.setString(6,date);
                                statement.execute();


                            } catch (NoSuchAlgorithmException | SQLException  e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CheckAdmin.this, "Successful Registration", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                                    intent.putExtra("admin_username",admin_username);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }).start();
                }
            });
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirm(view);
                    logout.setEnabled(false);
                }
            });
        }
    }
    @SuppressLint("SimpleDateFormat")
    public void confirm(View view) {
        usernameview.setText(username);
        passwordview.setText("");
        password2view.setText("");
        emailview.setText(email);
        salaryview.setText(salary);
        confim_update.setText("Update User");
        usernameview.setEnabled(true);
        passwordview.setEnabled(true);
        password2view.setEnabled(true);
        emailview.setEnabled(true);
        salaryview.setEnabled(true);
        statueview.setEnabled(true);
        modify.setText("Register");
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(usernameview.getText().toString().trim().length() <= 0){
                    usernameview.setError("Username Cannot be empty");
                }
                if(passwordview.getText().toString().trim().length() <= 0){
                    passwordview.setError("Password Cannot be empty");
                }
                if(password2view.getText().toString().trim().length() <= 0){
                    password2view.setError("Password Cannot be empty");
                }
                if(emailview.getText().toString().trim().length() <= 0){
                    emailview.setError("E-mail Cannot be empty");
                }
                if(salaryview.getText().toString().trim().length() <= 0){
                    salaryview.setError("Salary Cannot be empty");
                }if(!(passwordview.getText().toString().equals(password2view.getText().toString()))) {
                    password2view.setError("2nd Password mismatch 1st Password");
                }
                if(usernameview.getText().toString().trim().length() > 0 && passwordview.getText().toString().trim().length() > 0 && password2view.getText().toString().trim().length() > 0 && emailview.getText().toString().trim().length() > 0 &&  salaryview.getText().toString().trim().length() > 0 && passwordview.getText().toString().equals(password2view.getText().toString()) ){

                    char[] checkemail = emailview.getText().toString().toCharArray();
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
                    if (testingemail) {
                        CNIL password=new CNIL(passwordview.getText().toString());
                        if(password.check_CNIL()){
                            Intent check = new Intent(getApplicationContext(), CheckAdmin.class);
                            check.putExtra("username_modify", usernameview.getText().toString());
                            check.putExtra("password_modify", passwordview.getText().toString());
                            check.putExtra("email_modify", emailview.getText().toString());
                            check.putExtra("salary_modify", salaryview.getText().toString());
                            check.putExtra("statue_modify", statueview.getSelectedItem().toString());
                            startActivity(check);
                        }else{
                            passwordview.setError("Check the password please ,\n Password should has : \n - more than 8 characters \n - One capital letter at least \n - One number at least \n - One Special character at least ");
                        }
                    } else {
                        emailview.setError("That's not an E-mail");
                    }

                }
            }
        });
    }

}










