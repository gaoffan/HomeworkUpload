package com.goufaan.homeworkupload.Controller;

import com.goufaan.homeworkupload.Misc;
import com.goufaan.homeworkupload.Model.ResponseModel;
import com.goufaan.homeworkupload.Model.Submission;
import com.goufaan.homeworkupload.Repository.IAuthRepository;
import com.goufaan.homeworkupload.Repository.IHomeworkRepository;
import com.goufaan.homeworkupload.Repository.ISubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
public class SubmitController {

    public static final String PATH = "./uploads/";
    @Autowired
    ISubmissionRepository sub;

    @Autowired
    IHomeworkRepository homew;

    @Autowired
    IAuthRepository auth;

    @RequestMapping("/api/getsubmitted")
    public ResponseModel GetIsSubmitted(Integer hid, String user){
        if (hid == null || user == null)
            return new ResponseModel(1000);
        var result = sub.GetLastSubmission(hid, user);
        var r = new ResponseModel(200);
        r.setData(result != null);
        return r;
    }

    @RequestMapping("/api/submit")
    public ResponseModel SubmitFile(Integer hid, String user, String password, @RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (hid == null || file == null || file.isEmpty())
            return new ResponseModel(1000);

        if (!StringUtils.hasText(user) || !StringUtils.hasText(password))
            return new ResponseModel(1002);

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

        if (StringUtils.hasText(h.getFileNameFormat()) && !Pattern.matches(h.getFileNameFormat(), user))
            return new ResponseModel(3006);

        var lastSub = sub.GetLastSubmission(hid, user);
        if (lastSub != null && !lastSub.getPassword().equals(password))
            return new ResponseModel(3004);

        if (lastSub == null && sub.GetAllSubmission(hid).size() >= h.getSubmissionLimit())
            return new ResponseModel(3005);

        Path path;
        try{
            path = Files.createDirectories(Paths.get(PATH + h.getId()+ "/").toAbsolutePath().normalize())
                    .resolve(user + "." + ext);
            file.transferTo(path);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseModel(500);
        }

        if (lastSub == null){
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
            return new ResponseModel(sub.UpdateSubmission(hid, user, request.getRemoteAddr()), "重新提交成功！");
        }
    }

    @RequestMapping("/api/auth/removesubmission")
    public ResponseModel RemoveSubmission(Integer hid, String user){
        if (hid == null || user == null)
            return new ResponseModel(1000);
        return new ResponseModel(sub.RemoveSubmission(hid,user));
    }

    @RequestMapping("/api/candownloadsubmission")
    public ResponseModel CanDownloadSubmission(Integer hid, String user, String password) {
        if (hid == null)
            return new ResponseModel(1000);
        if (!StringUtils.hasText(user) || !StringUtils.hasText(password))
            return new ResponseModel(4003);
        var lastSub = sub.GetLastSubmission(hid, user);
        if (lastSub == null)
            return new ResponseModel(4001);
        if (!sub.IsMySubmission(lastSub, password))
            return new ResponseModel(4002);
        return new ResponseModel(200);
    }

    public ResponseEntity<Resource> DownloadFile(Path path, HttpServletRequest request)
    {
        try {
            Resource resource = new UrlResource(path.toUri());
            String contentType;
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if(contentType == null)
                contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping("/api/downloadsubmission")
    public ResponseEntity<Resource> DownloadSubmission(Integer hid, String user, String password, HttpServletRequest request) {
        if (CanDownloadSubmission(hid, user, password).getRet() != 200)
            return null;
        var lastSub = sub.GetLastSubmission(hid, user);
        var source = Paths.get(lastSub.getFilePath());
        return DownloadFile(source, request);
    }

    @RequestMapping("/api/auth/downloadall")
    public ResponseEntity<Resource> DownloadAllSubmission(Integer hid, HttpServletRequest request){
        if (hid == null)
            return null;
        if (homew.GetHomework(hid).getOwner() != auth.GetLoginAs(request).getUid())
            return null;
        if (sub.GetAllSubmission(hid).size() == 0)
            return null;

        var sourcePath = Paths.get("./uploads/" + hid + "/").toAbsolutePath().normalize();
        var zipFile = Paths.get("./tmp/" + hid + ".zip").toAbsolutePath().normalize();

        try {
            Files.createDirectories(Paths.get("./tmp/").toAbsolutePath().normalize());
            Misc.ZipMultiFile(sourcePath.toString(), zipFile.toString());

            return DownloadFile(Paths.get("./tmp/" + hid + ".zip").toAbsolutePath().normalize(), request);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
