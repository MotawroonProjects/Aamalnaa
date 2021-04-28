package com.creative.share.apps.aamalnaa.activities_fragments.activity_home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_ads.Ads_Activity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Messages;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_More;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Notifications;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_search.Search_Activity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.aamalnaa.adapters.CityAdapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityHomeBinding;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.ChatUserModel;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.Filter_Model;
import com.creative.share.apps.aamalnaa.models.MessageModel;
import com.creative.share.apps.aamalnaa.models.NotificationCount;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
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
    private EditText edt_search;
    private Button btnNearby, btnFurthest, btnWithImage, btcancel, btfilter;
    private Spinner spinner;
    private String lat = "0.0", lng = "0.0";
    private CityAdapter adapter;
    private List<Cities_Model.Data> dataList;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private ProgressDialog dialog;
    private String token;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(MessageModel.SingleMessageModel messageModel) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra("data",messageModel.getSender_id()+"");
//        intent.putExtra("name",messageModel.getUser_name());
//      ///  intent.putExtra("phone",messageModel.);
//
//        startActivityForResult(intent,1000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {
            CheckPermission();

            displayFragmentMain();
        }
        if (userModel != null) {
            updateToken();
            getNotificationCount();
            getMessageCount();
        }

    }

    private void updateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                            task.getResult().getId();
                            Log.e("sssssss", token);
                            Api.getService(Tags.base_url)
                                    .updateToken(userModel.getUser().getId(), token, "android")
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            if (response.isSuccessful()) {
                                                try {
                                                    Log.e("Success", "token updated");
                                                } catch (Exception e) {
                                                    //  e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {
                                                Log.e("Error", t.getMessage());
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        dataList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        fragmentManager = getSupportFragmentManager();
        binding.toolbar.setTitle("");
        root = findViewById(R.id.root);
        btnNearby = findViewById(R.id.btnNearby);
        btnFurthest = findViewById(R.id.btnNew);
        btnWithImage = findViewById(R.id.btnWithImage);
        btcancel = findViewById(R.id.btnCancel);
        btfilter = findViewById(R.id.btnfilter);
        spinner = findViewById(R.id.spinner);
        edt_search=findViewById(R.id.edtSearch);
        Filter_Model.setCity_id("all");
        Filter_Model.setLat("all");
        Filter_Model.setLng("all");
        Filter_Model.setIs_new(0);
        Filter_Model.setphoto(0);
        adapter = new CityAdapter(dataList, this);
        spinner.setAdapter(adapter);
        getCities();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Filter_Model.setCity_id("all");
                } else {
                    Filter_Model.setCity_id(dataList.get(i).getId() + "");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnNearby.setOnClickListener(view -> {
            if (Filter_Model.getLat().equals("all")) {
                btnNearby.setBackgroundResource(R.drawable.selected_filter);
                btnNearby.setTextColor(ContextCompat.getColor(this, R.color.white));
                dialog = Common.createProgressDialog(this, getString(R.string.wait));

                dialog.show();
                CheckPermission();
                Filter_Model.setLat(lat);
                Filter_Model.setLng(lng);
                Log.e("lat", lat + "  " + lng);
            } else {
                btnNearby.setBackgroundResource(R.drawable.un_selected_filter);
                btnNearby.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                Filter_Model.setLat("all");
                Filter_Model.setLng("all");
            }

        });

        btnFurthest.setOnClickListener(view -> {
            if (Filter_Model.getIs_new() == 0) {
                btnFurthest.setBackgroundResource(R.drawable.selected_filter);
                btnFurthest.setTextColor(ContextCompat.getColor(this, R.color.white));
                Filter_Model.setIs_new(1);
            } else {
                btnFurthest.setBackgroundResource(R.drawable.un_selected_filter);
                btnFurthest.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                Filter_Model.setIs_new(0);
            }

        });


        btnWithImage.setOnClickListener(view -> {
            if (Filter_Model.getPhoto() == 0) {
                btnWithImage.setBackgroundResource(R.drawable.selected_filter);

                btnWithImage.setTextColor(ContextCompat.getColor(this, R.color.white));
                Filter_Model.setphoto(1);
            } else {
                btnWithImage.setBackgroundResource(R.drawable.un_selected_filter);
                btnWithImage.setTextColor(ContextCompat.getColor(this, R.color.textColor));
                Filter_Model.setphoto(0);

            }

        });


        binding.imageFilter.setOnClickListener(view -> {
            binding.llfab.setVisibility(View.INVISIBLE);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        });

        binding.imageSearch.setOnClickListener(view -> {

        });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddAdsActivity.class);
            startActivityForResult(intent, 1);

        });
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llfab.setVisibility(View.VISIBLE);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        btfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search=edt_search.getText().toString();
                Filter_Model.setSearch(search);
                Intent intent = new Intent(HomeActivity.this, Ads_Activity.class);
                startActivity(intent);
            }
        });
        binding.imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, Search_Activity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(binding.toolbar);
        setUpBottomNavigation();
        setUpBottomSheet();
    }

    private void setUpBottomSheet() {

        behavior = BottomSheetBehavior.from(root);

    }

    private void setUpBottomNavigation() {
        AHNotification.Builder builder = new AHNotification.Builder();
        builder.setTextColor(ContextCompat.getColor(this, R.color.white));
        builder.setBackgroundColor(ContextCompat.getColor(this, R.color.golden_stars));
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
                    if (userModel != null) {
                        builder.setText("");
                        binding.ahBottomNav.setNotification(builder.build(), 1);
                        displayFragmentMessages();

                    } else {
                        Common.CreateNoSignAlertDialog(this);
                    }
                    break;
                case 2:
                    if (userModel != null) {
                        builder.setText("");
                        binding.ahBottomNav.setNotification(builder.build(), 2);
                        displayFragmentNotification();
                    } else {
                        Common.CreateNoSignAlertDialog(this);

                    }
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
            } else {
                fragment_main.setcat_id("all");
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

    public void logout() {
        if (userModel == null) {
            NavigateToSignInActivity();
        } else {
            deletetoken();
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            binding.llfab.setVisibility(View.VISIBLE);

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

    public void deletetoken() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));

        dialog.show();
        Api.getService(Tags.base_url)
                .delteToken(userModel.getUser().getId(), token)
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
                            Logout();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
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

    private void updateCityAdapter(Cities_Model body) {

        dataList.add(new Cities_Model.Data("إختر"));
        if (body.getData() != null) {
            dataList.addAll(body.getData());
            adapter.notifyDataSetChanged();
        }
    }

    private void getCities() {
        try {
//            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
//            dialog.setCancelable(false);
//            dialog.show();
            Api.getService(Tags.base_url)
                    .getCity()
                    .enqueue(new Callback<Cities_Model>() {
                        @Override
                        public void onResponse(Call<Cities_Model> call, Response<Cities_Model> response) {
                           // dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getData() != null) {
                                    updateCityAdapter(response.body());
                                } else {
                                    Log.e("error", response.code() + "_" + response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Cities_Model> call, Throwable t) {
                            try {
                             //   dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{gps_perm}, gps_req);
        } else {
            initGoogleApiClient();

        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.
                Builder(this).
                addOnConnectionFailedListener(this).
                addConnectionCallbacks(this).
                addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 1255) {
            if (requestCode == Activity.RESULT_OK) {
                startLocationUpdate();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == gps_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initGoogleApiClient();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        lat = location.getLatitude() + "";
        lng = location.getLongitude() + "";

        //AddMarker(lat, lang);

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        if (locationRequest != null) ;
        {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(HomeActivity.this, 1255);
                        } catch (Exception e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void showdetials(int id) {
        Intent intent = new Intent(HomeActivity.this, AdsDetialsActivity.class);
        intent.putExtra("search", id);
        startActivity(intent);
    }

    public void gotomessage(int receiver_id, String receiver_name, String receiver_mobile) {
        Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
        intent.putExtra("data", receiver_id + "");
        intent.putExtra("name", receiver_name);
        intent.putExtra("phone", receiver_mobile);

        startActivity(intent);
    }

    public void deletenotification(int id) {
        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragment_notifications.deletenotification(id);
        }
    }

    public void NavigateToSignInActivity(boolean isSignIn) {
//Log.e("data",isSignIn+"");
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        intent.putExtra("sign_up", isSignIn);
        startActivity(intent);
        finish();

    }

    private void getNotificationCount() {
        Api.getService(Tags.base_url)
                .getUnreadNotificationCount(userModel.getUser().getId() + "")
                .enqueue(new Callback<NotificationCount>() {
                    @Override
                    public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
                        if (response.isSuccessful()) {
                            updateNotificationCount(response.body());
                        } else {
                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationCount> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateNotificationCount(NotificationCount body) {
        AHNotification.Builder builder = new AHNotification.Builder();
        builder.setTextColor(ContextCompat.getColor(this, R.color.white));
        builder.setBackgroundColor(ContextCompat.getColor(this, R.color.golden_stars));
        binding.ahBottomNav.setNotification(builder.build(), 2);
        if (body.getCount() > 0) {
            builder.setText(body.getCount() + "");
            binding.ahBottomNav.setNotification(builder.build(), 2);
        }
    }

    private void getMessageCount() {
        Api.getService(Tags.base_url)
                .getUnreadMeaasgeCount(userModel.getUser().getId() + "")
                .enqueue(new Callback<NotificationCount>() {
                    @Override
                    public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
                        if (response.isSuccessful()) {
                            updateMessgeCount(response.body());
                        } else {
                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationCount> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateMessgeCount(NotificationCount body) {
        AHNotification.Builder builder = new AHNotification.Builder();
        builder.setTextColor(ContextCompat.getColor(this, R.color.white));
        builder.setBackgroundColor(ContextCompat.getColor(this, R.color.golden_stars));
        binding.ahBottomNav.setNotification(builder.build(), 1);
        if (body.getCount() > 0) {
            builder.setText(body.getCount() + "");
            binding.ahBottomNav.setNotification(builder.build(), 1);
        }
    }
}
