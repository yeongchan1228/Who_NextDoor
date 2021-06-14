package com.example.who_nextdoor;

public class UidDataInfo implements Comparable<UidDataInfo>{

    private String uid;
    private String date;

    public UidDataInfo(String contents){
        this.uid = uid;
    }

    public UidDataInfo(){
        this.uid = uid;
    }

    public String getUid(){return this.uid;}
    public void setUid(String uid){this.uid = uid;}
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}

    @Override
    public int compareTo(UidDataInfo o) {
        return this.date.compareTo(o.date);
    }
}