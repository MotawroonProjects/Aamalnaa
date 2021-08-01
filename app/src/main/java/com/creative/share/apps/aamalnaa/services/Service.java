package com.creative.share.apps.aamalnaa.services;


import com.creative.share.apps.aamalnaa.models.Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.AllMessageModel;
import com.creative.share.apps.aamalnaa.models.App_Data_Model;
import com.creative.share.apps.aamalnaa.models.BankDataModel;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;
import com.creative.share.apps.aamalnaa.models.Cities_Model;
import com.creative.share.apps.aamalnaa.models.MessageModel;
import com.creative.share.apps.aamalnaa.models.NotificationCount;
import com.creative.share.apps.aamalnaa.models.NotificationDataModel;
import com.creative.share.apps.aamalnaa.models.PlaceGeocodeData;
import com.creative.share.apps.aamalnaa.models.PlaceMapDetailsData;
import com.creative.share.apps.aamalnaa.models.Profit_Model;
import com.creative.share.apps.aamalnaa.models.Service_Model;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.Slider_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.models.UserRoomModelData;
import com.creative.share.apps.aamalnaa.models.Wallet_Model;

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
    @POST("api/user_profile_update")
    Call<UserModel> editprofile(@Field("name") String name,
                                @Field("mobile") String mobile,
                                @Field("email") String email,
                                @Field("city_id") String city_id,
                                @Field("id") int id,
                                @Field("about") String about
    );

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<ResponseBody> updateToken(
            @Field("user_id_fk") int user_id_fk,
            @Field("phone_token") String phone_token,
            @Field("software_type") String software_type
    );

    @FormUrlEncoded
    @POST("api/firebase-tokens-delete")
    Call<ResponseBody> delteToken(
            @Field("user_id_fk") int user_id_fk,
            @Field("phone_token") String phone_token
    );

    @Multipart
    @POST("api/user_image")
    Call<UserModel> editUserImage(@Part("id") RequestBody id,
                                  @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/confirm-code")
    Call<UserModel> confirmCode(@Field("mobile") String mobile,
                                @Field("confirm_code") String confirm_code
    );

    @FormUrlEncoded
    @POST("api/rest-pass")
    Call<UserModel> changpass(
            @Field("mobile") String mobile,
            @Field("confirm_code") String confirm_code,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("api/resend-code")
    Call<ResponseBody> resendCode(@Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> Logout(@Field("id") String id

    );

    @GET("api/is_install")
    Call<Slider_Model> get_slider();

    @GET("api/all_categories")
    Call<Catogries_Model> getDepartment(


    );

    @FormUrlEncoded
    @POST("api/subs")
    Call<Adversiment_Model> getMAINAds(
            @Field("page") int page,

            @Field("subcategory_id") String category_id
    );

    @FormUrlEncoded
    @POST("api/my_notification")
    Call<NotificationDataModel> getnotification(
            @Field("page") int page,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/my_money_report")
    Call<NotificationDataModel> gettransactions(
            @Field("page") int page,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/search")
    Call<Adversiment_Model> getAds(

            @Field("key_word") String key_word
    );

    @FormUrlEncoded
    @POST("api/fillter")
    Call<Adversiment_Model> getAds(
            @Field("city_id") String city_id,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("is_new") int is_new,
            @Field("title") String title
    );

    @FormUrlEncoded
    @POST("api/favorite_ads")
    Call<Adversiment_Model> getFAds(
            @Field("page") int page,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/my_message")
    Call<AllMessageModel> getMessge(
            @Field("receiver_id") String receiver_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/my_ads")
    Call<Adversiment_Model> getMyAds(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/single_ad")
    Call<Single_Adversiment_Model> getSingleAds(

            @Field("ad_id") String ad_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/is_like")
    Call<ResponseBody> Like(

            @Field("ad_id") String ad_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/add_comment")
    Call<ResponseBody> comment(

            @Field("ad_id") String ad_id,
            @Field("user_id") String user_id,
            @Field("comment") String comment
    );

    @FormUrlEncoded
    @POST("api/reporte")
    Call<ResponseBody> Report(

            @Field("reported_id") String reported_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/changepr")
    Call<UserModel> Transform(

            @Field("user_id") String user_id,
            @Field("points") double points

    );

    @FormUrlEncoded
    @POST("api/delete_ad")
    Call<ResponseBody> DelteAds(

            @Field("ad_id") String ad_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/update_ad_time")
    Call<ResponseBody> updateAdstime(

            @Field("ad_id") String ad_id
    );

    @FormUrlEncoded
    @POST("api/delete_nots")
    Call<ResponseBody> Deltenotes(

            @Field("not_id") String not_id
    );

    @FormUrlEncoded
    @POST("api/my_pre_customer")
    Call<ResponseBody> precustomer(

            @Field("follower_id") String follower_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/delete_follower")
    Call<ResponseBody> deltecustomer(

            @Field("follower_id") String follower_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/delete_pre")
    Call<ResponseBody> delteworks(

            @Field("follower_id") String follower_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/follow")
    Call<ResponseBody> becustomer(

            @Field("followed_id") String followed_id,
            @Field("follower_id") String follower_id
    );

    @GET("api/conditions")
    Call<App_Data_Model> getterms();

    @GET("api/about_us")
    Call<App_Data_Model> getabout();

    @GET("api/advantages")
    Call<App_Data_Model> getadvantages();

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
            @Field("user_id") String user_id,
            @Field("register_id") String register_id

    );


    @FormUrlEncoded
    @POST("api/my_wallet")
    Call<Wallet_Model> getmywallet(
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("api/my_profit")
    Call<Profit_Model> getmyprofit(
            @Field("user_id") String user_id);

    @GET("api/all_banks")
    Call<BankDataModel> getBanks();

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
             @Part("total") RequestBody total,
             @Part List<MultipartBody.Part> partimageInsideList

//
            );

    @FormUrlEncoded
    @POST("api/add_ad")
    Call<ResponseBody> sendorderwithoutimage
            (
                    @Field("user_id") String user_id,
                    @Field("category_id") String category_id,
                    @Field("subcategory_id") String subcategory_id,
                    @Field("city_id") String city_id,
                    @Field("ads_type") String ads_type,
                    @Field("title") String title,
                    @Field("details") String details,
                    @Field("price") String price,
                    @Field("address") String address,
                    @Field("lng") String lng,
                    @Field("lat") String lat,
                    @Field("views_num") String views_num,
                    @Field("is_Special") String is_Special,
                    @Field("is_Install") String is_Install,
                    @Field("commented") String commented,
                    @Field("total") String total


//
            );

    @Multipart
    @POST("api/update_ad")
    Call<ResponseBody> Updateorder
            (
                    @Part("ad_id") RequestBody ad_id,

                    @Part("user_id") RequestBody user_id,
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
                    @Part("total") RequestBody total,
                    @Part List<MultipartBody.Part> partimageInsideList

//
            );

    @FormUrlEncoded
    @POST("api/update_ad")
    Call<ResponseBody> Updateorder
            (
                    @Field("ad_id") String ad_id,
                    @Field("user_id") String user_id,
                    @Field("category_id") String category_id,
                    @Field("subcategory_id") String subcategory_id,
                    @Field("city_id") String city_id,
                    @Field("ads_type") String ads_type,
                    @Field("title") String title,
                    @Field("details") String details,
                    @Field("price") String price,
                    @Field("address") String address,
                    @Field("lng") String lng,
                    @Field("lat") String lat,
                    @Field("views_num") String views_num,
                    @Field("is_Special") String is_Special,
                    @Field("is_Install") String is_Install,
                    @Field("commented") String commented,
                    @Field("total") String total

//
            );

    @Multipart
    @POST("api/payment")
    Call<ResponseBody> Payment
            (@Part("user_id") RequestBody user_id,
             @Part("bank_id") RequestBody bank_id,
             @Part("name") RequestBody name,
             @Part("number") RequestBody number,
             @Part("amount") RequestBody amount,

             @Part MultipartBody.Part partimage

//
            );

    @FormUrlEncoded
    @POST("api/payWithpaypalMobile")
    Call<ResponseBody> Payment
            (
                    @Field("amount") String amount,
                    @Field("user_id") String user_id

//
            );

    @FormUrlEncoded
    @POST("api/allRoms")
    Call<UserRoomModelData> getRooms(@Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/send_message")
    Call<MessageModel> sendmessagetext
            (
                    @Field("sender_id") String sender_id,
                    @Field("receiver_id") String receiver_id,
                    @Field("message") String message,
                    @Field("type") String type

//
            );

    @FormUrlEncoded
    @POST("api/send_message")
    Call<MessageModel> sendmessageaddress
            (
                    @Field("sender_id") String sender_id,
                    @Field("receiver_id") String receiver_id,
                    @Field("message") String message,
                    @Field("type") String type,
                    @Field("lat") double lat,
                    @Field("lang") double lang
//
            );

    @Multipart
    @POST("api/send_message")
    Call<MessageModel> sendmessagewithimage
            (
                    @Part("sender_id") RequestBody sender_id,

                    @Part("receiver_id") RequestBody receiver_id,
                    @Part("type") RequestBody type,
                    @Part MultipartBody.Part imagepart

//
            );

    @FormUrlEncoded
    @POST("api/delete_room")
    Call<ResponseBody> delteroom(

            @Field("room_id") String room_id
    );

    @FormUrlEncoded
    @POST("api/toggle_show_info")
    Call<ResponseBody> showinfo(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/user_rate")
    Call<ResponseBody> rateuser(
            @Field("user_id") String user_id,
            @Field("rated_id") String rated_id,
            @Field("liked") String liked,
            @Field("reason") String reason


    );

    @FormUrlEncoded
    @POST("api/unSeenNotifications")
    Call<NotificationCount> getUnreadNotificationCount(
            @Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("api/unSeenMessages")
    Call<NotificationCount> getUnreadMeaasgeCount(
            @Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("api/ad/delete")
    Call<ResponseBody> DeleteMyAd(@Field("ad_id") int ad_id,
                                  @Field("user_id") int user_id
    );
}