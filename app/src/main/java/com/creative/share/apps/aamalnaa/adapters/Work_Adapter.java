package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.databinding.WorkRowBinding;
import com.creative.share.apps.aamalnaa.models.UserModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Work_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel.Previous> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private ProfileActivity activity;
    private int i = 0;

    public Work_Adapter(List<UserModel.Previous> orderlist, Context context) {
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


        WorkRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.work_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setLang(lang);
        eventHolder.binding.setWorkmodel(orderlist.get(position));
        eventHolder.binding.btDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deletework(eventHolder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public WorkRowBinding binding;

        public EventHolder(@NonNull WorkRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
