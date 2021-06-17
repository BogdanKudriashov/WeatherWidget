package com.bogdandroid.weatherwidget;

import android.app.*;
import android.os.*;
import android.widget.*;
import org.json.*;
import java.util.*;
import java.text.*;
import android.util.*;
import android.text.*;
import android.content.*;
import android.view.*;
import android.graphics.*;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class MainActivity extends Activity {
	
	Context context;
	
	TextView updatedField;
	TextView cityField;
    ImageView weatherIcon;
	TextView currentTemperatureField;
    TextView detailsField;
    
    TextView tvTime1;
	TextView tvTime2;
	TextView tvTime3;
	TextView tvTime4;
	TextView tvTime5;
	TextView tvTime6;
	TextView tvTime7;
	TextView tvTime8;
	
	ImageView ivW1;
	ImageView ivW2;
	ImageView ivW3;
	ImageView ivW4;
	ImageView ivW5;
	ImageView ivW6;
	ImageView ivW7;
	ImageView ivW8;
	
	TextView tvTemp1;
	TextView tvTemp2;
	TextView tvTemp3;
	TextView tvTemp4;
	TextView tvTemp5;
	TextView tvTemp6;
	TextView tvTemp7;
	TextView tvTemp8;
	
	static TextView tvJsonInfo;
	
	///
	
	
	TextView tvLocationGPS;
	TextView tvLocationNet;

    Handler handler = new Handler();
	
	LocationManager locationManager;
	Location location;
	String lat;
	String lon;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
        setContentView(R.layout.main);
		
        updatedField = (TextView) findViewById(R.id.updated_field);
		cityField = (TextView) findViewById(R.id.city_field);
		weatherIcon = (ImageView) findViewById(R.id.weather_icon);
		currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        detailsField = (TextView) findViewById(R.id.details_field);
		
		tvTime1 = (TextView) findViewById(R.id.tv_time_1);
		tvTime2 = (TextView) findViewById(R.id.tv_time_2);
		tvTime3 = (TextView) findViewById(R.id.tv_time_3);
		tvTime4 = (TextView) findViewById(R.id.tv_time_4);
		tvTime5 = (TextView) findViewById(R.id.tv_time_5);
		tvTime6 = (TextView) findViewById(R.id.tv_time_6);
		tvTime7 = (TextView) findViewById(R.id.tv_time_7);
		tvTime8 = (TextView) findViewById(R.id.tv_time_8);
		
		ivW1 = (ImageView) findViewById(R.id.iv_w_1);
		ivW2 = (ImageView) findViewById(R.id.iv_w_2);
		ivW3 = (ImageView) findViewById(R.id.iv_w_3);
		ivW4 = (ImageView) findViewById(R.id.iv_w_4);
		ivW5 = (ImageView) findViewById(R.id.iv_w_5);
		ivW6 = (ImageView) findViewById(R.id.iv_w_6);
		ivW7 = (ImageView) findViewById(R.id.iv_w_7);
		ivW8 = (ImageView) findViewById(R.id.iv_w_8);
		
		tvTemp1 = (TextView) findViewById(R.id.tv_temp_1);
		tvTemp2 = (TextView) findViewById(R.id.tv_temp_2);
		tvTemp3 = (TextView) findViewById(R.id.tv_temp_3);
		tvTemp4 = (TextView) findViewById(R.id.tv_temp_4);
		tvTemp5 = (TextView) findViewById(R.id.tv_temp_5);
		tvTemp6 = (TextView) findViewById(R.id.tv_temp_6);
		tvTemp7 = (TextView) findViewById(R.id.tv_temp_7);
		tvTemp8 = (TextView) findViewById(R.id.tv_temp_8);
		
		tvJsonInfo = (TextView) findViewById(R.id.json_info);
        
        
		tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
		tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		updateWeatherData(new CityPreference(this).getCity());
		
    }
	
	@Override
	protected void onStart() {
	
		super.onStart();
	}

	@Override
	protected void onResume() {
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*10, 10, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*10, 10, locationListener);
											   
		checkEnabled();
											   
		super.onResume();
	}

	@Override
	protected void onPause() {
		
		locationManager.removeUpdates(locationListener);
		super.onPause();
	}
	
	@Override
	protected void onStop() {
	
		new CityPreference(this).setCity(cityField.getText().toString());
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
	
		
		super.onDestroy();
	}
	
 
	private void updateWeatherData(final String city){
		new Thread(){
			public void run(){
				final JSONObject json = RemoteFetch.getJSONCityName(context, city);
				if(json == null){
					handler.post(new Runnable(){
							public void run(){
					            showMessage();
							}
						});
				} else {
					handler.post(new Runnable(){
							public void run(){
								renderWeather(json);
							}
						});
				}               
			}
		}.start();
	}
	
	private void updateWeatherDataFromLocation(final String lat, final String lon) {
		new Thread(){
			public void run(){
				final JSONObject json = RemoteFetch.getJSONLatLon(context, lat, lon);
				if(json == null){
					handler.post(new Runnable(){
							public void run(){
					            showMessage();
							}
						});
				} else {
					handler.post(new Runnable(){
							public void run(){
								renderWeather(json);
							}
						});
				}               
			}
		}.start();
	}
	
	
	private void renderWeather(JSONObject json){
    try {
		
		//////////// Current block ///////////
		
		JSONObject details0 = json.getJSONArray("list").getJSONObject(0);
		String updatedO = details0.getString("dt_txt");
		updatedField.setText("Last update: " + updatedO);
		
		JSONObject city = json.getJSONObject("city");
		cityField.setText(city.getString("name").toUpperCase(Locale.US) + ", " + 
						  city.getString("country"));
						  
        JSONObject dscr0 = details0.getJSONArray("weather").getJSONObject(0);
		//String daynight = details0.getJSONObject("sys").getString("pod");
		weatherIcon.setImageBitmap(getImage(dscr0.getInt("id"), details0.getJSONObject("sys").getString("pod")));
		
        JSONObject temp0 = details0.getJSONObject("main");
	    currentTemperatureField.setText(Long.toString((Math.round(temp0.getDouble("temp") - 273.15))) + " ℃");
		
		
		detailsField.setText(dscr0.getString("description").toUpperCase(Locale.US) +
							 "\n" + "Humidity: " + temp0.getString("humidity") + "%" +
							 "\n" + "Pressure: " + temp0.getString("pressure") + " hPa");
		
		
		/// Block 1
		
		JSONObject details1 = json.getJSONArray("list").getJSONObject(1);
		String updated1 = details1.getString("dt_txt");
		tvTime1.setText(updated1.substring(10,16));
		
		JSONObject dscr1 = details1.getJSONArray("weather").getJSONObject(0);
		ivW1.setImageBitmap(getImage(dscr1.getInt("id"), details1.getJSONObject("sys").getString("pod")));
		
		JSONObject temp1 = details1.getJSONObject("main");
	    tvTemp1.setText(Long.toString((Math.round(temp1.getDouble("temp") - 273.15))) + " ℃");
		
		/// Block 2
		
		JSONObject details2 = json.getJSONArray("list").getJSONObject(2);
		String updated2 = details2.getString("dt_txt");
		tvTime2.setText(updated2.substring(10,16));
		
		JSONObject dscr2 = details2.getJSONArray("weather").getJSONObject(0);
		ivW2.setImageBitmap(getImage(dscr2.getInt("id"), details2.getJSONObject("sys").getString("pod")));
		
		JSONObject temp2 = details2.getJSONObject("main");
	    tvTemp2.setText(Long.toString((Math.round(temp2.getDouble("temp") - 273.15))) + " ℃");
		
		/// Block 3
		
		JSONObject details3 = json.getJSONArray("list").getJSONObject(3);
		String updated3 = details3.getString("dt_txt");
		tvTime3.setText(updated3.substring(10,16));
		
		JSONObject dscr3 = details3.getJSONArray("weather").getJSONObject(0);
		ivW3.setImageBitmap(getImage(dscr3.getInt("id"), details3.getJSONObject("sys").getString("pod")));
		
		JSONObject temp3 = details3.getJSONObject("main");
	    tvTemp3.setText(Long.toString((Math.round(temp3.getDouble("temp") - 273.15))) + " ℃");
		
		/// Block 4
		
		JSONObject details4 = json.getJSONArray("list").getJSONObject(4);
		String updated4 = details4.getString("dt_txt");
		tvTime4.setText(updated4.substring(10,16));
		
		JSONObject dscr4 = details4.getJSONArray("weather").getJSONObject(0);
		ivW4.setImageBitmap(getImage(dscr4.getInt("id"), details4.getJSONObject("sys").getString("pod")));
		
		JSONObject temp4 = details4.getJSONObject("main");
	    tvTemp4.setText(Long.toString((Math.round(temp4.getDouble("temp") - 273.15))) + " ℃");
		
		/// Block 5
		
		JSONObject details5 = json.getJSONArray("list").getJSONObject(5);
		String updated5 = details5.getString("dt_txt");
		tvTime5.setText(updated5.substring(10,16));
		
		JSONObject dscr5 = details5.getJSONArray("weather").getJSONObject(0);
		ivW5.setImageBitmap(getImage(dscr5.getInt("id"), details5.getJSONObject("sys").getString("pod")));
		
		JSONObject temp5 = details5.getJSONObject("main");
	    tvTemp5.setText(Long.toString((Math.round(temp5.getDouble("temp") - 273.15))) + " ℃");
		
		/// Block 6
		
		JSONObject details6 = json.getJSONArray("list").getJSONObject(6);
		String updated6 = details6.getString("dt_txt");
		tvTime6.setText(updated6.substring(10,16));
		
		JSONObject dscr6 = details6.getJSONArray("weather").getJSONObject(0);
		ivW6.setImageBitmap(getImage(dscr6.getInt("id"), details6.getJSONObject("sys").getString("pod")));
		
		JSONObject temp6 = details6.getJSONObject("main");
	    tvTemp6.setText(Long.toString((Math.round(temp6.getDouble("temp") - 273.15))) + " ℃");
		
		/// Block 7
		
		JSONObject details7 = json.getJSONArray("list").getJSONObject(7);
		String updated7 = details7.getString("dt_txt");
		tvTime7.setText(updated7.substring(10,16));
		
		JSONObject dscr7 = details7.getJSONArray("weather").getJSONObject(0);
		ivW7.setImageBitmap(getImage(dscr7.getInt("id"), details7.getJSONObject("sys").getString("pod")));
		
		JSONObject temp7 = details7.getJSONObject("main");
	    tvTemp7.setText(Long.toString((Math.round(temp7.getDouble("temp") - 273.15))) + " ℃");
		
		/// Block 8
		
		JSONObject details8 = json.getJSONArray("list").getJSONObject(8);
		String updated8 = details8.getString("dt_txt");
		tvTime8.setText(updated8.substring(10,16));
		
		JSONObject dscr8 = details8.getJSONArray("weather").getJSONObject(0);
		ivW8.setImageBitmap(getImage(dscr8.getInt("id"), details8.getJSONObject("sys").getString("pod")));
		
		JSONObject temp8 = details8.getJSONObject("main");
	    tvTemp8.setText(Long.toString((Math.round(temp8.getDouble("temp") - 273.15))) + " ℃");
		
		//tvJsonInfo.setText(json.toString());
         
    }catch(Exception e){
        Log.e("SimpleWeather", "One or more fields not found in the JSON data");
    }
  }
  
	
	public Bitmap getImage(int actualId, String daynight) {
		
		int id = actualId / 100;
		Bitmap icon = null;
		
		if(actualId == 800){
		 
			if(daynight.equalsIgnoreCase("d")) {
		             icon = BitmapFactory.decodeResource(getResources(),R.drawable.sun_48);
			} else if(daynight.equalsIgnoreCase("n")) {
		             icon = BitmapFactory.decodeResource(getResources(),R.drawable.moon_48);
		           }
		} else {
		
		switch(id) {
			case 2 : 
				icon = BitmapFactory.decodeResource(getResources(),R.drawable.storm_48);
				break;         
			case 3 : 
				icon = BitmapFactory.decodeResource(getResources(),R.drawable.moderate_rain_48);
				break;
			case 5 : 
				icon = BitmapFactory.decodeResource(getResources(),R.drawable.rain_48);
				break;
			case 6 : 
				icon = BitmapFactory.decodeResource(getResources(),R.drawable.snow_48);
				break;
			case 7 : 
				icon = BitmapFactory.decodeResource(getResources(),R.drawable.dust_48);
				break;
			case 8 : 
				icon = BitmapFactory.decodeResource(getResources(),R.drawable.partly_cloudy_48);
				break;
		 }
	   }
		
		return icon;
	}
	
	private void showInputDialog(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Change city");
    final EditText input = new EditText(this);
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);
    builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
			updateWeatherData(input.getText().toString());
        }
    });
    builder.show();
    }
	
	public void updateCity() {
		updateWeatherData(new CityPreference(this).getCity());
	}
 
    public void changeCity(String city) {
		new CityPreference(this).setCity(cityField.getText().toString());
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_change_city:
				showInputDialog();
				break;
			case R.id.btn_my_location:
				if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
					//startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					tvLocationGPS.setText("Please!");
					tvLocationNet.setText("Turn on geolocation!");
				}
				if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
					updateWeatherDataFromLocation(tvLocationGPS.getText().toString(), tvLocationNet.getText().toString());
				}
				break;
		}
	}
	
	private void checkEnabled() {
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
			
			tvLocationGPS.setText("Geolocation off.");
			tvLocationNet.setText(" ");
		}
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
			
			tvLocationGPS.setText("Geolocation is enabled.");
			tvLocationNet.setText(" ");
			
		}
	}
	
	
	public void showMessage() {
		Toast.makeText(this, "Place not found or No Internet Connection", Toast.LENGTH_SHORT).show();
	}
	
	
	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			showLocation(location);
			updateWeatherDataFromLocation(tvLocationGPS.getText().toString(), tvLocationNet.getText().toString());
		}

		@Override
		public void onProviderDisabled(String provider) {
			
			checkEnabled();
		}

		@Override
		public void onProviderEnabled(String provider) {
			//showLocation(locationManager.getLastKnownLocation(provider));
			tvLocationGPS.setText("Geolocation data retrieval.");
			tvLocationNet.setText("Please wait....");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		
		}
	};

	private void showLocation(Location location) {
		if (location == null)
			return;
		if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			tvLocationGPS.setText(formatLocationLat(location));
			tvLocationNet.setText(formatLocationLon(location));
		} else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
			  tvLocationGPS.setText(formatLocationLat(location));
			  tvLocationNet.setText(formatLocationLon(location));
		}
	}

	private String formatLocationLat(Location location) {
		if (location == null)
			return "";
		return String.format("%.4f", location.getLatitude()); 
	}
	
	private String formatLocationLon(Location location) {
		if (location == null)
			return "";
		return String.format("%.4f", location.getLongitude());
	}
}
