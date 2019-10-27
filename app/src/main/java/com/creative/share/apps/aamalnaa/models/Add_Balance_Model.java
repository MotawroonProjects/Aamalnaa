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


public class Add_Balance_Model extends BaseObservable implements Serializable {
    private String bank_id;
    private String name;
    private String number;
    private String amount;




    public ObservableField<String> name_error = new ObservableField<>();
    public ObservableField<String> number_error = new ObservableField<>();
    public ObservableField<String> amount_error = new ObservableField<>();


    public boolean isDataValidStep1(Context context)
    {
        Log.e("llll",name+" "+number+amount);
        if (!bank_id.equals("")&&

                !TextUtils.isEmpty(name)&&
                !TextUtils.isEmpty(number)&&
                !TextUtils.isEmpty(amount)



        )
        {
            name_error.set(null);
            number_error.set(null);
            amount_error.set(null);


            return true;
        }else
            {
                if (bank_id==null||bank_id.equals(""))
                {
                    Toast.makeText(context, R.string.choose_bank_acount, Toast.LENGTH_SHORT).show();
                }



                if (name==null||TextUtils.isEmpty(name))
                {
                    name_error.set(context.getString(R.string.field_req));
                }else
                    {
                        name_error.set(null);

                    }

                if (TextUtils.isEmpty(amount))
                {
                    amount_error.set(context.getString(R.string.field_req));
                }else
                {
                    amount_error.set(null);

                }

                if (TextUtils.isEmpty(number))
                {
                    number_error.set(context.getString(R.string.field_req));
                }else
                {
                    number_error.set(null);

                }

                return false;
            }
    }






    @Bindable
    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
        notifyPropertyChanged(BR.bank_id);

    }







    @Bindable
    public String getName (){
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }
    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        notifyPropertyChanged(BR.number);

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
