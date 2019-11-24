package com.creative.share.apps.aamalnaa.general_ui_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.share.TimeAgo;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }


    @BindingAdapter({"imageActivityEndPoint","type"})
    public static void displayImage2(ImageView imageView, String imageEndPoint,int type)
    {
if(type==1) {
    Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_Ads_URL + imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);
}
else if(type==2){
    Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_user_URL + imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

}
else {
    Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_message_URL+ imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

}
    }
    @BindingAdapter("url")
    public static void imageUrl(RoundedImageView imageView,String url)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).fit().into(imageView);

    }
    @BindingAdapter("date")
    public static void convertToNotDate(TextView textView,long date)
    {
        long d = date*1000;
        String n_date = TimeAgo.getTimeAgo(d,textView.getContext());
        textView.setText(n_date);

    }
    @BindingAdapter({"imageUserEndPoint","type"})
    public static void displayImage3(CircleImageView imageView, String imageEndPoint,int type)
    {
        if(type==1) {
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_Ads_URL + imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);
        }
        else if(type==2){
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_user_URL + imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

        }
        else {
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_message_URL+ imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

        }
    }

    @BindingAdapter({"date","islogin"})
    public static void displayDate (TextView textView,long date,int islogin)
    {
        if(islogin==1){
            textView.setText("متصل");
        }
        else {
            long d = date*1000;
            String n_date = TimeAgo.getTimeAgo(d,textView.getContext());
            textView.setText(n_date);
        }
    }
    @BindingAdapter({"imageEventEndPoint","type"})
    public static void displayImage(RoundedImageView imageView, String imageEndPoint,int type)
    {
        if(type==1) {
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_Ads_URL + imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);
        }
        else if(type==2){
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_user_URL + imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

        }
        else {
            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_message_URL+ imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

        }
    }

    @BindingAdapter("rate")
    public static void rate (SimpleRatingBar simpleRatingBar, double rate)
    {
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget((float) rate)
                .setDuration(1000)
                .setRepeatCount(0)
                .setInterpolator(new LinearInterpolator());
        builder.start();
    }







}
