package com.skillbridge.skillbridgeai.model;

public class AnalysisRequest {
     private Resume resume;
    private JobDescription jobDescription;
    public Resume getResume() {
        return resume;
    }
    public void setResume(Resume resume) {
        this.resume = resume;
    }
    public JobDescription getJobDescription() {
        return jobDescription;
    }
    public void setJobDescription(JobDescription jobDescription) {
        this.jobDescription = jobDescription;
    }
}
