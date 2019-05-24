package com.goufaan.homeworkupload.Controller;

import com.goufaan.homeworkupload.Model.Homework;
import com.goufaan.homeworkupload.Model.ResponseModel;
import com.goufaan.homeworkupload.Repository.IAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class HomeController {

    @Autowired
    IAuthRepository Homew;
    @Autowired
    private MongoTemplate mongo;

    @RequestMapping("/getlist")
    public ResponseModel GetHomeworkList() {
        var r = new ResponseModel(200);
        var result = mongo.findAll(Homework.class);
        r.setData(result);
        return r;
    }

    @RequestMapping("/get/{id}")
    public ResponseModel GetHomework(@PathVariable Integer id) {
        if (id == null)
            return new ResponseModel(1000);
        var query = new Query(Criteria.where("id").is(id));
        var result =  mongo.findOne(query , Homework.class);
        var r = new ResponseModel(200);
        r.setData(result);
        return r;
    }

    @RequestMapping("/new")
    public ResponseModel NewHomework(String name, @RequestParam(value = "stype") String[] stype, String fnExample, Integer sLimit, String fnFormat ,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date deadline) {
        if (name == null || stype == null || fnExample == null || sLimit == null || fnFormat == null || deadline == null)
            return new ResponseModel(1000);
        var m = new Homework();
        try {
            m.setId(mongo.findAll(Homework.class).size());
            m.setName(name);
            m.setOwner("me");
            m.setSupportType(stype);
            m.setFileNameExample(fnExample);
            m.setSubmissionLimit(sLimit);
            m.setFileNameFormat(fnFormat);
            m.setDeadline(deadline);
            m.setCreateDate(new Date());
            mongo.insert(m);
        }
        catch(Exception e){
            return new ResponseModel(500);
        }
        var result = new ResponseModel("新增作业成功！");
        result.setData(m.getId());
        return result;
    }
}