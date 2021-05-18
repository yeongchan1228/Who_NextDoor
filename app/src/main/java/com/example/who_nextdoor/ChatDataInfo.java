package com.example.who_nextdoor;

public class ChatDataInfo {
    private String msg; // 메시지
    private String nickname; // 닉네임
    private String uid;

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){ this.msg = msg;}

    public String getNickname(){
        return this.nickname;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getUid(){return this.uid;}
    public void setUid(String uid){this.uid = uid;}
}
