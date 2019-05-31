package com.goufaan.homeworkupload.Controller;

import com.goufaan.homeworkupload.DateUtils;
import com.goufaan.homeworkupload.Model.Homework;
import com.goufaan.homeworkupload.Model.ResponseModel;
import com.goufaan.homeworkupload.Repository.IAuthRepository;
import com.goufaan.homeworkupload.Repository.IHomeworkRepository;
import com.goufaan.homeworkupload.Repository.ISubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class HomeworkController {

    @Autowired
    IAuthRepository auth;
    @Autowired
    IHomeworkRepository homew;
    @Autowired
    ISubmissionRepository sub;

    @GetMapping("/api/getlist")
    public ResponseModel GetHomeworkList() {
        var r = new ResponseModel(200);
        var result = homew.GetAllHomework();
        var dat = new ArrayList<HashMap<String,Object>>();
        for (var item : result){
            var hm = new HashMap<String,Object>();
            hm.put("id", item.getId());
            hm.put("name", item.getName());
            hm.put("owner", auth.GetUser(item.getOwner()).getUserName());
            hm.put("deadline_format", DateUtils.FormatDate(item.getDeadline()));
            dat.add(hm);
        }
        r.setData(dat);
        return r;
    }
    @GetMapping("/api/auth/getlist")
    public ResponseModel GetMyHomeworkList(HttpServletRequest request) {
        var r = new ResponseModel(200);
        var result = homew.GetMyAllHomework(auth.GetLoginAs(request).getUid());
        var dat = new ArrayList<HashMap<String,Object>>();
        for (var item : result){
            var hm = new HashMap<String,Object>();
            hm.put("id", item.getId());
            hm.put("name", item.getName());
            hm.put("createDate", item.getCreateDate());
            dat.add(hm);
        }
        r.setData(dat);
        return r;
    }

    @GetMapping("/api/get/{id}")
    public ResponseModel GetHomework(@PathVariable Integer id, HttpServletRequest request) {
        if (id == null)
            return new ResponseModel(1000);
        var result = homew.GetHomework(id);
        if (result == null)
            return new ResponseModel(3000);
        var r = new ResponseModel(200);
        var map = new HashMap<String,Object>();
        map.put("name",result.getName());
        map.put("createDate",result.getCreateDate());
        map.put("deadline",result.getDeadline());
        map.put("deadline_format", DateUtils.FormatDate(result.getDeadline()));
        map.put("sLimit",result.getSubmissionLimit());
        map.put("owner",auth.GetUser(result.getOwner()).getRealName());
        map.put("format",result.getSupportType());
        var subs = sub.GetAllSubmission(id);
        map.put("count", subs.size());
        map.put("fnExample",result.getFileNameExample());
        if ((boolean)IsMyHomework(id, request).getData() == true){
            var p = new ArrayList<HashMap<String,Object>>();
            for (var item : subs){
                var hm = new HashMap<String,Object>();
                hm.put("name", item.getUser());
                hm.put("time", item.getCreateDate());
                p.add(hm);
            }
            map.put("submitted", p);
        }
        r.setData(map);
        return r;
    }

    @PostMapping("/api/auth/newhomework")
    public ResponseModel NewHomework(String name, @RequestParam(value = "stype") String[] stype, String fnExample, Integer sLimit, String fnFormat ,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline, HttpServletRequest request) {
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

    @GetMapping("/api/auth/ismyhomework")
    public ResponseModel IsMyHomework(Integer hid, HttpServletRequest request) {
        if (hid == null)
            return new ResponseModel(1000);
        var result = new ResponseModel(200);
        var admin = auth.GetLoginAs(request);
        if (admin == null)
            result.setData(false);
        else
            result.setData(homew.IsMyHomework(hid, admin.getUid()));
        return result;
    }

    @RequestMapping("/api/auth/deletehomework")
    public ResponseModel DeleteHomework(Integer hid, HttpServletRequest request){
        if (hid == null)
            return new ResponseModel(1000);
        if (!homew.IsMyHomework(hid, auth.GetLoginAs(request).getUid()))
            return new ResponseModel(4000);
        return new ResponseModel(homew.RemoveHomework(hid),"删除成功");
    }
}