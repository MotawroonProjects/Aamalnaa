package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.aamalnaa.databinding.SubCatogryRowBinding;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.R;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder> {

    private Context context;
    private List<Catogries_Model.Data.Subcategory> subCategoryModelList;
    private HomeActivity activity;
    private String lang;
private Fragment_Main fragment_main;
private Fragment fragment;
    public SubCategoryAdapter(Context context, List<Catogries_Model.Data.Subcategory> subCategoryModelList, Fragment fragment) {
        this.context = context;
        this.subCategoryModelList = subCategoryModelList;
        this.activity = (HomeActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.fragment=fragment;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SubCatogryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.sub_catogry_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

       holder.binding.setCatogrymodel(subCategoryModelList.get(position));
holder.binding.setLang(lang);
        holder.itemView.setOnClickListener(view -> {
            if(fragment instanceof Fragment_Main){
                fragment_main=(Fragment_Main)fragment;
                fragment_main.setcat_id(subCategoryModelList.get(position).getId()+"");

            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private SubCatogryRowBinding binding;

        public MyHolder(@NonNull SubCatogryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
