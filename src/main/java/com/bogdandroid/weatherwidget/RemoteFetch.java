package com.bogdandroid.weatherwidget;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoteFetch {
	
	

	
	private static final String OPEN_WEATHER_MAP_API_CITY_NAME = "http://api.openweathermap.org/data/2.5/forecast?q=";
	
	private static final String OPEN_WEATHER_MAP_API_LAT_LON = "http://api.openweathermap.org/data/2.5/forecast?";
	
	private static final String API_KEY = "";
	

    public static JSONObject getJSONCityName(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_CITY_NAME + city + "&APPID=" + API_KEY));           
            HttpURLConnection connection = 
				(HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
	}
	
	public static JSONObject getJSONLatLon(Context context, String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_LAT_LON + "lat=" + lat + "&lon=" + lon + "&APPID=" + API_KEY));           
            HttpURLConnection connection = 
				(HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
	}
}


