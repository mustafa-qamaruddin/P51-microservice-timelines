package com.mqubits.timelines.services;

import com.mqubits.timelines.models.Timeline;
import com.mqubits.timelines.models.dto.TimelineDTO;
import com.mqubits.timelines.repositories.TimelineRepository;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Component
public class TimelineService {

    @Value("${membership.scheme}")
    protected String membershipScheme;

    @Value("${membership.host}")
    protected String membershipHost;

    @Value("${membership.path}")
    protected String membershipPath;

    @Value("${membership.port}")
    protected String membershipPort;

    @Autowired
    protected TimelineRepository timelineRepository;

    ConcurrentHashMap<String, String> openSessions = new ConcurrentHashMap<>();

    public void createTimeline(TimelineDTO timelineDTO) {
        var network = new Timeline(timelineDTO.getTimeline(), timelineDTO.getCustomer());
        timelineRepository.save(network);
    }

    public void suspendTimeline(TimelineDTO timelineDTO) {
        openSessions.remove(timelineDTO.getCustomer());
    }

    public Optional<Timeline> fetchTimeline(String timelineId, String customerId) {
        // Timeline Service asks for Access Permission from Membership Service given Customer(Employer/or Employee) ID + Timeline ID
        RestTemplate restTemplate = new RestTemplate();
        String url = null;
        try {
            url = new URIBuilder()
                    .setScheme(membershipScheme)
                    .setHost(membershipHost)
                    .setPort(Integer.parseInt(membershipPort))
                    .setPath(membershipPath)
                    .addParameter("customerId", customerId)
                    .addParameter("timeline", timelineId)
                    .build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            return Optional.empty();
        }

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            openSessions.put(customerId, timelineId);
            try {
                return Optional.of(timelineRepository.findOneById(timelineId).get());
            } catch (InterruptedException | ExecutionException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
