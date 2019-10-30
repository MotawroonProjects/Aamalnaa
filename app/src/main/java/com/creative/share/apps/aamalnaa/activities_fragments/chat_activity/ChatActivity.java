package com.creative.share.apps.aamalnaa.activities_fragments.chat_activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.adapters.Chat_Adapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityChatBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.MessageModel;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatBinding binding;
    private String lang;
    private Chat_Adapter chat_adapter;
    private List<MessageModel.Data> messagedatalist;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private String reciver_id="0";
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);
        initView();
        getmessge();

    }

    private void initView() {
        if(getIntent().getStringExtra("data")!=null){
            reciver_id=getIntent().getStringExtra("data");
        }
        messagedatalist=new ArrayList<>();
preferences=Preferences.getInstance();
userModel=preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        manager = new LinearLayoutManager(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
        chat_adapter = new Chat_Adapter(messagedatalist,userModel.getUser().getId(),this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
       // binding.llMsgContainer.setVisibility(View.GONE);

binding.recView.setAdapter(chat_adapter);

    }
    public void getmessge() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        Log.e("lkk",reciver_id+" "+userModel.getUser().getId());
        messagedatalist.clear();
        chat_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService( Tags.base_url)
                    .getMessge(reciver_id,userModel.getUser().getId()+"")
                    .enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                           // binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                messagedatalist.clear();
                                messagedatalist.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                   // binding.llMsgContainer.setVisibility(View.GONE);
                                    chat_adapter.notifyDataSetChanged();
                                    binding.recView.scrollToPosition(messagedatalist.size()-1);

                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    chat_adapter.notifyDataSetChanged();

                                 //   binding.llNoStore.setVisibility(View.VISIBLE);

                                }
                            } else {

                                chat_adapter.notifyDataSetChanged();

                                //binding.llNoStore.setVisibility(View.VISIBLE);
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            try {
                                //binding.swipeRefresh.setRefreshing(false);

                                binding.progBar.setVisibility(View.GONE);
                                //binding.llNoStore.setVisibility(View.VISIBLE);


                                Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }catch (Exception e){
            binding.progBar.setVisibility(View.GONE);
            //binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void back() {
        finish();
    }
}
