package com.example.who_nextdoor;

public class UserInfo{
    private String name; // 이름
    private String Alias; // 닉네임
    private String gender; // 성별
    private String shcoolNumber; // 학번
    private String phoneNumber; // 핸드폰
    private String birthDay; // 생일
    private String address; // 이메일

    public UserInfo(){

    }
    public UserInfo(String name, String phoneNumber, String birthDay, String Alias, String schoolNumber, String gender, String address){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.Alias = Alias;
        this.shcoolNumber = schoolNumber;
        this.gender = gender;
        this.address = address;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getBirthDay(){
        return this.birthDay;
    }
    public void setBirthDay(String birthDay){
        this.birthDay = birthDay;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAlias(){
        return this.Alias;
    }
    public void setAlias(String Alias){
        this.Alias = Alias;
    }
    public String getShcoolNumber(){
        return this.shcoolNumber;
    }
    public void setShcoolNumber(String shcoolNumber){
        this.shcoolNumber = shcoolNumber;
    }
    public String getGender(){
        return this.gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
}