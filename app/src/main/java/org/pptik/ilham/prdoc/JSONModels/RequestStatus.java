package org.pptik.ilham.prdoc.JSONModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan sebagai getter dan setter dari response JSON
 */

public class RequestStatus {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("RC")
    @Expose
    private String rC;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRC() {
        return rC;
    }

    public void setRC(String rC) {
        this.rC = rC;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}