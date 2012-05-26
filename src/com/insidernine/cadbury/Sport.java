package com.insidernine.cadbury;

public class Sport
{
  private int mId;
  private String mName;
  
  public Sport(int id, String name)
  {
    mId = id;
    mName = name;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getId()
  {
    return mId;
  }
}
