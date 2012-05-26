package com.insidernine.cadbury;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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
  
  private static final String OUR_SERVER = "http://server";
  
  private final Context mContext;
  private final AndroidHttpClient mHttpClient;
  
  private final VenueResponseHandler mVenueResponseHandler = new VenueResponseHandler();
  private final SportsResponseHandler mSportsResponseHandler = new SportsResponseHandler();
  
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
  
  public Sport[] getSportsList() throws IOException
  {
    HttpGet get = new HttpGet(OUR_SERVER + "/checkin/sports");
    
    return mHttpClient.execute(get, mSportsResponseHandler);
  }
}
