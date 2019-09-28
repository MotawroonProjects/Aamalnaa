package com.creative.share.apps.aamalnaa.activities_fragments.activity_profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_About;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Clients;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Comments;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.fragments.Fragment_Works;
import com.creative.share.apps.aamalnaa.adapters.MyPagerAdapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityProfileBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProfileBinding binding;
    private String lang;
    private MyPagerAdapter pagerAdapter;
    private TextView tvWorkCount,tvClientCount;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.tab.setupWithViewPager(binding.pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(getFragments());
        pagerAdapter.addTitle(getTitles());
        binding.pager.setAdapter(pagerAdapter);

        View tab_item1 = LayoutInflater.from(this).inflate(R.layout.tab_custom_view,null);
        View tab_item2 = LayoutInflater.from(this).inflate(R.layout.tab_custom_view,null);

        tvWorkCount = tab_item1.findViewById(R.id.tvCount);
        tvClientCount = tab_item2.findViewById(R.id.tvCount);

        TextView tvTitle = tab_item1.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.works));

        TextView tvTitle2 = tab_item2.findViewById(R.id.tvTitle);
        tvTitle2.setText(getString(R.string.clients));

        binding.tab.getTabAt(1).setCustomView(tab_item1);

        binding.tab.getTabAt(2).setCustomView(tab_item2);

        updateWorkCount(0);
        updateClientCount(0);


        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==1)
                {
                    TextView tvTitle = tab.getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.colorPrimary));

                    TextView tvTitle2 = binding.tab.getTabAt(2).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.textColor));


                }else if (tab.getPosition()==2)
                {
                    TextView tvTitle = tab.getCustomView().findViewById(R.id.tvTitle);
                    tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.colorPrimary));

                    TextView tvTitle2 = binding.tab.getTabAt(1).getCustomView().findViewById(R.id.tvTitle);
                    tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.textColor));


                }else
                    {
                        TextView tvTitle = binding.tab.getTabAt(1).getCustomView().findViewById(R.id.tvTitle);
                        tvTitle.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.textColor));


                        TextView tvTitle2 = binding.tab.getTabAt(2).getCustomView().findViewById(R.id.tvTitle);
                        tvTitle2.setTextColor(ContextCompat.getColor(ProfileActivity.this,R.color.textColor));

                    }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void updateWorkCount(int count) {
        tvWorkCount.setText(String.format("%s%s%s","(",String.valueOf(count),")"));
    }

    private void updateClientCount(int count) {
        tvClientCount.setText(String.format("%s%s%s","(",String.valueOf(count),")"));
    }

    private List<Fragment> getFragments()
    {
        List<Fragment> fragmentList= new ArrayList<>();
        fragmentList.add(Fragment_About.newInstance());
        fragmentList.add(Fragment_Works.newInstance());
        fragmentList.add(Fragment_Clients.newInstance());
        fragmentList.add(Fragment_Comments.newInstance());

        return fragmentList;

    }

    private List<String> getTitles()
    {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.infor));
        titles.add(getString(R.string.works));
        titles.add(getString(R.string.clients));
        titles.add(getString(R.string.comments));
        return titles;

    }




    @Override
    public void back() {
        finish();
    }
}
