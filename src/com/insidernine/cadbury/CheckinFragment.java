package com.insidernine.cadbury;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CheckinFragment extends Fragment
{
  private static final String KEY_LOCATION = "location";
  private static final String KEY_VENUE = "venue";
  
  public static CheckinFragment newInstance(Location location, Venue venue)
  {
    Bundle args = new Bundle(2);
    args.putParcelable(KEY_LOCATION, location);
    args.putParcelable(KEY_VENUE, venue);
    CheckinFragment frag = new CheckinFragment();
    frag.setArguments(args);
    return frag;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.check_in, container, false);
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    // Load the sport list
    // TODO: Cache this!
    getLoaderManager().initLoader(0, null, new LoaderCallbacks<Result<Sport[]>>()
    {
      @Override
      public Loader<Result<Sport[]>> onCreateLoader(int arg0, Bundle arg1)
      {
        return new SportLoader(getActivity());
      }

      @Override
      public void onLoadFinished(Loader<Result<Sport[]>> loader,
          Result<Sport[]> result)
      {
        if (result.isFailure())
        {
          Toast.makeText(getActivity(), "Failed to load sports list: " + result.getException(), Toast.LENGTH_LONG).show();
          return;
        }
        
        // TODO: Populate sports list
      }

      @Override
      public void onLoaderReset(Loader<Result<Sport[]>> arg0)
      {
        // Shouldn't be needed        
      }
    });
  }
}
