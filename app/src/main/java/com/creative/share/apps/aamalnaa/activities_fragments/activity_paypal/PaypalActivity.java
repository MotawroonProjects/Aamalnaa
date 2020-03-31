package com.creative.share.apps.aamalnaa.activities_fragments.activity_paypal;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.PaypalwebviewActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_addbalance.AddBalanceActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_my_wallet.WalletActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.adapters.CityAdapter;
import com.creative.share.apps.aamalnaa.adapters.ImagesAdapter;
import com.creative.share.apps.aamalnaa.adapters.Service_Adapter;
import com.creative.share.apps.aamalnaa.adapters.Spinner_Category_Adapter;
import com.creative.share.apps.aamalnaa.adapters.Spinner_Sub_Category_Adapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityAddAdsBinding;
import com.creative.share.apps.aamalnaa.databinding.ActivityPaypalBinding;
import com.creative.share.apps.aamalnaa.databinding.DialogSelectImageBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Add_Balance_Model;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.Order_Upload_Model;
import com.creative.share.apps.aamalnaa.models.PayPal_Model;
import com.creative.share.apps.aamalnaa.models.SelectedLocation;
import com.creative.share.apps.aamalnaa.models.Service_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaypalActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityPaypalBinding binding;
private PayPal_Model payPal_model;
    private String lang;

private Preferences preferences;
private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_paypal);
        initView();

    }


    private void initView() {
        payPal_model=new PayPal_Model();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setAddbalancemodel(payPal_model);



        binding.btnLogin.setOnClickListener(view -> {
            if (payPal_model.isDataValidStep1(this)) {
                if (userModel != null) {

                    payment(payPal_model);


                } else {
                    Common.CreateNoSignAlertDialog(this);
                }


            }

        });

    }

    private void payment(PayPal_Model add_balance_model) {
        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();



        try {
            Api.getService(Tags.base_url)
                    .Payment(payPal_model.getAmount(),userModel.getUser().getId()+"").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {


                            JSONObject obj = null;

                            try {
                                String re=response.body().string();
                                Log.e("data",re);
                                obj = new JSONObject(re);
                                // Log.e("data",obj.stri);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("data",e.getMessage());
                            }
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(PaypalActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        Intent intent = new Intent(PaypalActivity.this, PaypalwebviewActivity.class);
                        try {
                            intent.putExtra("url",obj.get("url").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity(intent);
                        finish();
                    } else {
                        try {
if(response.code()==404){
                            Toast.makeText(PaypalActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();}
                            Log.e("Error", response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers()+" "+response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(PaypalActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }


    @Override
    public void back() {
        finish();
    }





}
