package com.insidernine.cadbury;

import android.content.Context;
import android.widget.ArrayAdapter;

public class VenueAdapter extends ArrayAdapter<Venue>
{
  public VenueAdapter(Context context)
  {
    super(context, R.layout.venue_list_item, android.R.id.text1);
  }

  public void addAll(Venue[] data)
  {
    for (Venue venue : data)
    {
      add(venue);
    }
    
  }
}
