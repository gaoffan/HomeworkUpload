package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Homework;

import java.util.List;

public interface IHomeworkRepository {

    List<Homework> GetAllHomework();

    List<Homework> GetMyAllHomework(int uid);

    Homework GetHomework(int id);

    int AddHomework(Homework h);

    int EditHomework(Homework h);

    int RemoveHomework(int id);

    boolean IsMyHomework(int id, int uid);
}
