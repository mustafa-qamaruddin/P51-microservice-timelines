package com.mqubits.timelines.controllers;

import com.mqubits.timelines.models.Timeline;
import com.mqubits.timelines.services.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TimelineController {
    @Autowired
    protected TimelineService timelineService;

    @GetMapping("/timeline")
    public ResponseEntity<Timeline> fetchTimeline(
            @RequestParam(required = true, name = "customerId") String customerId,
            @RequestParam(required = true, name = "timeline") String timelineId
    ) {

        var timeline = timelineService.fetchTimeline(timelineId, customerId);
        if (timeline.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(timeline.get(), HttpStatus.OK);
        }
    }

}
