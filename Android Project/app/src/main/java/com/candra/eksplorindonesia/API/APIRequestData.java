package com.candra.eksplorindonesia.API;

import com.candra.eksplorindonesia.Model.ModelAllResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIRequestData
{

    // Retrieve ALl Data
    // User
    @GET("user/retrieve_user.php")
    Call<ModelAllResponse> ardRetrieveDataUser();
    // Kuliner
    @GET("kuliner/retrieve_kuliner.php")
    Call<ModelAllResponse> ardRetrieveDataKuliner();
    // Wisata
    @GET("wisata/retrieve_wisata.php")
    Call<ModelAllResponse> ardRetrieveDataWisata();

    // Create ALl Data
    // User
    @Multipart
    @POST("user/create_user.php")
    Call<ModelAllResponse> ardCreateDataUser(
            @Part("fullname") RequestBody fullname,
            @Part("email") RequestBody email,
            @Part("role") RequestBody role,
            @Part("phone") RequestBody phone,
            @Part MultipartBody.Part foto,
            @Part("password") RequestBody password
    );

    // Kuliner
    @Multipart
    @POST("kuliner/create_kuliner.php")
    Call<ModelAllResponse> ardCreateDataKuliner(
            @Part("nama_kuliner") RequestBody nama_kuliner,
            @Part("asal_kuliner") RequestBody asal_kuliner,
            @Part MultipartBody.Part foto_kuliner,
            @Part("deskripsi_kuliner") RequestBody deskripsi_kuliner
    );
    // Wisata
    @Multipart
    @POST("wisata/create_wisata.php")
    Call<ModelAllResponse> ardCreateDataWisata(
            @Part("nama_wisata") RequestBody nama_wisata,
            @Part("lokasi_wisata") RequestBody lokasi_wisata,
            @Part("maps_wisata") RequestBody maps_wisata,
            @Part MultipartBody.Part foto_wisata,
            @Part("deskripsi_wisata") RequestBody deskripsi_wisata
    );


    // Update All Data
    @Multipart
    @POST("user/update_user.php")
    Call<ModelAllResponse> ardUpdateDataUser(
            @Part("id_user") RequestBody id_user,
            @Part("fullname") RequestBody fulname,
            @Part("email") RequestBody email,
            @Part("role") RequestBody role,
            @Part("phone") RequestBody phone,
            @Part MultipartBody.Part foto,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("user/update_tanpa_foto_user.php")
    Call<ModelAllResponse> ardUpdateDataUserTanpaFoto(
            @Part("id_user") RequestBody id_user,
            @Part("fullname") RequestBody fulname,
            @Part("email") RequestBody email,
            @Part("role") RequestBody role,
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("kuliner/update_kuliner.php")
    Call<ModelAllResponse> ardUpdateDataKuliner(
            @Part("id_kuliner") RequestBody id_kuliner,
            @Part("nama_kuliner") RequestBody nama_kuliner,
            @Part("asal_kuliner") RequestBody asal_kuliner,
            @Part MultipartBody.Part foto_kuliner,
            @Part("deskripsi_kuliner") RequestBody deskripsi_kuliner
    );

    @Multipart
    @POST("kuliner/update_tanpa_foto_kuliner.php")
    Call<ModelAllResponse> ardUpdateDataKulinerTanpaFoto(
            @Part("id_kuliner") RequestBody id_kuliner,
            @Part("nama_kuliner") RequestBody nama_kuliner,
            @Part("asal_kuliner") RequestBody asal_kuliner,
            @Part("deskripsi_kuliner") RequestBody deskripsi_kuliner
    );

    @Multipart
    @POST("wisata/update_wisata.php")
    Call<ModelAllResponse> ardUpdateDataWisata(
            @Part("id_wisata") RequestBody id_wisata,
            @Part("nama_wisata") RequestBody nama_wisata,
            @Part("lokasi_wisata") RequestBody lokasi_wisata,
            @Part("maps_wisata") RequestBody maps_wisata,
            @Part MultipartBody.Part foto_wisata,
            @Part("deskripsi_wisata") RequestBody deskripsi_wisata
    );

    @Multipart
    @POST("wisata/update_tanpa_foto_wisata.php")
    Call<ModelAllResponse> ardUpdateDataWisataTanpaFoto(
            @Part("id_wisata") RequestBody id_wisata,
            @Part("nama_wisata") RequestBody nama_wisata,
            @Part("lokasi_wisata") RequestBody lokasi_wisata,
            @Part("maps_wisata") RequestBody maps_wisata,
            @Part("deskripsi_wisata") RequestBody deskripsi_wisata
    );

    // Delete All Data
    // User
    @FormUrlEncoded
    @POST("user/delete_user.php")
    Call<ModelAllResponse> ardDeleteDataUser(
            @Field("id_user") String id_user
    );
    // Kuliner
    @FormUrlEncoded
    @POST("kuliner/delete_kuliner.php")
    Call<ModelAllResponse> ardDeleteDataKuliner(
            @Field("id_kuliner") String id_kuliner
    );

    // Wisata
    @FormUrlEncoded
    @POST("wisata/delete_wisata.php")
    Call<ModelAllResponse> ardDeleteDataWisata(
            @Field("id_wisata") String id_wisata
    );

    // Login User
    @FormUrlEncoded
    @POST("user/login_user.php")
    Call<ModelAllResponse> ardLoginUser(
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("wisata/scan_wisata.php")
    Call<ModelAllResponse> ardScanWisata(
            @Field("nama_wisata") String nama_wisata
    );

    @FormUrlEncoded
    @POST("kuliner/scan_kuliner.php")
    Call<ModelAllResponse> ardScanKuliner(
            @Field("nama_kuliner") String nama_kuliner
    );

    @FormUrlEncoded
    @POST("user/scan_user.php")
    Call<ModelAllResponse> ardScanUser(
            @Field("fullname") String fullname
    );





}
