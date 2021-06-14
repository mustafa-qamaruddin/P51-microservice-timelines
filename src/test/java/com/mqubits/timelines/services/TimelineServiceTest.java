package com.mqubits.timelines.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TimelineServiceTest {

    // @todo mock http request

    @Autowired
    TimelineService timelineService;

    @Autowired
    TestKafkaConsumer testKafkaConsumer;

    @Test
    void canCRUDMembership() {
        // create membership
        var testemployer = "testEmployer";
        var testEmployee = "testEmployee";
        var testTimeline = "testTimeline";
        timelineService.createMembership(new MembershipDTO(testemployer, testEmployee, testTimeline));

        // check membership
        assertFalse(timelineService.checkMembership(testemployer, testTimeline));
        assertTrue(timelineService.checkMembership(testEmployee, testTimeline));

        // revoke membership
        timelineService.revokeMembership(testEmployee, testTimeline);
        assertFalse(timelineService.checkMembership(testEmployee, testTimeline));

        // is notification pushed to kafka?
        verifyCountDown(testKafkaConsumer);
        assertEquals(testKafkaConsumer.getTimelineDTO().getCustomer(), testEmployee);
        assertEquals(testKafkaConsumer.getTimelineDTO().getTimeline(), testTimeline);
    }

    private void verifyCountDown(TestKafkaConsumer consumer) {
        try {
            consumer.getLatch().await(59, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            consumer.resetLatch();
        }
        assertThat(consumer.getLatch().getCount(), equalTo(0L));
    }
}