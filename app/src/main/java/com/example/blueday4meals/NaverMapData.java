package com.example.blueday4meals;

import com.google.gson.annotations.SerializedName;

public class NaverMapData {
    @SerializedName("가맹점명")
    private String franchisee_name;
    @SerializedName("가맹점유형코드")
    private String food_type;
    @SerializedName("시도명")
    private String province;
    @SerializedName("시군구명")
    private String city;
    @SerializedName("시군구코드")
    private String zipcode;
    @SerializedName("소재지도로명주소")
    private String road_address;
    @SerializedName("소재지주소")
    private String dong_address;
    @SerializedName("위도")
    private double latitude;
    @SerializedName("경도")
    private double longitude;
    @SerializedName("전화번호")
    private String tel_num;


    public String getfranchisee_name() {
        return franchisee_name;
    }

    public String getfood_type() {
        return food_type;
    }

    public String getprovince() {
        return province;
    }

    public String getcity() {
        return city;
    }

    public String getzipcode() {
        return zipcode;
    }

    public String getroad_address() {
        return road_address;
    }

    public String getdong_address() {
        return dong_address;
    }

    public double getlatitude() {
        return latitude;
    }

    public double getlongitude() {
        return longitude;
    }

    public String gettel_num() {
        return tel_num;
    }
}