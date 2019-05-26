package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Homework;

import java.util.List;

public interface IHomeworkRepository {

    List<Homework> GetAllHomework();

    Homework GetHomework(int id);

    int AddHomework(Homework h);

    int EditHomework(Homework h);

    int RemoveHomework(int id);

    boolean IsMyHomework(int id, int uid);
}
