package com.app.hitechnews.Util;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InterfaceFileUpload {

    @Multipart
    @POST("FileAPI/UploadFiles")
    Call<String> UploadFile(@Part MultipartBody.Part file,
                                    @Part("fileName") RequestBody CustomerID
                                    );

}
