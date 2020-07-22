package com.creative.share.apps.aamalnaa.notification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.aamalnaa.models.ChatUserModel;
import com.creative.share.apps.aamalnaa.models.MessageModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

public class FireBaseMessaging extends FirebaseMessagingService {

    Preferences preferences = Preferences.getInstance();
    private String image;
    private String phone;
    private String name;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> map = remoteMessage.getData();

        for (String key:map.keySet())
        {
            Log.e("keys",key+"    value "+map.get(key));
        }

        if (getSession().equals(Tags.session_login))
        {
            if (map.get("receiver_id")!=null)
            {
                int to_id = Integer.parseInt(map.get("receiver_id"));

                if (getCurrentUser_id()==to_id)
                {
                    manageNotification(map);
                }

            }
            else {
                manageNotification(map);
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createNewNotificationVersion(map);
        }else
            {
                createOldNotificationVersion(map);

            }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createNewNotificationVersion(Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

        if (not_type!=null&&not_type.equals("chat"))
        {
            String file_link="";
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class =activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            int sender_id = Integer.parseInt(map.get("sender_id"));
            int receiver_id = Integer.parseInt(map.get("receiver_id"));
            String message = map.get("message");
            String type = map.get("type");
          image=map.get("avatar");
            phone=map.get("mobile");
            name=map.get("name");
            double lat;
            double lang;
            try {
                lat = Double.parseDouble(map.get("lat"));
                lang = Double.parseDouble(map.get("lang"));
            }
            catch (Exception e){
                lat=0.0;
                lang=0.0;
            }






            MessageModel.SingleMessageModel messageModel = new MessageModel.SingleMessageModel(sender_id,receiver_id,message,type,lat,lang);

            if (current_class.equals("com.creative.share.apps.aamalnaa.activities_fragments.activity_chat.ChatActivity"))
            {

                int chat_user_id = getChatUser_id();

                if (chat_user_id==sender_id)
                {
                    EventBus.getDefault().post(messageModel);
                }else
                {
                    LoadChatImage(messageModel,sound_Path,1);
                }




            }else
            {

                EventBus.getDefault().post(messageModel);
                LoadChatImage(messageModel,sound_Path,1);


            }

        }
        else  {
            String CHANNEL_ID = "my_channel_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.drawable.ic_nav_notification);


            builder.setContentTitle(map.get("title"));


            builder.setContentText(map.get("body"));


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            builder.setLargeIcon(bitmap);
            manager.createNotificationChannel(channel);
            manager.notify(new Random().nextInt(200), builder.build());


        }

    }
    private void LoadChatImage(MessageModel.SingleMessageModel messageModel, String sound_path, int type) {


        Target target = new Target() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                if (type==1)
                {
                    sendChatNotification_VersionNew(messageModel,sound_path,bitmap);

                }else
                {
                    sendChatNotification_VersionOld(messageModel,sound_path,bitmap);

                }
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {


                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);

                if (type==1)
                {
                    sendChatNotification_VersionNew(messageModel,sound_path,bitmap);

                }else
                {
                    sendChatNotification_VersionOld(messageModel,sound_path,bitmap);

                }

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        new Handler(Looper.getMainLooper()).postDelayed(() -> Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.base_url+image)).into(target),100);

    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createOldNotificationVersion(Map<String, String> map)
    {


        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

        if (not_type!=null&&not_type.equals("chat"))
        {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class =activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            int sender_id = Integer.parseInt(map.get("sender_id"));
            int receiver_id = Integer.parseInt(map.get("receiver_id"));
            String message = map.get("message");
            String type = map.get("type");
            double lat;
            double lang;
            image=map.get("avatar");
            phone=map.get("mobile");
            name=map.get("name");
            try {
                lat = Double.parseDouble(map.get("lat"));
                lang = Double.parseDouble(map.get("lang"));
            }
            catch (Exception e){
                lat=0.0;
                lang=0.0;
            }






            MessageModel.SingleMessageModel messageModel = new MessageModel.SingleMessageModel(sender_id,receiver_id,message,type,lat,lang);

            if (current_class.equals("com.creative.share.apps.aamalnaa.activities_fragments.activity_chat.ChatActivity"))
            {

                int chat_user_id = getChatUser_id();

                if (chat_user_id==sender_id)
                {
                    EventBus.getDefault().post(messageModel);
                }else
                {
                    LoadChatImage(messageModel,sound_Path,0);
                }




            }else
            {

                EventBus.getDefault().post(messageModel);
                LoadChatImage(messageModel,sound_Path,0);


            }

        }
        else {


            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.drawable.ic_nav_notification);

            builder.setContentTitle(map.get("title"));


            builder.setContentText(map.get("body"));


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            builder.setLargeIcon(bitmap);
            manager.notify(new Random().nextInt(200), builder.build());


        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendChatNotification_VersionNew(MessageModel.SingleMessageModel messageModel, String sound_path,Bitmap bitmap) {


        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_nav_notification);
        builder.setContentTitle(name);
        builder.setLargeIcon(bitmap);
        Intent intent = new Intent(this, ChatActivity.class);
        ChatUserModel chatUserModel = new ChatUserModel(name,image,messageModel.getSender_id(),phone);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("chat_user_data", chatUserModel);
        intent.putExtra("from_fire", true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


            builder.setContentText(messageModel.getMessage());



        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(12352, builder.build());
        }


    }

    private void sendChatNotification_VersionOld(MessageModel.SingleMessageModel messageModel, String sound_path, Bitmap bitmap) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_nav_notification);
        builder.setContentTitle(name);

        Intent intent = new Intent(this, ChatActivity.class);
        ChatUserModel chatUserModel = new ChatUserModel(name,image,messageModel.getSender_id(),phone);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("chat_user_data", chatUserModel);
        intent.putExtra("from_fire", true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


            builder.setContentText(messageModel.getMessage());



        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(12352, builder.build());
        }


    }




    private int getCurrentUser_id()
    {
        return preferences.getUserData(this).getUser().getId();
    }

    private int getChatUser_id()
    {
        if (preferences.getChatUserData(this)!=null)
        {
            return preferences.getChatUserData(this).getId();

        }else
            {
                return -1;

            }
    }

    private String getSession()
    {
        return preferences.getSession(this);
    }


}
