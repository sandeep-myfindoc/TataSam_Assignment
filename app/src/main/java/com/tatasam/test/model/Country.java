package com.tatasam.test.model;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String countryCode,countryName,countryRegion;
    private boolean isLike;
    public boolean isLike() {
        return isLike;
    }
    public void setLike(boolean like) {
        isLike = like;
    }

    public Country() {
    }

    public Country(String countryCode, String countryName, String countryRegion) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.countryRegion = countryRegion;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String getCountryRegion() {
        return countryRegion;
    }
    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

}
