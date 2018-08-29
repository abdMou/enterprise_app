package com.example.ik.myapplication;

public class Account {
    String uuid;
    String username;
    String password;
    String email;
    String salary;
    String statue;
    String registrationdate;
    Account(String uuid, String username, String password, String email, String salary, String statue, String registrationdate){
        this.uuid=uuid;
        this.username=username;
        this.password=password;
        this.email=email;
        this.salary=salary;
        this.statue=statue;
        this.registrationdate=registrationdate;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRegistrationdate() {
        return registrationdate;
    }

    public String getSalary() {
        return salary;
    }

    public String getStatue() {
        return statue;
    }

   
}
