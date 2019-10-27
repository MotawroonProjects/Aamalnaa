package com.creative.share.apps.aamalnaa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.bank_activity.BanksActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_my_wallet.WalletActivity;
import com.creative.share.apps.aamalnaa.databinding.BankRowBinding;
import com.creative.share.apps.aamalnaa.models.BankDataModel;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyHolder> {

    private List<BankDataModel.BankModel> bankDataModelList;
    private Context context;
private BanksActivity banksActivity;
private WalletActivity walletActivity;
    public BankAdapter(List<BankDataModel.BankModel> bankDataModelList, Context context) {
        this.bankDataModelList = bankDataModelList;
        this.context = context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BankRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bank_row,parent,false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        BankDataModel.BankModel bankModel = bankDataModelList.get(position);
        holder.bankRowBinding.setBankModel(bankModel);
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(context instanceof  BanksActivity){
            banksActivity=(BanksActivity)context;
            banksActivity.gettoaddbalance();
        }
        else if(context instanceof  WalletActivity){
            walletActivity=(WalletActivity)context;
            walletActivity.gotoaddbalance();
        }
    }
});

    }

    @Override
    public int getItemCount() {
        return bankDataModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private BankRowBinding bankRowBinding;

        public MyHolder(BankRowBinding bankRowBinding) {
            super(bankRowBinding.getRoot());
            this.bankRowBinding = bankRowBinding;



        }


    }
}
