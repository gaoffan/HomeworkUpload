package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class AuthRepository implements IAuthRepository {

    @Autowired
    private MongoTemplate mongo;

    @Override
    public Admin GetUser(int uid) {
        var q = new Query(Criteria.where("id").is(uid));
        var result = mongo.findOne(q, Admin.class);
        return result;
    }

    @Override
    public Admin GetUser(String OPENID) {
        var q = new Query(Criteria.where("OPENID").is(OPENID));
        var result = mongo.findOne(q, Admin.class);
        return result;
    }

    @Override
    public String Login(String userName, String password) {
        var q = new Query(Criteria.where("UserName").is(userName));
        var result = mongo.findOne(q, Admin.class);
        var pwd = password + Admin.SALT;
        if (result == null || !result.getPassword().equals(DigestUtils.md5DigestAsHex(pwd.getBytes())))
            return null;
        return result.getOPENID();
    }

    @Override
    public int Register(String userName, String password, String email) {
        var q = new Query(Criteria.where("UserName").is(userName));
        var result = mongo.findOne(q, Admin.class);
        if (result != null)
            return 2001;
        var n = new Admin();
        try {
            n.setUid(mongo.findAll(Admin.class).size());
            n.setUserName(userName);
            n.setEmail(email);
            n.setPassword(password);
            mongo.insert(n);
        }
        catch(Exception e){
            return 500;
        }
        return 200;
    }
}
