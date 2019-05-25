package com.goufaan.homeworkupload.Controller;

import com.goufaan.homeworkupload.Model.Homework;
import com.goufaan.homeworkupload.Model.ResponseModel;
import com.goufaan.homeworkupload.Repository.IAuthRepository;
import com.goufaan.homeworkupload.Repository.IHomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class HomeController {

    @Autowired
    IAuthRepository auth;
    @Autowired
    IHomeworkRepository homew;

    @RequestMapping("/api/getlist")
    public ResponseModel GetHomeworkList() {
        var r = new ResponseModel(200);
        var result = homew.GetAllHomework();
        r.setData(result);
        return r;
    }

    @RequestMapping("/api/get/{id}")
    public ResponseModel GetHomework(@PathVariable Integer id) {
        if (id == null)
            return new ResponseModel(1000);
        var result = homew.GetHomework(id);
        if (result == null)
            return new ResponseModel(3000);
        var r = new ResponseModel(200);
        r.setData(result);
        return r;
    }

    @RequestMapping("/api/auth/new")
    public ResponseModel NewHomework(String name, @RequestParam(value = "stype") String[] stype, String fnExample, Integer sLimit, String fnFormat ,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date deadline, HttpServletRequest request) {
        if (name == null || stype == null || fnExample == null || sLimit == null || fnFormat == null || deadline == null)
            return new ResponseModel(1000);
        var user = auth.GetLoginAs(request);
        var m = new Homework();
        m.setName(name);
        m.setOwner(user.getUid());
        m.setSupportType(stype);
        m.setFileNameExample(fnExample);
        m.setSubmissionLimit(sLimit);
        m.setFileNameFormat(fnFormat);
        m.setDeadline(deadline);
        m.setCreateDate(new Date());
        var ret = homew.AddHomework(m);
        if (ret != 200)
            return new ResponseModel(ret);
        var result = new ResponseModel("新增作业成功！");
        result.setData(m.getId());
        return result;
    }
}