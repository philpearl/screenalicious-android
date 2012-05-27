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
      PickVenueFragment frag = PickVenueFragment.newInstance((Location) getIntent().getParcelableExtra(CadburyMapActivity.EXTRA_LOCATION));
      getSupportFragmentManager().beginTransaction()
      .add(android.R.id.content, frag)
      .hide(frag)
      .commit();
    }        
  }
}