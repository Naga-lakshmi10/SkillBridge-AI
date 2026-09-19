package com.skillbridge.skillbridgeai.model;

import java.util.List;

public class Roadmap {

    private String targetRole;

    private List<RoadmapPhase> phases;

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public List<RoadmapPhase> getPhases() {
        return phases;
    }

    public void setPhases(List<RoadmapPhase> phases) {
        this.phases = phases;
    }
}