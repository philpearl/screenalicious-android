package com.insidernine.cadbury;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Store
{
  private static final String KEY_USER_ID = "user id";
  
  private static Store sInstance = null;
  private final SharedPreferences mSharedPreferences;
  
  
  public static synchronized Store getInstance(Context context)
  {
    if (sInstance == null)
    {
      sInstance = new Store(context.getApplicationContext());
    }
    return sInstance;
  }
  
  private Store(Context context)
  {
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }
  
  public synchronized String getUserId()
  {
    String userId = mSharedPreferences.getString(KEY_USER_ID, null);
    if (userId == null)
    {
      userId = UUID.randomUUID().toString();
      mSharedPreferences.edit()
      .putString(KEY_USER_ID, userId)
      .apply();
    }
    
    return userId;
  }  
}
