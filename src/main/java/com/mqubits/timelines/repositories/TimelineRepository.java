package com.mqubits.timelines.repositories;

import com.mqubits.timelines.models.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, String> {
    CompletableFuture<Timeline> findOneById(String timelineId);
}
