package com.example.who_nextdoor;



public class ComentsInfo implements Comparable<ComentsInfo> {
    private String coments;
    private String date;
    private String uid_date;
    private String parentTitle;
    private String parentDate;
    private String iamsub;
    private String Email;
    private String alias;

    public ComentsInfo(){
        this.coments = null;
    }
    public ComentsInfo(String conments){
        this.coments = coments;
    }



    public String getComents(){
        return this.coments;
    }
    public void setComents(String coments){
        this.coments = coments;
    }
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}
    public String getUid_date(){return this.uid_date;}
    public void setUid_date(String uid_date){this.uid_date = uid_date;}
    public String getParentTitle(){return this.parentTitle;}
    public void setParentTitle(String parentTitle){this.parentTitle = parentTitle;}
    public String getParentDate(){return this.parentDate;}
    public void setParentDate(String parentDate){this.parentDate = parentDate;}
    public String getIamsub(){return this.iamsub;}
    public void setIamsub(String iamsub){this.iamsub = iamsub;}
    public String getEmail(){return this.Email;}
    public void setEmail(String Email){this.Email = Email;}
    public String getAlias(){return this.alias;}
    public void setAlias(String alias){this.alias = alias;}

    @Override
    public int compareTo(ComentsInfo o) {
        return this.date.compareTo(o.date);
    }
}