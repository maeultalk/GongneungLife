package com.maeultalk.gongneunglife.uploadImage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadImageInterface {
    @Multipart
    @POST("img_upload_test2.php")
    Call<UploadObject> uploadFile(@Part MultipartBody.Part file);
//    Call<UploadObject> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);
}