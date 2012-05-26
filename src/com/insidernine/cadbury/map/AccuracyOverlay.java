package com.insidernine.cadbury.map;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class AccuracyOverlay extends ItemizedOverlay<AccuracyItem>
{
  @SuppressWarnings("unused")
  private static final String TAG = "AccuracyOverlay";
  private List<AccuracyItem> mItems = new ArrayList<AccuracyItem>();

  public AccuracyOverlay(Drawable defaultMarker)
  {
    super(defaultMarker);
  }

  public void clear()
  {
    mItems.clear();
    populate();
  }

  @Override
  protected AccuracyItem createItem(int i)
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
      return;
    }

    // Manualy draw circles
    for(AccuracyItem item : mItems)
    {

      // Calculate where to draw circle on screen
      Point center = new Point();
      Projection projection = mapView.getProjection();
      projection.toPixels(item.getPoint(), center);

      // Draw circle
      drawBlueCircleOfUncertaintyAndPain(canvas, center, item.getRadius(mapView));
    }

    super.draw(canvas, mapView, shadow);
  }

  @Override
  public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
  {
    return super.draw(canvas, mapView, shadow, when);
  }

  public void addItem(AccuracyItem item)
  {
    mItems.add(item);
    populate();
  }

  private void drawBlueCircleOfUncertaintyAndPain(Canvas canvas, Point center, float radius)
  {
    Paint paint = new Paint();
    //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    paint.setAntiAlias(true);

    // Draw inner circle
    paint.setStyle(Style.FILL);
    paint.setColor(0x183399ff);
    canvas.drawCircle(center.x,
                      center.y,
                      radius,
                      paint);

    // Draw outer circle
    paint.setColor(0x441f8fff);
    paint.setStyle(Style.STROKE);
    canvas.drawCircle(center.x,
                      center.y,
                      radius,
                      paint);

  }
}
