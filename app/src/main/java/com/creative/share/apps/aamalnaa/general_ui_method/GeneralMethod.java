package com.creative.share.apps.aamalnaa.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.aamalnaa.R;
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


    @BindingAdapter("imageActivityEndPoint")
    public static void displayImage2(ImageView imageView, String imageEndPoint)
    {

        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

    }
    @BindingAdapter("url")
    public static void imageUrl(RoundedImageView imageView,String url)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).fit().into(imageView);

    }

    @BindingAdapter("imageUserEndPoint")
    public static void displayImage3(CircleImageView imageView, String imageEndPoint)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_Ads_URL+imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

    }

    @BindingAdapter({"date","workTimehoosen","workTime"})
    public static void displayDate (TextView textView,long date,String work_time_choosen,String work_time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MMM",Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date));
        textView.setText(String.format("%s",m_date)+" "+work_time+" "+work_time_choosen);

    }
    @BindingAdapter("imageEventEndPoint")
    public static void displayImage(RoundedImageView imageView, String imageEndPoint)
    {

        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_Ads_URL+imageEndPoint)).placeholder(R.drawable.logo).fit().into(imageView);

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
