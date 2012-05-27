package com.insidernine.cadbury;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SportVenueResponseHandler extends JSONResponseHandler<SportVenue[]>
{
  @Override
  protected SportVenue[] handleJSON(JSONObject json) throws JSONException
  {
    JSONArray jsonVenues = json.getJSONArray("venues");
    
    SportVenue[] venues = new SportVenue[jsonVenues.length()];
    for (int i=0; i<venues.length; i++)
    {
      JSONObject jsonVenue = jsonVenues.getJSONObject(i); 
      SportEnum sport = SportEnum.values()[jsonVenue.getInt("sport")];
          
      Venue venue = new Venue(jsonVenue.getString("venue__foursq_id"), 
          jsonVenue.getString("venue__name"),
          jsonVenue.getDouble("venue__lat"),
          jsonVenue.getDouble("venue__lon"));
      
      venues[i] = new SportVenue(venue, sport);
    }
    return venues;
  }
}
