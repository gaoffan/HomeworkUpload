package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Admin;

public interface IAuthRepository {

    Admin GetUser(int uid);

    String Login(String userName, String password);

    int Register(String userName, String password, String email);
}
