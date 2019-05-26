package com.goufaan.homeworkupload.Controller;

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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class SubmitController {

    public static final String PATH = "./uploads/";
    @Autowired
    ISubmissionRepository sub;

    @Autowired
    IHomeworkRepository homew;

    @Autowired
    IAuthRepository auth;

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

        if (lastsub == null && sub.GetAllSubmission(hid).size() >= h.getSubmissionLimit())
            return new ResponseModel(3005);

        Path path;
        try{
            path = Files.createDirectories(Paths.get(PATH + h.getId()+ "/").toAbsolutePath().normalize())
                    .resolve(user + "." + ext);
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
    @RequestMapping("/api/downloadsubmission")
    public ResponseEntity<Resource> DownloadSubmission(Integer hid, String user, String password, HttpServletRequest request) {
        return null; // TODO
    }

    @RequestMapping("/api/auth/download")
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
            Files.createDirectories(zipFile);
            ZipMultiFile(sourcePath.toString(), zipFile.toString());
            Resource resource = new UrlResource(Paths.get("./tmp/" + hid + ".zip").toAbsolutePath().normalize().toUri());
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

    public static void ZipMultiFile(String filepath ,String zippath) {
        try {
            File file = new File(filepath);
            File zipFile = new File(zippath);
            InputStream input = null;
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(int i = 0; i < files.length; ++i){
                    input = new FileInputStream(files[i]);
                    zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + files[i].getName()));
                    int temp = 0;
                    while((temp = input.read()) != -1){
                        zipOut.write(temp);
                    }
                    input.close();
                }
            }
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
