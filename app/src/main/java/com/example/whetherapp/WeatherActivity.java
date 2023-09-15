package com.example.whetherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    String cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather2);
        Intent intent = getIntent();
        if(intent != null)
        {
            cityName = intent.getStringExtra("cityName");
            Log.e("cityName",""+cityName);
        }

        getLatitudeLongitude(cityName);
    }
    private void getLatitudeLongitude(String cityName) {
        Geocoder geocoder = new Geocoder(this);
        try {
            // using the geocode which gives the lontitide and latitude on providing the city name
            List<Address> addressList = geocoder.getFromLocationName(cityName, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                Log.e("response","  latitude  "+latitude+"  longitude  "+longitude);
                getResponce(latitude,longitude);
            }
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }
    }
    private void  getResponce(double latitude,double longitude) {
        // Referance(instance) banavala Request queue cha
        RequestQueue queue = Volley.newRequestQueue(this);
        String url="https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+ApiContent.API_KEY;
        StringRequest stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("Response Printing",""+response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String name = jsonObject.getString("name");
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject weather = jsonArray.getJSONObject(0);
                    String main = weather.getString("main");
                    String description = weather.getString("description");
                    JSONObject jsonObject1 = jsonObject.getJSONObject("main");
                    String temp = jsonObject1.getString("temp");
                    String feels_like = jsonObject1.getString("feels_like");

                    String iconId= weather.getString("icon");
                    Log.e("","weather "+iconId);
                    double temp2 = Double.parseDouble(temp);
                    temp2 = Math.ceil(temp2- 273.15);
                    double feels_like2 = Double.parseDouble(feels_like);
                    feels_like2 = Math.ceil(feels_like2- 273.15);

                    TextView textViewLocation = findViewById(R.id.textViewLocation);
                    textViewLocation.setText(""+name);

                    TextView textViewTemperature = findViewById(R.id.textViewTemperature);
                    textViewTemperature.setText(""+temp2);

                    TextView textViewWeatherDescription = findViewById(R.id.textViewWeatherDescription);
                    textViewWeatherDescription.setText(main);

                    TextView textViewFeelsAlike = findViewById(R.id.textViewFeelsAlike);
                    textViewFeelsAlike.setText("Feels like : "+feels_like2);

                    TextView textViewWeatherDescription2 = findViewById(R.id.textViewWeatherDescription2);
                    textViewWeatherDescription2.setText(description);
                    getWeatherImage(iconId);

                }
                catch (Exception e)
                {
                    Log.e("ERROR",""+e);
                }
                Log.e("HIHIIHIIHIHIHIHIHIH","HIHIHIIHIIIIHIIHIHI");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error in response",""+error);
            }
        });
        queue.add(stringRequest);
    }
    private void getWeatherImage(String iconId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String iconUrl = "https://openweathermap.org/img/wn/"+iconId+".png";
        StringRequest stringRequest2 =new StringRequest(Request.Method.GET,iconUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //Log.e("",""+response);
                   ImageView imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);
                    Picasso.get().load(iconUrl).into(imageViewWeatherIcon);
                }
                catch (Exception e)
                {
                    Log.e("Image Not Found",""+e);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error in response",""+error);
            }

        });
        queue.add(stringRequest2);
    }

}