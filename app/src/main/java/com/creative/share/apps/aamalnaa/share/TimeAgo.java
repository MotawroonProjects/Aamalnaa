package com.creative.share.apps.aamalnaa.share;

import android.content.Context;


import com.creative.share.apps.aamalnaa.R;

import java.util.Calendar;

public class TimeAgo {
    private static final int SECOND_MILS = 1000;
    private static final int MINUTE_MILS = 60*SECOND_MILS;
    private static final int HOUR_MILS = 60*MINUTE_MILS;
    private static final int DAY_MILS = 24*HOUR_MILS;
/*
    public static String getTimeAgo(long time, Context context)
    {
        long now = Calendar.getInstance().getTimeInMillis();
        if (time < 1000000000000L)
        {
            time*=1000;
        }

        if (time > now || time<=0)
        {
            return null;
        }

        final long diff = now-time;
        if (diff < MINUTE_MILS)
        {
            return context.getString(R.string.just_now);

        }
        else if (diff < 2* MINUTE_MILS)
        {
            return context.getString(R.string.a_minute_ago);

        }else if (diff < 50*MINUTE_MILS)
        {
            return diff/MINUTE_MILS+" "+ context.getString(R.string.min);
        }
        else if (diff < 90*MINUTE_MILS)
        {
            return context.getString(R.string.an_hour_ago);
        }

        else if (diff < 24*HOUR_MILS)
        {
            return diff/HOUR_MILS +" "+context.getString(R.string.hs);
        }
        else if (diff < 48*HOUR_MILS)

        {
            return context.getString(R.string.yesterday);
        }else
            {
                return diff/DAY_MILS +" "+context.getString(R.string.day);
            }
    }*/

}
