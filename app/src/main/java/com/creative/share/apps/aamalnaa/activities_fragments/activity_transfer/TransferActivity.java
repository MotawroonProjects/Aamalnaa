package com.creative.share.apps.aamalnaa.activities_fragments.activity_transfer;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.databinding.ActivityTransferBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;

import java.util.Locale;

import io.paperdb.Paper;

public class TransferActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityTransferBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transfer);
        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

    }

    @Override
    public void back() {
        finish();
    }
}
