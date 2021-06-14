package com.mqubits.timelines.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Timeline {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    @Column(name = "`created_at`")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "`updated_at`")
    @UpdateTimestamp
    private Timestamp updatedAt;

    private String owner;

    @OneToMany
    private List<String> messages;

    public Timeline() {
    }

    public Timeline(String id, String owner) {
        this.owner = owner;
        this.id = id;
    }
}
