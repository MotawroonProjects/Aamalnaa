package com.creative.share.apps.aamalnaa.services;




import com.creative.share.apps.aamalnaa.models.Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.App_Data_Model;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.Slider_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Service {

    @GET("api/cities")
    Call<Cities_Model> getCity();
    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> sign_up(@Field("name") String name,
                            @Field("mobile") String mobile,

                            @Field("email") String email,
                            @Field("city_id") String city_id,
                            @Field("password") String password,
                            @Field("accepted") int accepted
    );

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(
                          @Field("mobile") String mobile,
                          @Field("password") String password
    );
    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> Logout(@Field("id") String id

    );
    @GET("api/all_slider")
    Call<Slider_Model> get_slider();
    @GET("api/all_categories")
    Call<Catogries_Model> getDepartment(



    );
    @FormUrlEncoded
    @POST("api/fillter_by_cat")
    Call<Adversiment_Model> getAds(
            @Field("page")int page,
            @Field("category_id")String category_id
    );
    @FormUrlEncoded
    @POST("api/favorite_ads")
    Call<Adversiment_Model> getFAds(
            @Field("page")int page,
            @Field("user_id")String user_id
    );
    @FormUrlEncoded
    @POST("api/my_ads")
    Call<Adversiment_Model> getMyAds(
            @Field("page")int page,
            @Field("user_id")String user_id
    );

    @GET("api/conditions")
    Call<App_Data_Model> getterms();
    @FormUrlEncoded
    @POST("api/contact_us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("title") String title,
                                   @Field("content") String content
    );
    @FormUrlEncoded
    @POST("api/commission")
    Call<App_Data_Model> transfer(
            @Field("amount") String amount,
            @Field("category_id") String category_id);
}


