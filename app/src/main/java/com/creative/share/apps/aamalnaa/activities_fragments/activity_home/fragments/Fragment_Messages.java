package com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.adapters.Room_Adapter;
import com.creative.share.apps.aamalnaa.databinding.FragmentMessagesBinding;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.models.UserRoomModelData;
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

public class Fragment_Messages extends Fragment {

    private HomeActivity activity;
    private FragmentMessagesBinding binding;
    private Preferences preferences;
    private LinearLayoutManager manager;
    private UserModel userModel;
private List<UserRoomModelData.UserRoomModel> userRoomModels;
private Room_Adapter room_adapter;

    public static Fragment_Messages newInstance() {
        return new Fragment_Messages();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages,container,false);
        initView();
        if(userModel!=null){
        getRooms();}
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
userRoomModels=new ArrayList<>();
room_adapter=new Room_Adapter(userRoomModels,activity,this);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
binding.recView.setItemViewCacheSize(25);
binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
binding.recView.setDrawingCacheEnabled(true);
binding.recView.setAdapter(room_adapter);
binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        getRooms();
    }
});
    }
    public void getRooms() {
        Api.getService(Tags.base_url)
                .getRooms(userModel.getUser().getId()+"")
                .enqueue(new Callback<UserRoomModelData>() {
                    @Override
                    public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                        binding.swipeRefresh.setRefreshing(false);
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            if (response.body().getData().size()>0)
                            {
                                userRoomModels.clear();
                                userRoomModels.addAll(response.body().getData());
                                binding.llConversation.setVisibility(View.GONE);
                                room_adapter.notifyDataSetChanged();

                            }else
                            {
                                binding.llConversation.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                        try {
                            binding.swipeRefresh.setRefreshing(false);

                            binding.progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }



        public void delteroom(int layoutPosition) {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();

            // rec_sent.setVisibility(View.GONE);
            try {


                Api.getService(Tags.base_url)
                        .delteroom(userRoomModels.get(layoutPosition).getId()+"")
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                dialog.dismiss();

                                //  binding.progBar.setVisibility(View.GONE);
                                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                    //binding.coord1.scrollTo(0,0);
                                  userRoomModels.remove(layoutPosition);
                                  room_adapter.notifyItemRemoved(layoutPosition);

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
        getRooms();
    }
}
