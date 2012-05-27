package com.insidernine.cadbury;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CheckInActivity extends FragmentActivity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null)
    {
      // Starting from scratch
      getSupportFragmentManager().beginTransaction()
      .add(android.R.id.content, PickVenueFragment.newInstance((Location) getIntent().getParcelableExtra(CadburyMapActivity.EXTRA_LOCATION)))
      .commit();
    }        
  }
}