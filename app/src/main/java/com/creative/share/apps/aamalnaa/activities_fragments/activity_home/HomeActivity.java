package com.creative.share.apps.aamalnaa.activities_fragments.activity_home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Messages;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_More;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Notifications;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.databinding.ActivityHomeBinding;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Messages fragment_messages;
    private Fragment_Notifications fragment_notifications;
    private Fragment_More fragment_more;
    private Preferences preferences;
    private UserModel userModel;
    private BottomSheetBehavior behavior;
    private View root;
    private Button btnNearby, btnFurthest, btnWithImage, btnWithoutImage, btnClient, btnGolden;
    private Spinner spinner;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {
            displayFragmentMain();
        }


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        fragmentManager = getSupportFragmentManager();
        binding.toolbar.setTitle("");
        root = findViewById(R.id.root);
        btnNearby = findViewById(R.id.btnNearby);
        btnFurthest = findViewById(R.id.btnFurthest);
        btnWithImage = findViewById(R.id.btnWithImage);
        btnWithoutImage = findViewById(R.id.btnWithoutImage);
        btnClient = findViewById(R.id.btnClient);
        btnGolden = findViewById(R.id.btnGolden);


        btnNearby.setOnClickListener(view -> {
            btnNearby.setBackgroundResource(R.drawable.selected_filter);
            btnFurthest.setBackgroundResource(R.drawable.un_selected_filter);
            btnNearby.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnFurthest.setTextColor(ContextCompat.getColor(this, R.color.textColor));

        });

        btnFurthest.setOnClickListener(view -> {
            btnFurthest.setBackgroundResource(R.drawable.selected_filter);
            btnNearby.setBackgroundResource(R.drawable.un_selected_filter);

            btnFurthest.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnNearby.setTextColor(ContextCompat.getColor(this, R.color.textColor));

        });


        btnWithImage.setOnClickListener(view -> {
            btnWithImage.setBackgroundResource(R.drawable.selected_filter);
            btnWithoutImage.setBackgroundResource(R.drawable.un_selected_filter);

            btnWithImage.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnWithoutImage.setTextColor(ContextCompat.getColor(this, R.color.textColor));

        });

        btnWithoutImage.setOnClickListener(view -> {
            btnWithoutImage.setBackgroundResource(R.drawable.selected_filter);
            btnWithImage.setBackgroundResource(R.drawable.un_selected_filter);

            btnWithoutImage.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnWithImage.setTextColor(ContextCompat.getColor(this, R.color.textColor));

        });

        btnClient.setOnClickListener(view -> {
            btnClient.setBackgroundResource(R.drawable.selected_filter);
            btnGolden.setBackgroundResource(R.drawable.un_selected_filter);

            btnClient.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnGolden.setTextColor(ContextCompat.getColor(this, R.color.textColor));

        });

        btnGolden.setOnClickListener(view -> {
            btnGolden.setBackgroundResource(R.drawable.selected_filter);
            btnClient.setBackgroundResource(R.drawable.un_selected_filter);

            btnGolden.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnClient.setTextColor(ContextCompat.getColor(this, R.color.textColor));

        });

        binding.imageFilter.setOnClickListener(view -> {
            binding.fab.setVisibility(View.INVISIBLE);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        });

        binding.imageSearch.setOnClickListener(view -> {

        });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAdsActivity.class);
            startActivityForResult(intent,1);

        });

        setSupportActionBar(binding.toolbar);
        setUpBottomNavigation();
        setUpBottomSheet();
    }

    private void setUpBottomSheet() {

        behavior = BottomSheetBehavior.from(root);

    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_nav_mail);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_nav_notification);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.ic_nav_menu);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.ahBottomNav.setTitleTextSizeInSp(14, 12);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.icon));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);

        updateBottomNavigationPosition(0);

        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    displayFragmentMain();
                    break;
                case 1:
                    displayFragmentMessages();
                    break;
                case 2:
                    displayFragmentNotification();
                    break;
                case 3:
                    displayFragmentMore();
                    break;

            }
            return false;
        });


    }

    private void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);
    }

    private void displayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }
            if (fragment_messages != null && fragment_messages.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_messages).commit();
            }
            if (fragment_notifications != null && fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_notifications).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }
            updateBottomNavigationPosition(0);
        } catch (Exception e) {
        }
    }

    private void displayFragmentMessages() {
        try {
            if (fragment_messages == null) {
                fragment_messages = Fragment_Messages.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_notifications != null && fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_notifications).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_messages.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_messages).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_messages, "fragment_messages").addToBackStack("fragment_messages").commit();

            }
            updateBottomNavigationPosition(1);
        } catch (Exception e) {
        }
    }

    private void displayFragmentNotification() {
        try {
            if (fragment_notifications == null) {
                fragment_notifications = Fragment_Notifications.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_messages != null && fragment_messages.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_messages).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_notifications).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_notifications, "fragment_notifications").addToBackStack("fragment_notifications").commit();

            }
            updateBottomNavigationPosition(2);
        } catch (Exception e) {
        }
    }

    private void displayFragmentMore() {
        try {
            if (fragment_more == null) {
                fragment_more = Fragment_More.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_messages != null && fragment_messages.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_messages).commit();
            }
            if (fragment_notifications != null && fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_notifications).commit();
            }
            if (fragment_more.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_more).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

            }
            updateBottomNavigationPosition(3);
        } catch (Exception e) {
        }
    }




    private void NavigateToSignInActivity() {
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void logout()
    {
        if (userModel==null)
        {
            NavigateToSignInActivity();
        }else
            {
Logout();
            }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            binding.fab.setVisibility(View.VISIBLE);

            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {
                if (userModel == null) {
                    NavigateToSignInActivity();
                } else {
                    finish();
                }
            } else {
                displayFragmentMain();
            }
        }

    }
    public void Logout() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService(Tags.base_url)
                .Logout(userModel.getUser().getId() + "")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            /*new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            manager.cancelAll();
                                        }
                                    },1);
                            userSingleTone.clear(ClientHomeActivity.this);*/
                            preferences.create_update_userdata(HomeActivity.this, null);
                            preferences.create_update_session(HomeActivity.this, Tags.session_logout);
                            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }



}
