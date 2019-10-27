package com.creative.share.apps.aamalnaa.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.aamalnaa.BR;
import com.creative.share.apps.aamalnaa.R;

import java.io.Serializable;
import java.util.List;


public class Order_Upload_Model extends BaseObservable implements Serializable {
    private String category_id;
    private String subcategory_id;
    private String city_id;
    private String title;
    private String longitude;
    private String latitude;
    private String address;
    private String details;
    private String price;



    public ObservableField<String> address_error = new ObservableField<>();
    public ObservableField<String> title_error = new ObservableField<>();
    public ObservableField<String> detials_error = new ObservableField<>();


    public boolean isDataValidStep1(Context context)
    {
        if (!category_id.equals("")&&
                !subcategory_id.equals("")&&
                !city_id.equals("")&&
                !TextUtils.isEmpty(address)&&
                !TextUtils.isEmpty(title)&&
                !TextUtils.isEmpty(details)



        )
        {
            address_error.set(null);
            title_error.set(null);
            detials_error.set(null);


            return true;
        }else
            {
                if (category_id==null||category_id.equals(""))
                {
                    Toast.makeText(context, R.string.Choose_Catogry, Toast.LENGTH_SHORT).show();
                }

                if (subcategory_id==null||subcategory_id.equals(""))
                {
                    Toast.makeText(context, R.string.Choose_Sub_Category, Toast.LENGTH_SHORT).show();
                }

                if (city_id==null||city_id.equals(""))
                {
                    Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();
                }

                if (address==null||TextUtils.isEmpty(address))
                {
                    address_error.set(context.getString(R.string.field_req));
                }else
                    {
                        address_error.set(null);

                    }

                if (TextUtils.isEmpty(title))
                {
                    title_error.set(context.getString(R.string.field_req));
                }else
                {
                    title_error.set(null);

                }

                if (TextUtils.isEmpty(details))
                {
                    detials_error.set(context.getString(R.string.field_req));
                }else
                {
                    detials_error.set(null);

                }

                return false;
            }
    }






    @Bindable
    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
        notifyPropertyChanged(BR.category_id);

    }
    @Bindable
    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
        notifyPropertyChanged(BR.subcategory_id);
    }

    @Bindable
    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
        notifyPropertyChanged(BR.city_id);

    }






    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);

    }
    @Bindable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.latitude);

    }

    @Bindable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }



}
