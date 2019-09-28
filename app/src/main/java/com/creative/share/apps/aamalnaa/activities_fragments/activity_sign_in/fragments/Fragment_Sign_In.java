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
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.databinding.FragmentSignInBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.models.LoginModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener,Listeners.CreateAccountListener,Listeners.SkipListener,Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String current_language;
    private Preferences preferences;
    private CountryPicker countryPicker;
    private LoginModel loginModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        loginModel = new LoginModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLoginModel(loginModel);
        binding.setLang(current_language);
        binding.setLoginListener(this);
        binding.setNewAccountListener(this);
        binding.setSkipListener(this);
        binding.setShowDialogListener(this);
        createCountryDialog();




    }

    private void createCountryDialog() {
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
            loginModel.setPhone_code(code.replace("+","00"));

        }

    }


    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }


    @Override
    public void checkDataLogin(String phone_code, String phone, String password) {
        loginModel = new LoginModel(phone_code,phone,password);
        binding.setLoginModel(loginModel);

        if (loginModel.isDataValid(activity))
        {
            login(phone_code,phone,password);
        }
    }

    private void login(String phone_code, String phone, String password) {

    }

    @Override
    public void skip() {
        navigateToHomeActivity();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public void createNewAccount() {
        activity.DisplayFragmentSignUp();
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
        loginModel.setPhone_code(country.getDialCode().replace("+","00"));

    }
}
