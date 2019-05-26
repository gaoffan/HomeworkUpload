package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Submission;

import java.util.List;

public interface ISubmissionRepository {

    int AddSubmission(Submission s);

    int UpdateSubmission(String user, int hid, String ipAddress);

    Submission GetLastSubmission(String user, int hid);

    List<Submission> GetAllSubmission(int hid);
}
