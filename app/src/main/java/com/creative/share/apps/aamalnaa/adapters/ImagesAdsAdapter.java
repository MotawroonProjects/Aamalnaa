package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_update_ads.UpdateAdsActivity;
import com.creative.share.apps.aamalnaa.databinding.ImageRowBinding;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.util.List;

public class ImagesAdsAdapter extends RecyclerView.Adapter<ImagesAdsAdapter.MyHolder> {

    private List<UserModel.Ads.Images> urlList;
    private Context context;
    private AddAdsActivity activity;
private UpdateAdsActivity updateAdsActivity;
    public ImagesAdsAdapter(List<UserModel.Ads.Images> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;

if(context instanceof  AddAdsActivity){
    activity=(AddAdsActivity)context;
}
else if(context instanceof UpdateAdsActivity){
    updateAdsActivity=(UpdateAdsActivity)context;
}

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_row,parent,false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Uri url = Uri.parse(Tags.IMAGE_Ads_URL+urlList.get(position).getImage());

        holder.imageRowBinding.setUrl(url.toString());

     holder.imageRowBinding.imageDelete.setVisibility(View.GONE);


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
