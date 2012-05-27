package com.insidernine.cadbury;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CheckinButtonFragment extends Fragment
{
  private static final String TAG = "CheckinButtonFragment";
  
  private Spinner mSportSpinner;
  private Button mCheckInButton;
  private SportEnum mSport;
  private Venue mVenue;
  private CheckInAsyncTask mCheckInAsyncTask;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.checkin_sub_layout, container, false);
  }
  
  public void setVenue(Venue venue)
  {
    mVenue = venue;
  }
  
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
    mCheckInButton = (Button) view.findViewById(R.id.check_in_button);
    mCheckInButton.setOnClickListener(new OnClickListener()
    {      
      @Override
      public void onClick(View v)
      {
        Log.d(TAG, "Check into " + mVenue + " to watch " + mSport);
        if (mCheckInAsyncTask != null)
        {
          mCheckInAsyncTask.cancel(false);          
        }
        mCheckInAsyncTask = new CheckInAsyncTask();
        mCheckInAsyncTask.execute();        
      }
    });
    mSportSpinner = (Spinner) view.findViewById(R.id.sport_spinner);
    mSportSpinner.setAdapter(new ArrayAdapter<SportEnum>(getActivity(), 
        android.R.layout.simple_spinner_item, 
        SportEnum.values()));

    mSportSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
    {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        mSport = (SportEnum)parent.getItemAtPosition(position);
        checkEnableCheckin();
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0)
      {
      }
    });
    mSportSpinner.getSelectedItem();
  }
  
  @Override
  public void onDestroy()
  {
    if (mCheckInAsyncTask != null)
    {
      mCheckInAsyncTask.cancel(true);
      mCheckInAsyncTask = null;
    }
    super.onDestroy();
  }

  private void checkEnableCheckin()
  {
    mCheckInButton.setEnabled((mSport != null) && (mVenue != null));
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
      
      Log.d(TAG, "replace " + getId());
      getFragmentManager().beginTransaction()
      .replace(getId(), CheckedInFragment.newInstance(mSport))
      .commit(); 
    }
  }
}
