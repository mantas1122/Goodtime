package com.apps.adrcotfas.goodtime.BL;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.apps.adrcotfas.goodtime.Main.TimerActivity;
import com.apps.adrcotfas.goodtime.R;
import com.apps.adrcotfas.goodtime.Util.Constants;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class AppNotificationManager {

    private static final String TAG = AppNotificationManager.class.getSimpleName();

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public AppNotificationManager(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(notificationChannel);
            mBuilder = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            mBuilder = new NotificationCompat.Builder(context);
        }
    }

    public Notification createNotification(Context context, CurrentSession currentSession) {

        mBuilder.mActions.clear();
        mBuilder.addAction(buildStopAction(context));

        if (currentSession.getSessionType().getValue() == SessionType.WORK) {
            if (currentSession.getTimerState().getValue() == TimerState.ACTIVE) {
                mBuilder.addAction(buildPauseAction(context))
                        .setContentTitle("Work session in progress")
                        .setContentText("x minutes remaining");
            } else if (currentSession.getTimerState().getValue() == TimerState.PAUSED) {
                mBuilder.addAction(buildResumeAction(context))
                        .setContentTitle("Work session is paused")
                        .setContentText("Continue?");
            } else {
                Log.wtf(TAG, "Trying to create a notification in an invalid state.");
            }
        }

        return mBuilder
                .setSmallIcon(R.drawable.ic_status_goodtime)
                .setContentIntent(createActivityIntent(context))
                .setOngoing(true)
                .build();
    }

    private PendingIntent createActivityIntent(Context context) {
        return PendingIntent.getActivity(context,
                0, new Intent(context, TimerActivity.class), 0);
    }

    //TODO: add string resources
    private NotificationCompat.Action buildStopAction(Context context) {
        Intent stopIntent = new Intent(context, TimerService.class);
        stopIntent.setAction(Constants.ACTION.STOP_TIMER);
        PendingIntent stopPendingIntent = PendingIntent.getService(context,
                0, stopIntent, 0);

        return new NotificationCompat.Action.Builder(
                R.drawable.ic_notification_stop,
                "STOP",
                stopPendingIntent).build();
    }

    private NotificationCompat.Action buildResumeAction(Context context) {
        Intent toggleIntent = new Intent(context, TimerService.class);
        toggleIntent.setAction(Constants.ACTION.TOGGLE_TIMER);
        PendingIntent togglePendingIntent = PendingIntent.getService(context,
                0, toggleIntent, 0);

        return new NotificationCompat.Action.Builder(
                R.drawable.ic_notification_resume,
                "RESUME",
                togglePendingIntent).build();
    }

    private NotificationCompat.Action buildPauseAction(Context context) {
        Intent toggleIntent = new Intent(context, TimerService.class);
        toggleIntent.setAction(Constants.ACTION.TOGGLE_TIMER);
        PendingIntent togglePendingIntent = PendingIntent.getService(context,
                0, toggleIntent, 0);

        return new NotificationCompat.Action.Builder(
                R.drawable.ic_notification_pause,
                "PAUSE",
                togglePendingIntent).build();
    }

    private NotificationCompat.Action buildStartWorkAction(Context context) {
        Intent toggleIntent = new Intent(context, TimerService.class);
        toggleIntent.setAction(Constants.ACTION.START_WORK);
        PendingIntent togglePendingIntent = PendingIntent.getService(context,
                0, toggleIntent, 0);

        return new NotificationCompat.Action.Builder(
                R.drawable.ic_notification_resume,
                "START WORK",
                togglePendingIntent).build();
    }

    private NotificationCompat.Action buildStartBreakAction(Context context) {
        Intent toggleIntent = new Intent(context, TimerService.class);
        toggleIntent.setAction(Constants.ACTION.START_BREAK);
        PendingIntent togglePendingIntent = PendingIntent.getService(context,
                0, toggleIntent, 0);

        return new NotificationCompat.Action.Builder(
                R.drawable.ic_notification_resume,
                "START BREAK",
                togglePendingIntent).build();
    }

    private NotificationCompat.Action buildSkipBreakAction(Context context) {
        Intent toggleIntent = new Intent(context, TimerService.class);
        toggleIntent.setAction(Constants.ACTION.SKIP_BREAK);
        PendingIntent togglePendingIntent = PendingIntent.getService(context,
                0, toggleIntent, 0);

        return new NotificationCompat.Action.Builder(
                R.drawable.ic_notification_skip,
                "SKIP BREAK",
                togglePendingIntent).build();
    }

    public void notifyFinished(Context context, CurrentSession currentSession) {

        mBuilder.mActions.clear();

        //TODO: set sound according to preferences
        if (currentSession.getSessionType().getValue() == SessionType.WORK) {
            mBuilder.setContentTitle("Work session finished")
                    .setContentText("Continue?")
                    .addAction(buildStartBreakAction(context))
                    .addAction(buildSkipBreakAction(context));
        } else {
            mBuilder.setContentTitle("Break finished")
                    .setContentText("Continue?")
                    .addAction(buildStartWorkAction(context));
        }

        Notification finishedNotification = mBuilder
                .setOngoing(false)
                .build();

        mNotificationManager.notify(Constants.NOTIFICATION_ID, finishedNotification);
    }
}
