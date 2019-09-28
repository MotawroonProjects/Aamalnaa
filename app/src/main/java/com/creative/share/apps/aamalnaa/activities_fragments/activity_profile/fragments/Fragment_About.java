package com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.databinding.FragmentAboutMeBinding;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;

public class Fragment_About extends Fragment {

    private ProfileActivity activity;
    private FragmentAboutMeBinding binding;
    private Preferences preferences;
    private UserModel userModel;


    public static Fragment_About newInstance() {
        return new Fragment_About();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_me,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (ProfileActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);


    }


}
