package com.insidernine.cadbury;

import android.content.Context;
import android.widget.ArrayAdapter;

public class VenueAdapter extends ArrayAdapter<Venue>
{
  public VenueAdapter(Context context)
  {
    super(context, android.R.layout.simple_list_item_1);
  }

  public void addAll(Venue[] data)
  {
    for (Venue venue : data)
    {
      add(venue);
    }
    
  }
}
