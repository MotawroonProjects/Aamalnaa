package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.databinding.AdsUserRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ReatedRowBinding;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class My_Ads_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel.Ads> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private ProfileActivity activity;
    private int i = -1;
    public My_Ads_Adapter(List<UserModel.Ads> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (ProfileActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        AdsUserRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.ads_user_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setLang(lang);
eventHolder.binding.setAdversimentmodel(orderlist.get(position));
        if(orderlist.get(position).getIs_Special()==0){
            eventHolder.binding.imstar.setVisibility(View.GONE);
        }
eventHolder.binding.setUsermodel(Preferences.getInstance().getUserData(activity).getUser());
eventHolder.binding.expandLayout.setOrientation(ExpandableLayout.HORIZONTAL);
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        i=position;
        notifyDataSetChanged();
    }
});
eventHolder.binding.imageedit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        activity.editads(orderlist.get(eventHolder.getLayoutPosition()));
    }
});
eventHolder.binding.imageDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        activity.delete(eventHolder.getLayoutPosition());
    }
});
/*
eventHolder.binding.cons1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        activity.showdetials(orderlist.get(eventHolder.getLayoutPosition()).getId());
    }
});*/
if(i==position){
    if(eventHolder.binding.expandLayout.isExpanded()){
        eventHolder.binding.expandLayout.collapse(true);
    }
    else {
        eventHolder.binding.expandLayout.expand(true);
    }
}
else {
    eventHolder.binding.expandLayout.collapse(true);
}
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public AdsUserRowBinding binding;

        public EventHolder(@NonNull AdsUserRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
