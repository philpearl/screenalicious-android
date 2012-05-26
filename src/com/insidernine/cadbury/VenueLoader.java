package com.insidernine.cadbury;

import java.io.IOException;

import android.content.Context;
import android.location.Location;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

public class VenueLoader extends AsyncTaskLoader<Result<Venue[]>>
{
  private static final String TAG = "VenueLoader";
  private final HttpLayer mHttpLayer;
  private final Location mLocation;
  
  public VenueLoader(Context context, Location location)
  {
    super(context);
    mHttpLayer = new HttpLayer(context);
    mLocation = location;
  }

  @Override
  public Result<Venue[]> loadInBackground()
  {    
    try
    {
      Log.d(TAG, "loadInBackground" + mLocation);
      return new Result<Venue[]>(mHttpLayer.getVenueList(mLocation));
    }
    catch (IOException e)
    {
      Log.e(TAG, "Failed to get location list", e);
      return new Result<Venue[]>(e);      
    }
  }
  
  @Override
  protected void onStartLoading()
  {
    // TODO: caching
    forceLoad();
  }
  
  @Override
  protected void onStopLoading()
  {
    cancelLoad();
  }

}
