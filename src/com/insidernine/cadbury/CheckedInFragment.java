package com.insidernine.cadbury;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CheckedInFragment extends Fragment
{
  public static final String KEY_SPORT = "sport";
  private TextView mEnjoy;
  
  public static CheckedInFragment newInstance(SportEnum sport)
  {
    Bundle args = new Bundle(1);
    args.putInt(KEY_SPORT, sport.ordinal());
    CheckedInFragment frag = new CheckedInFragment();
    frag.setArguments(args);
    return frag;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.checked_in, container, false);
  }
  
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
    mEnjoy = (TextView)view.findViewById(R.id.enjoy_the_sport);
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    SportEnum sport = SportEnum.values()[getArguments().getInt(KEY_SPORT)];
    mEnjoy.setText("Enjoy the " + sport.getName() + "!".toUpperCase());
  }
}
