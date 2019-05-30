package com.goufaan.homeworkupload.Controller;

import com.goufaan.homeworkupload.Model.ResponseModel;
import com.goufaan.homeworkupload.Repository.IAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AdminController {

    @Autowired
    IAuthRepository auth;

    @RequestMapping("/api/register")
    public String Register(String n,String p){
        auth.Register(n,p,"123@abc.com");
        return "ok!";
    }

    @RequestMapping("/api/signin")
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

    @RequestMapping("/api/signout")
    public ResponseModel SignOut(HttpServletRequest request){
        request.getSession().removeAttribute("OPENID");
        return new ResponseModel("成功退出登录");
    }

    @RequestMapping("/api/auth/issignin")
    public ResponseModel GetIsSignIn(HttpServletRequest request){
        return new ResponseModel(200, auth.GetLoginAs(request).getUserName());
    }

}
