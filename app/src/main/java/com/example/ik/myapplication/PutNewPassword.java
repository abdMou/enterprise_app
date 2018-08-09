package com.example.ik.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PutNewPassword extends AppCompatActivity {
    public String md5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(text.getBytes());
        byte[] digest= messageDigest.digest();
        BigInteger bigInt=new BigInteger(1,digest);
        String hashtext=bigInt.toString(16);
        while (hashtext.length()<32){
            hashtext="0"+hashtext;
        }
        return (hashtext);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_new_password);
        final String username=getIntent().getExtras().getString("username");
        final EditText newpassword=findViewById(R.id.newpassword);
        final int passwordlength=newpassword.getText().toString().trim().length();
        Button putnewpassword=findViewById(R.id.putnewpassword);
        putnewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Boolean[] Allow_modify_password = {false};
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (newpassword.getText().toString().trim().length() <= 0) {
                                    newpassword.setError("Password Cannot be empty");
                                }

                                if (newpassword.getText().toString().trim().length() > 0) {

                                    CNIL password = new CNIL(newpassword.getText().toString());
                                    if (password.check_CNIL()) {
                                       Allow_modify_password[0] =true;
                                       new Thread(new Runnable() {
                                           @Override
                                           public void run() {
                                               if(Allow_modify_password[0]){
                                                   dbconnect Dbconnect=null;
                                                   try {
                                                       String SqlQuery="{CALL put_new_password(?,?)}";
                                                       CallableStatement callableStatement=dbconnect.dbconnection().prepareCall(SqlQuery);
                                                       callableStatement.setString(1,username);
                                                       callableStatement.setString(2,md5(newpassword.getText().toString()));
                                                       callableStatement.execute();

                                                       runOnUiThread(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               Intent intent =new Intent(PutNewPassword.this,MainActivity.class);
                                                               Toast.makeText(PutNewPassword.this,"Password Changed",Toast.LENGTH_LONG).show();
                                                               startActivity(intent);
                                                               finish();
                                                           }
                                                       });
                                                   } catch (SQLException | NoSuchAlgorithmException e) {
                                                       e.printStackTrace();
                                                   }

                                               }

                                           }
                                       }).start();

                                    } else {
                                        newpassword.setError("Check the password please ,\n Password should has : \n - more than 8 characters \n - One capital letter at least \n - One number at least \n - One Special character at least ");

                                    }


                                }


                            }

                        });




                    }

                }).start();



            }
        });
    }
}
