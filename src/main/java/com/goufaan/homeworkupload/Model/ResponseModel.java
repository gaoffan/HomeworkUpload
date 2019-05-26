package com.goufaan.homeworkupload.Model;

import java.util.HashMap;
import java.util.Map;

public class ResponseModel {
    public static final Map<Integer,String> ERROR = new HashMap<>(){{
        put(500, "服务器内部错误");
        put(1000, "参数不全");
        put(1001, "参数非法");

        put(1999, "请先登录");
        put(2000, "账户不存在或密码错误");
        put(2001, "用户已存在");

        put(3000, "作业不存在或被删除");
        put(3001, "很遗憾，作业提交时间已过");
        put(3002, "不接受此文件扩展名");
        put(3003, "抱歉，提交的文件过大");
        put(3004, "本次提交的密码与最初提交的不一致，无法处理本次提交");
        put(3005, "本作业的提交人数已超过上限，无法作为新人提交文件");

        put(4000, "权限错误");
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
