package com.example.ik.myapplication;

public class Stored {
    String  uuid;
    String imei;
    String ip;
    String login_date;
    String logout_date;
    Stored (String uuid,String imei,String ip,String login_date,String logout_date){
           this.uuid=uuid;
           this.imei=imei;
           this.ip=ip;
           this.login_date=login_date;
           this.logout_date=logout_date;
    }

    public String getUuid() {
        return uuid;
    }

    public String getImei() {
        return imei;
    }

    public String getIp() {
        return ip;
    }

    public String getLogin_date() {
        return login_date;
    }

    public String getLogout_date() {
        return logout_date;
    }
}
