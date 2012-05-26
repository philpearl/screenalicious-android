package com.insidernine.cadbury;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationUtils
{
  private static final String TAG = "LocationUtils";
  
  private static long OLD_THRESHOLD = 5 * 60 * 1000;
  
  public static Location getBestLastKnownLocation(LocationManager locationManager)
  {
    long now = System.currentTimeMillis();
    
    Location best = null;
    
    // Find the best current location from all providers
    for (String providerName : locationManager.getProviders(false))
    {
      Log.d(TAG, "Provider name: " + providerName);
      Location location = locationManager.getLastKnownLocation(providerName);
      
      if (location != null)
      {
        if (best == null)
        {
          Log.d(TAG, "No best, use this one");
          best = location;
        }
        else if (now - best.getTime() > OLD_THRESHOLD)
        {
          Log.d(TAG, "Current best is old");
          if (now - location.getTime() <= OLD_THRESHOLD)
          {
            Log.d(TAG, "new isn't old");
            best = location;
          }
          else if (location.hasAccuracy() && (!best.hasAccuracy() || (location.getAccuracy() < best.getAccuracy())))
          {            
            Log.d(TAG, "Best either has no accurary or is less accurate");
            best = location;
          }
          else
          {
            Log.d(TAG, "Stick with best");
          }
        }
        else 
        {
          Log.d(TAG, "best is not old");
          if (now - location.getTime() <= OLD_THRESHOLD)
          {
            Log.d(TAG, "new isn't old");
            if (location.hasAccuracy() && (!best.hasAccuracy() || (location.getAccuracy() < best.getAccuracy())))
            {            
              Log.d(TAG, "Best either has no accurary or is less accurate");
              best = location;
            }
            else
            {
              Log.d(TAG, "Stick wth best");
            }
          }
          else
          {
            Log.d(TAG, "Stick wth best");
          }
        }
      }
    }
    
    return best;
  }
}
