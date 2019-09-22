package com.creative.share.apps.aamalnaa.activites_fragments.activity_sign_in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activites_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.databinding.FragmentSignUpBinding;
import com.creative.share.apps.aamalnaa.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Sign_Up extends Fragment {
    private SignInActivity activity;
    private String current_language;
    private FragmentSignUpBinding binding;
    private Preferences preferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (SignInActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.getInstance();
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);




    }

    public static Fragment_Sign_Up newInstance() {
        return new Fragment_Sign_Up();
    }



}
