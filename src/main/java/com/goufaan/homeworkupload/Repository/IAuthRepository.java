package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Admin;

import javax.servlet.http.HttpServletRequest;

public interface IAuthRepository {

    Admin GetLoginAs(HttpServletRequest request);

    boolean isLogin(HttpServletRequest request);

    Admin GetUser(int uid);

    boolean HasUser(String username);

    Admin GetUser(String OPENID);

    Admin Login(String userName, String password);

    int Register(String userName, String realName, String password, String email);
}
