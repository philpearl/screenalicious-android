package com.insidernine.cadbury;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class HttpLayer
{
  private static final String TAG = "HttpLayer";
  private static final String FOURSQUARE_CLIENT_ID = "B3J3RIXGVWI4VVI4JN51F54TIAPZPEYSDB2ZWQVPCCJ1ACZO";
  // TODO: Really, really should obscurize this!
  private static final String FOURSQUARE_CLIENT_SECRET = "HGEGKAUGKXV45WQN4YXMIDCGZBKWM5PKQMLLJ2PSQA0EQT2C";
  
  private static final String OUR_SERVER = "http://vm15223.vps.tagadab.com";
  
  private final Context mContext;
  private final AndroidHttpClient mHttpClient;
  
  private final VenueResponseHandler mVenueResponseHandler = new VenueResponseHandler();
  private final CheckinsResponseHandler mCheckinsResponseHandler = new CheckinsResponseHandler();
  private final SportVenueResponseHandler mSportVenueResponseHandler = new SportVenueResponseHandler();
  
  public HttpLayer(Context context)
  {
    mContext = context;
    mHttpClient = AndroidHttpClient.newInstance("Foursquare");
  }
  
  public void onDestroy()
  {
    mHttpClient.close();
  }
  
  private void addClientInfo(List<BasicNameValuePair> params)
  {
    params.add(new BasicNameValuePair("client_id", FOURSQUARE_CLIENT_ID));
    params.add(new BasicNameValuePair("client_secret", FOURSQUARE_CLIENT_SECRET));
  }
  
  public Venue[] getVenueList(Location location) throws IOException
  {
    List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(5);

    String ll = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
    
    params.add(new BasicNameValuePair("ll", ll));
    params.add(new BasicNameValuePair("intent", "checkin"));    
    params.add(new BasicNameValuePair("v", "20120526"));
    addClientInfo(params);

    String url = "https://api.foursquare.com/v2/venues/search?" + URLEncodedUtils.format(params, "UTF-8");
    Log.d(TAG, "Get url " + url);
    HttpGet get = new HttpGet(url);
    
    return mHttpClient.execute(get, mVenueResponseHandler);
  }
  
  public void checkIn(Venue venue, SportEnum sport, String id) throws IOException
  {
    List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(5);
    
    params.add(new BasicNameValuePair("venue", venue.getId()));
    params.add(new BasicNameValuePair("venue_name", venue.getName()));
    params.add(new BasicNameValuePair("lat", String.valueOf(venue.getLat())));
    params.add(new BasicNameValuePair("lon", String.valueOf(venue.getLon())));
    params.add(new BasicNameValuePair("person", id));
    params.add(new BasicNameValuePair("sport", String.valueOf(sport.ordinal() + 1)));
    
    String url = OUR_SERVER + "/checkin/checkin?" + URLEncodedUtils.format(params, "UTF-8");
    Log.d(TAG, "checkIn url: " + url);
    HttpGet get = new HttpGet(url);
    
    ResponseHandler<Void> handler = new ResponseHandler<Void>()
    {
      public Void handleResponse(HttpResponse response)
          throws ClientProtocolException, IOException
      {
        StatusLine statusLine = response.getStatusLine();
        Log.d(TAG, "Have response " + statusLine);
        if (statusLine.getStatusCode() > 299)
        {
          throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }

        return null;
      }
    };

    mHttpClient.execute(get, handler);
  }
  
  public Checkin[] getCurrentCheckins() throws IOException
  {
    HttpGet get = new HttpGet(OUR_SERVER + "/checkin/get_current_checkins");
    
    return mHttpClient.execute(get, mCheckinsResponseHandler);
  }
  
  public SportVenue[] getSportVenues() throws IOException
  {
    HttpGet get = new HttpGet(OUR_SERVER + "/checkin/get_venues");
    
    return mHttpClient.execute(get, mSportVenueResponseHandler);
  }
}
