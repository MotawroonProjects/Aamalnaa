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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.adapters.My_Ads_Adapter;
import com.creative.share.apps.aamalnaa.adapters.Work_Adapter;
import com.creative.share.apps.aamalnaa.databinding.FragmentWorksBinding;
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

public class Fragment_Works extends Fragment {

    private ProfileActivity activity;
    private FragmentWorksBinding binding;
    private LinearLayoutManager manager;
    private List<UserModel.Previous> adsList;
    private Work_Adapter work_adapter;
    private Preferences preferences;
    private UserModel userModel;
    private String id;
    private int can_rate;

    public static Fragment_Works newInstance() {
        return new Fragment_Works();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_works,container,false);
        initView();
        if(userModel!=null){
            getprofiledata();}
        return binding.getRoot();
    }

    private void initView() {
        adsList = new ArrayList<>();
        activity = (ProfileActivity) getActivity();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        id= Filter_Model.getId();

        if (id.equals(userModel.getUser().getId() + "")) {

            work_adapter = new Work_Adapter(adsList, activity,1,can_rate);}
        else {
            work_adapter = new Work_Adapter(adsList, activity,2,can_rate);}

        Log.e("hhhh",id);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.recView.setAdapter(work_adapter);


    }

    public void getprofiledata() {

    try {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Log.e("lkjjj",id);
            if(id==null||id.isEmpty()){
                id=userModel.getUser().getId()+"";
            }

            Api.getService(Tags.base_url)
                    .getmyprofile(id+ "",userModel.getUser().getId()+"")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateprofile(response.body());
                            } else {

                                // Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error_data", response.code() + "_" + response.errorBody().string());
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
                                        //   Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("", e.getCause().toString());
        }
    }catch (Exception e){

    }
    }

    private void updateprofile(UserModel userModel) {
Log.e("ldldll","ssss");
        can_rate=userModel.getUser().getCan_rate();

        if (userModel.getPrevious() != null) {

            setads(userModel.getPrevious());
            activity.updateWorkCount(userModel.getPrevious().size());

        }

    }

    private void setads(List<UserModel.Previous> ads) {


        adsList.clear();
        adsList.addAll(ads);
        Log.e("dlldldl",can_rate+"");
        work_adapter.can_rate=can_rate;
        work_adapter.notifyDataSetChanged();
    }
    public void delte(int layoutPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("mmmm", userModel.getUser().getId() + " " + adsList.get(layoutPosition).getId());

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .delteworks(adsList.get(layoutPosition).getId() + "", userModel.getUser().getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                adsList.remove(layoutPosition);
                                work_adapter.notifyItemRemoved(layoutPosition);
                                activity.updateWorkCount(adsList.size());

                            } else {


                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        if(id!=null){
        getprofiledata();}
    }
}
