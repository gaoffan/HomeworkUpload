package com.goufaan.homeworkupload.Repository;

import com.goufaan.homeworkupload.Model.Submission;

public interface ISubmissionRepository {

    int AddSubmission(Submission s);

    int UpdateSubmission(String user, int hid, String ipAddress);

    Submission GetLastSubmission(String user, int hid);

    int GetNowSubmittedCount(int hid);
}
