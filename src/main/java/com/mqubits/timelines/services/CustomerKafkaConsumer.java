package com.mqubits.timelines.services;

import com.mqubits.timelines.models.dto.TimelineDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class CustomerKafkaConsumer {

    public final static String TOPIC_TIMELINE = "create-timeline";
    protected static final Logger LOGGER = LoggerFactory.getLogger(CustomerKafkaConsumer.class);

    @Autowired
    TimelineService timelineService;

    @KafkaListener(topics = TOPIC_TIMELINE)
    public void receive(TimelineDTO timelineDTO) {
        LOGGER.info("received payload='{}'", timelineDTO.toString());
        timelineService.createTimeline(timelineDTO);
    }
}
