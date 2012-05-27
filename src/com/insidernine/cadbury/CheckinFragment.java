package com.insidernine.cadbury;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CheckinFragment extends Fragment
{
  private static final String TAG = "CheckinFragment";
  
  public static final String KEY_VENUE = "venue";
  
  private TextView mCheckInTitle;
  private CheckinButtonFragment mCheckinButtonFragment;
  
  public static CheckinFragment newInstance(Venue venue)
  {
    Log.d(TAG, "newInstance " + venue);
    Bundle args = new Bundle(1);    
    args.putParcelable(KEY_VENUE, venue);
    CheckinFragment frag = new CheckinFragment();
    frag.setArguments(args);
    return frag;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);    
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.check_in, container, false);
  }
  
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState)
  {
    Log.d(TAG, "onViewCreated");
    super.onViewCreated(view, savedInstanceState);

    mCheckInTitle = (TextView) view.findViewById(R.id.check_in_title);
    mCheckinButtonFragment = new CheckinButtonFragment();
    getFragmentManager().beginTransaction()
    .add(R.id.checkin_button_frag, mCheckinButtonFragment)
    .commit();
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
    Venue venue = getArguments().getParcelable(KEY_VENUE);
    mCheckInTitle.setText(venue.getName());
    mCheckinButtonFragment.setVenue(venue);
  }
}
