package com.goufaan.homeworkupload.Model;

import java.nio.ByteBuffer;

public class SubmissionModel {
    String Name;
    public String getName(){
        return Name;
    }
    public void setName(String n){
        Name = n;
    }
    String Password;
    public String getPassword(){
        return Password;
    }
    public void setPassword(String n){
        Password = n;
    }
    String FilePath;
    public String getFilePath(){
        return FilePath;
    }
    public void setFilePath(String n){
        FilePath = n;
    }
}
