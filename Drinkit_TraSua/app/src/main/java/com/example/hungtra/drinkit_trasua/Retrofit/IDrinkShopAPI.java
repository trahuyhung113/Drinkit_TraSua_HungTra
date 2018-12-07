package com.example.hungtra.drinkit_trasua.Retrofit;



import com.example.hungtra.drinkit_trasua.Model.Banner;
import com.example.hungtra.drinkit_trasua.Model.Category;
import com.example.hungtra.drinkit_trasua.Model.CheckUserResponse;
import com.example.hungtra.drinkit_trasua.Model.Drink;
import com.example.hungtra.drinkit_trasua.Model.Order;
import com.example.hungtra.drinkit_trasua.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IDrinkShopAPI {

    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("address") String address,
                               @Field("birthdate") String birthdate);

    @FormUrlEncoded
    @POST("getdrink.php")
    io.reactivex.Observable<List<Drink>> getDrink(@Field("menuid") String menuID);

    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("phone") String phone);


    @GET("getbanner.php")
    io.reactivex.Observable<List<Banner>> getBanners();

    @GET("getmenu.php")
    io.reactivex.Observable<List<Category>> getMenu();

    @Multipart
    @POST("upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part phone, @Part MultipartBody.Part file);

    @GET("getalldrinks.php")
    io.reactivex.Observable<List<Drink>> getAllDrinks();

    @FormUrlEncoded
    @POST("submitorder.php")
    Call<String> submitOrder(@Field("price") float oderPrice,
                             @Field("orderDetail") String orderDetail,
                             @Field("comment") String comment,
                             @Field("address") String address,
                             @Field("phone") String phone,
                             @Field("paymentMethod") String paymentMethod);

    @FormUrlEncoded
    @POST("braintree/checkout.php")
    Call<String> payment(@Field("nonce") String nonce,
                         @Field("amount") String amount);

    @FormUrlEncoded
    @POST("getorder.php")
    io.reactivex.Observable<List<Order>> getOrder(@Field("userPhone") String userPhone,
                                                  @Field("status") String status);

//    @FormUrlEncoded
//    @POST("updatetoken.php")
//    Call<String> updateToken(@Field("phone") String phone,
//                         @Field("token") String token);
}
