package com.insidernine.cadbury;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment
{
  public static ProgressDialogFragment newInstance(String title, String message)
  {
    Bundle args = new Bundle(2);
    args.putString("title", title);
    args.putString("message", message);
    ProgressDialogFragment frag = new ProgressDialogFragment();
    frag.setArguments(args);
    return frag;
  }
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  { 
    Bundle args = getArguments();
    ProgressDialog dialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
    dialog.setTitle(args.getString("title"));
    dialog.setMessage(args.getString("message"));
    return super.onCreateDialog(savedInstanceState);
  }
}
