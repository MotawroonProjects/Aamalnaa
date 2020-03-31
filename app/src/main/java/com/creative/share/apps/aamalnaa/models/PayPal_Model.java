package com.creative.share.apps.aamalnaa.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.aamalnaa.BR;
import com.creative.share.apps.aamalnaa.R;

import java.io.Serializable;


public class PayPal_Model extends BaseObservable implements Serializable {

    private String amount;





    public ObservableField<String> amount_error = new ObservableField<>();


    public boolean isDataValidStep1(Context context)
    {

        if (
                !TextUtils.isEmpty(amount)



        )
        {

            amount_error.set(null);


            return true;
        }else
            {


                if (TextUtils.isEmpty(amount))
                {
                    amount_error.set(context.getString(R.string.field_req));
                }else
                {
                    amount_error.set(null);

                }



                return false;
            }
    }







    @Bindable
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amount);

    }



}
