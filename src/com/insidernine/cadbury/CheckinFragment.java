package com.insidernine.cadbury;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    // TODO Auto-generated method stub
    super.onActivityCreated(savedInstanceState);
  }
}
