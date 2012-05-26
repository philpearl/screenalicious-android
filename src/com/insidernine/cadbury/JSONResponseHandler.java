package com.insidernine.cadbury;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class JSONResponseHandler<T> implements ResponseHandler<T>
{
  private static final String TAG = "JSONResponseHandler";

  @Override
  public T handleResponse(HttpResponse response)
      throws ClientProtocolException, IOException
  {
    StatusLine statusLine = response.getStatusLine();
    Log.d(TAG, "Have response " + statusLine);
    if (statusLine.getStatusCode() > 299)
    {
      throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
    }

    try
    {      
      JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
      Log.v(TAG, json.toString(2));
      return handleJSON(json);
    }
    catch (ParseException e)
    {
      Log.e(TAG, "Exception retrieving data", e);
      throw (IOException)(new IOException().initCause(e));
    }
    catch (JSONException e)
    {
      Log.e(TAG, "Exception retrieving data", e);
      throw (IOException)(new IOException().initCause(e));
    }
  }

  protected abstract T handleJSON(JSONObject json) throws JSONException;
}
