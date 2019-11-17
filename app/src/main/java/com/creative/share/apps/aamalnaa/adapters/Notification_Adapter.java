package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_ads.Ads_Activity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_favorite.FavoriteActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_my_ads.MyAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_search.Search_Activity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_transaction.TransactionActivity;
import com.creative.share.apps.aamalnaa.databinding.AdsRowBinding;
import com.creative.share.apps.aamalnaa.databinding.LoadMoreBinding;
import com.creative.share.apps.aamalnaa.databinding.NotificationRowBinding;
import com.creative.share.apps.aamalnaa.models.Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.NotificationDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Notification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<NotificationDataModel.NotificationModel> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
private TransactionActivity transactionActivity;
private HomeActivity homeActivity;
    public Notification_Adapter(List<NotificationDataModel.NotificationModel> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_DATA)
        {
            NotificationRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.notification_row,parent,false);
            return new EventHolder(binding);

        }else
            {
                LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more,parent,false);
                return new LoadHolder(binding);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationDataModel.NotificationModel order_data = orderlist.get(position);
        if (holder instanceof EventHolder)
        {
            EventHolder eventHolder = (EventHolder) holder;

            eventHolder.binding.setNotificationModel(order_data);
eventHolder.binding.imdelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof  TransactionActivity){
            transactionActivity=(TransactionActivity)context;
            transactionActivity.deletenotification(eventHolder.getLayoutPosition());

        }
        else if(context instanceof  HomeActivity ){
            homeActivity=(HomeActivity)context;
            homeActivity.deletenotification(eventHolder.getLayoutPosition());
        }
    }
});


        }else
            {
                LoadHolder loadHolder = (LoadHolder) holder;
                loadHolder.binding.progBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public NotificationRowBinding binding;
        public EventHolder(@NonNull NotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;
        public LoadHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        NotificationDataModel.NotificationModel order_Model = orderlist.get(position);
        if (order_Model!=null)
        {
            return ITEM_DATA;
        }else
            {
                return LOAD;
            }

    }


}
