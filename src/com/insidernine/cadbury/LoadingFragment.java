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
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
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
    
    mHandler.postDelayed(mSplash, MINIMUM_TIME);
  }
  
  private final Runnable mSplash = new Runnable()
  {    
    @Override
    public void run()
    {
      startActivity(new Intent(getActivity(), CadburyMapActivity.class));
      getActivity().finish();
    }
  };
  
  @Override
  public void onDestroy()
  {
    mHandler.removeCallbacks(mSplash);
    super.onDestroy();
  }
}
