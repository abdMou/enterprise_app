package com.example.ik.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Sqlite{

    db DbUser;

    public Sqlite(Context applicationContext) {
        DbUser =new db(applicationContext);
    }
    public long insert_logout_user_data(String uuid,String IMEI,String IP ,String LOGIN_DATE,String LOGOUT_DATE){
        SQLiteDatabase sqLiteDatabase= DbUser.getWritableDatabase();
        ContentValues values=new ContentValues();

            values.put(DbUser.STORING_UUID,uuid);
            values.put( DbUser.STORING_IMEI,IMEI);
            values.put( DbUser.STORING_IP,IP);
            values.put( DbUser.STORING_LOGIN_DATE,LOGIN_DATE);
            values.put( DbUser.STORING_LOGOUT_DATE,LOGOUT_DATE);
            return sqLiteDatabase.insert( DbUser.TABLE_STORING_NAME,null,values );




    }//TODO:---------------------------------------------------------------------
    public ArrayList<Account> send_to_mysql_users(){
        SQLiteDatabase sqLiteDatabase=DbUser.getReadableDatabase();
        ArrayList<Account> admin_add=new ArrayList<>();
        Cursor  cursor = sqLiteDatabase.rawQuery("select * from "+db.TABLE_ADMIN_NAME,null);
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()){
                int index1=cursor.getColumnIndex(DbUser.UUID_ADMIN);
                int index2=cursor.getColumnIndex(DbUser.USERNAME_ADMIN);
                int index3=cursor.getColumnIndex(DbUser.PASSWORD_ADMIN);
                int index4=cursor.getColumnIndex(DbUser.EMAIL_ADMIN);
                int index5=cursor.getColumnIndex(DbUser.SALARY_ADMIN);
                int index6=cursor.getColumnIndex(DbUser.STATUE_ADMIN);
                int index7=cursor.getColumnIndex(DbUser.REGISTERINGDATE);

                admin_add.add(new Account(cursor.getString(index1),cursor.getString(index2),cursor.getString(index3),cursor.getString(index4),cursor.getString(index5),cursor.getString(index6),cursor.getString(index7)));
                cursor.moveToNext();
            }
        }
        return admin_add;
    }
    public void delete_users_in_sqlite_after_send_it_to_mysql(){
        //TODO : DELETING STORED INFORMATIONS AT OFFLINE MODE IN SQLITE DATABASE AFTER SEND IT TO MYSQL DATABASE :)
        SQLiteDatabase db = DbUser.getWritableDatabase();
        db.execSQL("delete from "+ DbUser.TABLE_ADMIN_NAME);
        db.close();
    }
    //TODO:---------------------------------------------------------------------
    public ArrayList<Stored> send_to_mysql_uuid(){
        SQLiteDatabase sqLiteDatabase=DbUser.getReadableDatabase();
        ArrayList<Stored> stored_list=new ArrayList<>();
        Cursor  cursor = sqLiteDatabase.rawQuery("select * from "+db.TABLE_STORING_NAME,null);
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()){
            int index1=cursor.getColumnIndex(DbUser.STORING_UUID);

            int index2=cursor.getColumnIndex(DbUser.STORING_IMEI);
            int index3=cursor.getColumnIndex(DbUser.STORING_IP);
            int index4=cursor.getColumnIndex(DbUser.STORING_LOGIN_DATE);
            int index5=cursor.getColumnIndex(DbUser.STORING_LOGOUT_DATE);
            stored_list.add(new Stored(cursor.getString(index1),cursor.getString(index2),cursor.getString(index3),cursor.getString(index4),cursor.getString(index5)));
            cursor.moveToNext();
          }
        }
        return stored_list;
    }

    //TODO:--------------------------------------------------------------------
    public void delete_after_login_in_onlinemode(){
        //TODO : DELETING STORED INFORMATIONS AT OFFLINE MODE IN SQLITE DATABASE AFTER SEND IT TO MYSQL DATABASE :)
        SQLiteDatabase db = DbUser.getWritableDatabase();
        db.execSQL("delete from "+ DbUser.TABLE_STORING_NAME);
        db.close();
      }

    public long user_insert_infos(String uuid,String username,String password ,String salary,String statue,String Type){
        SQLiteDatabase sqLiteDatabase= DbUser.getWritableDatabase();
        ContentValues values=new ContentValues();
        if(!GETUSERNAME_PASSWORD_TYPE(username).first.equals("")){
            return 0L;
            //TODO: NOTHING
        }else{
            values.put(DbUser.UUID,uuid);
            values.put( DbUser.USERNAME,username);
            values.put( DbUser.PASSWORD,password);
            values.put( DbUser.SALARY,salary);
            values.put( DbUser.STATUE,statue);
            values.put( DbUser.TYPE,Type);
            return sqLiteDatabase.insert( DbUser.TABLE_NAME,null,values );

        }


    }

    public long insert_admin_users(String uuid,String username,String password,String email ,String salary,String statue,String registrationdate){
        SQLiteDatabase sqLiteDatabase= DbUser.getWritableDatabase();
        ContentValues values=new ContentValues();
        if(!CHECK_USERNAME_EMAIL_IF_EXISTS(username).second.equals("")){
            return 0L;
            //TODO: NOTHING
        }else{
            values.put(DbUser.UUID_ADMIN,uuid);
            values.put( DbUser.USERNAME_ADMIN,username);
            values.put( DbUser.PASSWORD_ADMIN,password);
            values.put( DbUser.EMAIL_ADMIN,email);
            values.put( DbUser.SALARY_ADMIN,salary);
            values.put( DbUser.STATUE_ADMIN,statue);
            values.put( DbUser.REGISTERINGDATE,registrationdate);

            return sqLiteDatabase.insert( DbUser.TABLE_ADMIN_NAME,null,values );

        }


    }
     //TODO: DO IT LATER :)
    public String UUID(String username_put){
        SQLiteDatabase sqLiteDatabase=DbUser.getReadableDatabase();
        String[] columns={DbUser.UUID,DbUser.USERNAME,DbUser.PASSWORD,DbUser.SALARY,DbUser.STATUE};
        String uuid=null;
        Cursor cursor=sqLiteDatabase.query(DbUser.TABLE_NAME,columns,DbUser.USERNAME+" = '"+username_put+"'",null, null,null,null);
        while(cursor.moveToNext()){
             int index1=cursor.getColumnIndex(DbUser.UUID);
            uuid=cursor.getString(index1);
        }
        return uuid;

    }


    public Pair<String, String> GETUSERNAME_PASSWORD_TYPE(String username_put){
        SQLiteDatabase sqLiteDatabase= DbUser.getReadableDatabase();
        String[] columns={DbUser.USERNAME,DbUser.PASSWORD,DbUser.SALARY,DbUser.STATUE,DbUser.TYPE};
        Cursor cursor=sqLiteDatabase.query(DbUser.TABLE_NAME,columns,DbUser.USERNAME+" = '"+username_put+"'",null,null,null,null);
        String password="";
        String type="";
        while (cursor.moveToNext()){
            int index1=cursor.getColumnIndex(DbUser.PASSWORD);
            int index2=cursor.getColumnIndex(DbUser.TYPE);
            password=cursor.getString(index1);
            type=cursor.getString(index2);
        }
        return new Pair<>(password,type);
    }

    public Pair<String, String> CHECK_USERNAME_EMAIL_IF_EXISTS(String username_put){
        SQLiteDatabase sqLiteDatabase= DbUser.getReadableDatabase();
        String[] columns={DbUser.UUID_ADMIN,DbUser.USERNAME_ADMIN,DbUser.PASSWORD_ADMIN,DbUser.EMAIL_ADMIN,DbUser.SALARY_ADMIN,DbUser.STATUE_ADMIN,DbUser.REGISTERINGDATE};
        Cursor cursor=sqLiteDatabase.query(DbUser.TABLE_ADMIN_NAME,columns,DbUser.USERNAME+" = '"+username_put+"'",null,null,null,null);
        String username="";
        String email="";
        while (cursor.moveToNext()){
            int index1=cursor.getColumnIndex(DbUser.USERNAME_ADMIN);
            int index2=cursor.getColumnIndex(DbUser.EMAIL_ADMIN);
            username=cursor.getString(index1);
            email=cursor.getString(index2);
        }
        return new Pair<>(username,email);
    }

/*    TODO: PROBABLY THIS WILL BE FOR ADMIN , SO CHECK IT LATER :)
      public Pair<Boolean, Boolean> CHECK_USERNAME_EMAIL_IF_AVAILABLE(String username_put,String email_put){
        SQLiteDatabase sqLiteDatabase= Dbinfo.getReadableDatabase();
        String[] columns={Dbinfo.USERNAME,Dbinfo.PASSWORD,Dbinfo.SALARY,Dbinfo.STATUE};
        Cursor cursor=sqLiteDatabase.query(Dbinfo.TABLE_NAME,columns,Dbinfo.USERNAME+" = '"+username_put+"'",null,null,null,null);
        String username="";
        String email="";
        while (cursor.moveToNext()){
            int index1=cursor.getColumnIndex(Dbinfo.USERNAME);
            int index2=cursor.getColumnIndex(Dbinfo.PASSWORD);
            username=cursor.getString(index1);
            email=cursor.getString(index2);
        }
        Boolean checkusername=false;
        Boolean checkemail=false;
        if(!username_put.equals(username)){
            checkusername=true;
        }
        if(!email_put.equals(email)){
            checkemail=true;
        }
        return new Pair<>(checkusername,checkemail);
    }

*/

    public Pair<String, String> GETSTATUE_SALARY(String username_put){
        SQLiteDatabase sqLiteDatabase= DbUser.getReadableDatabase();
        String[] columns={DbUser.USERNAME,DbUser.PASSWORD,DbUser.SALARY,DbUser.STATUE};
        Cursor cursor=sqLiteDatabase.query(DbUser.TABLE_NAME,columns,DbUser.USERNAME+" = '"+username_put+"'",null,null,null,null);
        String salary=null;
        String statue=null;
        while (cursor.moveToNext()){
            int index1=cursor.getColumnIndex(DbUser.SALARY);
            int index2=cursor.getColumnIndex(DbUser.STATUE);
            salary=cursor.getString(index1);
            statue=cursor.getString(index2);
        }
        return new Pair<>(salary,statue);
    }


    static class db extends SQLiteOpenHelper {
        private static final String DATABASE_NAME="offline_mode";
        private static final int DATABASE_VERSION=119;

        //----------------------------------------------- ADMIN USER
        private static final String TABLE_ADMIN_NAME="Admin";
        private static final String UUID_ADMIN="UUID";
        private static final String USERNAME_ADMIN="Username";
        private static final String PASSWORD_ADMIN="Password";
        private static final String EMAIL_ADMIN="Email";
        private static final String SALARY_ADMIN="Salary";
        private static final String STATUE_ADMIN="Statue";
        private static final String REGISTERINGDATE="RegistrationDate";
        private static final String DROPTABLE_ADMIN="DROP TABLE IF EXISTS "+TABLE_ADMIN_NAME;
        private static final String CREATE_ADMIN_TABLE="CREATE TABLE "+TABLE_ADMIN_NAME+
                " ("+UUID_ADMIN+" VARCHAR(40), "+USERNAME_ADMIN+" VARCHAR(30), "+PASSWORD_ADMIN+" VARCHAR(32), "+EMAIL_ADMIN+" VARCHAR(100), "+SALARY_ADMIN+" VARCHAR(30), "+STATUE_ADMIN+" VARCHAR(30), "+REGISTERINGDATE+" VARCHAR(100));";

        //----------------------------------------------- NORMAL USER
        private static final String TABLE_NAME="Users";
        private static final String UUID="UUID";
        private static final String USERNAME="Username";
        private static final String PASSWORD="Password";
        private static final String SALARY="Salary";
        private static final String STATUE="Statue";
        private static final String TYPE="TYPE";

        private static final String DROPTABLE="DROP TABLE IF EXISTS "+TABLE_NAME;
        private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+
                " ("+UUID+" VARCHAR(30), "+USERNAME+" VARCHAR(30), "+PASSWORD+" VARCHAR(32), "+SALARY+" VARCHAR(30), "+STATUE+" VARCHAR(30), "+TYPE+" VARCHAR(1));";

        //----------------------------------------------- LOGIN/LOGOUT DATA
        private static final String TABLE_STORING_NAME="Users_Log";
        private static final String STORING_UUID="UUID";
        private static final String STORING_IMEI="IMEI";
        private static final String STORING_IP="IP";
        private static final String STORING_LOGIN_DATE="LOGINDATE";
        private static final String STORING_LOGOUT_DATE="LOGOUTDATE";
        private static final String STORING_DROPTABLE="DROP TABLE IF EXISTS "+TABLE_STORING_NAME;
        private static final String STORING_CREATE_TABLE="CREATE TABLE "+TABLE_STORING_NAME+
                " ("+STORING_UUID+" VARCHAR(50), "+STORING_IMEI+" VARCHAR(50), "+STORING_IP+" VARCHAR(50), "+STORING_LOGIN_DATE+" VARCHAR(50), "+STORING_LOGOUT_DATE+" VARCHAR(50));";

        private Context context;
        db(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try{
                sqLiteDatabase.execSQL(CREATE_ADMIN_TABLE);
                sqLiteDatabase.execSQL(CREATE_TABLE);
                sqLiteDatabase.execSQL(STORING_CREATE_TABLE);
            }catch (SQLException e){
                e.getMessage();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try{
                sqLiteDatabase.execSQL(DROPTABLE);
                sqLiteDatabase.execSQL(STORING_DROPTABLE);
                sqLiteDatabase.execSQL(DROPTABLE_ADMIN);
                onCreate(sqLiteDatabase);
            }catch (SQLException e){
                e.getMessage();
            }
        }

    }


}