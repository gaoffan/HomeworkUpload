package com.goufaan.homeworkupload.Interceptor;

import com.goufaan.homeworkupload.Repository.IAuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    //private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    IAuthRepository auth;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials","true");
        httpServletResponse.addHeader("Access-Control-Allow-Origin","http://127.0.0.1:8081");
        if(auth.isLogin(httpServletRequest)){
            return true;
        }
        else{
            PrintWriter printWriter = httpServletResponse.getWriter();
            printWriter.write("{\"ret\":1999,\"desc\":\"Not signed in\",\"data\":null}");
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

}
