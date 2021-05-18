package com.example.who_nextdoor;

public class ChatDataInfo {
    private String msg; // 메시지
    private String nickname; // 닉네임
    private String uid1;
    private String uid2;

    public String getMsg(){
        return this.msg;}
    public void setMsg(String msg){ this.msg = msg;}

    public String getNickname(){
        return this.nickname;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getUid1(){return this.uid1;}
    public void setUid1(String uid1){this.uid1 = uid1;}

    public String getUid2(){return this.uid2;}
    public void setUid2(String uid2){this.uid2 = uid2;}
}
