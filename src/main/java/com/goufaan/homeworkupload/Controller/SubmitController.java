package com.goufaan.homeworkupload.Controller;

import com.goufaan.homeworkupload.Model.ResponseModel;
import com.goufaan.homeworkupload.Model.Submission;
import com.goufaan.homeworkupload.Repository.IHomeworkRepository;
import com.goufaan.homeworkupload.Repository.ISubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@RestController
public class SubmitController {

    public static final String PATH = "./uploads/";
    @Autowired
    ISubmissionRepository sub;

    @Autowired
    IHomeworkRepository homew;

    @RequestMapping("/api/submit")
    public ResponseModel SubmitFile(Integer hid, String user, String password, @RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (hid == null || user == null || password == null || file == null || file.isEmpty())
            return new ResponseModel(1000);

        var h = homew.GetHomework(hid);
        if (h == null)
            return new ResponseModel(3000);

        var date = new Date();
        if (!date.before(h.getDeadline()))
            return new ResponseModel(3001);

        var fileName = StringUtils.cleanPath(file.getOriginalFilename());
        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);

        if (!Arrays.asList(h.getSupportType()).contains(ext))
            return new ResponseModel(3002);

        if (file.getSize() > 10 * 1024 * 1024)
            return new ResponseModel(3003);

        var lastsub = sub.GetLastSubmission(user, hid);
        if (lastsub != null && !lastsub.getPassword().equals(password))
            return new ResponseModel(3004);

        if (lastsub == null && sub.GetNowSubmittedCount(hid) >= h.getSubmissionLimit())
            return new ResponseModel(3005);

        Path path;
        try{
            path = Files.createDirectories(Paths.get(PATH + h.getId()+ "/").toAbsolutePath().normalize()).resolve(fileName);
            file.transferTo(path);

        }catch (Exception e){
            return new ResponseModel(500);
        }

        if (lastsub == null){
            var s = new Submission();
            s.setHomeworkId(hid);
            s.setUser(user);
            s.setPassword(password);
            s.setFilePath(path.toString());
            s.setIPAddress(request.getRemoteAddr());
            s.setCreateDate(new Date());
            return new ResponseModel(sub.AddSubmission(s), "提交成功！");
        }
        else{
            return new ResponseModel(sub.UpdateSubmission(user, hid, request.getRemoteAddr()), "重新提交成功！");
        }
    }

    @RequestMapping("/api/auth/download")
    public ResponseModel DownloadSubmission(){
        return new ResponseModel(200);
    }
}
