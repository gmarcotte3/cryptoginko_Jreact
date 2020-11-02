package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.model.CoinList;
import com.marcotte.blockhead.datastore.PortfolioTracker;

import java.util.List;

public class PortfolioCheckResults
{
  private List<PortfolioTracker> portfollioSummary;
  private List<CoinList> portfolioList;

  public PortfolioCheckResults(List<PortfolioTracker> portfollioSummary, List<CoinList> portfolioList)
  {
    this.portfollioSummary = portfollioSummary;
    this.portfolioList = portfolioList;
  }

  public List<PortfolioTracker> getPortfollioSummary()
  {
    return portfollioSummary;
  }

  public void setPortfollioSummary(List<PortfolioTracker> portfollioSummary)
  {
    this.portfollioSummary = portfollioSummary;
  }

  public List<CoinList> getPortfolioList()
  {
    return portfolioList;
  }

  public void setPortfolioList(List<CoinList> portfolioList)
  {
    this.portfolioList = portfolioList;
  }
}
