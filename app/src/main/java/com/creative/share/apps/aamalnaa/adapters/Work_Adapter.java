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
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.share.Common;

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
    private int type;
    private Preferences preferences;
    private UserModel userModel;
    public int can_rate;

    public Work_Adapter(List<UserModel.Previous> orderlist, Context context, int type, int can_rate) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (ProfileActivity) context;
        this.type = type;
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        this.can_rate = can_rate;

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
        if (type == 2) {
            eventHolder.binding.btDelte.setText(activity.getResources().getString(R.string.rate));
        }

        //Common.CreateAlertDialog(activity,orderlist.get(position).getId()+" "+ userModel.getUser().getId());
        if ( can_rate == 0 && type == 2) {
            eventHolder.binding.btDelte.setVisibility(View.GONE);
        } else {
            if (type == 2 && orderlist.get(position).getId() != userModel.getUser().getId()) {
                eventHolder.binding.btDelte.setVisibility(View.GONE);

            } else {
                eventHolder.binding.btDelte.setVisibility(View.VISIBLE);
            }
        }
        eventHolder.binding.btDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == 1) {
                    activity.deletework(eventHolder.getLayoutPosition());
                } else if (type == 2) {
                    activity.Createratedialog(context);
                }

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
