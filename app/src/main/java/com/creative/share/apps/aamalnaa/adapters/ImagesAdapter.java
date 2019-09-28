package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aamalnaa.databinding.ImageRowBinding;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyHolder> {

    private List<String> urlList;
    private Context context;
    private AddAdsActivity activity;

    public ImagesAdapter(List<String> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;
        activity = (AddAdsActivity) context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_row,parent,false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        String url = urlList.get(position);

        holder.imageRowBinding.setUrl(url);

        holder.imageRowBinding.imageDelete.setOnClickListener(view -> activity.deleteImage(holder.getAdapterPosition())
        );


    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageRowBinding imageRowBinding;

        public MyHolder(ImageRowBinding imageRowBinding) {
            super(imageRowBinding.getRoot());
            this.imageRowBinding = imageRowBinding;



        }


    }
}
