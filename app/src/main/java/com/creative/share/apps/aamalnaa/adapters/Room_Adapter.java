package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Messages;
import com.creative.share.apps.aamalnaa.databinding.CommentRowBinding;
import com.creative.share.apps.aamalnaa.databinding.UserSearchRowBinding;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.models.UserRoomModelData;
import com.creative.share.apps.aamalnaa.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Room_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserRoomModelData.UserRoomModel> userRoomModels;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private HomeActivity activity;
private int i=-1;
private Fragment_Messages fragment_messages;
private Fragment fragment;
private UserModel userModel;
private Preferences preferences;
    public Room_Adapter(List<UserRoomModelData.UserRoomModel> userRoomModels, Context context, Fragment fragment) {
        this.userRoomModels = userRoomModels;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (HomeActivity) context;
        this.fragment=fragment;
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(activity);
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
        if(userRoomModels.get(position).getReceiver_id()==userModel.getUser().getId()){
            userRoomModels.get(position).setReceiver_avatar(userRoomModels.get(position).getSender_avatar());
            userRoomModels.get(position).setReceiver_id(userRoomModels.get(position).getSender_id());
            userRoomModels.get(position).setReceiver_name(userRoomModels.get(position).getSender_name());
            userRoomModels.get(position).setReceiver_mobile(userRoomModels.get(position).getSender_mobile());

            userRoomModels.get(position).setSender_id(userModel.getUser().getId());
            userRoomModels.get(position).setSender_avatar(userModel.getUser().getAvatar());
            userRoomModels.get(position).setSender_name(userModel.getUser().getName());
            userRoomModels.get(position).setSender_mobile(userModel.getUser().getMobile());

            userRoomModels.get(position).setLast_message(userRoomModels.get(position).getLast_message());

        }
eventHolder.binding.setUserroommodel(userRoomModels.get(position));
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        i=position;
        notifyDataSetChanged();
       // activity.gotomessage(userRoomModels.get(eventHolder.getLayoutPosition()).getReceiver_id());
    }
});
if(i==position){
    if(eventHolder.binding.expandLayout.isExpanded()){
        eventHolder.binding.expandLayout.collapse(true);
    }
    else {
        eventHolder.binding.expandLayout.expand(true);
    }

}

eventHolder.binding.imageedit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        activity.gotomessage(userRoomModels.get(eventHolder.getLayoutPosition()).getReceiver_id(),userRoomModels.get(eventHolder.getLayoutPosition()).getReceiver_name(),userRoomModels.get(eventHolder.getLayoutPosition()).getReceiver_mobile());
    }
});
eventHolder.binding.imageDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(fragment instanceof Fragment_Messages){
            fragment_messages=(Fragment_Messages)fragment;
        fragment_messages.delteroom(eventHolder.getLayoutPosition());}
    }
});
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
