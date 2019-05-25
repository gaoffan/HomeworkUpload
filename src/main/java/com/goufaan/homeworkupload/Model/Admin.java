package com.goufaan.homeworkupload.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.DigestUtils;

public class Admin {
    public static final String SALT = "iahsdtgbfaskLjgfhbanersdkjfaberfhqerhjkagblv";

    int uid;
    String UserName;
    String Password;
    String Email;
    String OPENID;

    public int getUid(){
        return uid;
    }
    public void setUid(int id){
        uid = id;
    }

    public String getUserName(){
        return UserName;
    }
    public void setUserName(String n){
        UserName = n;
        UpdateOPENID();
    }

    @JsonIgnore
    public String getPassword(){
        return Password;
    }
    public void setPassword(String n){
        n = n + SALT;
        Password = DigestUtils.md5DigestAsHex(n.getBytes());
        UpdateOPENID();
    }

    public String getEmail(){
        return Email;
    }
    public void setEmail(String n){
        Email = n;
    }

    @JsonIgnore
    public String getOPENID(){
        return OPENID;
    }
    void UpdateOPENID(){
        var str = UserName + "|" + Password;
        OPENID = DigestUtils.md5DigestAsHex(str.getBytes());
    }

}
