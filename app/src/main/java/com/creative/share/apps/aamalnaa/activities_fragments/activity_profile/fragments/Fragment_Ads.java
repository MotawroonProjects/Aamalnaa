package com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.adapters.My_Ads_Adapter;
import com.creative.share.apps.aamalnaa.adapters.Rated_Adapter;
import com.creative.share.apps.aamalnaa.databinding.FragmentAdsBinding;
import com.creative.share.apps.aamalnaa.models.Filter_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Ads extends Fragment {

    private ProfileActivity activity;
    private FragmentAdsBinding binding;
    private Preferences preferences;
    private UserModel userModel;

    private List<UserModel.Ads> adsList;
    private My_Ads_Adapter ads_adapter;
    private String id;

    public static Fragment_Ads newInstance() {
        return new Fragment_Ads();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ads, container, false);
        initView();
        if(userModel!=null){
            getprofiledata();}
        return binding.getRoot();
    }

    private void initView() {
        adsList = new ArrayList<>();
        activity = (ProfileActivity) getActivity();
        id = Filter_Model.getId();
        Log.e("hhhh", id);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        if (id.equals(userModel.getUser().getId() + "")) {
            ads_adapter = new My_Ads_Adapter(adsList, activity, 1);
        } else {
            ads_adapter = new My_Ads_Adapter(adsList, activity, 2);
        }
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.recView.setAdapter(ads_adapter);


    }

    private void getprofiledata() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getmyprofile(id + "", userModel.getUser().getId() + "")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateprofile(response.body());
                            } else {

                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error_data4", response.code() + "_" + response.errorBody().string());
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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            if (dialog != null) {
                dialog.dismiss();
            }
//            Log.e("err", e.getMessage());
        }
    }

    private void updateprofile(UserModel userModel) {


        if (userModel.getAds() != null) {

            setads(userModel.getAds());
            activity.updateadsCount(userModel.getAds().size());

        }

    }

    private void setads(List<UserModel.Ads> ads) {
        adsList.clear();
        adsList.addAll(ads);
        ads_adapter.notifyDataSetChanged();
    }


    public void deleteitem(int layoutPosition) {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .DelteAds(adsList.get(layoutPosition).getId() + "", userModel.getUser().getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                adsList.remove(layoutPosition);
                                ads_adapter.notifyItemRemoved(layoutPosition);
                                activity.updateadsCount(adsList.size());
                            } else {


                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (Exception e) {
                                    Log.e("Error_code", response.code() + "");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }


    public void update(int layoutPosition) {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("idsssss",adsList.get(layoutPosition).getId()+"");
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .updateAdstime(adsList.get(layoutPosition).getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                getprofiledata();
                            } else {

                                if (response.code() == 422) {

                                    try {
                                        Toast.makeText(activity, response.errorBody().string(), Toast.LENGTH_SHORT).show();

                                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (Exception e) {
                                    Log.e("Error_code", response.code() + "");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }
}
