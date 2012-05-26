package com.insidernine.cadbury;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class PickVenueFragment extends ListFragment implements LoaderCallbacks<Result<Venue[]>>
{
  private static final String TAG = "PickLocationFragment";
  
  private Location mLocation;
  private LocationManager mLocationManager;
  private VenueAdapter mAdapter;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    mAdapter = new VenueAdapter(getActivity());
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    // Get current location and then query the  venue list
    // TODO: Keep looking for better location in background and 
    // offer a refresh
    setListShownNoAnimation(false);
    
    setListAdapter(mAdapter);
    
    mLocation = LocationUtils.getBestLastKnownLocation(mLocationManager);
    Bundle args = new Bundle(1);
    args.putParcelable("location", mLocation);
    getLoaderManager().initLoader(1, args, this);
  }
  
  @Override
  public void onListItemClick(ListView l, View v, int position, long id)
  {
    Venue venue = mAdapter.getItem(position);
    Log.d(TAG, "Picked venue " + venue);
    CheckinFragment frag = CheckinFragment.newInstance(mLocation, venue);
    getFragmentManager().beginTransaction()
    .replace(getId(), frag)
    .commit();
  }

  @Override
  public Loader<Result<Venue[]>> onCreateLoader(int id, Bundle args)
  {
    Log.d(TAG, "onCreateLoader");
    return new VenueLoader(getActivity(), (Location) args.getParcelable("location"));
  }

  @Override
  public void onLoadFinished(Loader<Result<Venue[]>> loader, Result<Venue[]> result)
  {
    Log.d(TAG, "onLoadFinished");
    
    if (result.isFailure())
    {
      Toast.makeText(getActivity(), "Failed to get venues: " + result.getException(), Toast.LENGTH_LONG).show();
      return;
    }
    
    mAdapter.clear();
    mAdapter.addAll(result.getData());
    mAdapter.notifyDataSetChanged();
    if (isResumed())
    {
      setListShown(true);
    }
    else
    {
      setListShownNoAnimation(true);
    }    
  }

  @Override
  public void onLoaderReset(Loader<Result<Venue[]>> loader)
  {
    mAdapter.clear();
    mAdapter.notifyDataSetChanged();
  }
}
