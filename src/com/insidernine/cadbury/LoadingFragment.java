package com.insidernine.cadbury;

import java.io.IOException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LoadingFragment extends Fragment
{
  private static final String TAG = "LoadingFragment";
  private static final long MINIMUM_TIME = 1337;
  private Handler mHandler;
  private CadburyApplication mApplication;
  
  private AsyncTask<Void, Void, Result<Sport[]>> mGetSportsAsyncTask;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mApplication = (CadburyApplication)getActivity().getApplication();
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.loading, container, false);
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    mHandler = new Handler();
    // Load the sport list
    mGetSportsAsyncTask = new GetSportsAsyncTask().execute();
    
    mHandler.postDelayed(mSplash, MINIMUM_TIME);
  }
  
  private boolean mTimeUp;
  private final Runnable mSplash = new Runnable()
  {    
    @Override
    public void run()
    {
      mTimeUp = true;
      moveOn();
    }
  };
  
  @Override
  public void onDestroy()
  {
    mHandler.removeCallbacks(mSplash);
    if (mGetSportsAsyncTask != null)
    {
      mGetSportsAsyncTask.cancel(false);
      mGetSportsAsyncTask = null;
    }
    super.onDestroy();
  }
  
  class GetSportsAsyncTask extends AsyncTask<Void, Void, Result<Sport[]>>
  {

    @Override
    protected Result<Sport[]> doInBackground(Void... params)
    {
      HttpLayer httpLayer = new HttpLayer(getActivity());
      try
      {        
        return new Result<Sport[]>(httpLayer.getSportsList());
      }
      catch (IOException e)
      {
        Log.e(TAG, "Failed to get sport list", e);
        return new Result<Sport[]>(e);      
      }
      finally
      {
        httpLayer.onDestroy();
      }
    }
    
    @Override
    protected void onPostExecute(Result<Sport[]> result)
    {
      if (result.isFailure())
      {
        Toast.makeText(getActivity(), "Failed to load sports list: " + result.getException(), Toast.LENGTH_LONG).show();
        return;
      }
      
      // Cache sports list and continue
      mApplication.setSports(result.getData());
      moveOn();
    }
  }
  
  private void moveOn()
  {
    if ((mApplication.getSports() != null) && (mTimeUp))
    {
      startActivity(new Intent(getActivity(), CheckInActivity.class));
      getActivity().finish();
    }
  }
}
