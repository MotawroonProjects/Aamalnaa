package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.databinding.CustomerRowBinding;
import com.creative.share.apps.aamalnaa.models.UserModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Customer_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel.Customers> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private ProfileActivity activity;
    private int i = 0;
    int type;

    public Customer_Adapter(List<UserModel.Customers> orderlist, Context context, int type) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (ProfileActivity) context;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CustomerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.customer_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setLang(lang);
        eventHolder.binding.setWorkmodel(orderlist.get(position));
        eventHolder.binding.btBecustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.changetoworks(eventHolder.getLayoutPosition());
            }
        });
        eventHolder.binding.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.delteclients(eventHolder.getLayoutPosition());

            }
        });
        eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("data", orderlist.get(eventHolder.getLayoutPosition()).getId() + "");
                context.startActivity(intent);
            }
        });
        if (type == 2) {
            eventHolder.binding.btDelete.setVisibility(View.GONE);
            eventHolder.binding.btBecustomer.setVisibility(View.GONE);
        }
        if (orderlist.get(position).getBy_my_previous() == 0) {
            eventHolder.binding.btBecustomer.setVisibility(View.GONE);
        } else if (type == 1) {
            eventHolder.binding.btBecustomer.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public CustomerRowBinding binding;

        public EventHolder(@NonNull CustomerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
