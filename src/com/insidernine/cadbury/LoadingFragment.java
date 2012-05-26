package com.insidernine.cadbury;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LoadingFragment extends Fragment
{
  private static final String TAG = "LoadingFragment";
  private AsyncTask<Void, Void, Result<Sport[]>> mGetSportsAsyncTask;
  
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
    // Load the sport list
    // TODO: Cache this!
    mGetSportsAsyncTask = new GetSportsAsyncTask().execute();
  }
  
  @Override
  public void onDestroy()
  {
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
      CadburyApplication app = (CadburyApplication) getActivity().getApplication();
      app.setSports(result.getData());
      
      getFragmentManager().beginTransaction()
      .replace(android.R.id.content, new CheckinFragment())
      .commit();

    }
  }
}
