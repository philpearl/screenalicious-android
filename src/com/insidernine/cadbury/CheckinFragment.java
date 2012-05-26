package com.insidernine.cadbury;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CheckinFragment extends Fragment
{
  private static final String KEY_LOCATION = "location";
  private static final String KEY_VENUE = "venue";
  
  private Spinner mSportSpinner;
  private Button mCheckInButton;
  private Sport mSport;
  private Venue mVenue;
  
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
  public void onViewCreated(View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
    mCheckInButton = (Button) view.findViewById(R.id.check_in_button);
    mSportSpinner = (Spinner) view.findViewById(R.id.sport_spinner);
    mSportSpinner.setAdapter(new ArrayAdapter<Sport>(getActivity(), 
        android.R.layout.simple_spinner_item, 
        ((CadburyApplication)getActivity().getApplication()).getSports()));
    mSportSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
    {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        mSport = (Sport)parent.getItemAtPosition(position);
        checkEnableCheckin();
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0)
      {
      }
    });
    
    checkEnableCheckin();
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    
  }
  
  private void checkEnableCheckin()
  {
    mCheckInButton.setEnabled((mSport != null) && (mVenue != null));
  }
}
