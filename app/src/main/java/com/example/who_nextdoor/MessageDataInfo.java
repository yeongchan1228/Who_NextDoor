package com.example.who_nextdoor;

import java.util.Comparator;
import java.util.Date;

public class MessageDataInfo implements Comparable<MessageDataInfo>{
    private String contents;
    private String board_image;
    private String uid;
    private String date;


    public MessageDataInfo(String contents){
        this.contents = contents;
        this.board_image = "F";
    }

    public MessageDataInfo(){
        this.contents = contents;
        this.board_image = "F";
    }

    public String getContents(){
        return this.contents;
    }
    public void setContents(String contents){
        this.contents = contents;
    }
    public String getboard_image(){
        return this.board_image;
    }
    public void setboard_image(String board_image){
        this.board_image = board_image;
    }
    public String getUid(){return this.uid;}
    public void setUid(String uid){this.uid = uid;}
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}

    @Override
    public int compareTo(MessageDataInfo o) {
        return this.date.compareTo(o.date);
    }
}