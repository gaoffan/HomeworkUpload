package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Homework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class HomeworkRepository implements IHomeworkRepository {

    @Autowired
    private MongoTemplate mongo;

    @Override
    public List<Homework> GetAllHomework() {
        var d = Calendar.getInstance();
        d.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH) - 7);
        var q = new Query(Criteria.where("Deadline").gte(d.getTime()));
        q.addCriteria(Criteria.where("IsDeleted").is(false));
        q.with(new Sort(Sort.Direction.DESC, "id"));
        return mongo.find(q, Homework.class);
    }

    @Override
    public List<Homework> GetMyAllHomework(int uid) {
        var q = new Query(Criteria.where("Owner").is(uid));
        q.addCriteria(Criteria.where("IsDeleted").is(false));
        q.with(new Sort(Sort.Direction.DESC, "id"));
        return mongo.find(q , Homework.class);
    }

    @Override
    public Homework GetHomework(int id) {
        var q = new Query(Criteria.where("id").is(id));
        q.addCriteria(Criteria.where("IsDeleted").is(false));
        return mongo.findOne(q , Homework.class);
    }

    @Override
    public int AddHomework(Homework h) {
        try {
            h.setId(mongo.findAll(Homework.class).size() + 1);
            h.setDeleted(false);
            mongo.insert(h);
        }
        catch(Exception e){
            e.printStackTrace();
            return 500;
        }
        return 200;
    }

    @Override
    public int EditHomework(Homework h) {
        try {
            mongo.save(h);
        }
        catch(Exception e){
            e.printStackTrace();
            return 500;
        }
        return 200;
    }

    @Override
    public int RemoveHomework(int id) {
        try {
            var q = new Query(Criteria.where("id").is(id));
            var u = new Update().set("IsDeleted",true);
            mongo.updateFirst(q,u, Homework.class);
        }
        catch(Exception e){
            e.printStackTrace();
            return 500;
        }
        return 200;
    }

    @Override
    public boolean IsMyHomework(int id, int uid) {
        try {
            var q = new Query(Criteria.where("id").is(id));
            var result = mongo.findOne(q, Homework.class);
            return result != null && result.getOwner() == uid;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
