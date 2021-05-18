package com.example.who_nextdoor;

public class ChatDataInfo implements Comparable<ChatDataInfo>{
    private String msg; // 메시지
    private String nickname; // 닉네임
    private String uid;
    private String date;

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
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}

    @Override
    public int compareTo(ChatDataInfo o) {
        return this.date.compareTo(o.date);
    }
}
