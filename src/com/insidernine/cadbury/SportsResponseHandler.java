package com.insidernine.cadbury;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SportsResponseHandler extends JSONResponseHandler<Sport[]>
{

  @Override
  protected Sport[] handleJSON(JSONObject json) throws JSONException
  {
    JSONArray sportsJson = json.getJSONArray("sports");
    Sport[] sports = new Sport[sportsJson.length()];
    
    for (int i=0; i<sports.length; i++)
    {
      JSONObject sportJson = sportsJson.getJSONObject(i);
      sports[i] = new Sport(sportJson.getInt("id"), sportJson.getString("name"));
    }
    return sports;
  }
}
