package com.creative.share.apps.aamalnaa.activities_fragments.activity_profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Ads;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Clients;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Rated;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Works;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_update_ads.UpdateAdsActivity;
import com.creative.share.apps.aamalnaa.adapters.MyPagerAdapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityProfileBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProfileBinding binding;
    private String lang;
    private MyPagerAdapter pagerAdapter;
    private TextView tvads, tvWorkCount, tvClientCount, tvratedcount;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        initView();


    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
//    Log.e("y",userModel.getUser().getId()+"");

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.tab.setupWithViewPager(binding.pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(getFragments());
        pagerAdapter.addTitle(getTitles());
        binding.pager.setAdapter(pagerAdapter);
        binding.setUsermodel(userModel.getUser());
        View tab_item1 = LayoutInflater.from(this).inflate(R.layout.tab_custom_view, null);
        View tab_item2 = LayoutInflater.from(this).inflate(R.layout.tab_custom_view, null);
        View tab_item3 = LayoutInflater.from(this).inflate(R.layout.tab_custom_view, null);
        View tab_item0 = LayoutInflater.from(this).inflate(R.layout.tab_custom_view, null);

        tvWorkCount = tab_item1.findViewById(R.id.tvCount);
        tvClientCount = tab_item2.findViewById(R.id.tvCount);
        tvratedcount = tab_item3.findViewById(R.id.tvCount);
        tvads = tab_item0.findViewById(R.id.tvCount);
        TextView tvTitle = tab_item1.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.works));

        TextView tvTitle2 = tab_item2.findViewById(R.id.tvTitle);
        tvTitle2.setText(getString(R.string.clients));
        TextView tvTitle0 = tab_item0.findViewById(R.id.tvTitle);
        tvTitle0.setText(getString(R.string.my_ads));
        TextView tvTitle3 = tab_item3.findViewById(R.id.tvTitle);
        tvTitle3.setText(getString(R.string.rated));
        binding.tab.getTabAt(0).setCustomView(tab_item0);

        binding.tab.getTabAt(1).setCustomView(tab_item1);

        binding.tab.getTabAt(2).setCustomView(tab_item2);
        binding.tab.getTabAt(3).setCustomView(tab_item3);

        updateWorkCount(0);
        updateClientCount(0);
updateratedCount(0);
        updateadsCount(0);

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    TextView tvTitle = tab.getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimary));

                    TextView tvTitle2 = binding.tab.getTabAt(1).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));


                }
                else if (tab.getPosition() == 0) {
                    TextView tvTitle = tab.getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimary));

                    TextView tvTitle2 = binding.tab.getTabAt(0).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));


                }
                else if (tab.getPosition() == 2) {
                    TextView tvTitle = tab.getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimary));

                    TextView tvTitle2 = binding.tab.getTabAt(2).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));


                } else {
                    TextView tvTitle = binding.tab.getTabAt(1).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));


                    TextView tvTitle2 = binding.tab.getTabAt(2).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));
                    TextView tvTitle3 = binding.tab.getTabAt(0).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle3.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getprofiledata();


    }

    public void updateadsCount(int count) {
        tvads.setText(String.format("%s%s%s", "(", String.valueOf(count), ")"));

    }

    public void updateWorkCount(int count) {
        tvWorkCount.setText(String.format("%s%s%s", "(", String.valueOf(count), ")"));
    }

    public void updateClientCount(int count) {
        tvClientCount.setText(String.format("%s%s%s", "(", String.valueOf(count), ")"));
    }

    public void updateratedCount(int count) {
        tvratedcount.setText(String.format("%s%s%s", "(", String.valueOf(count), ")"));
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(Fragment_Ads.newInstance());
        fragmentList.add(Fragment_Works.newInstance());
        fragmentList.add(Fragment_Clients.newInstance());
        fragmentList.add(Fragment_Rated.newInstance());

        return fragmentList;

    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.my_ads));
        titles.add(getString(R.string.works));
        titles.add(getString(R.string.clients));
        titles.add(getString(R.string.rated));
        return titles;

    }


    @Override
    public void back() {
        finish();
    }

    private void getprofiledata() {
       final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getmyprofile(userModel.getUser().getId() + "")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateprofile(response.body());
                            } else {

                                Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ProfileActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            if(dialog!=null){
            dialog.dismiss();}

           // Log.e("err", e.getMessage());
        }
    }

    private void updateprofile(UserModel userModel) {
        binding.setUsermodel(userModel.getUser());

    }

    public void showdetials(int id) {
        Intent intent=new Intent(ProfileActivity.this, AdsDetialsActivity.class);
        intent.putExtra("search",id);
        startActivity(intent);
    }

    public void editads(UserModel.Ads ads) {
        Intent intent=new Intent(ProfileActivity.this, UpdateAdsActivity.class);
        intent.putExtra("data",ads);
        startActivity(intent);
    }
}
