package com.example.ik.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;


public class Account_state_adapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<String> username;
    private final ArrayList<Integer> state;
    private final String admin_uuid;

          public Account_state_adapter(Activity context, ArrayList<String> username, ArrayList<Integer> state,String admin_uuid){
              super(context,R.layout.account_state,username);
              this.context=context;
              this.username=username;
              this.state=state;
              this.admin_uuid=admin_uuid;


          }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       LayoutInflater inflater=context.getLayoutInflater();
       View rowView= inflater.inflate(R.layout.account_state,null,true);
       TextView username_view=rowView.findViewById(R.id.username_state);
       TextView state_view= rowView.findViewById(R.id.state_state);
       Button delete_view=rowView.findViewById(R.id.state_delete);
       Button edit_view=rowView.findViewById(R.id.state_edit);
       Button block_view=rowView.findViewById(R.id.state_block);
       username_view.setText(username.get(position));
        if(state.get(position)==1){
            state_view.setText("Active");
            int color = Color.parseColor("#04B624");
            state_view.setTextColor(color);
        }if(state.get(position)==0){
            state_view.setText("Banned");
            state_view.setTextColor(Color.RED);
        }
        delete_view.setText("DELETE");
        edit_view.setText("EDIT");
        block_view.setText("BLOCK");

        if(state.get(position)==0){
            block_view.setText("UNBLOCK");

        }
        delete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog= new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Are you sure to delete this account?")
                        .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                  //TODO : nothing to do
                            }
                        }).setNegativeButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dbconnect dbconnect=null;
                                        try {
                                            String sqlquery=("{CALL account_username(?,?,?,?,?)}");
                                            CallableStatement statement=dbconnect.dbconnection().prepareCall(sqlquery);
                                            statement.setString(1,username.get(position));
                                            statement.registerOutParameter(2, Types.VARCHAR);
                                            statement.registerOutParameter(3, Types.VARCHAR);
                                            statement.registerOutParameter(4, Types.VARCHAR);
                                            statement.registerOutParameter(5, Types.VARCHAR);
                                            statement.execute();
                                            final String uuid=statement.getString(5);
                                            sqlquery=("{CALL delete_user(?)}");
                                            statement=dbconnect.dbconnection().prepareCall(sqlquery);
                                            statement.setString(1,username.get(position));
                                            statement.execute();
                                            sqlquery=("{CALL put_in_journal(?,?,?,?)}");
                                            statement=dbconnect.dbconnection().prepareCall(sqlquery);
                                            statement.setString(1,admin_uuid);
                                            statement.setString(2,"DELETE");
                                            statement.setString(3,uuid);
                                            statement.setString(4,"");
                                            statement.execute();
                                            context.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(),"Deleted",Toast.LENGTH_LONG).show();
                                                    Intent intent=new Intent(getContext(),AccountSettings.class);
                                                    intent.putExtra("uuid_admin",admin_uuid);
                                                    context.startActivity(intent);
                                                    context.finish();

                                                }

                                            });
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).start();

                            }
                        }).show();







            }
        });
        edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbconnect dbconnect=null;
                        try {

                            String sqlquery=("{CALL account_username(?,?,?,?,?)}");
                            CallableStatement statement=dbconnect.dbconnection().prepareCall(sqlquery);
                            statement.setString(1,username.get(position));
                            statement.registerOutParameter(2, Types.VARCHAR);
                            statement.registerOutParameter(3, Types.VARCHAR);
                            statement.registerOutParameter(4, Types.VARCHAR);
                            statement.registerOutParameter(5, Types.VARCHAR);
                            statement.execute();
                            final String email=statement.getString(2);
                            final String salary=statement.getString(3);
                            final String statue=statement.getString(4);





                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent editusers=new Intent(getContext(),CheckAdmin.class);
                                    editusers.putExtra("username",username.get(position));
                                    editusers.putExtra("email",email);
                                    editusers.putExtra("salary",salary);
                                    editusers.putExtra("statue",statue);
                                    editusers.putExtra("fromaccountlist",1);
                                    editusers.putExtra("uuid_admin",admin_uuid);


                                    context.startActivity(editusers);
                                    context.finish();

                                }

                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();


            }
        });

        block_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.get(position)==0){
                    AlertDialog alertDialog= new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Want to unblock this user?")
                            .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //TODO : nothing to do
                                }
                            }).setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dbconnect dbconnect=null;
                                            String sqlquery="{CALL block_unblock(?,?)}";
                                            try {
                                                CallableStatement callableStatement=dbconnect.dbconnection().prepareCall(sqlquery);
                                                callableStatement.setString(1,username.get(position));
                                                callableStatement.setInt(2,1);
                                                callableStatement.execute();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                            context.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(),"Unblocked",Toast.LENGTH_LONG).show();
                                                        Intent intent=new Intent(getContext(),AccountSettings.class);
                                                        intent.putExtra("uuid_admin",admin_uuid);
                                                        context.startActivity(intent);
                                                        context.finish();

                                                    }

                                                });


                                        }
                                    }).start();

                                }
                            }).show();

                }
                if(state.get(position)==1){
                    AlertDialog alertDialog= new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Want to block this user?")
                            .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //TODO : nothing to do
                                }
                            }).setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dbconnect dbconnect=null;
                                            String sqlquery="{CALL block_unblock(?,?)}";

                                            try {
                                                CallableStatement callableStatement=dbconnect.dbconnection().prepareCall(sqlquery);
                                                callableStatement.setString(1,username.get(position));
                                                callableStatement.setInt(2,0);
                                                callableStatement.execute();

                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                            context.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(),"blocked",Toast.LENGTH_LONG).show();
                                                    Intent intent=new Intent(getContext(),AccountSettings.class);
                                                    intent.putExtra("uuid_admin",admin_uuid);
                                                    context.startActivity(intent);
                                                    context.finish();

                                                }

                                            });


                                        }
                                    }).start();

                                }
                            }).show();

                }







            }
        });


        return rowView;
    }
}
