package com.insidernine.cadbury;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class CadburyActivity extends FragmentActivity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    if (savedInstanceState == null)
    {
      // Starting from scratch
      Fragment frag = new LoadingFragment();
      getSupportFragmentManager().beginTransaction()
      .add(android.R.id.content, frag)
      .commit();
    }
  }
}