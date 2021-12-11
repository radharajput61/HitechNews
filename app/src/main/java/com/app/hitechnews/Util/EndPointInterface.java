package com.app.hitechnews.Util;

import com.app.hitechnews.Model.UploadResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EndPointInterface {
    @POST("Service/UploadPost")
    @FormUrlEncoded
    Call<UploadResponse> UplaodRequest(@Field("UserId") String UserId,
                                        @Field("City") String City,
                                        @Field("Location") String Location,
                                        @Field("Title") String Title,
                                        @Field("Description") String Description,
                                        @Field("PostRelto") String PostRelto,
                                        @Field("Videolink") String Videolink);


}
