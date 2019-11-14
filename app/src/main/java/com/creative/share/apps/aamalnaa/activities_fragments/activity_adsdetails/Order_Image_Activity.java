package com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.databinding.ActivityImageBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;

import java.util.Locale;

import io.paperdb.Paper;

public class Order_Image_Activity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityImageBinding binding;
    private Single_Adversiment_Model.Images data;
    private String lang;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);

        initView();

    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);

        data = (Single_Adversiment_Model.Images) getIntent().getExtras().getSerializable("detials");
       binding.setOrderImageModel(data);

    }


    @Override
    public void back() {
        finish();
    }


}
