package com.maeultalk.gongneunglife.uploadImage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadContentInterface {
    @Multipart
    @POST("upload_content.php")
    Call<UploadObject> uploadContent(@Part("place_code") RequestBody place_code, @Part("user") RequestBody user, @Part("content") RequestBody content, @Part("collect") RequestBody collect, @Part("priority") RequestBody priority);

    @Multipart
    @POST("upload_content_1img.php")
    Call<UploadObject> uploadContent(@Part("place_code") RequestBody place_code, @Part("user") RequestBody user, @Part("content") RequestBody content, @Part("collect") RequestBody collect, @Part("priority") RequestBody priority, @Part("imageName") RequestBody imageName, @Part MultipartBody.Part image);

    @Multipart
    @POST("upload_content_2img.php")
    Call<UploadObject> uploadContent(@Part("place_code") RequestBody place_code, @Part("user") RequestBody user, @Part("content") RequestBody content, @Part("collect") RequestBody collect, @Part("priority") RequestBody priority, @Part("imageName") RequestBody imageName, @Part("imageName2") RequestBody imageName2, @Part MultipartBody.Part image, @Part MultipartBody.Part image2);

    @Multipart
    @POST("upload_content_3img.php")
    Call<UploadObject> uploadContent(@Part("place_code") RequestBody place_code, @Part("user") RequestBody user, @Part("content") RequestBody content, @Part("collect") RequestBody collect, @Part("priority") RequestBody priority, @Part("imageName") RequestBody imageName, @Part("imageName2") RequestBody imageName2, @Part("imageName3") RequestBody imageName3, @Part MultipartBody.Part image, @Part MultipartBody.Part image2, @Part MultipartBody.Part image3);
}