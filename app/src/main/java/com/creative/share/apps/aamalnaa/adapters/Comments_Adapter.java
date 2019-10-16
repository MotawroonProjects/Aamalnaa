package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails.AdsDetialsActivity;
import com.creative.share.apps.aamalnaa.databinding.CommentRowBinding;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Comments_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Single_Adversiment_Model.Comments> commentsList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private AdsDetialsActivity activity;

    public Comments_Adapter(List<Single_Adversiment_Model.Comments> commentsList, Context context) {
        this.commentsList = commentsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (AdsDetialsActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CommentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.comment_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
eventHolder.binding.setLang(lang);
eventHolder.binding.setCommentmodel(commentsList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public CommentRowBinding binding;

        public EventHolder(@NonNull CommentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
