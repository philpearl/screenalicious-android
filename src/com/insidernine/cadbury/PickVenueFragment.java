package com.insidernine.cadbury;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class PickVenueFragment extends ListFragment implements LoaderCallbacks<Result<Venue[]>>
{
  private static final String TAG = "PickLocationFragment";
  
  private Location mLocation;
  private LocationManager mLocationManager;
  private VenueAdapter mAdapter;
  private Handler mHandler;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    mAdapter = new VenueAdapter(getActivity());
    mHandler = new Handler();    
  }
  
  private final Runnable mQuitRunnable = new Runnable()
  {
    public void run()
    {
      getFragmentManager().popBackStack();
    }
  };
  
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
  {
    return inflater.inflate(R.layout.venue_list, container, false);
  }
    
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    // Get current location and then query the  venue list
    // TODO: Keep looking for better location in background and 
    // offer a refresh
    setListAdapter(mAdapter);
    
    mLocation = LocationUtils.getBestLastKnownLocation(mLocationManager);
    Bundle args = new Bundle(1);
    args.putParcelable("location", mLocation);
    getLoaderManager().initLoader(1, args, this);
  }
  
  @Override
  public void onDestroy()
  {
    mHandler.removeCallbacks(mQuitRunnable);
    super.onDestroy();
  }
  
  @Override
  public void onListItemClick(ListView l, View v, int position, long id)
  {
    Venue venue = mAdapter.getItem(position);
    Log.d(TAG, "Picked venue " + venue);
    //((VenueCallback)getTargetFragment()).onVenuePicked(venue);
    getActivity().setResult(getTargetRequestCode(), new Intent().putExtra(PickVenueActivity.EXTRA_VENUE, venue));
    getActivity().finish();
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
      // Can't pop frag of stack diretcly in loader callback
      mHandler.post(mQuitRunnable);
      return;
    }
    
    mAdapter.clear();
    mAdapter.addAll(result.getData());
    mAdapter.notifyDataSetChanged();
  }

  @Override
  public void onLoaderReset(Loader<Result<Venue[]>> loader)
  {
    mAdapter.clear();
    mAdapter.notifyDataSetChanged();
  }
  
  public interface VenueCallback
  {
    public void onVenuePicked(Venue venue);
  }
}
