package com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_about.AboutActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_be_golden_client.BeGoldenClientActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_contact.ContactActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_favorite.FavoriteActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_my_ads.MyAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_my_wallet.WalletActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profit.ProfitActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_transfer.TransferActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_video.VideoActivity;
import com.creative.share.apps.aamalnaa.databinding.DialogLanguageBinding;
import com.creative.share.apps.aamalnaa.databinding.FragmentMoreBinding;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {

    private HomeActivity activity;
    private FragmentMoreBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;


    public static Fragment_More newInstance() {

        return new Fragment_More();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userModel = preferences.getUserData(activity);
        if (userModel != null) {
            Picasso.with(activity).load(Tags.IMAGE_user_URL + userModel.getUser().getAvatar()).placeholder(R.drawable.user_profile).fit().into(binding.image);
            binding.tvName.setText(userModel.getUser().getName());
        }
        binding.image.setOnClickListener(view -> {


            if (userModel != null) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                startActivity(intent);
            } else {
                Common.CreateNoSignAlertDialog(activity);

            }
        });

        binding.llFavorite.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(activity, FavoriteActivity.class);
                startActivity(intent);
            } else {
                Common.CreateNoSignAlertDialog(activity);
            }
        });

        binding.llAds.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(activity, MyAdsActivity.class);
                startActivity(intent);

            } else {
                Common.CreateNoSignAlertDialog(activity);
            }


        });

        binding.llWallet.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(activity, WalletActivity.class);
                startActivity(intent);
            } else {
                Common.CreateNoSignAlertDialog(activity);

            }
        });

        binding.llprofit.setOnClickListener(view -> {
            if (userModel != null) {
                Intent intent = new Intent(activity, ProfitActivity.class);
                startActivity(intent);
            }
        });

        binding.llTerm.setOnClickListener(view -> {
            Intent intent = new Intent(activity, TermsActivity.class);
            startActivity(intent);
        });
        binding.llvideo.setOnClickListener(view -> {
            Intent intent = new Intent(activity, VideoActivity.class);
            startActivity(intent);
        });
        binding.llAbout.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AboutActivity.class);
            startActivity(intent);
        });
        binding.llGoldenClient.setOnClickListener(view -> {
            Intent intent = new Intent(activity, BeGoldenClientActivity.class);
            startActivity(intent);
        });

        binding.llLang.setOnClickListener(view -> {
            CreateLangDialog();
        });

        binding.llShare.setOnClickListener(view -> {
            String app_url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, "تطبيق عالم أعمالنا");
            intent.putExtra(Intent.EXTRA_TEXT, app_url);
            startActivity(intent);
        });

        binding.llRate.setOnClickListener(view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));

            }
        });

        binding.llContact.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactActivity.class);
            startActivity(intent);
        });

        binding.llLogout.setOnClickListener(view -> {
            if (userModel != null) {

                activity.logout();
            } else {
                Common.CreateNoSignAlertDialog(activity);
            }
        });


    }


    private void CreateLangDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_language, null, false);
        String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);
        } else {
            binding.rbEn.setChecked(true);

        }
        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        binding.rbAr.setOnClickListener(view -> {
            dialog.dismiss();
            activity.refreshActivity("ar");


        });
        binding.rbEn.setOnClickListener(view -> {
            dialog.dismiss();
            activity.refreshActivity("en");


        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


}
