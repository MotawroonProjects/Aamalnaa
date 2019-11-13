package com.creative.share.apps.aamalnaa.activities_fragments.activity_edit_profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aamalnaa.adapters.CityAdapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityEditProfileBinding;
import com.creative.share.apps.aamalnaa.databinding.DialogSelectImageBinding;
import com.creative.share.apps.aamalnaa.databinding.FragmentSignUpBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.EditprofileModel;
import com.creative.share.apps.aamalnaa.models.SignUpModel;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_Profile_Activity extends AppCompatActivity implements Listeners.EditprofileListener, Listeners.BackListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private String current_language;
    private ActivityEditProfileBinding binding;
    private CountryPicker countryPicker;
    private EditprofileModel editprofileModel;
    private Preferences preferences;
    private CityAdapter adapter;
    private List<Cities_Model.Data> dataList;
    private UserModel userModel;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        initView();
        if (userModel != null) {
            updatedata(userModel);
        }
    }

    private void updatedata(UserModel userModel) {
        this.userModel = userModel;
        preferences.create_update_userdata(this, userModel);
        editprofileModel.setCity_id(this.userModel.getUser().getCity_id() + "");
        editprofileModel.setName(this.userModel.getUser().getName());
        editprofileModel.setPhone(this.userModel.getUser().getMobile());
        editprofileModel.setEmail(this.userModel.getUser().getEmail());
        binding.setUserModel(userModel);
        binding.setViewModel(editprofileModel);
    }

    private String city_id = "";

    private void initView() {
        dataList = new ArrayList<>();
        editprofileModel = new EditprofileModel();
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        binding.setViewModel(editprofileModel);
        binding.setBackListener(this);
        binding.setEditprofilelistner(this);
        binding.setShowDialogListener(this);
        binding.setUserModel(userModel);
        createCountryDialog();
        adapter = new CityAdapter(dataList, this);
        binding.spinnerCity.setAdapter(adapter);
        binding.image.setOnClickListener(view -> CreateImageAlertDialog());


        getCities();

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";
                    editprofileModel.setCity_id(city_id);
                    binding.setViewModel(editprofileModel);
                } else {
                    city_id = String.valueOf(dataList.get(i).getId());
                    editprofileModel.setCity_id(city_id);
                    binding.setViewModel(editprofileModel);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateCityAdapter(Cities_Model body) {

        dataList.add(new Cities_Model.Data("إختر"));
        if (body.getData() != null) {
            dataList.addAll(body.getData());
            adapter.notifyDataSetChanged();
            if (userModel != null) {
                for (int i = 1; i < dataList.size(); i++) {
                    if (dataList.get(i).getId() == userModel.getUser().getCity_id()) {
                        binding.spinnerCity.setSelection(i);
                    }
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
                                    Toast.makeText(Edit_Profile_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(Edit_Profile_Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(Edit_Profile_Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Edit_Profile_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }


    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(this)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM() != null) {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        } else if (telephonyManager != null && countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        } else if (countryPicker.getCountryByLocale(Locale.getDefault()) != null) {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        } else {
            String code = "+966";
            // binding.tvPhoneCode.setText(code);
            //  editprofileModel.setPhone_code(code.replace("+","00"));

        }

    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(this);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country) {
        // binding.tvPhoneCode.setText(country.getDialCode());
        // editprofileModel.setPhone_code(country.getDialCode().replace("+","00"));

    }

    @Override
    public void Editprofile(String name, String phone, String email) {
        if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }
        editprofileModel = new EditprofileModel(name, city_id, phone, email);
        binding.setViewModel(editprofileModel);
        if (editprofileModel.isDataValid(this)) {
            signUp(editprofileModel);
        }
    }

    private void signUp(EditprofileModel editprofileModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .editprofile(editprofileModel.getName(), editprofileModel.getPhone(), editprofileModel.getEmail(), editprofileModel.getCity_id(), userModel.getUser().getId())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(Edit_Profile_Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                                preferences.create_update_userdata(Edit_Profile_Activity.this, response.body());
                                finish();

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(Edit_Profile_Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(Edit_Profile_Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Edit_Profile_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    @Override
    public void back() {
        finish();
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
        if (ContextCompat.checkSelfPermission(Edit_Profile_Activity.this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Edit_Profile_Activity.this, new String[]{camera_permission, write_permission}, IMG_REQ2);
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
                Toast.makeText(Edit_Profile_Activity.this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(Edit_Profile_Activity.this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(Edit_Profile_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(Edit_Profile_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imgUri1 = getUriFromBitmap(bitmap);
            editImageProfile(userModel.getUser().getId() + "", imgUri1.toString());


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            imgUri1 = data.getData();
            editImageProfile(userModel.getUser().getId() + "", imgUri1.toString());


        }

    }

    private void editImageProfile(String user_id, String image) {
        ProgressDialog dialog = Common.createProgressDialog(this, getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody id_part = Common.getRequestBodyText(String.valueOf(user_id));

        MultipartBody.Part image_part = Common.getMultiPart(this, Uri.parse(image), "avatar");

        Api.getService(Tags.base_url)
                .editUserImage(id_part, image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            //listener.onSuccess(response.body());

                            Toast.makeText(Edit_Profile_Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            updatedata(response.body());

                        } else {
                            Log.e("codeimage", response.code() + "_");
                            try {
                                Toast.makeText(Edit_Profile_Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("respons", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // listener.onFailed(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(Edit_Profile_Activity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(Edit_Profile_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(Edit_Profile_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

}
