package com.example.who_nextdoor;


import java.util.Comparator;
import java.util.Date;

public class informationInfo implements Comparable<informationInfo> {
    private String title;
    private String contents;
    private String board_image;
    private String uid;
    private String date;

    public informationInfo(){
        this.title = null;
        this.contents = null;
        this.board_image = "F";
    }
    public informationInfo(String title, String contents){
        this.title = title;
        this.contents = contents;
        this.board_image = "F";
    }
    public informationInfo(String Board_image, String title, String contents){
        this.title = title;
        this.contents = contents;
        this.board_image = Board_image;
    }


    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
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
    public int compareTo(informationInfo o) {
        return this.date.compareTo(o.date);
    }
}