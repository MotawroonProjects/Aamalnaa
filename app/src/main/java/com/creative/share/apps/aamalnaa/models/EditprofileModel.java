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

public class EditprofileModel extends BaseObservable implements Serializable {

    private String name;
   // private String phone_code;
    private String phone;
    private String email;
    private String city_id;
    public ObservableField<String> error_name = new ObservableField<>();
   // public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();



    public EditprofileModel() {
        this.name = "";
     //   this.phone_code = "";
        this.phone="";
        this.email = "";
        this.city_id = "";
    }

    public EditprofileModel(String name, String city_id, String phone, String email) {
        setName(name);
        //setPhone_code(phone_code);
        setPhone(phone);
        setEmail(email);
        setCity_id(city_id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);


    }

  /*  @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }*/

    @Bindable

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);


    }



    @Bindable
    public String getCity_id() {
        return city_id;
    }



    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
    public boolean isDataValid(Context context)
    {
        if (//!TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone)&&
                !TextUtils.isEmpty(name)&&
                !TextUtils.isEmpty(email)&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                !TextUtils.isEmpty(city_id)
        )
        {
            error_name.set(null);
           // error_phone_code.set(null);
            error_phone.set(null);
            error_email.set(null);

            return true;
        }else
        {
            if (name.isEmpty())
            {
                error_name.set(context.getString(R.string.field_req));
            }else
            {
                error_name.set(null);
            }

            if (email.isEmpty())
            {
                error_email.set(context.getString(R.string.field_req));
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                error_email.set(context.getString(R.string.inv_email));
            }else
            {
                error_email.set(null);

            }

          /*  if (phone_code.isEmpty())
            {
                error_phone_code.set(context.getString(R.string.field_req));
            }else
            {
                error_phone_code.set(null);
            }*/

            if (phone.isEmpty())
            {
                error_phone.set(context.getString(R.string.field_req));
            }else
            {
                error_phone.set(null);
            }



            if (city_id.isEmpty())
            {
                Toast.makeText(context, context.getString(R.string.ch_city), Toast.LENGTH_SHORT).show();
            }



            return false;
        }
    }
}
