package com.example.weathermap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ImageButton change_city;
    protected LocationManager mLocationManager;
    protected LocationListener mLocationListener;
    private int LOCATION_CODE = 123;
    private TextView mTextViewTemp;
    private TextView mTextViewArea;
    private TextView mTextViewDetails;
    private ImageView mWeatherImage;
    private String URL = "http://api.openweathermap.org/data/2.5/weather";
    protected String APP_ID = "e2ed03ef864d2881b0ebdf70247b23ba";
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);
        change_city = (ImageButton) findViewById(R.id.changeCityButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTextViewTemp = (TextView) findViewById(R.id.tempTV);
        mTextViewArea = (TextView) findViewById(R.id.locationTV);
        mTextViewDetails = (TextView) findViewById(R.id.weatherText);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);

        // TODO : GO yo the city change layout

        change_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent city = new Intent(MainActivity.this, ChangeCity.class);
                startActivity(city);
            }
        });

    }

    //TODO : Create onResume fuction

    @Override
    protected void onResume() {
        super.onResume();
        Intent myIntent = getIntent();
        String city = myIntent.getStringExtra("City");
        if(city != null){
            getCityLocation(city);
        }else {
            getCurrentLocation();
        }
    }

    //TODO : Create a method to get city Location
    private void getCityLocation(String city){
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);
        networking(params);
    }

    //TODO : Create a method to get current Location

    private void getCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("WeatherMap", "onLocationChanged method called !");
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);

                networking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("WeatherMap", "onStatusChanged method called !");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("WeatherMap", "onProviderEnabled method called !");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("WeatherMap", "onProviderDisabled method called !");
                Toast.makeText(MainActivity.this, "Check Your GPS Connection", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 300, mLocationListener);
    }

    // TODO : Create onRequestPermissionResult method

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (LOCATION_CODE) {
            case 123 :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
        }
    }

    // TODO : Create a method to bring details from API

    private void networking(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("WeatherMap", "Success JSON !" +response.toString());

                WeatherDataModel weatherDataModel = WeatherDataModel.fromJson(response);
                updateUI(weatherDataModel);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,Throwable error, JSONObject response) {
            Toast.makeText(MainActivity.this, "Something went Wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(WeatherDataModel weatherDataModel){

        mTextViewTemp.setText(weatherDataModel.getTemperature());
        mTextViewArea.setText(weatherDataModel.getCity());
        mTextViewDetails.setText(weatherDataModel.getWeatherCondition());
        int resourceID = getResources().getIdentifier(weatherDataModel.getIconName(), "drawable", getPackageName());
        mWeatherImage.setImageResource(resourceID);
        mProgressBar.setVisibility(View.GONE);

    }

    //TODO : Create a onPause method to save battery and other


    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager != null) mLocationManager.removeUpdates(mLocationListener);
    }
}

