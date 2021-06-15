package com.mqubits.memberships.models.dto;

public class TimelineDTO {
    private String customer;
    private String timeline;

    public TimelineDTO() {
    }

    public TimelineDTO(String customer, String timeline) {
        this.customer = customer;
        this.timeline = timeline;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }
}
