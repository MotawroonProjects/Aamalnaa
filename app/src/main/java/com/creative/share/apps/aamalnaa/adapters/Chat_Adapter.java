package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.databinding.ChatImageLeftRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatImageRightRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatMessageLeftRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ChatMessageRightRowBinding;
import com.creative.share.apps.aamalnaa.databinding.LoadMoreBinding;
import com.creative.share.apps.aamalnaa.databinding.NotificationRowBinding;
import com.creative.share.apps.aamalnaa.models.MessageModel;
import com.creative.share.apps.aamalnaa.models.NotificationDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT = 2;
    private final int ITEM_image_LEFT = 1;
    private final int ITEM_image_RIGHT = 2;
    private final String lang;


    private List<MessageModel.Data> messageModelList;
    private int current_user_id;
    private String chat_user_image;
    private Context context;
    private LayoutInflater inflater;

    public Chat_Adapter(List<MessageModel.Data> messageModelList, int current_user_id, String chat_user_image, Context context) {
        this.messageModelList = messageModelList;
        this.current_user_id = current_user_id;
        this.chat_user_image = chat_user_image;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_MESSAGE_RIGHT)
        {
            ChatMessageRightRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_message_right_row,parent,false);
            return new RightMessageEventHolder(binding);

        }
        else  if (viewType==ITEM_MESSAGE_LEFT)
        {
            ChatMessageLeftRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_message_left_row,parent,false);
            return new LeftMessageEventHolder(binding);

        }
        else  if (viewType==ITEM_image_LEFT)
        {
            ChatImageLeftRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_image_left_row,parent,false);
            return new LeftImageEventHolder(binding);

        }
        else
        {
            ChatMessageRightRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.chat_image_right_row,parent,false);
            return new RightMessageEventHolder(binding);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel.Data messageModel = messageModelList.get(position);
        if (holder instanceof RightImageEventHolder)
        {
            RightImageEventHolder eventHolder = (RightImageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
eventHolder.binding.setLang(lang);


        }
        else   if (holder instanceof LeftImageEventHolder)
        {
            LeftImageEventHolder eventHolder = (LeftImageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        }
        else   if (holder instanceof RightMessageEventHolder)
        {
            RightMessageEventHolder eventHolder = (RightMessageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        }
        else   if (holder instanceof LeftMessageEventHolder)
        {
            LeftMessageEventHolder eventHolder = (LeftMessageEventHolder) holder;

            eventHolder.binding.setMessagemodel(messageModel);
            eventHolder.binding.setLang(lang);


        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class RightMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageRightRowBinding binding;
        public RightMessageEventHolder(@NonNull ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public class LeftMessageEventHolder extends RecyclerView.ViewHolder {
        public ChatMessageLeftRowBinding binding;
        public LeftMessageEventHolder(@NonNull ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public class LeftImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageLeftRowBinding binding;
        public LeftImageEventHolder(@NonNull ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public class RightImageEventHolder extends RecyclerView.ViewHolder {
        public ChatImageRightRowBinding binding;
        public RightImageEventHolder(@NonNull ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        MessageModel.Data messageModel = messageModelList.get(position);

        if (messageModel.getReceiver_id() == current_user_id) {
            if (messageModel.getType().equals("text ")) {
                return ITEM_MESSAGE_LEFT;
            } else  {
                return ITEM_image_LEFT;
            }
        }
        else {
            if (messageModel.getType().equals("text ")) {
                return ITEM_MESSAGE_RIGHT;
            } else  {
                return ITEM_image_RIGHT;
            }


        }


}}
