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
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.aamalnaa.databinding.CatogryRowBinding;
import com.creative.share.apps.aamalnaa.databinding.SliderBinding;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Category_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Catogries_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private HomeActivity activity;
    private int i = 0;
private Fragment_Main fragment_main;
private Fragment fragment;
    public Category_Adapter(List<Catogries_Model.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (HomeActivity) context;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CatogryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.catogry_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
eventHolder.binding.setLang(lang);
eventHolder.binding.setCatogrymodel(orderlist.get(position));
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        i=position;
        if(fragment instanceof  Fragment_Main){
            fragment_main=(Fragment_Main)fragment;
            fragment_main.setcat_id(orderlist.get(position).getId());
        }
        notifyDataSetChanged();

    }
});
if(i==position){
    eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_green));

}
else {
    eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_white));

}
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public CatogryRowBinding binding;

        public EventHolder(@NonNull CatogryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
