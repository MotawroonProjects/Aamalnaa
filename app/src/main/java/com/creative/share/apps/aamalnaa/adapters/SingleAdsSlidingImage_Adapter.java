package com.creative.share.apps.aamalnaa.adapters;


import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.databinding.SliderBinding;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.Slider_Model;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SingleAdsSlidingImage_Adapter extends PagerAdapter {


    List<Single_Adversiment_Model.Images> IMAGES;
    private LayoutInflater inflater;
     Context context;


    public SingleAdsSlidingImage_Adapter(Context context, List<Single_Adversiment_Model.Images> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        SliderBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slider,view,false);


        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+IMAGES.get(position).getImage())).fit().into(rowBinding.image);
        view.addView(rowBinding.getRoot());
        return rowBinding.getRoot();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
