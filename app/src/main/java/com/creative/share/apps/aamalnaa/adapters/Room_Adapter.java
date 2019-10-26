package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.databinding.CommentRowBinding;
import com.creative.share.apps.aamalnaa.databinding.UserSearchRowBinding;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.UserRoomModelData;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Room_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserRoomModelData.UserRoomModel> userRoomModels;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private HomeActivity activity;

    public Room_Adapter(List<UserRoomModelData.UserRoomModel> userRoomModels, Context context) {
        this.userRoomModels = userRoomModels;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (HomeActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        UserSearchRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_search_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
eventHolder.binding.setUserroommodel(userRoomModels.get(position));
    }

    @Override
    public int getItemCount() {
        return userRoomModels.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public UserSearchRowBinding binding;

        public EventHolder(@NonNull UserSearchRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
