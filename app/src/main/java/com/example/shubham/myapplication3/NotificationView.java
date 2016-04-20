package com.example.shubham.myapplication3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Shubham on 29-11-2015.
 */
public class NotificationView extends Activity{
    NotificationManager mNotificationManager;
    int numMessages=0;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    //    displayNotification();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void displayNotification() {
        Log.i("Start", "notification");

   /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.icon);

   /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is first line....");
        events[1] = new String("This is second line...");
        events[2] = new String("This is third line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(8, mBuilder.build());
    }
}