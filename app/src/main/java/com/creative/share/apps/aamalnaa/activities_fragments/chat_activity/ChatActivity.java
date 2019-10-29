package com.creative.share.apps.aamalnaa.activities_fragments.chat_activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.databinding.ActivityChatBinding;
import com.creative.share.apps.aamalnaa.language.Language;

import com.creative.share.apps.aamalnaa.models.MessageModel;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
private ActivityChatBinding binding;
    private TextView tv_name;
    private CircleImageView image,image_chat_user;
    private ImageView image_send,image_back,image_info,image_delete;
    private EditText edt_msg_content;
    private ConstraintLayout cons_typing;
    private LinearLayout ll_back;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ProgressBar progBar,progBarLoadMore;
    private List<MessageModel> messageModelList;
    private ChatAdapter adapter;
    private int current_page = 1;
    private boolean isLoading = false;
    private UserModel userModel;
    private String current_language;
    private Preferences preferences;
    private boolean canSendTyping = true;
    private boolean from = true;
    private MediaPlayer mp;
    private boolean isNewMsg = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_ads);
        initView();
    }



    private void initView()
    {
        EventBus.getDefault().register(this);
        messageModelList = new ArrayList<>();
        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        image_back = findViewById(R.id.image_back);
        if (current_language.equals("ar")) {
            image_back.setImageResource(R.drawable.ic_right_arrow);
        } else {
            image_back.setImageResource(R.drawable.ic_left_arrow);

        }



        image_info = findViewById(R.id.image_info);
        image_delete = findViewById(R.id.image_delete);

        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBarLoadMore = findViewById(R.id.progBarLoadMore);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        tv_name = findViewById(R.id.tv_name);
        image = findViewById(R.id.image);
        image_send =findViewById(R.id.image_send);
        edt_msg_content = findViewById(R.id.edt_msg_content);
        ll_back = findViewById(R.id.ll_back);

        cons_typing = findViewById(R.id.cons_typing);
        image_chat_user = findViewById(R.id.image_chat_user);


        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(true);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy<0)
                {

                    int lastVisibleItemPos = manager.findLastCompletelyVisibleItemPosition();
                    int total_item = recView.getAdapter().getItemCount();

                    if (total_item >= 20 && lastVisibleItemPos >= (total_item-5)&&!isLoading)
                    {
                        progBarLoadMore.setVisibility(View.VISIBLE);
                        int next_page = current_page+1;
                        isLoading = true;
                        loadMore(next_page);
                    }
                }
            }
        });




        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Back();
            }
        });





        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_msg_content.getText().toString().trim();
                if (!TextUtils.isEmpty(msg))
                {
                    edt_msg_content.setText("");
                    canSendTyping = true;
                    updateTyingState(Tags.END_TYPING);
                    SendMessage(msg);
                }
            }
        });

        edt_msg_content.addTextChangedListener(new TextWatcher() {
            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String msg = edt_msg_content.getText().toString();
                if (msg.length()>0)
                {
                    if (canSendTyping)
                    {
                        canSendTyping = false;
                        updateTyingState(Tags.START_TYPING);
                    }
                }else
                {
                    canSendTyping = true;
                    updateTyingState(Tags.END_TYPING);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDeleteDialog();
            }
        });

        image_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatUserInfoActivity.class);
                intent.putExtra("data",chatUserModel);
                startActivity(intent);
            }
        });


    }
    private void CreateDeleteDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delete_messages,null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteChat();
            }
        });

        //dialog.getWindow().getAttributes().windowAnimation= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
    ///////////////////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToTyping(TypingModel typingModel)
    {
        Log.e("",typingModel.getTyping_value());
        if (typingModel.getTyping_value().equals("1"))
        {
            cons_typing.setVisibility(View.VISIBLE);

            new MyAsyncTask().execute();

        }else if (typingModel.getTyping_value().equals("2"))
        {

            cons_typing.setVisibility(View.GONE);
            if (mp!=null)
            {
                mp.release();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToNewMessage(MessageModel messageModel)
    {
        isNewMsg = true;
        if (adapter==null)
        {
            messageModelList.add(messageModel);
            adapter = new ChatAdapter(messageModelList,userModel.getUser_id(),chatUserModel.getImage(), ChatActivity.this);
            recView.setAdapter(adapter);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recView.scrollToPosition(messageModelList.size()-1);

                }
            },100);

        }else
        {
            messageModelList.add(messageModel);
            adapter.notifyItemInserted(messageModelList.size()-1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recView.scrollToPosition(messageModelList.size()-1);

                }
            },100);

        }
    }

    ///////////////////////////////////////////////

    private void updateTyingState(int typingState)
    {

        Api.getService()
                .typing(chatUserModel.getRoom_id(),userModel.getUser_id(),chatUserModel.getId(),typingState)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful())
                        {
                            Log.e("success typing","true");
                        }else
                        {
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void UpdateUI(ChatUserModel chatUserModel)
    {
        preferences.saveChatUserData(this,chatUserModel);
        tv_name.setText(chatUserModel.getName());
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+chatUserModel.getImage())).placeholder(R.drawable.logo).fit().into(image);
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+chatUserModel.getImage())).placeholder(R.drawable.logo).fit().into(image_chat_user);
    }
    private void SendMessage(String msg)
    {

        sendTextMessage(msg);


    }
    private void sendTextMessage(String msg)
    {
        Api.getService()
                .sendMessage(chatUserModel.getRoom_id(),userModel.getUser_id(),chatUserModel.getId(),msg)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful())
                        {
                            if (adapter==null)
                            {
                                messageModelList.add(response.body());
                                adapter = new ChatAdapter(messageModelList,userModel.getUser_id(),chatUserModel.getImage(), ChatActivity.this);
                                adapter.notifyDataSetChanged();
                                recView.setAdapter(adapter);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recView.scrollToPosition(messageModelList.size()-1);

                                    }
                                },100);
                            }else
                            {
                                messageModelList.add(response.body());
                                adapter.notifyItemInserted(messageModelList.size()-1);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recView.scrollToPosition(messageModelList.size()-1);

                                    }
                                },100);
                            }

                        }else
                        {

                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void getChatMessages()
    {
        Api.getService()
                .getChatMessages(userModel.getUser_id(),chatUserModel.getRoom_id(),1)
                .enqueue(new Callback<MessageDataModel>() {
                    @Override
                    public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful())
                        {

                            if (response.body()!=null&&response.body().getData().size()>0)
                            {
                                if (!from)
                                {
                                    chatUserModel = new ChatUserModel(response.body().getRoom_data().getSecond_user_name(),response.body().getRoom_data().getSecond_user_image(),response.body().getRoom_data().getSecond_user(),response.body().getRoom_data().getRoom_id(),response.body().getRoom_data().getDate_registration());
                                    UpdateUI(chatUserModel);
                                }
                                image_delete.setVisibility(View.VISIBLE);

                                messageModelList.addAll(response.body().getData());
                                adapter = new ChatAdapter(messageModelList,userModel.getUser_id(),chatUserModel.getImage(), ChatActivity.this);
                                recView.setAdapter(adapter);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recView.scrollToPosition(messageModelList.size()-1);

                                    }
                                },100);
                            }else
                                {
                                    image_delete.setVisibility(View.INVISIBLE);
                                }
                        }else
                        {

                            Toast.makeText(ChatActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void loadMore(int next_page)
    {
        Api.getService()
                .getChatMessages(userModel.getUser_id(),chatUserModel.getRoom_id(),next_page)
                .enqueue(new Callback<MessageDataModel>() {
                    @Override
                    public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBarLoadMore.setVisibility(View.GONE);

                            if (response.body()!=null&&response.body().getData().size()>0)
                            {


                                messageModelList.addAll(0,response.body().getData());
                                current_page = response.body().getMeta().getCurrent_page();
                                adapter.notifyItemRangeInserted(0,response.body().getData().size()-1);

                            }
                            isLoading = false;

                        }else
                        {
                            isLoading = false;
                            progBarLoadMore.setVisibility(View.VISIBLE);


                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            progBarLoadMore.setVisibility(View.VISIBLE);
                            Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void deleteChat()
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService()
                .deleteChat(chatUserModel.getRoom_id(),userModel.getUser_id())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            image_delete.setVisibility(View.INVISIBLE);
                            messageModelList.clear();
                            adapter.notifyDataSetChanged();

                            new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();

                                        }
                                    },1500);
                        }else
                        {
                            dialog.dismiss();

                            Toast.makeText(ChatActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void Back()
    {
        if (from)
        {
            Intent intent = getIntent();
            setResult(RESULT_OK,intent);
            finish();
        }else
        {
            Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        Back();
    }
    @Override
    public void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        preferences.clearChatUserData(ChatActivity.this);
        if (mp!=null)
        {
            mp.release();
        }

    }
    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mp = MediaPlayer.create(ChatActivity.this, R.raw.typing);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            mp.start();
            mp.setLooping(false);
            return null;
        }
    }
}
