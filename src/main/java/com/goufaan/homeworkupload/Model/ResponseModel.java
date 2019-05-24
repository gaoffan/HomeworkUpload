package com.goufaan.homeworkupload.Model;

import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResponseModel {
    public static final Map<Integer,String> ERROR = new HashMap<>(){{
        put(500, "服务器内部错误");
        put(1000, "参数不全");
        put(2000, "账户不存在或密码错误");
        put(2001, "用户已存在");
    }};

    int ret;
    String desc;
    Object data;

    public ResponseModel(int r, String t){
        ret = r;
        desc = t;
    }
    public ResponseModel(int r){
        ret = r;
    }
    public ResponseModel(String t){
        ret = 200;
        desc = t;
    }

    public void setRet(int c){
        ret = c;
    }
    public int getRet(){
        return ret;
    }

    public void setDesc(String t){
        desc = t;
    }
    public String getDesc(){
        return ret == 200 ? desc : ERROR.get(ret);
    }

    public void setData(Object o){
        data = o;
    }
    public Object getData(){
        return ret == 200 ? data : null;
    }

}
