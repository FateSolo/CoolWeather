package com.fatesolo.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.fatesolo.coolweather.db.CoolWeatherDB;
import com.fatesolo.coolweather.model.City;
import com.fatesolo.coolweather.model.County;
import com.fatesolo.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");

            for (String p : allProvinces) {
                String[] array = p.split("\\|");
                Province province = new Province();

                province.setProvinceCode(array[0]);
                province.setProvinceName(array[1]);

                coolWeatherDB.saveProvince(province);
            }

            return true;
        }
        return false;
    }

    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");

            for (String c : allCities) {
                String[] array = c.split("\\|");
                City city = new City();

                city.setCityCode(array[0]);
                city.setCityName(array[1]);
                city.setProvinceId(provinceId);

                coolWeatherDB.saveCity(city);
            }

            return true;
        }
        return false;
    }

    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");

            for (String c : allCounties) {
                String[] array = c.split("\\|");
                County county = new County();

                county.setCountyCode(array[0]);
                county.setCountyName(array[1]);
                county.setCityId(cityId);

                coolWeatherDB.saveCounty(county);
            }

            return true;
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject weatherInfo = new JSONObject(response).getJSONObject("weatherinfo");

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

            editor.putBoolean("city_selected", true);
            editor.putString("city_name", weatherInfo.getString("city"));
            editor.putString("weather_code", weatherInfo.getString("cityid"));
            editor.putString("temp1", weatherInfo.getString("temp1"));
            editor.putString("temp2", weatherInfo.getString("temp2"));
            editor.putString("weather_desp", weatherInfo.getString("weather"));
            editor.putString("publish_time", weatherInfo.getString("ptime"));
            editor.putString("current_date", new SimpleDateFormat("yyyy年M月d日", Locale.CHINA).format(new Date()));

            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
