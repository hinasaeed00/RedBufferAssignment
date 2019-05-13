package com.redbuffer.redbufferassignment.communication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;
import com.redbuffer.redbufferassignment.R;
import com.redbuffer.redbufferassignment.Utils.Utils;
import com.redbuffer.redbufferassignment.communication.responses.ServerError;
import com.redbuffer.redbufferassignment.ui.customViews.CustomProgressBar;
import com.redbuffer.redbufferassignment.ui.customViews.MyDialogs;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ActivityBlockingCallBack<T> implements Callback<T> {
    // private static final Logger logger = Logger.getLogger(ActivityBlockingCallBack.class);
    private final Activity activity;
    private CustomProgressBar pd;

    private boolean interactive = true;
    private boolean finishActivityOnError;

    public ActivityBlockingCallBack(Activity activity) {
        this.activity = activity;
        initModalDialog();
    }

    public ActivityBlockingCallBack(Activity activity, boolean interactive) {
        this.activity = activity;
        this.interactive = interactive;
        initModalDialog();
    }

    public ActivityBlockingCallBack(Activity activity, boolean interactive, boolean finishActivityOnError) {
        this.activity = activity;
        this.interactive = interactive;
        this.finishActivityOnError = finishActivityOnError;
        initModalDialog();
    }

    private void initModalDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    pd = CustomProgressBar.getInstance();
                    pd.showProgress(activity, false);
                } catch (Exception ex) {

                }

            }
        });
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public boolean isFinishActivityOnError() {
        return finishActivityOnError;
    }

    public void setFinishActivityOnError(boolean finishActivityOnError) {
        this.finishActivityOnError = finishActivityOnError;
    }

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        try {
            if (response.isSuccessful()) {
                handleSuccess(response);
            } else {
                try {
                    handleServerError(processResponseForServerError(response));
                } catch (Exception e) {
//                    logger.e("onResponse: handleServerError :: failed > " + e.toString(), e);
                    handleFailure(e);
                }
            }
        } finally {
            if (pd != null && pd.isShowing()) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pd.hideProgress();
                        } catch (Exception ex) {
                            Log.e(getClass().getName(), ex.toString(), ex);
                        }
                    }
                });
            }
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        try {
            handleFailure(t);
        } finally {
            try {
                pd.hideProgress();
            } catch (Exception ex) {
                Log.e(getClass().getName(), ex.toString(), ex);
            }
        }
    }

    public abstract void handleSuccess(Response<T> response);

    public void handleServerError(ServerError serverError) {
        String errorMessage = serverError != null ? serverError.getError() : null;
        errorMessage = errorMessage == null ? serverError.getMessage() : null;
        Log.e("handleServerError > ", errorMessage);
        if (interactive) {

            MaterialStyledDialog dialog = MyDialogs.INSTANCE.createErrorDialog("An error occurred, please try again.", activity);
            if (finishActivityOnError) {
                dialog = getActivityFinishDialog(dialog);
            }
            dialog.show();
        }
    }

    public void handleFailure(Throwable t) {
        Log.e("handleFailure > " + t, t.getMessage());
        if (interactive && !Utils.isInternetAvailable(activity)) {
            MaterialStyledDialog dialog = MyDialogs.INSTANCE.createErrorDialog(activity.getString(R.string.check_internet_connection), activity);
            if (finishActivityOnError) {
                dialog = getActivityFinishDialog(dialog);
            }
            dialog.show();
        } else if (interactive) {
            MaterialStyledDialog dialog = MyDialogs.INSTANCE.createErrorDialog("An error occurred, please try again.", activity);
            if (finishActivityOnError) {
                dialog = getActivityFinishDialog(dialog);
            }
            dialog.show();
        }
    }

    private MaterialStyledDialog getActivityFinishDialog(MaterialStyledDialog dialog) {
        MaterialStyledDialog.Builder builder = dialog.getBuilder();
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                activity.finish();
            }
        });
        return builder.build();
    }

    private ServerError processResponseForServerError(Response<T> response) throws IOException {
        ServerError serverError = new ServerError();
        String jsonString = response.errorBody().string();
//        logger.d("processResponseForServerError > " + jsonString);
        serverError = new Gson().fromJson(jsonString, ServerError.class);
        return serverError;
    }

}
