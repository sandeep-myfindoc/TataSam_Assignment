package com.tatasam.test.model;

import com.google.gson.annotations.SerializedName;

public class CountryListResponse {
    @SerializedName("status-code")
    private String status_code;
    @SerializedName("total")
    private String total;
    @SerializedName("access")
    private String access;
    @SerializedName("offset")
    private String offset;
    @SerializedName("data")
    private Countries data;
    @SerializedName("limit")
    private String limit;
    @SerializedName("version")
    private String version;
    @SerializedName("status")
    private String status;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public Countries getData() {
        return data;
    }

    public void setData(Countries data) {
        this.data = data;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
