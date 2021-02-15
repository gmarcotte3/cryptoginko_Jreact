package com.marcotte.blockhead.services.datetracker;

import com.marcotte.blockhead.datastore.datetraker.DateTracker;
import com.marcotte.blockhead.datastore.datetraker.DateTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DateTrackerService
{
    private static final Logger log = LoggerFactory.getLogger(DateTrackerService.class);

    @Autowired
    private DateTrackerRepository dateTrackerRepository;

    public void save(DateTracker dateTracker)
    {
        dateTrackerRepository.save(dateTracker);
    }

    public DateTracker getDateTrackerByID(Long dateTrackerID)
    {
        Optional<DateTracker> foundDateTracker =  dateTrackerRepository.findById(dateTrackerID);
        if ( foundDateTracker.isPresent())
        {
            return foundDateTracker.get();
        }
        log.error("DateTracker not found for ID={}", dateTrackerID);
        return null;
    }

    public List<DateTracker> getAll()
    {
        List<DateTracker> foundDateTrackers = new ArrayList<>();
        for (DateTracker dateTracker : dateTrackerRepository.findAll())
        {
           foundDateTrackers.add(dateTracker);
        }
        return foundDateTrackers;
    }
}
