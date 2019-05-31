package com.goufaan.homeworkupload.Model;

import java.util.HashMap;
import java.util.Map;

public class ResponseModel {
    public static final Map<Integer,String> ERROR = new HashMap<>(){{
        put(500, "服务器内部错误");
        put(1000, "参数不全");
        put(1001, "参数非法");
        put(1002, "请不要皮这个系统！（填写所有空！）");

        put(1999, "请先登录");
        put(2000, "账户不存在或密码错误");
        put(2001, "用户名已存在");
        put(2002, "邀请码不正确");
        put(2003, "两次输入的密码不一致");

        put(3000, "作业不存在或被删除");
        put(3001, "很遗憾，作业提交时间已截止，请与发布者联系单独提交");
        put(3002, "不接受此文件扩展名，请仔细看看作业要求哦");
        put(3003, "抱歉，提交的文件过大");
        put(3004, "本次提交的密码与最初提交的不一致，无法处理本次提交（如果别人顶替了你，请与本次作业发布者联系处理");
        put(3005, "本作业的提交人数已超过上限，无法作为新人提交文件（要不跟本次作业发布者联系看看有没有人以多个身份重复提交？");
        put(3006, "您的身份好像不能通过正则表达式的匹配，请重新输入");

        put(4000, "权限错误");
        put(4001, "你还没有提交过作业呢，怎么下载呀");
        put(4002, "密码错误，无法下载之前提交的文件（别想偷看别人作业哦");
        put(4003, "你需要输入你的身份和匹配的密码才能下载之前提交的文件呢");
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
