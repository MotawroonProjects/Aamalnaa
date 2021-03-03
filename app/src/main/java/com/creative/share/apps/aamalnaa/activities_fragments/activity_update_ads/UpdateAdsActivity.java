package com.creative.share.apps.aamalnaa.activities_fragments.activity_update_ads;

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
import com.creative.share.apps.aamalnaa.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.adapters.CityAdapter;
import com.creative.share.apps.aamalnaa.adapters.ImagesAdapter;
import com.creative.share.apps.aamalnaa.adapters.Service_Adapter;
import com.creative.share.apps.aamalnaa.adapters.Spinner_Category_Adapter;
import com.creative.share.apps.aamalnaa.adapters.Spinner_Sub_Category_Adapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityUpdateAdsBinding;
import com.creative.share.apps.aamalnaa.databinding.DialogSelectImageBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.Order_Upload_Model;
import com.creative.share.apps.aamalnaa.models.SelectedLocation;
import com.creative.share.apps.aamalnaa.models.Service_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

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

public class UpdateAdsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityUpdateAdsBinding binding;
    private SelectedLocation selectedLocation;

    private String lang;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url = null;
    private List<Uri> urlList;
    private LinearLayoutManager manager, manager2;
    private ImagesAdapter imagesAdapter;
    private List<Service_Model.Data> dataList;
    private List<Cities_Model.Data> cDataList;
    private Service_Adapter service_adapter;
    private int views_num = 0, is_Special = 0, is_Install = 0, commented = 0;
    private Spinner_Category_Adapter adapter;
    private List<Catogries_Model.Data> dataList2;
    private Spinner_Sub_Category_Adapter spinner_sub_category_adapters;
    private List<Catogries_Model.Data.Subcategory> subcategories;
    private String cat_id, sub_cat_id;
    private CityAdapter cityadapter;
    private String city_id;
    private String type_id = "1";
    private List<String> type_ids;
    private ArrayAdapter<String> arrayAdapter;
    private Order_Upload_Model order_upload_model;
    private Preferences preferences;
    private UserModel userModel;
    private UserModel.Ads ads;
    private double balance = 0, total = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_ads);
        initView();
        getCities();

        getservice();
        getDepartments();


    }

    private void updateCatogryAdapter(Catogries_Model body) {

        dataList2.add(new Catogries_Model.Data("إختر القسم الرئيسى"));
        if (body.getData() != null) {
            dataList2.addAll(body.getData());
            adapter.notifyDataSetChanged();

        }
        if (ads != null) {
            int x = 0;
            for (int i = 1; i < dataList2.size(); i++) {
                Log.e("kkk", ads.getCategory_id() + "  " + dataList2.get(i).getId());

                if (dataList2.get(i).getId().equals(ads.getCategory_id() + "")) {

                    binding.spinnerMainDepart.setSelection(i);
                    x = i;
                    break;
                }
            }
            updatesublist(dataList2.get(x).getSubcategory());
            for (int i = 1; i < subcategories.size(); i++) {

                if (subcategories.get(i).getId() == ads.getSubcategory_id()) {

                    binding.spinnerSubDepart.setSelection(i);
                }
            }

        }

    }

    public void getDepartments() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .getDepartment()
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                        //   progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                //   ll_no_order.setVisibility(View.GONE);
                                updateCatogryAdapter(response.body());                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                //  ll_no_order.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(UpdateAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Catogries_Model> call, Throwable t) {
                        try {


                            Toast.makeText(UpdateAdsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void initView() {
        if (getIntent().getSerializableExtra("data") != null) {
            ads = (UserModel.Ads) getIntent().getSerializableExtra("data");
        }
        order_upload_model = new Order_Upload_Model();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        type_ids = new ArrayList<>();
        type_ids.addAll(Arrays.asList(getResources().getStringArray(R.array.models)));
        urlList = new ArrayList<>();
        dataList = new ArrayList<>();
        dataList2 = new ArrayList<>();
        subcategories = new ArrayList<>();
        cDataList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setOrderModel(order_upload_model);
        binding.imageSelectPhoto.setOnClickListener(view -> CreateImageAlertDialog());

        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        binding.recView.setLayoutManager(manager);
        imagesAdapter = new ImagesAdapter(urlList, this);
        binding.recView.setAdapter(imagesAdapter);
        service_adapter = new Service_Adapter(dataList, this);
        binding.recService.setLayoutManager(manager2);
        binding.recService.setAdapter(service_adapter);
        adapter = new Spinner_Category_Adapter(dataList2, this);
        binding.spinnerMainDepart.setAdapter(adapter);
        spinner_sub_category_adapters = new Spinner_Sub_Category_Adapter(subcategories, this);
        binding.spinnerSubDepart.setAdapter(spinner_sub_category_adapters);
        cityadapter = new CityAdapter(cDataList, this);
        binding.spinnerAdCity.setAdapter(cityadapter);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_ids) {

            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = super.getDropDownView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

        };
        binding.spinnerAdType.setAdapter(arrayAdapter);
        binding.spinnerMainDepart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    cat_id = "";

                } else {
                    cat_id = String.valueOf(dataList2.get(i).getId());
                    updatesublist(dataList2.get(i).getSubcategory());

                }
                order_upload_model.setCategory_id(cat_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerSubDepart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sub_cat_id = "";
                } else {
                    sub_cat_id = String.valueOf(subcategories.get(i).getId());
                }
                order_upload_model.setSubcategory_id(sub_cat_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.spinnerAdCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";

                } else {
                    city_id = String.valueOf(cDataList.get(i).getId());


                }
                order_upload_model.setCity_id(city_id);
                binding.setOrderModel(order_upload_model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerAdType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Toast.makeText(UpdateAdsActivity.this,"اختر نوع الاعلان",Toast.LENGTH_LONG).show();
                }
                else {
                    type_id = (i) + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateAdsActivity.this, MapActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnSend.setOnClickListener(view -> {
            if (order_upload_model.isDataValidStep1(this)) {
                if (userModel != null) {
                    if (urlList != null && urlList.size() > 0) {
                        updateorder(order_upload_model);
                    } else {
                        updateorderwithoutimage(order_upload_model);
                    }
                } else {
                    Common.CreateNoSignAlertDialog(this);
                }


            }

        });

    }

    private void updatedata(UserModel.Ads ads) {
        balance += ads.getTotal_points();
        order_upload_model.setPrice(ads.getPrice() + "");
        order_upload_model.setCity_id(ads.getCity_id() + "");
        order_upload_model.setSubcategory_id(ads.getSubcategory_id() + "");
        order_upload_model.setTitle(ads.getTitle());
        order_upload_model.setAddress(ads.getAddress());
        SelectedLocation selectedLocation = new SelectedLocation(ads.getLat(), ads.getLng(), ads.getAddress());

        binding.setLocation(selectedLocation);
        binding.tvLocation.setText(ads.getAddress());
        binding.tvTitle.setText(ads.getTitle());
        Log.e("eee", ads.getTitle() + "  " + ads.getAddress());
        order_upload_model.setLatitude(ads.getLat() + "");
        order_upload_model.setLongitude(ads.getLng() + "");
        order_upload_model.setDetails(ads.getDetails());
        type_id = ads.getAds_type() + "";
        if (type_id.equals("1")) {
            binding.spinnerAdType.setSelection(0);
        } else if (type_id.equals("2")) {
            binding.spinnerAdType.setSelection(1);

        } else if (type_id.equals("3")) {
            binding.spinnerAdType.setSelection(2);

        }
Log.e("lsllsl",ads.getCommented()+""+ads.getIs_Special()+""+ads.getViews_num()+""+ads.getIs_Install());
        if (ads.getCommented() == 1) {
            service_adapter.setSelection(0);
            commented = 1;
            total += dataList.get(0).getPrice();
        }
        if (ads.getIs_Special() == 1) {
            service_adapter.setSelection(1);
            is_Special=1;
            total += dataList.get(1).getPrice();

        }
        if (ads.getViews_num() > 0) {
            service_adapter.setSelection(2);
            views_num=ads.getViews_num();
            total += dataList.get(2).getPrice();

        }
        if (ads.getIs_Install() == 1) {
            service_adapter.setSelection(3);
            is_Install=1;
            total += dataList.get(3).getPrice();

        }
        binding.tvtotal.setText(total + " " + getResources().getString(R.string.sar));

        binding.setOrderModel(order_upload_model);

    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(UpdateAdsActivity.this, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    private void updateorder(Order_Upload_Model order_upload_model) {
        final Dialog dialog = Common.createProgressDialog(UpdateAdsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // Log.e("data",userModel.getUser().getId()+" "+order_upload_model.getCategory_id()+" "+order_upload_model.getSubcategory_id()+" "+order_upload_model.getCity_id()+" "+type_id+" "+order_upload_model.getTitle()+" "+order_upload_model.getDetails()+" "+order_upload_model.getAddress()+" "+order_upload_model.getLongitude()+" "+order_upload_model.getLatitude()+" "+views_num+" "+is_Special+" "+is_Install+" "+commented);
        RequestBody ad_part = Common.getRequestBodyText(ads.getId() + "");

        RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");

        RequestBody category_part = Common.getRequestBodyText(order_upload_model.getCategory_id());
        RequestBody subcategory_part = Common.getRequestBodyText(order_upload_model.getSubcategory_id());
        RequestBody city_part = Common.getRequestBodyText(order_upload_model.getCity_id());
        RequestBody type_part = Common.getRequestBodyText(type_id);
        RequestBody title_part = Common.getRequestBodyText(order_upload_model.getTitle());
        RequestBody detials_part = Common.getRequestBodyText(order_upload_model.getDetails());
        RequestBody price_part;
        if (order_upload_model.getPrice() == null) {
            price_part = Common.getRequestBodyText("0");

        } else {
            price_part = Common.getRequestBodyText(order_upload_model.getPrice() + "");

        }
        RequestBody address_part = Common.getRequestBodyText(order_upload_model.getAddress());
        RequestBody long_part = Common.getRequestBodyText(order_upload_model.getLongitude());
        RequestBody lat_part = Common.getRequestBodyText(order_upload_model.getLatitude());
        RequestBody views_num_part = Common.getRequestBodyText(views_num + "");
        RequestBody is_Special_part = Common.getRequestBodyText(is_Special + "");
        RequestBody is_Install_part = Common.getRequestBodyText(is_Install + "");
        RequestBody commented_part = Common.getRequestBodyText(commented + "");
        RequestBody total_part = Common.getRequestBodyText(total + "");

        List<MultipartBody.Part> partimageList = getMultipartBodyList(urlList, "image[]");
        try {
            Api.getService(Tags.base_url)
                    .Updateorder(ad_part, user_part, category_part, subcategory_part, city_part, type_part, title_part, detials_part, price_part, address_part, long_part, lat_part, views_num_part, is_Special_part, is_Install_part, commented_part, total_part, partimageList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(UpdateAdsActivity.this, getString(R.string.suc2), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        Intent intent = new Intent(UpdateAdsActivity.this, ProfileActivity.class);

                        startActivity(intent);
                        finish();
                    } else {
                        try {

                            Toast.makeText(UpdateAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(UpdateAdsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
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

    private void updateorderwithoutimage(Order_Upload_Model order_upload_model) {
        final Dialog dialog = Common.createProgressDialog(UpdateAdsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {
            Api.getService(Tags.base_url)
                    .Updateorder(ads.getId() + "", userModel.getUser().getId() + "", order_upload_model.getCategory_id(), order_upload_model.getSubcategory_id(), order_upload_model.getCity_id(), type_id, order_upload_model.getTitle(), order_upload_model.getDetails(), order_upload_model.getPrice(), order_upload_model.getAddress(), order_upload_model.getLongitude(), order_upload_model.getLatitude(), views_num + "", is_Special + "", is_Install + "", commented + "", total + "").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(UpdateAdsActivity.this, getString(R.string.suc2), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        Intent intent = new Intent(UpdateAdsActivity.this, ProfileActivity.class);

                        startActivity(intent);
                        finish();
                    } else {
                        try {

                            Toast.makeText(UpdateAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(UpdateAdsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
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

    private void updateCityAdapter(Cities_Model body) {

        cDataList.add(new Cities_Model.Data("إختر المدينه"));
        if (body.getData() != null) {
            cDataList.addAll(body.getData());
            cityadapter.notifyDataSetChanged();
        }
        if (ads != null) {
            for (int i = 1; i < cDataList.size(); i++) {
                if (cDataList.get(i).getId() == ads.getCity_id()) {
                    Log.e("kkk", ads.getCity_id() + "");
                    binding.spinnerAdCity.setSelection(i);
                }
            }
        }
    }

    private void getCities() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getCity()
                    .enqueue(new Callback<Cities_Model>() {
                        @Override
                        public void onResponse(Call<Cities_Model> call, Response<Cities_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getData() != null) {
                                    updateCityAdapter(response.body());
                                } else {
                                    Log.e("error", response.code() + "_" + response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(UpdateAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(UpdateAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Cities_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(UpdateAdsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    private void updatesublist(List<Catogries_Model.Data.Subcategory> subcategory) {
        subcategories.clear();
        subcategories.add(new Catogries_Model.Data.Subcategory("إختر القسم الفرعى"));
        if (subcategory != null) {
            subcategories.addAll(subcategory);
            spinner_sub_category_adapters.notifyDataSetChanged();

        }
    }


    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            url = getUriFromBitmap(bitmap);
            urlList.add(url);
            imagesAdapter.notifyDataSetChanged();


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            url = data.getData();
            urlList.add(url);
            imagesAdapter.notifyDataSetChanged();


        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("location")) {
                selectedLocation = (SelectedLocation) data.getSerializableExtra("location");
                binding.setLocation(selectedLocation);
                order_upload_model.setLatitude(String.valueOf(selectedLocation.getLat()));
                order_upload_model.setLongitude(String.valueOf(selectedLocation.getLng()));
                order_upload_model.setAddress(selectedLocation.getAddress());
                binding.setOrderModel(order_upload_model);

            }
        }

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

    public void deleteImage(int adapterPosition) {
        urlList.remove(adapterPosition);
        imagesAdapter.notifyItemRemoved(adapterPosition);

    }

    @Override
    public void back() {
        finish();
    }


    public int setcommented() {

        if (commented == 0) {
            if (balance > total && (total + dataList.get(0).getPrice() <= balance)) {
                commented = 1;
                total += dataList.get(0).getPrice();
            } else {
                Toast.makeText(this, "رصيد نقاطك غير كافى", Toast.LENGTH_LONG).show();
                return 0;
            }
        } else {
            commented = 0;
            total -= dataList.get(0).getPrice();

        }
        binding.tvtotal.setText(total + " " + getResources().getString(R.string.sar));
        return 1;
    }

    public int setspicial() {
        if (is_Special == 0) {
            if (balance > total && (total + dataList.get(1).getPrice() <= balance)) {

                is_Special = 1;
                total += dataList.get(1).getPrice();
            } else {
                Toast.makeText(this, "رصيد نقاطك غير كافى", Toast.LENGTH_LONG).show();
                return 0;
            }

        } else {
            is_Special = 0;
            total -= dataList.get(1).getPrice();

        }

        binding.tvtotal.setText(total + " " + getResources().getString(R.string.sar));
        return 1;
    }

    public int setviews() {
        if (views_num == 0) {
            if (balance > total && (total + dataList.get(2).getPrice() <= balance)) {

                views_num = 1;
                total += dataList.get(2).getPrice();
            } else {
                Toast.makeText(this, "رصيد نقاطك غير كافى", Toast.LENGTH_LONG).show();
                return 0;
            }

        } else {
            views_num = 0;
            total -= dataList.get(2).getPrice();

        }
        binding.tvtotal.setText(total + " " + getResources().getString(R.string.sar));
        return 1;
    }

    public int setisinstall() {
        if (is_Install == 0) {
            if (balance > total && (total + dataList.get(3).getPrice() <= balance)) {

                is_Install = 1;
                total += dataList.get(3).getPrice();
            } else {
                Toast.makeText(this, "رصيد نقاطك غير كافى", Toast.LENGTH_LONG).show();
                return 0;
            }

        } else {
            is_Install = 0;
            total -= dataList.get(3).getPrice();

        }
        binding.tvtotal.setText(total + " " + getResources().getString(R.string.sar));
        return 1;
    }

    private void getservice() {
        ProgressDialog dialog = Common.createProgressDialog(UpdateAdsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getservice()
                    .enqueue(new Callback<Service_Model>() {
                        @Override
                        public void onResponse(Call<Service_Model> call, Response<Service_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateservice(response.body());
                            } else {

                                Toast.makeText(UpdateAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Service_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(UpdateAdsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("err", e.getCause().toString());
        }
    }

    private void updateservice(Service_Model body) {
        dataList.clear();
        dataList.addAll(body.getData());
        service_adapter.notifyDataSetChanged();
        if (ads != null) {
            getprofiledata();

        }
//        double totla=0;
//        for(int i=0;i<body.getData().size();i++){
//            totla+=body.getData().get(i).getCoun();
//        }
//        binding.tvtotal.setText(totla+" "+getResources().getString(R.string.sar));
    }

    private void getprofiledata() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            //Log.e("data_f",id);

            Api.getService(Tags.base_url)
                    .getmyprofile(userModel.getUser().getId() + "", userModel.getUser().getId() + "")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                // updatepruofile(response.body());
                                balance += Double.parseDouble(response.body().getUser().getBalance());
                                updatedata(ads);

                            } else {

                                //Toast.makeText(WalletActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error_data5", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        //   Toast.makeText(WalletActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Toast.makeText(WalletActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            if (dialog != null) {
                dialog.dismiss();
            }

            // Log.e("err", e.getMessage());
        }
    }

}
