package com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.fragments;

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
import com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aamalnaa.databinding.FragmentSignUpBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.models.SignUpModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Sign_Up extends Fragment implements Listeners.SignUpListener,Listeners.BackListener,Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private SignInActivity activity;
    private String current_language;
    private FragmentSignUpBinding binding;
    private CountryPicker countryPicker;
    private SignUpModel signUpModel;
    private Preferences preferences;
    private boolean isAcceptTerms = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        signUpModel = new SignUpModel();
        activity = (SignInActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.getInstance();
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        binding.setSignUpModel(signUpModel);
        binding.setBackListener(this);
        binding.setSignUpListener(this);
        binding.setShowDialogListener(this);
        createCountryDialog();


        binding.checkbox.setOnClickListener(view -> {
            if (binding.checkbox.isChecked())
            {
                isAcceptTerms = true;
                navigateToTermsActivity();
            }else
                {
                    isAcceptTerms = false;
                }
        });

        binding.tvTerms.setOnClickListener(view -> {
            isAcceptTerms = true;
            navigateToTermsActivity();
        });






    }

    private void navigateToTermsActivity() {

        Intent intent = new Intent(activity, TermsActivity.class);
        startActivity(intent);

    }


    public static Fragment_Sign_Up newInstance() {
        return new Fragment_Sign_Up();
    }

    private void createCountryDialog()
    {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM()!=null)
        {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        }else if (telephonyManager!=null&&countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        }else if (countryPicker.getCountryByLocale(Locale.getDefault())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        }else
        {
            String code = "+966";
            binding.tvCode.setText(code);
            signUpModel.setPhone_code(code.replace("+","00"));

        }

    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(activity);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        signUpModel.setPhone_code(country.getDialCode().replace("+","00"));

    }

    @Override
    public void checkDataSignUp(String name, String phone_code, String phone,String email, String password) {
        signUpModel = new SignUpModel(name,phone_code,phone,email,password);
        binding.setSignUpModel(signUpModel);
        if (signUpModel.isDataValid(activity))
        {
            signUp(signUpModel);
        }
    }

    private void signUp(SignUpModel signUpModel) {

    }

    @Override
    public void back() {
        activity.Back();
    }


}
