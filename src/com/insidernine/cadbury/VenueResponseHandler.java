package com.insidernine.cadbury;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class VenueResponseHandler extends JSONResponseHandler<Venue[]>
{
  private static final String TAG = "VenueResponseHandler";
  
  @Override
  protected Venue[] handleJSON(JSONObject json) throws JSONException
  {
    JSONArray jsonVenues = json.getJSONObject("response").getJSONArray("venues");
    Venue[] venues = new Venue[jsonVenues.length()];
    for (int i = 0; i < venues.length; i++)
    {
      JSONObject jsonVenue = jsonVenues.getJSONObject(i);
      
      JSONObject location = jsonVenue.getJSONObject("location");
      
      Venue venue = new Venue(jsonVenue.getString("id"), jsonVenue.getString("name"), location.getDouble("lat"), location.getDouble("lng"));
      // TODO: determine category and get an icon url
      venues[i] = venue;
    }
    return venues;
  }
}
