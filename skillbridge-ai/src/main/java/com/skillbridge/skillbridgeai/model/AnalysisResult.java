package com.skillbridge.skillbridgeai.model;

import java.util.List;

public class AnalysisResult {

    private int matchPercentage;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    private List<String> resumeGaps;

    private List<String> recommendations;

    private List<String> projectRecommendations;

    private List<String> resumeImprovements;

    private Roadmap roadmap;

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getResumeGaps() {
        return resumeGaps;
    }

    public void setResumeGaps(List<String> resumeGaps) {
        this.resumeGaps = resumeGaps;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getProjectRecommendations() {
        return projectRecommendations;
    }

    public void setProjectRecommendations(
            List<String> projectRecommendations) {

        this.projectRecommendations =
                projectRecommendations;
    }

    public List<String> getResumeImprovements() {
        return resumeImprovements;
    }

    public void setResumeImprovements(
            List<String> resumeImprovements) {

        this.resumeImprovements =
                resumeImprovements;
    }

    public Roadmap getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(Roadmap roadmap) {
        this.roadmap = roadmap;
    }
}