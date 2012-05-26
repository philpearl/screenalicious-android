package com.insidernine.cadbury;

import java.io.IOException;

import com.insidernine.cadbury.PickVenueFragment.VenueCallback;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CheckinFragment extends Fragment implements VenueCallback
{
  private static final String TAG = "CheckinFragment";
  
  private static final String KEY_LOCATION = "location";
  private static final String KEY_VENUE = "venue";
  
  private Spinner mSportSpinner;
  private Button mCheckInButton;
  private Button mVenueButton;
  private Sport mSport;
  private Venue mVenue;
  private CheckInAsyncTask mCheckInAsyncTask;
  private TextView mCheckInTitle;
  
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
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    mCheckInAsyncTask = new CheckInAsyncTask();
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
    mCheckInButton = (Button) view.findViewById(R.id.check_in_button);
    mCheckInButton.setOnClickListener(new OnClickListener()
    {      
      @Override
      public void onClick(View v)
      {
        Log.d(TAG, "Check into " + mVenue + " to watch " + mSport);
        mCheckInAsyncTask.execute();
        
      }
    });
    mCheckInTitle = (TextView) view.findViewById(R.id.check_in_title);
    mVenueButton = (Button) view.findViewById(R.id.venue_button);
    mVenueButton.setOnClickListener(new OnClickListener()
    {      
      @Override
      public void onClick(View v)
      {
        startActivityForResult(new Intent(getActivity(), PickVenueActivity.class), 1);
        /*
        PickVenueFragment frag = new PickVenueFragment();
        frag.setTargetFragment(CheckinFragment.this, 0);
        getFragmentManager().beginTransaction()
        .addToBackStack(null)
        .replace(android.R.id.content, frag)
        .commit();
        */
      }
    });
    if (mVenue != null)
    {
      mVenueButton.setText(mVenue.getName());
    }
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
  
  @Override
  public void onDestroy()
  {
    mCheckInAsyncTask.cancel(true);
    super.onDestroy();
  }
  
  private void checkEnableCheckin()
  {
    mCheckInButton.setEnabled((mSport != null) && (mVenue != null));
  }

  @Override
  public void onVenuePicked(Venue venue)
  {    
    mVenue = venue;
    Log.d(TAG, "onVenuePicked " + mVenue);
  }
  
  class CheckInAsyncTask extends AsyncTask<Void, Void, Exception>
  {
    private ProgressDialogFragment mProgressDialogFragment;
    
    @Override
    protected void onPreExecute()
    {
      // TODO Show progress dialog
      mProgressDialogFragment = ProgressDialogFragment.newInstance("Checking in", "One moment please.");
      mProgressDialogFragment.show(getFragmentManager(), "Progress Dialog");
    }
    
    @Override
    protected Exception doInBackground(Void... params)
    {
      HttpLayer httpLayer = new HttpLayer(getActivity());
      try
      {
        httpLayer.checkIn(mVenue, mSport, Store.getInstance(getActivity()).getUserId());
      }
      catch (IOException e)
      {
        Log.e(TAG, "Failed to check in", e);
        return e;
      }
      finally
      {
        httpLayer.onDestroy();
      }
      return null;
    }
    
    @Override
    protected void onCancelled()
    {
      if (mProgressDialogFragment != null)
      {
        mProgressDialogFragment.dismiss();
      }
    }
    
    @Override
    protected void onPostExecute(Exception result)
    {
      mProgressDialogFragment.dismiss();
      if (result != null)
      {
        Toast.makeText(getActivity(), "Failed to check in: " + result, Toast.LENGTH_LONG).show();
        return;
      }
      
      // TODO: go somewhere, update to say checked in 
    }
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    Log.d(TAG, "onActivityResult " + data);
    if (data != null)
    {
      mVenue = data.getParcelableExtra(PickVenueActivity.EXTRA_VENUE);
      mVenueButton.setText(mVenue.getName());
      mCheckInTitle.setText(mVenue.getName());
      checkEnableCheckin();
    }
  }
}
