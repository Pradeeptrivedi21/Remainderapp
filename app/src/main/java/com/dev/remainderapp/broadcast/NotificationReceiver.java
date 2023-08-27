package com.dev.remainderapp.broadcast;

import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_DATE;
import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_EVENT_NOTES;
import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_EVENT_TIME;
import static com.dev.remainderapp.apputils.appconfig.CHANNEL_ID;
import static com.dev.remainderapp.apputils.appconfig.CHANNEL_NAME;
import static com.dev.remainderapp.apputils.appconfig.FRIEND_NAME;
import static com.dev.remainderapp.apputils.appconfig.NOTIFICATION_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.dev.remainderapp.R;
import com.dev.remainderapp.View.NotificationViewActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString(FRIEND_NAME);
        String notes = bundle.getString(BIRTHDAY_EVENT_NOTES);
        String date = bundle.getString(BIRTHDAY_DATE) + " " + bundle.getString(BIRTHDAY_EVENT_TIME);

        //Click on Notification

        Intent intent1 = new Intent(context, NotificationViewActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", text);
        intent1.putExtra("notes", notes);
        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,NOTIFICATION_ID);

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
//        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        contentView.setTextViewText(R.id.message,"Hello today is: "+text+" "+"birthday");
        contentView.setTextViewText(R.id.date, date);
        mBuilder.setSmallIcon(R.drawable.cake_24);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId =CHANNEL_ID;
            NotificationChannel channel = new NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);


    }
}
