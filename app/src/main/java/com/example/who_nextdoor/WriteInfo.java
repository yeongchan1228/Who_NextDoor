package com.example.who_nextdoor;

public class WriteInfo {
    private String title;
    private String contents;

    public WriteInfo(String title, String contents){
        this.title = title;
        this.contents = contents;
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

}
