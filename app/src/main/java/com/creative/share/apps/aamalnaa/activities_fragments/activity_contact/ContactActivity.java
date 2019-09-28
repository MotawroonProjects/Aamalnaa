package com.creative.share.apps.aamalnaa.activities_fragments.activity_contact;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.databinding.ActivityContactBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.ContactUsModel;

import java.util.Locale;

import io.paperdb.Paper;

public class ContactActivity extends AppCompatActivity implements Listeners.BackListener , Listeners.ContactListener {
    private ActivityContactBinding binding;
    private String lang;
    private ContactUsModel contactUsModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact);
        initView();

    }


    private void initView() {
        contactUsModel = new ContactUsModel();
        binding.setContactUs(contactUsModel);
        binding.setContactListener(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);


    }


    @Override
    public void back() {
        finish();
    }

    @Override
    public void sendContact(ContactUsModel contactUsModel)
    {

        if (contactUsModel.isDataValid(this))
        {

        }

    }
}
