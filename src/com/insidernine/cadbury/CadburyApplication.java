package com.insidernine.cadbury;

import android.app.Application;

public class CadburyApplication extends Application
{
  private Sport[] mSports;
  
  
  public void setSports(Sport[] sports)
  {
    mSports = sports;
  }
  
  public Sport[] getSports()
  {
    return mSports;
  }
}
