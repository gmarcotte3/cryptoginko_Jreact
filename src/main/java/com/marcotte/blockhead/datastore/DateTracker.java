package com.marcotte.blockhead.datastore;

import javax.persistence.*;
import java.time.Instant;


@Entity
public class DateTracker
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DateUpdated", nullable = false)
    private Instant dateUpdated;

    public DateTracker()
    {
        rightNow();
    }

    public void rightNow()
    {
        this.dateUpdated = Instant.now();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}