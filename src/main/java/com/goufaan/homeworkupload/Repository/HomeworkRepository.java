package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Homework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HomeworkRepository implements IHomeworkRepository {

    @Autowired
    private MongoTemplate mongo;

    @Override
    public List<Homework> GetAllHomework() {
        //var q = new Query(Criteria.where("id").not(id));
        return mongo.findAll(Homework.class);
    }

    @Override
    public Homework GetHomework(int id) {
        var q = new Query(Criteria.where("id").is(id));
        var result =  mongo.findOne(q , Homework.class);
        return result;
    }

    @Override
    public int AddHomework(Homework h) {
        try {
            h.setId(mongo.findAll(Homework.class).size());
            mongo.insert(h);
        }
        catch(Exception e){
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
            return 500;
        }
        return 200;
    }

    @Override
    public int RemoveHomework(int id) {
        try {
            var q = new Query(Criteria.where("id").is(id));
            mongo.remove(q, Homework.class);
        }
        catch(Exception e){
            return 500;
        }
        return 200;
    }
}
