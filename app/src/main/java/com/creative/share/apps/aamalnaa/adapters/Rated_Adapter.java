package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.databinding.CatogryRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ReatedRowBinding;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Rated_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel.Rateds> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private ProfileActivity activity;
    private int i = 0;
    public Rated_Adapter(List<UserModel.Rateds> orderlist, Context context) {
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


        ReatedRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.reated_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
eventHolder.binding.setRatedmodel(orderlist.get(position));
        Log.e("llll",orderlist.get(position).getLiked()+"");
if(orderlist.get(position).getLiked()==0){
    eventHolder.binding.imagelike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dislike));
}
else {
    eventHolder.binding.imagelike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));

}


    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public ReatedRowBinding binding;

        public EventHolder(@NonNull ReatedRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
