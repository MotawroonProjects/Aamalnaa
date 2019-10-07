package com.creative.share.apps.aamalnaa.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.aamalnaa.BR;
import com.creative.share.apps.aamalnaa.R;

import java.io.Serializable;

public class Transfer_Model extends BaseObservable implements Serializable {

    private String amount;

    private String cat_id;
    public ObservableField<String> error_amount = new ObservableField<>();



    public Transfer_Model() {
        this.amount = "";
        this.cat_id = "";

    }

    public Transfer_Model(String amount, String cat_id) {
        setAmount(amount);

        setCat_id(cat_id);
    }

    @Bindable
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amount);


    }





    @Bindable
    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
    public boolean isDataValid(Context context)
    {
        if (
                !TextUtils.isEmpty(amount)&&

                !TextUtils.isEmpty(cat_id)
        )
        {
            error_amount.set(null);


            return true;
        }else
        {
            if (amount.isEmpty())
            {
                error_amount.set(context.getString(R.string.field_req));
            }else
            {
                error_amount.set(null);
            }








            if (cat_id.isEmpty())
            {
                Toast.makeText(context, context.getString(R.string.Choose_Catogry), Toast.LENGTH_SHORT).show();
            }


            return false;
        }
    }
}
