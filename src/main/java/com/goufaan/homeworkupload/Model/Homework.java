package com.goufaan.homeworkupload.Model;

import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

public class Homework implements Serializable {

    private int id;

    @NotEmpty
    String Name;

    @NotEmpty
    int Owner;

    String[] SupportType;

    String FileNameExample;

    String FileNameFormat;
    @NotEmpty
    int SubmissionLimit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date CreateDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date Deadline;

    boolean IsDeleted;

    public boolean getDeleted (){
        return IsDeleted;
    }
    public void setDeleted(boolean b){
        IsDeleted = b;
    }

    public int getId(){
        return id;
    }
    public void setId(int n){
        id = n;
    }

    public String getName(){
        return Name;
    }
    public void setName(String n){
        Name = n;
    }

    public int getOwner(){
        return Owner;
    }
    public void setOwner(int o){
        Owner = o;
    }

    public String[] getSupportType(){
        return SupportType;
    }
    public void setSupportType(String[] lst){
        SupportType = lst;
    }

    public String getFileNameExample(){
        return FileNameExample;
    }
    public void setFileNameExample(String n){
        FileNameExample = n;
    }

    public String getFileNameFormat(){
        return FileNameFormat;
    }
    public void setFileNameFormat(String n){
        FileNameFormat = n;
    }

    public int getSubmissionLimit(){
        return SubmissionLimit;
    }
    public void setSubmissionLimit(int s){
        SubmissionLimit = s;
    }

    public Date getCreateDate(){
        return CreateDate;
    }
    public void setCreateDate(Date d){
        CreateDate = d;
    }

    public Date getDeadline(){
        return Deadline;
    }
    public void setDeadline(Date d){
        Deadline = d;
    }
}
