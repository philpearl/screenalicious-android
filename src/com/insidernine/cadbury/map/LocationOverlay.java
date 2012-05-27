package com.insidernine.cadbury.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LocationOverlay extends ItemizedOverlay<OverlayItem>
{
  @SuppressWarnings("unused")
  private static final String TAG = "LocationOverlay";
  private ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
  private final Context mContext;

  public LocationOverlay(Context context, Drawable defaultMarker)
  {
    super(boundCenter(defaultMarker));
    mContext = context;
    populate();
  }
  
  public static Drawable boundCenter1(Drawable dr)
  {
    return boundCenter(dr);
  }

  public void addItem(OverlayItem overlay)
  {
    mItems.add(overlay);
    populate();
  }

  public void clear()
  {
    mItems.clear();
    populate();
  }

  @Override
  protected OverlayItem createItem(int i)
  {
    return mItems.get(i);
  }

  @Override
  public int size()
  {
    return mItems.size();
  }

  @Override
  public void draw(Canvas canvas, MapView mapView, boolean shadow)
  {
    if (shadow)
    {
      // No shadows
      return;
    }
    // This draws the dot
    super.draw(canvas, mapView, shadow);
  }
  
  @Override
  protected boolean onTap(int index)
  {
    String title = mItems.get(index).getTitle();
    if (title.length() != 0)
    {
      Toast toast = Toast.makeText(mContext, title,
          Toast.LENGTH_SHORT);
      
      toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
      toast.show();
    }

    return true;
  }
}
