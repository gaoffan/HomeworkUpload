package com.goufaan.homeworkupload.Controller;

import com.goufaan.homeworkupload.ConfigUtils;
import com.goufaan.homeworkupload.Model.ResponseModel;
import com.goufaan.homeworkupload.Repository.IAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AdminController {

    @Autowired
    ConfigUtils config;
    @Autowired
    IAuthRepository auth;

    @PostMapping("/api/register")
    public ResponseModel Register(String code, String userName, String realName ,String password, String confirmPassword,
                                  String email, HttpServletRequest request){
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(password) || !StringUtils.hasText(realName)
                || !StringUtils.hasText(confirmPassword) || !StringUtils.hasText(email))
            return new ResponseModel(1002);
        if (!password.equals(confirmPassword))
            return new ResponseModel(2003);
        if (!StringUtils.hasText(code) || !code.equals(config.getRegistercode()))
            return new ResponseModel(2002);
        if (auth.HasUser(userName))
            return new ResponseModel(2001);
        auth.Register(userName,realName,password,email);
        var s = request.getSession();
        var result = auth.Login(userName, password);
        s.setAttribute("OPENID", result.getOPENID());
        s.setMaxInactiveInterval(30 * 60);
        return new ResponseModel(200,"注册成功");
    }

    @PostMapping("/api/signin")
    public ResponseModel SignIn(String userName, String password, HttpServletRequest request){
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(password))
            return new ResponseModel(1002);

        var result = auth.Login(userName, password);
        if (result == null)
            return new ResponseModel(2000);

        var s = request.getSession();
        s.setAttribute("OPENID", result.getOPENID());
        s.setMaxInactiveInterval(30 * 60);
        var r = new ResponseModel("登录成功");
        r.setData(result.getUserName());
        return r;
    }

    @PostMapping("/api/signout")
    public ResponseModel SignOut(HttpServletRequest request){
        request.getSession().removeAttribute("OPENID");
        return new ResponseModel("成功退出登录");
    }

    @GetMapping("/api/auth/issignin")
    public ResponseModel GetIsSignIn(HttpServletRequest request){
        var ret = new ResponseModel(200);
        ret.setData(auth.GetLoginAs(request));
        return ret;
    }

}
