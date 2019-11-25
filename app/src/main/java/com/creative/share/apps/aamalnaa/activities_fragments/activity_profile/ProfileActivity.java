package com.creative.share.apps.aamalnaa.activities_fragments.activity_profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_edit_profile.Edit_Profile_Activity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Ads;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Clients;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Rated;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Works;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_update_ads.UpdateAdsActivity;
import com.creative.share.apps.aamalnaa.adapters.MyPagerAdapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityProfileBinding;
import com.creative.share.apps.aamalnaa.databinding.DialogCustom2Binding;
import com.creative.share.apps.aamalnaa.databinding.RateDialogCustomBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Filter_Model;
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
import okhttp3.ResponseBody;
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
    private String id;
int work_count;
private       int like=-1;

    public String getId() {
        return id;
    }

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
    public  void Createratedialog(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        RateDialogCustomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.rate_dialog_custom, null, false);
binding.setUsermodel(userModel.getUser());
        binding.btnrate.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     String reason=binding.edtSearch.getText().toString();
                                                     if(reason!=null&&like!=-1&&! TextUtils.isEmpty(reason)){
                                                         rateuser(reason,like);
                                                         dialog.dismiss();

                                                     }
                                                     else {
                                                         if(TextUtils.isEmpty(reason)){
                                                         binding.edtSearch.setError(getResources().getString(R.string.field_req));}
                                                         if(like==-1){
                                                             Toast.makeText(context,getResources().getString(R.string.rate),Toast.LENGTH_LONG).show();
                                                         }
                                                     }
                                                 }
                                             }

        );
        binding.imlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imdislike.setBackgroundDrawable(getResources().getDrawable(R.drawable.like_bg));
                binding.imlike.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_up));
                like=1;
            }
        });
        binding.imdislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imlike.setBackgroundDrawable(getResources().getDrawable(R.drawable.like_bg));
                binding.imdislike.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_up));
                like=0;
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void rateuser(String reason, int like) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .rateuser( userModel.getUser().getId() + "",id,like+"",reason)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                Toast.makeText(ProfileActivity.this,getResources().getString(R.string.suc),Toast.LENGTH_LONG).show();

                            } else {


                                Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                Toast.makeText(ProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    private void initView() {
        preferences = Preferences.getInstance();

        userModel = preferences.getUserData(this);
        if(getIntent().getStringExtra("data")!=null){
            id=getIntent().getStringExtra("data");
            binding.llEdit.setVisibility(View.GONE);
            binding.llShow.setVisibility(View.GONE);

        }
        else {
            id=userModel.getUser().getId()+"";
            binding.btnSend.setVisibility(View.GONE);
        }

        Filter_Model.setId(id);
        Log.e("data_id",id);

//    Log.e("y",userModel.getUser().getId()+"");
binding.tvinfo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(binding.expandLayout.isExpanded()){
            binding.expandLayout.collapse(true);
        }
        else {
            binding.expandLayout.expand(true);
        }
    }
});
binding.llEdit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(ProfileActivity.this, Edit_Profile_Activity.class);
        startActivityForResult(intent,1002);

    }
});
binding.llShow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        show();
    }
});
binding.btnSend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Createratedialog(ProfileActivity.this);
    }
});

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

        binding.tab.getTabAt(2).setCustomView(tab_item1);

        binding.tab.getTabAt(1).setCustomView(tab_item2);
        binding.tab.getTabAt(3).setCustomView(tab_item3);

        updateWorkCount(0);
        updateClientCount(0);
updateratedCount(0);
        updateadsCount(0);

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
Log.e(";;llll",tab.getPosition()+"");
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


                }
                else if (tab.getPosition() == 3) {
                    TextView tvTitle = tab.getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimary));

                    TextView tvTitle2 = binding.tab.getTabAt(3).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));


                }
                else {
                    TextView tvTitle = binding.tab.getTabAt(1).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));


                    TextView tvTitle2 = binding.tab.getTabAt(2).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));
                    TextView tvTitle3 = binding.tab.getTabAt(0).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle3.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));
                    TextView tvTitle4 = binding.tab.getTabAt(3).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle4.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.textColor));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if(userModel!=null){
        getprofiledata();}


    }

    public void updateadsCount(int count) {
        tvads.setText(String.format("%s%s%s", "(", String.valueOf(count), ")"));

    }

    public void updateWorkCount(int count) {
        tvWorkCount.setText(String.format("%s%s%s", "(", String.valueOf(count), ")"));
        work_count=count;
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
        fragmentList.add(Fragment_Clients.newInstance());
        fragmentList.add(Fragment_Works.newInstance());
        fragmentList.add(Fragment_Rated.newInstance());

        return fragmentList;

    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.my_ads2));
        titles.add(getString(R.string.clients));
        titles.add(getString(R.string.works));

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
            Log.e("data_f",id);

            Api.getService(Tags.base_url)
                    .getmyprofile(id+ "",userModel.getUser().getId()+"")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateprofile(response.body());
                            } else {

                                Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error_data5", response.code() + "_" + response.errorBody().string());
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
        if(!id.equals(this.userModel.getUser().getId()+"")){
             binding.tvTitle.setText(userModel.getUser().getName());
             if(userModel.getUser().getShowinfo()==0){
                 binding.consinfo.setVisibility(View.GONE);
                 binding.llAbout.setVisibility(View.GONE);
                 binding.llcity.setVisibility(View.GONE);
                 binding.llEmail.setVisibility(View.GONE);
                 binding.llphone.setVisibility(View.GONE);
             }
             else {
                 binding.consinfo.setVisibility(View.VISIBLE);
                 binding.llAbout.setVisibility(View.VISIBLE);
                 binding.llcity.setVisibility(View.VISIBLE);
                 binding.llEmail.setVisibility(View.VISIBLE);
                 binding.llphone.setVisibility(View.VISIBLE);
             }
             if(userModel.getUser().getCan_rate()==0){
                 binding.btnSend.setVisibility(View.GONE);
             }
             else {
                 binding.btnSend.setVisibility(View.VISIBLE);
             }
        }


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

    public void deleteads(int layoutPosition) {
        if(pagerAdapter!=null&&pagerAdapter.getItem(0)!=null){
            Fragment_Ads fragment_ads= (Fragment_Ads) pagerAdapter.getItem(0);
            fragment_ads.deleteitem(layoutPosition);
        }
    }

    public void changetoworks(int layoutPosition) {
        if(pagerAdapter!=null&&pagerAdapter.getItem(1)!=null){
            Fragment_Clients fragment_clients= (Fragment_Clients) pagerAdapter.getItem(1);
            fragment_clients.changeitem(layoutPosition);
        }
    }

    public void delteclients(int layoutPosition) {
        if(pagerAdapter!=null&&pagerAdapter.getItem(1)!=null){
            Fragment_Clients fragment_clients= (Fragment_Clients) pagerAdapter.getItem(1);
            fragment_clients.delte(layoutPosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1002){
            userModel=preferences.getUserData(this);
            updateprofile(userModel);
        }
    }

    public void updateWork(int i) {

        updateWorkCount(i+work_count);
    }

    public void deletework(int layoutPosition) {
        if(pagerAdapter!=null&&pagerAdapter.getItem(2)!=null){
            Fragment_Works fragment_works= (Fragment_Works) pagerAdapter.getItem(2);
            fragment_works.delte(layoutPosition);
        }
    }
    public void show() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .showinfo( userModel.getUser().getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                           Toast.makeText(ProfileActivity.this,getResources().getString(R.string.suc),Toast.LENGTH_LONG).show();

                            } else {


                                Toast.makeText(ProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                Toast.makeText(ProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    public void updateads(int layoutPosition) {
        if(pagerAdapter!=null&&pagerAdapter.getItem(0)!=null){
            Fragment_Ads fragment_ads= (Fragment_Ads) pagerAdapter.getItem(0);
            fragment_ads.update(layoutPosition);
        }
    }
}
