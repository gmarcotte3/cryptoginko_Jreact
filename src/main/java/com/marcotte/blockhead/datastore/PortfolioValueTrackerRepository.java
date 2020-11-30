package com.marcotte.blockhead.datastore;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PortfolioValueTrackerRepository extends CrudRepository< PortfolioValueTracker, Long>
{
    public List<PortfolioValueTracker> findAllByDateTrackerID(Long dateTrackerID);
}
