package com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.aamalnaa.adapters.Comments_Adapter;
import com.creative.share.apps.aamalnaa.adapters.SingleAdsSlidingImage_Adapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityAdsDetialsBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAdsDetialsBinding binding;
    private String lang;
    private Comments_Adapter comments_adapter;
    private List<Single_Adversiment_Model.Comments> commentsList;
    private Single_Adversiment_Model single_adversiment_model;
    private LinearLayoutManager manager;

    private Preferences preferences;
    private UserModel userModel;
    private String search_id;
    private int current_page = 0, NUM_PAGES;

    private SingleAdsSlidingImage_Adapter singleslidingImage__adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ads_detials);
        initView();
        if (search_id != null) {
            getsingleads();

        }
        change_slide_image();


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
        if (getIntent().getIntExtra("search", -1) != 0) {
            search_id = getIntent().getIntExtra("search", -1) + "";
        }

        binding.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String app_url = Tags.base_url + "ad/" + search_id;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, "تطبيق عالم أعمالنا");
                intent.putExtra(Intent.EXTRA_TEXT, app_url);
                startActivity(intent);
            }
        });
        commentsList = new ArrayList<>();
        single_adversiment_model = new Single_Adversiment_Model();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        if (userModel != null) {
            binding.setUsermodel(userModel.getUser());
        } else {
            binding.cardChat.setVisibility(View.GONE);
            binding.cardrepor.setVisibility(View.GONE);
            binding.follow.setVisibility(View.GONE);
            binding.llShare.setVisibility(View.GONE);
        }
        manager = new LinearLayoutManager(this);
        binding.setAdsmodel(single_adversiment_model);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.reccomment.setItemViewCacheSize(25);
        binding.reccomment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.reccomment.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        binding.llAds.setVisibility(View.GONE);
        binding.reccomment.setLayoutManager(manager);

        comments_adapter = new Comments_Adapter(commentsList, this);
        binding.reccomment.setAdapter(comments_adapter);
        binding.llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Report();
            }
        });
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userModel != null) {
                    Likeads();
                } else {
                    Common.CreateNoSignAlertDialog(AdsDetialsActivity.this);
                }
            }
        });
        binding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                becustomer();
            }
        });

        binding.edtComment.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.edtComment.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.edtComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                String query = binding.edtComment.getText().toString();
                binding.edtComment.setText("");
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(AdsDetialsActivity.this, binding.edtComment);
                    if (userModel != null) {
                        comment(query);
                    } else {
                        Common.CreateNoSignAlertDialog(this);
                    }
                    return false;
                }
            }
            return false;
        });

        binding.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel.getUser().getId() != single_adversiment_model.getUser_id()) {
                    Intent intent = new Intent(AdsDetialsActivity.this, ChatActivity.class);
                    intent.putExtra("data", single_adversiment_model.getUser_id() + "");
                    intent.putExtra("name", single_adversiment_model.getUser());
                    intent.putExtra("phone", single_adversiment_model.getMobile());

                    startActivity(intent);
                }
            }
        });
        binding.llAdversitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel != null) {

                    if (userModel.getUser().getId() == single_adversiment_model.getUser_id()) {
                        Intent intent = new Intent(AdsDetialsActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AdsDetialsActivity.this, ProfileActivity.class);
                        intent.putExtra("data", single_adversiment_model.getUser_id() + "");
                        startActivity(intent);
                    }
                } else {
                    Common.CreateNoSignAlertDialog(AdsDetialsActivity.this);

                }
            }
        });
    }

    private void comment(String query) {
        ProgressDialog dialog = Common.createProgressDialog(AdsDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .comment(single_adversiment_model.getId() + "", userModel.getUser().getId() + "", query)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);

                                getsingleads();
                            } else {


                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    public void getsingleads() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(AdsDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {
            String id = null;
            if (userModel != null) {
                id = userModel.getUser().getId() + "";
            }

            Api.getService(Tags.base_url)
                    .getSingleAds(search_id, id)
                    .enqueue(new Callback<Single_Adversiment_Model>() {
                        @Override
                        public void onResponse(Call<Single_Adversiment_Model> call, Response<Single_Adversiment_Model> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);

                                update(response.body());
                            } else {


                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Single_Adversiment_Model> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    public void Likeads() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(AdsDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .Like(search_id, userModel.getUser().getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);

//getsingleads();

                                if (single_adversiment_model.getLike_ad() == 1) {
                                    single_adversiment_model.setLike_ad(0);
                                } else {
                                    single_adversiment_model.setLike_ad(1);
                                }
                                update(single_adversiment_model);
                            } else {


                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    public void Report() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(AdsDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .Report(single_adversiment_model.getUser_id() + "", userModel.getUser().getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                //binding.coord1.scrollTo(0,0);

                                // getsingleads();
                                if (single_adversiment_model.getReport() == 1) {
                                    single_adversiment_model.setReport(0);
                                } else {
                                    single_adversiment_model.setReport(1);
                                }
                                update(single_adversiment_model);

                            } else {


                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    public void becustomer() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        Log.e("kkkkk", single_adversiment_model.getUser_id() + "");
        ProgressDialog dialog = Common.createProgressDialog(AdsDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .becustomer(single_adversiment_model.getUser_id() + "", userModel.getUser().getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                if (single_adversiment_model.getFollow() == 1) {
                                    single_adversiment_model.setFollow(0);
                                } else {
                                    single_adversiment_model.setFollow(1);
                                }
                                update(single_adversiment_model);


                                //getsingleads();

                            } else {


                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    private void update(Single_Adversiment_Model body) {
        this.single_adversiment_model = body;
        if (userModel != null) {
            if (body.getUser_id() == userModel.getUser().getId()) {
                binding.cardrepor.setVisibility(View.GONE);
                binding.follow.setVisibility(View.GONE);
                binding.cardChat.setVisibility(View.GONE);
            }
        }
        //  Log.e("body",body.toString());
        //Common.CreateAlertDialog(this,body.toString());
        binding.setAdsmodel(body);
        commentsList.clear();
        if (body.getComments() != null && body.getCommented() == 0 && body.getComments().size() > 0) {
            commentsList.addAll(body.getComments());
            binding.llAds.setVisibility(View.GONE);
            comments_adapter.notifyDataSetChanged();
            binding.reccomment.setVisibility(View.VISIBLE);

            //      Log.e("llll",body.getComments().get(0).getComment()+"");

        } else {
            //  Log.e("lll",body.getCommented()+"");

            binding.llAds.setVisibility(View.VISIBLE);
        }
        if (body.getLike_ad() == 0) {
            binding.image.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
        } else {
            binding.image.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow2));

        }
        if (body.getFollow() == 0) {
            binding.follow.setText(getResources().getString(R.string.be_my_customer));

        } else {
            binding.follow.setText(getResources().getString(R.string.retreat));
        }
        if (body.getReport() == 0) {
            binding.tvReport.setText(getResources().getString(R.string.report));

        } else {
            binding.tvReport.setText(getResources().getString(R.string.delete_reporting));
        }
        if (body.getImages() != null && body.getImages().size() > 0) {
            Log.e("lll", body.getImages().size() + "");
            NUM_PAGES = body.getImages().size();
            singleslidingImage__adapter = new SingleAdsSlidingImage_Adapter(this, body.getImages());
            binding.pager.setAdapter(singleslidingImage__adapter);
        }
    }


    @Override
    public void back() {
        finish();
    }

    public void displayimage(Single_Adversiment_Model.Images images) {
        Intent intent = new Intent(AdsDetialsActivity.this, Order_Image_Activity.class);
        intent.putExtra("detials", images);

        startActivityForResult(intent, 1003);
    }
}
