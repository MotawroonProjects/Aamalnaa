package com.creative.share.apps.aamalnaa.services;




import com.creative.share.apps.aamalnaa.models.Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.App_Data_Model;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.PlaceGeocodeData;
import com.creative.share.apps.aamalnaa.models.PlaceMapDetailsData;
import com.creative.share.apps.aamalnaa.models.Service_Model;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.Slider_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {
    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);
    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );
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
    @POST("api/fillter")
    Call<Adversiment_Model> getAds(
            @Field("page")int page,
            @Field("city_id")String city_id,
            @Field("lat")String lat,
            @Field("lng")String lng,
            @Field("is_new")int is_new

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
    @FormUrlEncoded
    @POST("api/single_ad")
    Call<Single_Adversiment_Model> getSingleAds(

            @Field("ad_id")String ad_id
    );
    @GET("api/conditions")
    Call<App_Data_Model> getterms();

    @GET("api/advantages")
    Call<App_Data_Model> getabout();
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
    @FormUrlEncoded
    @POST("api/my_account")
    Call<UserModel> getmyprofile(
            @Field("user_id")String user_id);
    @GET("api/servicesPrice")
    Call<Service_Model> getservice();
    @Multipart
    @POST("api/add_ad")
    Call<ResponseBody> Sendorder
            (@Part("user_id") RequestBody user_id,
             @Part("category_id") RequestBody category_id,
             @Part("subcategory_id") RequestBody subcategory_id,
             @Part("city_id") RequestBody city_id,
             @Part("ads_type") RequestBody ads_type,
             @Part("title") RequestBody title,
             @Part("details") RequestBody details,
             @Part("price") RequestBody price,
             @Part("address") RequestBody address,
             @Part("lng") RequestBody lng,
             @Part("lat") RequestBody lat,
             @Part("views_num") RequestBody views_num,
             @Part("is_Special") RequestBody is_Special,
             @Part("is_Install") RequestBody is_Install,
             @Part("commented") RequestBody commented,
             @Part List<MultipartBody.Part> partimageInsideList

//
            );

}


