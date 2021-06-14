package com.example.who_nextdoor;


public class TradeInfo implements Comparable<TradeInfo>{
    private String title;
    private String contents;
    private String price;
    private String board_image;
    private String uid;
    private String date;
    private String Alias;
    private String email;

    public TradeInfo(){
        this.title = null;
        this.contents = null;
        this.price = null;
        this.board_image = "F";
    }
    public TradeInfo(String title, String contents, String price){
        this.title = title;
        this.contents = contents;
        this.price = price;
        this.board_image = "F";
    }
    public TradeInfo(String Board_image, String title, String contents, String price){
        this.title = title;
        this.contents = contents;
        this.price = price;
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
    public String getPrice(){
        return this.price;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public String getUid(){return this.uid;}
    public void setUid(String uid){this.uid = uid;}
    public String getboard_image(){
        return this.board_image;
    }
    public void setboard_image(String board_image){
        this.board_image = board_image;
    }
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}
    public String getEmail(){return this.email;}
    public void setEmail(String email){this.email = email;}
    public String getAlias(){return this.Alias;}
    public void setAlias(String Alias){this.Alias = Alias;}

    @Override
    public int compareTo(com.example.who_nextdoor.TradeInfo o) {
        return this.date.compareTo(o.date);
    }

}