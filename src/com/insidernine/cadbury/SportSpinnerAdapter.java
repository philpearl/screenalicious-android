package com.insidernine.cadbury;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SportSpinnerAdapter extends ArrayAdapter<SportEnum>
{
  private int mTextViewResourceId;
  
  public SportSpinnerAdapter(Context context, int resourceId, int textViewResourceId,
      SportEnum[] objects)
  {
    super(context, resourceId, textViewResourceId, objects);
    mTextViewResourceId = textViewResourceId;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    View view = super.getView(position, convertView, parent);
    SportEnum se = getItem(position);
    if (se == SportEnum.ALL)
    {
      TextView tv = (TextView) view.findViewById(mTextViewResourceId);
      tv.setText("What are you watching?");
    }
    return view;
  }
}
