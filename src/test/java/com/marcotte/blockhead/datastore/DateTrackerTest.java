package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.datastore.datetracker.DateTracker;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

public class DateTrackerTest {

    @Test
    public void rightNow()
    {
        DateTracker datetracker = new DateTracker();

        datetracker.rightNow();
        Instant rightnow = datetracker.getDateUpdated();

        System.out.println("rightnow.getupdated=" + datetracker.getDateUpdated().toString());
        assertTrue( rightnow.toString().endsWith("Z"));
    }

    @Test
    public void getDateUpdated() {
        DateTracker datetracker = new DateTracker();

        datetracker.rightNow();
        Instant rightnow = datetracker.getDateUpdated();

        System.out.println("getUpdated =" + rightnow.toString());
        assertTrue( rightnow.toString().endsWith("Z"));
    }
}