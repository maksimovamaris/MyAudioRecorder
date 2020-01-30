package com.example.myaudiorecorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;

public class AudioRecorder extends Service {
    public static final String ACTION_START = "Recording started";
    public static final String ACTION_STOP = "Recording stopped";
    public static final String ACTION_RESUME = "Recording resumed";
    public static final String ACTION_PAUSE = "Recording paused";
    private static final String CHANNEL_ID = "channel_1";
    private NotificationCompat.Builder builder;
    private String fileName;

    private MediaRecorder myRecorder = null;


    private static final int NOTIFICATION_ID = 1;

    private boolean isRecording = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String descr = getString(R.string.channel_descr);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(descr);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    //    private void updateNotification(@NonNull Notification notification, int maxProgress) {
//        progress = (int) (TIMER_PERIOD) + progress;
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        builder.setProgress(maxProgress, progress, false);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//
//    }
    private Notification createNotification() {
        RemoteViews view = new RemoteViews(this.getPackageName(), R.layout.remote_view);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent intentCloseService = new Intent(this, AudioRecorder.class);
        intentCloseService.setAction(ACTION_STOP);
        PendingIntent pendingIntentCloseService = PendingIntent.getService(this, 0, intentCloseService, 0);

        Intent intentPauseService = new Intent(this, AudioRecorder.class);
        intentPauseService.setAction(ACTION_PAUSE);
//        PendingIntent pendingIntentPauseService = PendingIntent.getService(this, 1, intentCloseService, 0);

        Intent intentResumeService = new Intent(this, AudioRecorder.class);
        intentResumeService.setAction(ACTION_RESUME);
//        PendingIntent pendingIntentResumeService = PendingIntent.getService(this, 2, intentCloseService, 0);
        view.setOnClickPendingIntent(R.id.remote_rec_but, pendingIntentCloseService);

        builder.setContentTitle(getResources().getString(R.string.rec_title))
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setProgress((int) (currentTime), progress, false)
//                .setContentText(getResources().getString(R.string.timer_desc) + currentTime)
                .setOnlyAlertOnce(true)
                .setCustomContentView(view)

//                .addAction(0, getString(R.string.stop), pendingIntentCloseService)
                .setContentIntent(pendingIntent);
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case (ACTION_STOP): {
//        Intent=new Intent(MainActivity.BROADCAST_ACTION)
                stopSelf();

                break;
            }
            case (ACTION_START): {
                try {
                    startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startForeground(NOTIFICATION_ID, createNotification());
                break;
            }
            case (ACTION_PAUSE):
                myRecorder.pause();
                break;
            case (ACTION_RESUME): {
                myRecorder.resume();
                break;
            }
        }
        return START_NOT_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() throws IOException {
        fileName = System.currentTimeMillis() + ".3gp";
        File externalAppDir = new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/" + MainActivity.FOLDER_NAME);

//        File externalAppDir = new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/");
        if (!externalAppDir.exists()) {
            externalAppDir.mkdirs();
        }

        File file = new File(externalAppDir, fileName);


        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        myRecorder.setOutputFile(file);
        myRecorder.prepare();
        myRecorder.start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecorder();
    }

    private void stopRecorder() {
        myRecorder.stop();
        myRecorder.reset();
        myRecorder.release();
        myRecorder = null;
    }

}
