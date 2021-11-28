package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDTO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("messeger")
    @Expose
    private String messeger;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMesseger() {
        return messeger;
    }

    public void setMesseger(String messeger) {
        this.messeger = messeger;
    }
}
