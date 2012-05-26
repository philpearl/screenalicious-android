package com.insidernine.cadbury;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class PickVenueActivity extends FragmentActivity
{
  public static final String EXTRA_VENUE = "venue";
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    if (savedInstanceState == null)
    {
      // Starting from scratch
      Fragment frag = new PickVenueFragment();
      getSupportFragmentManager().beginTransaction()
      .add(android.R.id.content, frag)
      .commit();
    }
  }
}
