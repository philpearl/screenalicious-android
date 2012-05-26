package com.insidernine.cadbury;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationUtils
{
  private static final String TAG = "LocationUtils";
  
  // Some phones don't report an accuracy for GPS - this is simply a bug.
  // If this happens we write in a minimum accuracy we expect for GPS
  private static final float GPS_ACCURACY_HACK = 50;

  // What constitutes "much newer" for a location fix?
  private static final int COMPARE_MUCH_NEWER = 5 * 60 * 1000;

  /**
   * Get the best current location from all possible providers.  Does not cause
   * an active location lookup
   * @param locationManager
   * @return Best location available
   */
  public static Location getBestLocation(LocationManager locationManager)
  {
    Location bestLocation = null;
    for (String providerName : locationManager.getAllProviders())
    {
      if (LocationManager.PASSIVE_PROVIDER.equals(providerName))
      {
        continue;
      }

      Log.d(TAG, "check " + providerName + " best so far " + bestLocation);

      Location location = locationManager.getLastKnownLocation(providerName);
      if (location == null)
      {
        Log.d(TAG, "no location");
        continue;
      }

      if (LocationManager.GPS_PROVIDER.equals(providerName) && !location.hasAccuracy())
      {
        location.setAccuracy(GPS_ACCURACY_HACK);
      }

      if (bestLocation == null)
      {
        bestLocation = location;
        continue;
      }

      // Possible criteria for comparing two locations
      // 1. One is old data
      // 2. Both are reasonably current, but the newer's accuracy circle implies
      //    movement
      // 3. One claims more accuracy than the other
      Location newer;
      Location older;
      if (location.getTime() > bestLocation.getTime())
      {
        newer = location;
        older = bestLocation;
        Log.d(TAG, "current best " + bestLocation.getProvider() + " is older ");
      }
      else
      {
        newer = bestLocation;
        older = location;
        Log.d(TAG, "current best " + bestLocation.getProvider() + " is newer");
      }

      if (newer.getTime() > older.getTime() + COMPARE_MUCH_NEWER)
      {
        Log.d(TAG, "newer is much newer");
        bestLocation = newer;
        continue;
      }

      // Both are reasonably timely

      float dist = newer.distanceTo(older);
      if (newer.hasAccuracy() && (dist > newer.getAccuracy() + older.getAccuracy()))
      {
        // Accuracy circles don't intersect, so new location implies movement
        Log.d(TAG, "newer doesn't intersect with old - moved");
        bestLocation = newer;
        continue;
      }

      // pick the most accurate
      if (older.hasAccuracy() &&
          ((!newer.hasAccuracy()) ||
           (older.getAccuracy() < newer.getAccuracy())))
      {
        // Older is more accurate
        Log.d(TAG, "older " + older.getProvider() + " is more accurate");
        bestLocation = older;
        continue;
      }

      // Newer is either newer or more accurate, go with it
      bestLocation = newer;
    }

    Log.d(TAG, "Best location: " + bestLocation);
    return bestLocation;
  }

  
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
