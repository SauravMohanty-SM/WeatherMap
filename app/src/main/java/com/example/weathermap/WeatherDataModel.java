package com.example.weathermap;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataModel {

    private String mTemperature, mCity, mIconName, mWeatherCondition;
    private int mCondition;

    public static WeatherDataModel fromJson(JSONObject jsonObject){
        WeatherDataModel weatherDataModel = new WeatherDataModel();
        try {
            weatherDataModel.mCity = jsonObject.getString("name");
            weatherDataModel.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");

            double tempTemperature = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempTemperature);

            weatherDataModel.mTemperature = Integer.toString(roundedValue);
            weatherDataModel.mIconName = updateWeatherIcon(weatherDataModel.mCondition);
            weatherDataModel.mWeatherCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        } catch (JSONException error) {
            error.printStackTrace();
            return null;
        }

        return weatherDataModel;
    }

    // TODO : Create a method to check weather condition

    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    // TODO: Create getter methods for temperature, city, weather Condition and icon name:

    public String getTemperature() {
        return mTemperature + "°c";
    }

    public String getCity() {
        return mCity;
    }

    public String getIconName() {
        return mIconName;
    }

    public String getWeatherCondition() {
        return mWeatherCondition;
    }
}
