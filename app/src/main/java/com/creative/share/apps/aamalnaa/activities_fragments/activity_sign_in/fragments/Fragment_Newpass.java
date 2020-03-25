package com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aamalnaa.databinding.FragmentNewpasswordBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.models.PasswordModel;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Newpass extends Fragment implements Listeners.PasswordListner {
    private FragmentNewpasswordBinding binding;
    private SignInActivity activity;
    private HomeActivity homeActivity;
    private String lang;
    private Preferences preferences;
    private PasswordModel passwordModel;
    private UserModel userModel;
    private int type;
    private static final String TAG = "DATA";
    private static final String TAG2 = "TYPE";

    public static Fragment_Newpass newInstance(UserModel userModel, int type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        bundle.putInt(TAG2, type);
        Fragment_Newpass fragment_newpass = new Fragment_Newpass();
        fragment_newpass.setArguments(bundle);

        return fragment_newpass;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_newpassword, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable(TAG);
            type = bundle.getInt(TAG2);
        }
        passwordModel = new PasswordModel();
activity=(SignInActivity)getActivity();
        preferences = Preferences.getInstance();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setPassModel(passwordModel);
        binding.setPassListener(this);


    }


    @Override
    public void checkDatapass(String pass) {
        if (type == 1) {
            if (passwordModel.isDataValid(activity)) {
                String cpass=binding.edtcPassword.getText().toString();
                String code=binding.edtCode.getText().toString();
                if(!TextUtils.isEmpty(cpass)&&pass.equals(cpass)&&!TextUtils.isEmpty(code)){
                    login(pass,code);

                }
                else {
                    if(TextUtils.isEmpty(cpass)){
                        binding.edtcPassword.setError(activity.getResources().getString(R.string.field_req));
                    }
                    else if(!cpass.equals(pass)){
                        binding.edtcPassword.setError(activity.getResources().getString(R.string.confirm_musr_equal));

                    }
                    if(TextUtils.isEmpty(code)){
                        binding.edtCode.setError(activity.getResources().getString(R.string.field_req));

                    }
                }
            }
        } else if (type == 2) {
            if (passwordModel.isDataValid(homeActivity)) {
                //change_pass(passwordModel.getPassword());
            }
        }

    }

    private void login(String password, String code) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .changpass(userModel.getUser().getMobile(),code, password)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                preferences.create_update_userdata(activity, response.body());
                           //     preferences.createSession(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                  //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    //  Log.e("error",response.code()+"_"+response.errorBody()+response.message()+password+phone+phone_code);

                                } else if (response.code() == 404) {
                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                //    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }
    }

//    private void change_pass(String pass) {
//        ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        try {
//
//            Api.getService(Tags.base_url)
//                    .edit_pass(userModel.getId() + "", pass)
//                    .enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                            dialog.dismiss();
//                            if (response.isSuccessful() && response.body() != null) {
//                                userModel = response.body();
//                                preferences.create_update_userData(homeActivity, userModel);
//                                Toast.makeText(homeActivity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
//
//                                homeActivity.refreshprofile(userModel);
//
//
//                            } else {
//                                try {
//
//                                    Log.e("error", response.code() + "_" + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                if (response.code() == 422) {
//                                    Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                } else if (response.code() == 403) {
//                                    Toast.makeText(homeActivity, R.string.user_not_active, Toast.LENGTH_SHORT).show();
//
//                                } else if (response.code() == 404) {
//                                    Toast.makeText(homeActivity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();
//
//                                } else if (response.code() == 405) {
//                                    Toast.makeText(homeActivity, R.string.not_active_phone, Toast.LENGTH_SHORT).show();
//
//                                } else if (response.code() == 500) {
//                                    Toast.makeText(homeActivity, "Server Error", Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage() != null) {
//                                    Log.e("error", t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                        Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(homeActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            dialog.dismiss();
//
//        }
//    }

}
