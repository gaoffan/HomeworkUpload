package com.goufaan.homeworkupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtils {

    @Value("${homeworkupload.isdev}")
    Boolean isDev;
    @Value("${homeworkupload.fronturl}")
    String FrontUrl;
    @Value("${homeworkupload.dev.fronturl}")
    String FrontUrl_dev;
    @Value("${homeworkupload.auth.salt}")
    String salt;
    @Value("${homeworkupload.registercode}")
    String registercode;

    @Value("${homeworkupload.uploadpath}")
    String Uploadpath;
    @Value("${homeworkupload.dev.uploadpath}")
    String Uploadpath_dev;

    @Value("${homeworkupload.tmppath}")
    String TmpPath;
    @Value("${homeworkupload.dev.tmppath}")
    String TmpPath_dev;

    public String getFrontUrl(){
        return isDev ? FrontUrl_dev : FrontUrl;
    }
    public String getUploadpath(){
        return isDev ? Uploadpath_dev : Uploadpath;
    }
    public String getTmpPath(){
        return isDev ? TmpPath_dev : TmpPath;
    }
    public String getSalt(){
        return salt;
    }
    public String getRegistercode(){
        return registercode;
    }
}
