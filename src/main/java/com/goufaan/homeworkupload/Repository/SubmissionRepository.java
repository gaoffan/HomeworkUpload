package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.ConfigUtils;
import com.goufaan.homeworkupload.Model.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Component
public class SubmissionRepository implements ISubmissionRepository {

    @Autowired
    ConfigUtils config;
    //@Autowired
    private MongoTemplate mongo;

    public SubmissionRepository (MongoTemplate m){
        mongo = m;
    }

    @Override
    public boolean IsMySubmission(Submission s, String password) {
        var pwd = password + config.getSalt();
        return s.getPassword().equals(DigestUtils.md5DigestAsHex(pwd.getBytes()));
    }

    @Override
    public int AddSubmission(Submission s) {
        var pwd = s.getPassword() + config.getSalt();
        s.setPassword(DigestUtils.md5DigestAsHex(pwd.getBytes()));
        mongo.insert(s);
        return 200;
    }

    @Override
    public int UpdateSubmission(int hid, String user, String ipAddress) {
        var q = new Query(Criteria.where("User").is(user));
        q.addCriteria(Criteria.where("HomeworkId").is(hid));
        Update update= new Update().set("CreateDate", new Date()).set("IPAddress", ipAddress);
        mongo.updateFirst(q, update, Submission.class);
        return 200;
    }

    @Override
    public int RemoveSubmission(int hid, String user) {
        try {
            var q = new Query(Criteria.where("User").is(user));
            q.addCriteria(Criteria.where("HomeworkId").is(hid));
            var result = mongo.findOne(q, Submission.class);
            Files.deleteIfExists(Paths.get(result.getFilePath()));
            mongo.remove(q, Submission.class);
        }catch (Exception e){
            e.printStackTrace();
            return 500;
        }
        return 200;
    }

    @Override
    public Submission GetLastSubmission(int hid, String user) {
        var q = new Query(Criteria.where("User").is(user));
        q.addCriteria(Criteria.where("HomeworkId").is(hid));
        return mongo.findOne(q, Submission.class);
    }

    @Override
    public List<Submission> GetAllSubmission(int hid) {
        var q = new Query(Criteria.where("HomeworkId").is(hid));
        return mongo.find(q, Submission.class);
    }

}
