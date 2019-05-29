package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthRepository implements IAuthRepository {

    @Value("${homeworkupload.auth.salt}")
    private String salt;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public Admin GetLoginAs(HttpServletRequest request) {
        return GetUser((String)request.getSession().getAttribute("OPENID"));
    }

    @Override
    public boolean isLogin(HttpServletRequest request) {
        if (request.getSession(false) == null)
            return false;
        return request.getSession().getAttribute("OPENID") != null;
    }

    @Override
    public Admin GetUser(int uid) {
        var q = new Query(Criteria.where("uid").is(uid));
        return mongo.findOne(q, Admin.class);
    }

    @Override
    public Admin GetUser(String OPENID) {
        if (OPENID == null)
            return null;
        var q = new Query(Criteria.where("OPENID").is(OPENID));
        return mongo.findOne(q, Admin.class);
    }

    @Override
    public Admin Login(String userName, String password) {
        var q = new Query(Criteria.where("UserName").is(userName));
        var result = mongo.findOne(q, Admin.class);
        var pwd = password + salt;
        if (result == null || !result.getPassword().equals(DigestUtils.md5DigestAsHex(pwd.getBytes())))
            return null;
        return result;
    }

    @Override
    public int Register(String userName, String password, String email) {
        var q = new Query(Criteria.where("UserName").is(userName));
        var result = mongo.findOne(q, Admin.class);
        if (result != null)
            return 2001;
        var n = new Admin();
        try {
            n.setUid(mongo.findAll(Admin.class).size() + 1);
            n.setUserName(userName);
            n.setEmail(email);
            n.setPassword(password);
            mongo.insert(n);
        }
        catch(Exception e){
            e.printStackTrace();
            return 500;
        }
        return 200;
    }
}
