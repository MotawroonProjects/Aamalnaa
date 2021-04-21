package com.creative.share.apps.aamalnaa.activities_fragments.activity_addbalance;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_my_ads.MyAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_my_wallet.WalletActivity;
import com.creative.share.apps.aamalnaa.adapters.Spinner_Bank_Adapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityAddBalanceBinding;
import com.creative.share.apps.aamalnaa.databinding.DialogSelectImageBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Add_Balance_Model;
import com.creative.share.apps.aamalnaa.models.BankDataModel;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBalanceActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAddBalanceBinding binding;

    private String lang;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url = null;

    private Add_Balance_Model add_balance_model;
private Preferences preferences;
private UserModel userModel;
private List<BankDataModel.BankModel> bankModelList;
private Spinner_Bank_Adapter spinner_bank_adapter;
    private String bank_id="";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_balance);
        initView();
getBankAccount();
    }

    private void initView() {
        add_balance_model=new Add_Balance_Model();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
bankModelList=new ArrayList<>();
spinner_bank_adapter=new Spinner_Bank_Adapter(bankModelList,this);
binding.spinnerBanks.setAdapter(spinner_bank_adapter);
add_balance_model.setBank_id(bank_id);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setAddbalancemodel(add_balance_model);

        binding.imageSelectPhoto.setOnClickListener(view -> CreateImageAlertDialog());

        binding.spinnerBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    bank_id = "";

                } else {
                    bank_id = String.valueOf(bankModelList.get(i).getId());


                }
                add_balance_model.setBank_id(bank_id);
                binding.setAddbalancemodel(add_balance_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnSend.setOnClickListener(view -> {
            if (add_balance_model.isDataValidStep1(this)&&url!=null) {
                if (userModel != null) {
                   payment(add_balance_model);
                } else {
                    Common.CreateNoSignAlertDialog(this);
                }


            }
            else {
                if(url==null){
                    Toast.makeText(this,getResources().getString(R.string.upload_picture),Toast.LENGTH_LONG).show();
                }

            }
        });

    }



    private void payment(Add_Balance_Model add_balance_model) {
        final Dialog dialog = Common.createProgressDialog(AddBalanceActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");

        RequestBody bank_part = Common.getRequestBodyText(add_balance_model.getBank_id());
        RequestBody name_part = Common.getRequestBodyText(add_balance_model.getName());
        RequestBody num_part = Common.getRequestBodyText(add_balance_model.getNumber());
        RequestBody amount_part = Common.getRequestBodyText(add_balance_model.getAmount());



        MultipartBody.Part partimage = Common.getMultiPart(this,url, "image");
        try {
            Api.getService(Tags.base_url)
                    .Payment(user_part, bank_part,name_part,num_part,amount_part,partimage).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(AddBalanceActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        Intent intent = new Intent(AddBalanceActivity.this, WalletActivity.class);


                        startActivity(intent);
                        finish();
                    } else {
                        try {

                            Toast.makeText(AddBalanceActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers()+" "+response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(AddBalanceActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
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



    private void CreateImageAlertDialog()
    {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.dialog_select_image,null,false);



        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();



        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void CheckReadPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this,camera_permission)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,write_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{camera_permission,write_permission},IMG_REQ2);
        }else
        {
            SelectImage(IMG_REQ2);

        }

    }
    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }else
            {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent,img_req);

        }else if (img_req ==IMG_REQ2)
        {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,img_req);
            }catch (SecurityException e)
            {
                Toast.makeText(this,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            url = getUriFromBitmap(bitmap);




        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            url = data.getData();




        }
        Picasso.get().load(url).fit().into(binding.image);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }



    @Override
    public void back() {
        finish();
    }
    private void getBankAccount() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getBanks()
                .enqueue(new Callback<BankDataModel>() {
                    @Override
                    public void onResponse(Call<BankDataModel> call, Response<BankDataModel> response) {
                     dialog.dismiss();
                        //  progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                        upatebanks(response.body());
                        }else
                        {
                            try {
                                Log.e("Error",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AddBalanceActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BankDataModel> call, Throwable t) {
                        try {
dialog.dismiss();
                            //  progBar.setVisibility(View.GONE);
                            Toast.makeText(AddBalanceActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        }catch (Exception e)
                        {
                        }
                    }
                });
    }

    private void upatebanks(BankDataModel body) {
bankModelList.clear();
bankModelList.add(new BankDataModel.BankModel("اختر البنك"));
bankModelList.addAll(body.getData());
spinner_bank_adapter.notifyDataSetChanged();
    }


}
