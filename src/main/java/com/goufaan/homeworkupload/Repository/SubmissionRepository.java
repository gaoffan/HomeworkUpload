package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Submission;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SubmissionRepository implements ISubmissionRepository {

    @Autowired
    private MongoTemplate mongo;

    @Override
    public int AddSubmission(Submission s) {
        mongo.insert(s);
        return 200;
    }

    @Override
    public int UpdateSubmission(String user, int hid, String ipAddress) {
        var q = new Query(Criteria.where("User").is(user));
        q.addCriteria(Criteria.where("HomeworkId").is(hid));
        Update update= new Update().set("CreateDate", new Date()).set("IPAddress", ipAddress);
        UpdateResult result = mongo.updateFirst(q, update, Submission.class);
        return 200;
    }

    @Override
    public Submission GetLastSubmission(String user, int hid) {
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
