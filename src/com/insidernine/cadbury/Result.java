package com.insidernine.cadbury;

public class Result<T>
{
  private Exception mException;
  private T mData;
  
  public Result(T data)
  {
    mData = data;
  }
  
  public Result(Exception e)
  {
    mException = e;
  }
  
  public boolean isFailure()
  {
    return mException != null;
  }
  
  public T getData()
  {
    return mData;
  }
  
  public Exception getException()
  {
    return mException;
  }
}
