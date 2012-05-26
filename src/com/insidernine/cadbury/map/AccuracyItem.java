package com.insidernine.cadbury.map;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class AccuracyItem extends OverlayItem
{
  private static final String TAG = "AccuracyItem";
  private float mRadius;

  public AccuracyItem(MapView map, GeoPoint point, float radius)
  {
    super(point, null, null);
    mRadius = radius;
  }

  public int getRadius(MapView map)
  {
    double latitude = ((double)mPoint.getLatitudeE6()) / 1000000;

    Log.d(TAG, "lat " + latitude);
    Log.d(TAG, "radius " + mRadius);

    float equatorPixels = map.getProjection().metersToEquatorPixels(mRadius);
    Log.d(TAG, "equator pixels " + equatorPixels);
    return (int) (equatorPixels / Math.cos(Math.toRadians(latitude)));
  }
}
