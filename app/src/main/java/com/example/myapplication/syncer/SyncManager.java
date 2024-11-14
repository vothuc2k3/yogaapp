package com.example.myapplication.syncer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class SyncManager {

    private static final String TAG = "SyncManager";

    public static void startSyncing(Context context) {
        if (!checkInternetConnection(context)) {
            showToast(context, "No internet connection");
            return;
        } else {
            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
            PeriodicWorkRequest syncRequest = new PeriodicWorkRequest.Builder(Syncer.class, 1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

            WorkManager.getInstance(context).enqueue(syncRequest);
            showToast(context, "Syncing started");
        }   
    }

    private static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private static void showToast(Context context, String message) {
        new Handler(Looper.getMainLooper()).post(() -> 
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        );
    }
}