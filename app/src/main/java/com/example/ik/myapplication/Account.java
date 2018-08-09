package com.example.ik.myapplication;



//DONE !


public class Account {
   private String uuid;
   private String Username;
    private String Password;
    private String Email;
    private String Salary;
    private String Statue;
    private String RegistrationNumber;
    private String RegistingTime;
    public Account(String uuid ,String username,String password){
        this.uuid=uuid;
        this.Username=username;
        this.Password=password;
    }

    public Account(String username ,String email){
        this.Username=username;
        this.Email=email;
    }
    public Account(String username,String password , String email, String salary , String statue){
        this.Username=username;
        this.Password=password;
        this.Email=email;
        this.Salary=salary;
        this.Statue=statue;
    }

    public Account(String uuid,String username,String password , String email, String salary , String statue){
        this.uuid=uuid;
        this.Username=username;
        this.Password=password;
        this.Email=email;
        this.Salary=salary;
        this.Statue=statue;
    }
    public Account(String username,String password , String email, String salary , String statue , String RegistrationNumber , String RegisteringTime){
        this.Username=username;
        this.Password=password;
        this.Email=email;
        this.Salary=salary;
        this.Statue=statue;
        this.RegistrationNumber=RegistrationNumber;
        this.RegistingTime=RegisteringTime;

    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }


    public String getEmail() {
        return Email;
    }

    public String getSalary() {
        return Salary;
    }

    public String getStatue() {
        return Statue;
    }



    public String getRegistrationNumber() {
        return RegistrationNumber;
    }

    public String getRegistingTime() {
        return RegistingTime;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }

    public void setStatue(String statue) {
        Statue = statue;
    }



    public void setRegistrationNumber(String registrationNumber) {
        RegistrationNumber = registrationNumber;
    }

    public void setRegistingTime(String registingTime) {
        RegistingTime = registingTime;
    }
}

