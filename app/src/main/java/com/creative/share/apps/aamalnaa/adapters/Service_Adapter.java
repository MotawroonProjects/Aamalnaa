package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_update_ads.UpdateAdsActivity;
import com.creative.share.apps.aamalnaa.databinding.CustomerRowBinding;
import com.creative.share.apps.aamalnaa.databinding.ServiceRowBinding;
import com.creative.share.apps.aamalnaa.models.Service_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Service_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Service_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private AddAdsActivity activity;
    private UpdateAdsActivity updateAdsActivity;

    private int i = 0;

    public Service_Adapter(List<Service_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        if(context instanceof  AddAdsActivity){
            activity=(AddAdsActivity)context;
        }
        else if(context instanceof UpdateAdsActivity){
            updateAdsActivity=(UpdateAdsActivity)context;
        }    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ServiceRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.service_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setServicemodel(orderlist.get(position));
eventHolder.binding.checkbox.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
if(context instanceof  AddAdsActivity){
        if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==1){
            activity.setcommented();
        }else  if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==2){
            activity.setspicial();
        }else  if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==3){
            activity.setviews();
        }else  if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==4){
            activity.setisinstall();
        }

    }
else if(context instanceof  UpdateAdsActivity){
    if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==1){
        updateAdsActivity.setcommented();
    }else  if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==2){
        updateAdsActivity.setspicial();
    }else  if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==3){
        updateAdsActivity.setviews();
    }else  if(orderlist.get(eventHolder.getLayoutPosition()).getCoun()==4){
        updateAdsActivity.setisinstall();
    }

}
    }
});
if(i==position){
    eventHolder.binding.checkbox.setChecked(true);

    if(orderlist.get(i).getCoun()==1){
        updateAdsActivity.setcommented();
    }else  if(orderlist.get(i).getCoun()==2){
        updateAdsActivity.setspicial();
    }else  if(orderlist.get(i).getCoun()==3){
        updateAdsActivity.setviews();
    }else  if(orderlist.get(i).getCoun()==4){
        updateAdsActivity.setisinstall();
    }
}
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public ServiceRowBinding binding;

        public EventHolder(@NonNull ServiceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public void setSelection(int selected_pos)
    {
        this.i = selected_pos;
        notifyDataSetChanged();
    }

}
