package com.app.hitechnews.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
