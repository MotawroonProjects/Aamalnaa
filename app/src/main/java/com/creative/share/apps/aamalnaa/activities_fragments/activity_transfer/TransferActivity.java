package com.creative.share.apps.aamalnaa.activities_fragments.activity_transfer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_contact.ContactActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.aamalnaa.adapters.CityAdapter;
import com.creative.share.apps.aamalnaa.adapters.Spinner_Category_Adapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityTransferBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.App_Data_Model;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.SignUpModel;
import com.creative.share.apps.aamalnaa.models.Transfer_Model;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferActivity extends AppCompatActivity implements Listeners.BackListener, Listeners.TransFerListener {
    private ActivityTransferBinding binding;
    private String lang;
    private Spinner_Category_Adapter adapter;
    private List<Catogries_Model.Data> dataList;
    private Transfer_Model transfer_model;
    private String cat_id;
private App_Data_Model app_data_model;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer);
        getDepartments();
        initView();
    }

    private void initView() {
        transfer_model = new Transfer_Model();
        app_data_model=new App_Data_Model();
        dataList = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setTransfelmodel(transfer_model);
        binding.setTransferListener(this);
        binding.setAppdatamodel(app_data_model);
        adapter = new Spinner_Category_Adapter(dataList, this);
        binding.spCategory.setAdapter(adapter);

        binding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    cat_id = "";
                    transfer_model.setCat_id(cat_id);
                    binding.setTransfelmodel(transfer_model);
                } else {
                    cat_id = String.valueOf(dataList.get(i).getId());
                    transfer_model.setCat_id(cat_id);
                    binding.setTransfelmodel(transfer_model);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateCatogryAdapter(Catogries_Model body) {

        dataList.add(new Catogries_Model.Data("إختر"));
        if (body.getData() != null) {
            dataList.addAll(body.getData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void back() {
        finish();
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

                            Toast.makeText(TransferActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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


                            Toast.makeText(TransferActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    @Override
    public void checkData(String amount) {

        transfer_model = new Transfer_Model(amount, cat_id);
        binding.setTransfelmodel(transfer_model);
        if (transfer_model.isDataValid(this)) {
            transfer(transfer_model);
        }
    }

    private void transfer(Transfer_Model transfer_model) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .transfer(transfer_model.getAmount(), transfer_model.getCat_id())
                    .enqueue(new Callback<App_Data_Model>() {
                        @Override
                        public void onResponse(Call<App_Data_Model> call, Response<App_Data_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getCommission() != 0.0 && response.body().getCommission() !=0.0) {

                                binding.setAppdatamodel(response.body());
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (Exception e) {
                                  //  e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(TransferActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(TransferActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<App_Data_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(TransferActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(TransferActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }
}
