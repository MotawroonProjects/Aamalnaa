package com.creative.share.apps.aamalnaa.activites_fragments.activity_sign_in.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activites_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.databinding.FragmentSignInBinding;
import com.creative.share.apps.aamalnaa.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Sign_In extends Fragment {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String cuurent_language;
    private Preferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        activity = (SignInActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(cuurent_language);

    }


    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }


}
