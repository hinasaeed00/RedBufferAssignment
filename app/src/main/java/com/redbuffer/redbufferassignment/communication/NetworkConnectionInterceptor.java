package com.redbuffer.redbufferassignment.communication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.redbuffer.redbufferassignment.R;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.Request;

public class NetworkConnectionInterceptor implements Interceptor {

    private Context mContext;

    public NetworkConnectionInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        if (!isOnline()) {
            Toast.makeText(mContext,mContext.getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
    public class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return "Network Connection exception";
        }

    }
}
