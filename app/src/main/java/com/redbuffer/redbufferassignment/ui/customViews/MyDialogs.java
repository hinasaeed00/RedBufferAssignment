package com.redbuffer.redbufferassignment.ui.customViews;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.redbuffer.redbufferassignment.R;


public enum MyDialogs {
    INSTANCE;
    private ProgressDialog mDialog;
    private Dialog dialog;
    //private Context mContext;

//    public void showLoader(Context context, String title) {
//        showLoader(context, title, false);
//    }

//    public void showLoader(Context context, String title, boolean cancelable) {
//        // this.mContext = context;
//        mDialog = new ProgressDialog(context);
//        mDialog.setCancelable(cancelable);
//        mDialog.setMessage(title);
//        mDialog.show();
//    }
    public MaterialStyledDialog createErrorDialog(String title, Context context) {
        // this.mContext = context;
        MaterialStyledDialog dialog =new MaterialStyledDialog.Builder(context)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setHeaderDrawable(R.drawable.header)
                .withDarkerOverlay(true)
                .setTitle("Error")
                .setDescription(title)
                .setPositiveText("OK").show();

        return  dialog;

    }
    public void dismissLoader(Context context) {
        try {
            if (null != context && !((AppCompatActivity) context).isFinishing()) {
                if (null != mDialog && null != mDialog.getContext() && mDialog.isShowing())
                    mDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
         //   RemoteLogger.get().logException(e);
        }
    }

    }
