package com.example.electrocity;

public class User {

    public String fullName, phoneNumber, email , option;

    public User(){

    }

    public User(String fullName, String phoneNumber, String email){
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public User(String fullName, String phoneNumber, String email , String option){
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.option = option;
    }


}
