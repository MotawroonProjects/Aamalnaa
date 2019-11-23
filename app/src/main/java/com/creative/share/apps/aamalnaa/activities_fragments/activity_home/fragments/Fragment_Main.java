package com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.adapters.Ads_Adapter;
import com.creative.share.apps.aamalnaa.adapters.Category_Adapter;
import com.creative.share.apps.aamalnaa.adapters.SlidingImage_Adapter;
import com.creative.share.apps.aamalnaa.adapters.SubCategoryAdapter;
import com.creative.share.apps.aamalnaa.databinding.DialogCustom2Binding;
import com.creative.share.apps.aamalnaa.databinding.DialogSubCatogryBinding;
import com.creative.share.apps.aamalnaa.databinding.FragmentMainBinding;
import com.creative.share.apps.aamalnaa.models.Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.Slider_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private static Dialog dialog;
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;
    private UserModel userModel;
    private String cat_id = "all";
    private SlidingImage_Adapter slidingImage__adapter;
    private boolean isLoading = false;
    private int current_page2 = 1;
    private int current_page = 0, NUM_PAGES;
    private List<Catogries_Model.Data> dataList;
    private Category_Adapter catogries_adapter;
    private Ads_Adapter ads_adapter;
    private List<Adversiment_Model.Data> advesriment_data_list;
    private SubCategoryAdapter subCategoryAdapter;
    private List<Catogries_Model.Data.Subcategory> subcategories;
private int pos;
private int width;
    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();
        get_slider();
        change_slide_image();
        getDepartments();

        getAds();

        return binding.getRoot();
    }

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }


    private void initView() {
        dataList = new ArrayList<>();
        subcategories=new ArrayList<>();
        advesriment_data_list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.progBar2.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setNestedScrollingEnabled(false);
        manager = new LinearLayoutManager(activity);
        manager2 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        binding.recView.setLayoutManager(manager);
        binding.recViewCategory.setLayoutManager(manager2);
        catogries_adapter = new Category_Adapter(dataList, activity, this);
        binding.recViewCategory.setItemViewCacheSize(25);
        binding.recViewCategory.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewCategory.setDrawingCacheEnabled(true);

        binding.recViewCategory.setAdapter(catogries_adapter);
        ads_adapter = new Ads_Adapter(advesriment_data_list, activity);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        binding.llNoStore.setVisibility(View.GONE);

        binding.recView.setAdapter(ads_adapter);
        binding.recView.setNestedScrollingEnabled(true);
        subCategoryAdapter = new SubCategoryAdapter(activity, subcategories,this );
        binding.recViewsub.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewsub.setAdapter(subCategoryAdapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy>0)
                {
                    int totalItems = ads_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading) {
                        isLoading = true;
                        advesriment_data_list.add(null);
                        ads_adapter.notifyItemInserted(advesriment_data_list.size() - 1);
                        int page = current_page2 + 1;
                        loadMore(page);


                    }
                }
            }
        });
        binding.recViewCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
int first=manager2.findFirstVisibleItemPosition();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0,0,0,0);
                binding.expandLayout.setLayoutParams(params);
if(pos>first){
                params.setMargins(0,0,(catogries_adapter.getItemCount()*width)-((catogries_adapter.getItemCount()-(pos-first))*(width)),0);
                binding.expandLayout.setLayoutParams(params);
            }}
        });

    }

    private void getAds() {
        advesriment_data_list.clear();
        ads_adapter.notifyDataSetChanged();
        binding.progBar2.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getAds(1, cat_id)
                    .enqueue(new Callback<Adversiment_Model>() {
                        @Override
                        public void onResponse(Call<Adversiment_Model> call, Response<Adversiment_Model> response) {
                            binding.progBar2.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                advesriment_data_list.clear();
                                advesriment_data_list.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoStore.setVisibility(View.GONE);
                                    ads_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    ads_adapter.notifyDataSetChanged();

                                    binding.llNoStore.setVisibility(View.VISIBLE);

                                }
                            } else {
                                ads_adapter.notifyDataSetChanged();

                                binding.llNoStore.setVisibility(View.VISIBLE);

                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Adversiment_Model> call, Throwable t) {
                            try {
                                binding.progBar2.setVisibility(View.GONE);
                                binding.llNoStore.setVisibility(View.VISIBLE);


                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar2.setVisibility(View.GONE);
            binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }

    private void loadMore(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getAds(page, cat_id)
                    .enqueue(new Callback<Adversiment_Model>() {
                        @Override
                        public void onResponse(Call<Adversiment_Model> call, Response<Adversiment_Model> response) {
                            advesriment_data_list.remove(advesriment_data_list.size() - 1);
                            ads_adapter.notifyItemRemoved(advesriment_data_list.size() - 1);
                            isLoading = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                advesriment_data_list.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page2 = response.body().getCurrent_page();
                                ads_adapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Adversiment_Model> call, Throwable t) {
                            try {
                                advesriment_data_list.remove(advesriment_data_list.size() - 1);
                                ads_adapter.notifyItemRemoved(advesriment_data_list.size() - 1);
                                isLoading = false;
                                // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            advesriment_data_list.remove(advesriment_data_list.size() - 1);
            ads_adapter.notifyItemRemoved(advesriment_data_list.size() - 1);
            isLoading = false;
        }
    }

    private void get_slider() {

        Api.getService(Tags.base_url).get_slider().enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                binding.progBar.setVisibility(View.GONE);

                if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null) {
                    if (response.body().getData().size() > 0) {
                        NUM_PAGES = response.body().getData().size();
                        slidingImage__adapter = new SlidingImage_Adapter(activity, response.body().getData());
                        binding.pager.setAdapter(slidingImage__adapter);

                    } else {

                        binding.pager.setVisibility(View.GONE);
                    }
                } else if (response.code() == 404) {
                    binding.pager.setVisibility(View.GONE);
                } else {
                    binding.pager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);
                    binding.pager.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {

                }

            }
        });

    }

    public void getDepartments() {
        Api.getService(Tags.base_url)
                .getDepartment()
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                        //   progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.add(new Catogries_Model.Data("all", "الكل"));

                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                //   ll_no_order.setVisibility(View.GONE);
                                catogries_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                //  ll_no_order.setVisibility(View.VISIBLE);

                            }
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
                    public void onFailure(Call<Catogries_Model> call, Throwable t) {
                        try {


                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }


    public void setcat_id(String id) {
        this.cat_id = id;
        if(dialog!=null){
            dialog.dismiss();
        }
        if(binding.expandLayout.isExpanded()){
            binding.expandLayout.collapse(true);
        }
        else {
            binding.expandLayout.expand(true);
        }
        getAds();
    }
    public static void CreateNoSignAlertDialog(Fragment fragment, Context context, List<Catogries_Model.Data.Subcategory>subcategories) {

        dialog = new Dialog(context);
      SubCategoryAdapter subCategoryAdapter;

        DialogSubCatogryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_sub_catogry, null, false);
        subCategoryAdapter = new SubCategoryAdapter(context, subcategories,fragment );
        binding.recViewsub.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        binding.recViewsub.setAdapter(subCategoryAdapter);
        //int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
       // int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        binding.ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(binding.getRoot());
        //dialog.getWindow().setAttributes(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        dialog.show();
    }


    public void setsublist( List<Catogries_Model.Data.Subcategory> subcategory,int pos,int count,int width) {
        this.subcategories.clear();
        this.subcategories.addAll(subcategory);
        this.pos=pos;
        this.width=width;
        subCategoryAdapter.notifyDataSetChanged();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,0,0);
        binding.expandLayout.setLayoutParams(params);
        params.setMargins(0,0,(count*width)-((count-pos)*(width)),0);
        binding.expandLayout.setLayoutParams(params);

        if (binding.expandLayout.isExpanded()) {

          binding.expandLayout.collapse(true);



        }
        else {

            //  ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


           binding.expandLayout.expand(true);
        }

    }
}
