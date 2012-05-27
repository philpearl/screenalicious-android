package com.insidernine.cadbury;

import java.io.IOException;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.insidernine.cadbury.map.AccuracyItem;
import com.insidernine.cadbury.map.AccuracyOverlay;
import com.insidernine.cadbury.map.LocationOverlay;

public class CadburyMapActivity extends MapActivity implements LocationListener
{
  private static final String TAG = "LocationDefinitionActivity";

  public static final String EXTRA_LOCATION = "location";

  private MapView mMapView;
  private Button mButton;
  private Spinner mSportSelector;
  private LocationManager mLocationManager;

  private List<Overlay> mMapOverlays;
  private Drawable mPinDrawable;
  private LocationOverlay mLocationOverlay;
  private AccuracyOverlay mAccuracyOverlay;
  private LocationOverlay mVenueOverlay;
  private SportVenueAsyncTask mSportVenueAsyncTask;
  private SportVenue[] mSportVenues;
  
  private Location mLocation;

  @Override
  protected void onCreate(Bundle icicle)
  {
    Log.d(TAG, "onCreate");
    super.onCreate(icicle);

    mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    setContentView(R.layout.map);

    mMapView = (MapView) findViewById(R.id.mapview);
    mButton = (Button) findViewById(R.id.checkin);
    mSportSelector = (Spinner) findViewById(R.id.sport_filter);
    
    ArrayAdapter<SportEnum> adapter = new ArrayAdapter<SportEnum>(this, 
        R.layout.spinner_item,
        android.R.id.text1,
        SportEnum.values()); 
    mSportSelector.setAdapter(adapter);
    adapter.setDropDownViewResource(R.layout.spinner_item_light);
    
    mSportSelector.setOnItemSelectedListener(new OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
          long arg3)
      {
        showSportVenues();
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0)
      {
      }
    });

    // These get in the way
    mMapView.setBuiltInZoomControls(false);

    mMapOverlays = mMapView.getOverlays();
    mPinDrawable = getResources().getDrawable(R.drawable.location_pin);
    mLocationOverlay = new LocationOverlay(this, mPinDrawable);
    mAccuracyOverlay = new AccuracyOverlay(mPinDrawable);
    mVenueOverlay = new LocationOverlay(this, mPinDrawable); // Won't ever use this pin!

    mMapOverlays.add(mVenueOverlay);
    mMapOverlays.add(mAccuracyOverlay);
    mMapOverlays.add(mLocationOverlay);    

    // Connect up the go button
    mButton.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        startActivity(new Intent(CadburyMapActivity.this, CheckInActivity.class)
        .putExtra(EXTRA_LOCATION, mLocation));
      }
    });
  }
  
  @Override
  protected void onDestroy()
  {
    if (mSportVenueAsyncTask != null)
    {
      mSportVenueAsyncTask.cancel(false);
      mSportVenueAsyncTask = null;
    }

    super.onDestroy();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    // Actively track position using all providers while the app is active
    for (String provider: mLocationManager.getProviders(true))
    {
      mLocationManager.requestLocationUpdates(provider, 60000, 10, this);
    }
    
    if (mSportVenueAsyncTask != null)
    {
      mSportVenueAsyncTask.cancel(false);
    }
    mSportVenueAsyncTask = new SportVenueAsyncTask();
    mSportVenueAsyncTask.execute();
  }

  @Override
  protected void onPause()
  {
    mLocationManager.removeUpdates(this);
    super.onPause();
  }

  public void onNewLocation(Location location)
  {
    mLocation = location;
    GeoPoint point = geoPoint(location.getLatitude(), location.getLongitude());

    mLocationOverlay.clear();
    mAccuracyOverlay.clear();
    
    mLocationOverlay.addItem(new OverlayItem(point, "", ""));
    mAccuracyOverlay.addItem(new AccuracyItem(mMapView, point, location.getAccuracy()));

    mMapView.setTag(location);

    MapController mapController = mMapView.getController();

    mapController.setCenter(point);
    mapController.setZoom(19);

    mMapView.postInvalidate();
  }

  private GeoPoint geoPoint(double lat, double lon)
  {
    return new GeoPoint((int)(1000000 * lat), (int)(1000000 * lon));
  }

  @Override
  protected boolean isRouteDisplayed()
  {
    return false;
  }

  @Override
  public void onLocationChanged(Location location)
  {
    onNewLocation(LocationUtils.getBestLocation(mLocationManager));
  }

  @Override
  public void onProviderDisabled(String provider)
  {
  }

  @Override
  public void onProviderEnabled(String provider)
  {
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras)
  {
  }
  
  class SportVenueAsyncTask extends AsyncTask<Void, Void, Result<SportVenue[]>>
  {
    @Override
    protected Result<SportVenue[]> doInBackground(Void... params)
    {
      HttpLayer httpLayer = new HttpLayer(CadburyMapActivity.this);
      try
      {
        return new Result<SportVenue[]>(httpLayer.getSportVenues());
      }
      catch (IOException e)
      {
        Log.e(TAG, "Failed to get list of sport venues");
        return new Result<SportVenue[]>(e);
      }
      finally
      {
        httpLayer.onDestroy();
      }
    }
    
    @Override
    protected void onPostExecute(Result<SportVenue[]> result)
    {
      if (result.isFailure())
      {
        Toast.makeText(getBaseContext(), "Failed to get checkin list: " + result.getException(), Toast.LENGTH_LONG).show();
      }

      mSportVenues = result.getData();
      showSportVenues();
    }
    
  }
  
  private void showSportVenues()
  {
    if (mSportVenues == null)
    {
      return;
    }
    SportEnum filter = (SportEnum) mSportSelector.getSelectedItem();    
   
    mVenueOverlay.clear();
    for (SportVenue sp: mSportVenues)
    {
      if ((filter != SportEnum.ALL) && (filter != sp.getSport()))
      {
        continue;
      }
      Venue venue = sp.getVenue();
      GeoPoint point = geoPoint(venue.getLat(), venue.getLon());
      OverlayItem item = new OverlayItem(point, venue.getName(), venue.getName());
      item.setMarker(sp.getSport().getPin(getBaseContext()));
      Log.d(TAG, "Add item for " + sp.getSport() + " @" + venue);
      Log.d(TAG, "Point is " + point);
      mVenueOverlay.addItem(item);        
    }

    mMapView.postInvalidate();
  }
}
