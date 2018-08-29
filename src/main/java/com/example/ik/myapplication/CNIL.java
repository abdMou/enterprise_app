package com.example.ik.myapplication;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

//DONE

public class CNIL extends Activity {

    private String password;

    public CNIL(String password) {
        this.password=password;

    }
    public boolean check_CNIL() {
        if (password.length() >= 8) {
            int i = 0;
            boolean uppercase = false;
            boolean number = false;
            boolean notletter = false;
            char ch;
            while (i < password.length()) {
                ch = password.charAt(i);
                if (Character.isUpperCase(ch)) {
                    uppercase = true;
                }
                if (Character.isDigit(ch)) {
                    number = true;
                }
                if (!Character.isLetter(ch) && !Character.isDigit(ch)) {
                    notletter = true;

                }
                i++;
            }

            if(uppercase && number && notletter){
                return true;
            }

        }

        return false;

    }

}
