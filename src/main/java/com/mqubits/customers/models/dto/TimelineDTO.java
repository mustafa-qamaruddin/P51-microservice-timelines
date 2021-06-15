package com.mqubits.customers.models.dto;

public class TimelineDTO {
    private String employer;
    private String timeline;

    public TimelineDTO() {
    }

    public TimelineDTO(String employer, String timeline) {
        this.employer = employer;
        this.timeline = timeline;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }
}
