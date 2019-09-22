package com.creative.share.apps.aamalnaa.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class Language {
    public static void setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        updateResources(c, language);
    }

    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);

        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());

        }
        return context;


    }


    private static void persistLanguage(Context c, String language) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",language);
        editor.apply();
    }}
