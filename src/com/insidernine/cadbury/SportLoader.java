package com.insidernine.cadbury;

import java.io.IOException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class SportLoader extends AsyncTaskLoader<Result<Sport[]>>
{
  private static final String TAG = "SportLoader";
  private final HttpLayer mHttpLayer;
  
  public SportLoader(Context context)
  {
    super(context);
    mHttpLayer = new HttpLayer(context);
  }

  @Override
  public Result<Sport[]> loadInBackground()
  {
    try
    {
      return new Result<Sport[]>(mHttpLayer.getSportsList());
    }
    catch (IOException e)
    {
      Log.e(TAG, "Failed to get sport list", e);
      return new Result<Sport[]>(e);      
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
