package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Submission;

import java.util.List;

public interface ISubmissionRepository {

    boolean IsMySubmission(Submission s, String password);

    int AddSubmission(Submission s);

    int UpdateSubmission(int hid, String user, String ipAddress);

    int RemoveSubmission(int hid, String user);

    Submission GetLastSubmission(int hid, String user);

    List<Submission> GetAllSubmission(int hid);
}
