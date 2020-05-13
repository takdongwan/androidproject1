package com.example.mure;

import android.widget.EditText;

public class MemberInfoActivity {
    private  String name;
    private  String phoneNumber;
    private  String birtyDay;
    private  String address;
    private  String photoUrl;

    public MemberInfoActivity(String name, String phoneNumber, String birtyDay, String address,String photoUrl){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.birtyDay=birtyDay;
        this.address=address;
        this.photoUrl=photoUrl;
    }
    public MemberInfoActivity(String name, String phoneNumber, String birtyDay, String address){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.birtyDay=birtyDay;
        this.address=address;

    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name= name;
    }
    //
    public String getphoneNumber(){
        return this.phoneNumber;
    }
    public void setphoneNumber(String phoneNumber){
        this.phoneNumber= phoneNumber;
    }
    //
    public String getbirtyDay(){
        return this.birtyDay;
    }
    public void setbirtyDay(String birtyDay){
        this.birtyDay= birtyDay;
    }
    //
    public String getaddress(){
        return this.address;
    }
    public void setaddress(String address){
        this.address= address;
    }

    public  String getPhotoUrl(){ return this.photoUrl;}
    public void setPhotoUrl(String photoUrl){this.photoUrl= photoUrl;}

}
