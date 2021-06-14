package com.mqubits.timelines.services;

import com.mqubits.timelines.models.dto.TimelineDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TimelineServiceTest {

    @Autowired
    TimelineService timelineService;

    private MockWebServer mockWebServer = new MockWebServer();

    @BeforeEach
    void setup() {
        try {
            mockWebServer.start(InetAddress.getByName("0.0.0.0"), 8080);
            mockWebServer.url("/starlighter/api/v1/membership");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void canCRUDMembership() {
        // create timeline
        var testEmployer = "testEmployer";
        var testEmployee = "testEmployee";
        var testTimeline = "testTimeline";
        timelineService.createTimeline(new TimelineDTO(testEmployer, testTimeline));

        // fetch timeline
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));
        var timeline = timelineService.fetchTimeline(testTimeline, testEmployee);
        assertTrue(timelineService.openSessions.containsKey(testEmployee));
        assertFalse(timeline.isEmpty());

        // confirm that your app made the HTTP requests you were expecting.
        RecordedRequest request1 = null;
        try {
            request1 = mockWebServer.takeRequest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("/starlighter/api/v1/membership?customerId=testEmployee&timeline=testTimeline", request1.getPath());
        assertEquals("GET", request1.getMethod());

        // revoke timeline
        timelineService.suspendTimeline(new TimelineDTO(testEmployee, testTimeline));

        // sessions closed on revoke
        assertFalse(timelineService.openSessions.containsKey(testEmployee));

        // fetch timeline again fails
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
        timeline = timelineService.fetchTimeline(testTimeline, testEmployee);
        assertTrue(timeline.isEmpty());
    }

    @AfterEach
    void teardown() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}