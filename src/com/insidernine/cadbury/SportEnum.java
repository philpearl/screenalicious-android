package com.insidernine.cadbury;

import com.google.android.maps.ItemizedOverlay;
import com.insidernine.cadbury.map.LocationOverlay;

import android.content.Context;
import android.graphics.drawable.Drawable;

public enum SportEnum
{
  ROAD_CYCLING("Road cycling", R.drawable.pin_road_cycling),
  BMX("BMX", R.drawable.pin_bmx),
  ATHLETICS("Athletics", R.drawable.pin_athletics),
  DIVING("Diving", R.drawable.pin_diving),
  ARCHERY("Archery", R.drawable.pin_archery)
  ;
    
  
  private final String mName;
  private final int mPinId;
  private Drawable mPin;
  private SportEnum(String name, int pinId)
  {
    mName = name;
    mPinId = pinId;
  }
  
  public String getName()
  {
    return mName;
  }
  
  @Override
  public String toString()
  {
    return mName;
  }

  public Drawable getPin(Context baseContext)
  {
    if (mPin == null)
    {
      mPin = LocationOverlay.boundCenter1(baseContext.getResources().getDrawable(mPinId));
    }
    return mPin;
  }
}
