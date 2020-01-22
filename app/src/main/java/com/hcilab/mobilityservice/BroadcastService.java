package com.hcilab.mobilityservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import static com.hcilab.mobilityservice.AppService.CHANNEL_ID;

import androidx.core.app.NotificationCompat;

import com.example.myreceiver.MyReceiver;

import java.util.List;

public class BroadcastService extends Service {
    private final Handler handler = new Handler();
    MyReceiver myReceiver;
    private String intentString;
    private Thread t;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent,flags, startId);
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent (this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("SensorService").setContentText(input).setSmallIcon(R.drawable.ic_android).setContentIntent(pendingIntent).build();
        startForeground(1,notification);

        Log.i("Ritesh", "OnStartCommand invoked");
        return START_NOT_STICKY;
    }

    public Runnable sendData = new Runnable() {

        public void run() {
            if(MainActivity.sendData) {
                intentString = MainActivity.sensorData.toString() ;
            }

            if (intentString != null) {
                Log.i("intentString",intentString);
            }
            else
            {
                Log.i("intentString", "NULL");
            }

            Intent sendIntent = new Intent();
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendIntent.setAction("com.hcilab.mobilityservice");
            sendIntent.putExtra(Intent.EXTRA_TEXT, intentString);

            PackageManager pm = BroadcastService.this.getPackageManager();
            List<ResolveInfo> matches = pm.queryBroadcastReceivers(sendIntent,0);
            for (ResolveInfo resolveInfo : matches){
                Intent explicit=new Intent(sendIntent);
                ComponentName cn = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,resolveInfo.activityInfo.name);
                explicit.setComponent(cn);
                BroadcastService.this.sendBroadcast(explicit);
            }

            handler.removeCallbacks(this);
            handler.postDelayed(this, 10);


        }
    };

    @Override
    public void onStart(Intent intent, int startid) {
        handler.removeCallbacks(sendData);
        handler.postDelayed(sendData, 10);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendData);
        Log.i("Ritesh", "Stopped");
        Log.i("MyService: ", "service stopped...");
    }
}
