package com.creative.share.apps.aamalnaa.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.aamalnaa.BR;
import com.creative.share.apps.aamalnaa.R;

import java.io.Serializable;

public class SignUpModel extends BaseObservable implements Serializable {

    private String name;
    private String phone_code;
    private String phone;
    private String email;
    private String password;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();

    public ObservableField<String> error_password = new ObservableField<>();


    public SignUpModel() {
        this.name = "";
        this.phone_code = "";
        this.phone="";
        this.password="";
        this.email = "";
    }

    public SignUpModel(String name, String phone_code, String phone,String email, String password) {
        setName(name);
        notifyPropertyChanged(BR.name);
        setPhone_code(phone_code);
        //notifyPropertyChanged(BR.phone_code);
        setPhone(phone);
        //notifyPropertyChanged(BR.phone);
        setPassword(password);
        //notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);


    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }



    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone)&&
                password.length()>=6&&
                !TextUtils.isEmpty(name)&&
                !TextUtils.isEmpty(email)&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()

        )
        {
            error_name.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
            error_password.set(null);
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


            if (phone_code.isEmpty())
            {
                error_phone_code.set(context.getString(R.string.field_req));
            }else
            {
                error_phone_code.set(null);
            }

            if (phone.isEmpty())
            {
                error_phone.set(context.getString(R.string.field_req));
            }else
            {
                error_phone.set(null);
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

            if (password.isEmpty())
            {
                error_password.set(context.getString(R.string.field_req));
            }else if (password.length()<6)
            {
                error_password.set(context.getString(R.string.pass_short));
            }else
            {
                error_password.set(null);

            }




            return false;
        }
    }
}
