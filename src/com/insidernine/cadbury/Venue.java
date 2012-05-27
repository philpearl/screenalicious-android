package com.insidernine.cadbury;

import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable
{
  private final String mName;
  private final String mId;
  private final Double mLat;
  private final Double mLon;
  
  public Venue(final String id, final String name, Double lat, Double lon)
  {
    mName = name;
    mId = id;
    mLat = lat;
    mLon = lon;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public Double getLat()
  {
    return mLat;
  }
  
  public Double getLon()
  {
    return mLon;
  }
  
  @Override
  public String toString()
  {
    return mName;
  }

  @Override
  public int describeContents()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(mId);
    dest.writeString(mName);
    dest.writeDouble(mLat);
    dest.writeDouble(mLon);
  }
  
  protected Venue(Parcel in)
  {
    mId = in.readString();
    mName = in.readString();
    mLat = in.readDouble();
    mLon = in.readDouble();
  }
  
  public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() 
  {
    public Venue createFromParcel(Parcel in) 
    {
      return new Venue(in);
    }

    public Venue[] newArray(int size) 
    {
      return new Venue[size];
    }
  };
}
