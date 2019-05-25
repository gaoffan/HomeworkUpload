package com.goufaan.homeworkupload.Model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Submission {
    String User;
    String Password;
    String IPAddress;
    int HomeworkId;
    String FilePath;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date CreateDate;

    public String getUser(){
        return User;
    }
    public void setUser(String n){
        User = n;
    }

    public String getPassword(){
        return Password;
    }
    public void setPassword(String n){
        Password = n;
    }

    public String getIPAddress(){
        return IPAddress;
    }
    public void setIPAddress(String n){
        IPAddress = n;
    }

    public int getHomeworkId(){
        return HomeworkId;
    }
    public void setHomeworkId(int id){
        HomeworkId = id;
    }

    public Date getCreateDate(){
        return CreateDate;
    }
    public void setCreateDate(Date d){
        CreateDate = d;
    }

    public String getFilePath(){
        return FilePath;
    }
    public void setFilePath(String p){
        FilePath = p;
    }

}
