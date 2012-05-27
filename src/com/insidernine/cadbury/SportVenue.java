package com.insidernine.cadbury;

public class SportVenue 
{
  private final SportEnum mSport;
  private final Venue mVenue;

  public SportVenue(Venue venue, SportEnum sport)
  {
    mVenue = venue;
    mSport = sport;
  }

  public Venue getVenue()
  {
    return mVenue;
  }
  
  public SportEnum getSport()
  {
    return mSport;
  }
}
