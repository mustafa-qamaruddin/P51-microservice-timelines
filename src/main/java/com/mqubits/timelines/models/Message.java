package com.mqubits.timelines.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Message {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
            name = "system-uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    @Column(name = "`created_at`")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "`updated_at`")
    @UpdateTimestamp
    private Timestamp updatedAt;

}
