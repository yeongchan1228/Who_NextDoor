package com.example.who_nextdoor;

public class UserInfo{
    private String name; // 이름
    private String Alias; // 닉네임
    private String gender; // 성별
    private String shcoolNumber; // 학번
    private String phoneNumber; // 핸드폰
    private String birthDay; // 생일
    private String address; // 이메일
    private String access; // 인증 여부
    private String department;
    private String first; // 첫 번째 회원 정보 입력을 했는지 판단
    private int temperature; // 매너 온도 구현

    public UserInfo(){
        this.name = null;
        this.phoneNumber = null;
        this.birthDay = null;
        this.Alias = null;
        this.shcoolNumber = null;
        this.gender = null;
        this.address = null;
        this.access = "F";
        this.first = "F";
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getAccess(){
        return this.access;
    }
    public void setAccess(String access){
        this.access = access;
    }
    public String getFirst(){
        return this.first;
    }
    public void setFirst(String first){
        this.first = first;
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
    public String getDepartment(){
        return this.department;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public int getTemperature(){
        return this.temperature;
    }
    public void setTemperature(int temperature){
        this.temperature = temperature;
    }
}