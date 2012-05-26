package com.insidernine.cadbury;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

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
      .add(android.R.id.content, new CheckinFragment())
      .commit();
    }        
  }
}