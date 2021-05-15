package com.example.who_nextdoor;


public class WriteInfo {
    private String title;
    private String contents;
    private String board_image;

    public WriteInfo(){
        this.title = null;
        this.contents = null;
        this.board_image = "F";
    }
    public WriteInfo(String title, String contents){
        this.title = title;
        this.contents = contents;
        this.board_image = "F";
    }
    public WriteInfo(String Board_image, String title, String contents){
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


}