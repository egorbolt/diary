package ru.nsuorg.shiftorg.entity_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterInfo {
    @SerializedName("status")
    @Expose
    private int status;


    public RegisterInfo(int status) {
        this.status = status;
    }
}
