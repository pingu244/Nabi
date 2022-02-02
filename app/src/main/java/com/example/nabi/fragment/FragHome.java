package com.example.nabi.fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.nabi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragHome extends Fragment {

    //AVD로 실행 시 구글 본사 위치

    @Nullable
    View view;
    LocationManager locationManager;
    boolean isGPSEnabled = false; //현재 GPS 사용유무
    boolean isNetworkEnabled = false; //네트워크 사용유무
    boolean isGetLocation = false; //GPS상태값
    Location location;
    double lat; //위도
    double lon; //경도
    private static int REQUEST_WEATHER_CODE = 1001;
    private static int REQUEST_GPS_CODE = 1002;

    TextView tv_weather,today_date,current_location,temp_now,tv_humidity,tv_uv,tv_rainPer,tv_feelWeather;
    ImageView weatherImg;

    //최소 GPS 정보 업데이트 거리 1000미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    //최소 업데이트 시간 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.frag_home, container, false);

        tv_weather = view.findViewById(R.id.tv_weather);
        weatherImg = view.findViewById(R.id.weatherImg);
        today_date = view.findViewById(R.id.today_date);
        current_location = view.findViewById(R.id.current_location);
        temp_now = view.findViewById(R.id.temp_now);
        tv_humidity = view.findViewById(R.id.tv_humidity);
        tv_uv = view.findViewById(R.id.tv_uv);
        tv_rainPer = view.findViewById(R.id.tv_rainPer);
        tv_feelWeather = view.findViewById(R.id.tv_feelWeather);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일");
        today_date.setText(sdf.format(date));

        getLocation();

        return view;

    }

    public Location getLocation() {
        Geocoder geocoder = new Geocoder(getContext()); //위도, 경도 값 현재 주소로 변환 위한 지오코더
        List<Address> gList = null;

        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            //gps정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //현재 네트워크 상태 값
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.isGetLocation = true;
                //네트워크 정보로부터 위치값 가져오기
                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //인터넷 권한, 위치 정보 접근 권한 설정 안 된 경우

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setTitle("권한이 필요한 이유");
                        builder.setMessage("현재 날씨 정보를 얻기 위해서는 인터넷 권한을 필요로 합니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, REQUEST_WEATHER_CODE);
                            }
                        });

                        builder.show();

                        locationPermission();
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);


                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                            try {
                                gList = geocoder.getFromLocation(lat, lon, 8);

                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, "setMaskLocation() - 서버에서 주소변환시 에러발생");
                                // Fragment1 으로 강제이동 시키기
                            }
                            if (gList != null) {
                                if (gList.size() == 0) {
                                    Toast.makeText(getContext(), " 현재위치에서 검색된 주소정보가 없습니다. ", Toast.LENGTH_SHORT).show();

                                } else {

                                    Address address = gList.get(0);
                                    String sido = address.getAdminArea(); //시,도
                                    String gugun = address.getSubLocality(); //구,군

                                    current_location.setText(sido+" "+gugun);
                                }

                            }
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                            try {
                                gList = geocoder.getFromLocation(lat, lon, 8);

                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, "setMaskLocation() - 서버에서 주소변환시 에러발생");
                                // Fragment1 으로 강제이동 시키기
                            }
                            if (gList != null) {
                                if (gList.size() == 0) {
                                    Toast.makeText(getContext(), " 현재위치에서 검색된 주소정보가 없습니다. ", Toast.LENGTH_SHORT).show();

                                } else {

                                    Address address = gList.get(0);
                                    String sido = address.getAdminArea();
                                    String gugun = address.getSubLocality();

                                    current_location.setText(sido+" "+gugun);
                                }


                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }
    private void locationPermission() {


        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        int finePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (finePermission != PackageManager.PERMISSION_GRANTED || coarsePermission != PackageManager.PERMISSION_GRANTED) {//권한이 허용되지 않은 경우
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("권한이 필요한 이유");
            builder.setMessage("현재 날씨 정보를 얻기 위해서는 위치 권한을 필요로 합니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_CODE);
                }
            });
            builder.show();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            getWeatherData(location.getLatitude(),location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };

    //현재 위도,경도 바탕으로 위치에 따른 날씨 가져오기
    private void getWeatherData(double lat, double lng){

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lng+"&units=metric&appid=4f7af2b5be3f6ff146c79e2fdba5dc5e";
        ReceiveWeatherTask receiveWeatherTask = new ReceiveWeatherTask();
        receiveWeatherTask.execute(url);


    }


    private class ReceiveWeatherTask extends AsyncTask<String,Void, JSONObject>{
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... datas) {
            try{
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while((readed=in.readLine())!=null){
                        JSONObject jsonObject = new JSONObject(readed);
                        return jsonObject;
                    }
                }else{
                    return null;
                }
                return null;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject result){
            //Log.i(TAG,result.toString());

            if(result!=null){
                String nowTemp = "";
                String maxTemp = "";
                String minTemp = "";
                String uv = "";
                String humidity = "";
                String description = "";
                String rainPer = "";
                String feelWeather = "";

                try{
                    //json데이터 파싱
                    JSONArray daily = result.getJSONArray("daily");

                    for(int i=0;i<daily.length();i++) {
                        minTemp = daily.getJSONObject(i).getJSONObject("temp").getString("min"); //일일 최소 온도
                        maxTemp = daily.getJSONObject(i).getJSONObject("temp").getString("max"); //일일 최대 온도
                        description = daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"); //일일 대표 날씨
                    }

                    nowTemp = result.getJSONObject("current").getString(("temp")); //현재 온도
                    uv = result.getJSONObject("current").getString(("uvi")); //자외선 지수
                    humidity = result.getJSONArray("daily").getJSONObject(0).getString("humidity"); //습도
                    rainPer = result.getJSONArray("daily").getJSONObject(0).getString("pop"); //강수확률
                    feelWeather = result.getJSONObject("current").getString(("feels_like")); //체감온도



                    temp_now.setText("현재 온도 : "+nowTemp);
                    tv_humidity.setText("습도 : "+humidity+"%");
                    tv_uv.setText("자외선 : "+uv);
                    tv_rainPer.setText("강수확률 : "+rainPer+"%");
                    tv_feelWeather.setText("체감온도 : "+feelWeather);

                }catch (JSONException e){
                    e.printStackTrace();

                }

                if (description == "clear sky"){//맑음
                    tv_weather.setText("맑음 "+minTemp+"/"+maxTemp);
                    weatherImg.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                }
                else if (description == "mist"&&description == "few clouds"){//안개,구름 조금
                    tv_weather.setText("조금 흐림 "+minTemp+"/"+maxTemp);
                    weatherImg.setImageResource(R.drawable.ic_baseline_cloud_queue_24);

                }
                else if (description == "overcast clouds"&&description == "scattered clouds"&&description == "thunderstorm"){//많은 구름, 흩어진 구름,천둥 번개
                    tv_weather.setText("흐림 "+minTemp+"/"+maxTemp);
                    weatherImg.setImageResource(R.drawable.ic_baseline_cloud_24);

                }
                else if (description == "light rain"&&description == "moderate rain"&&description == "heavy intensity rain"){//약한 비, 보통 비, 강한 비
                    tv_weather.setText("비 "+minTemp+"/"+maxTemp);
                    weatherImg.setImageResource(R.drawable.ic_baseline_wb_sunny_24);

                }
                else if (description == "snow"){//눈
                    tv_weather.setText("눈 "+minTemp+"/"+maxTemp);
                    weatherImg.setImageResource(R.drawable.ic_baseline_wb_sunny_24);

                }
                else{
                    tv_weather.setText("맑음 "+minTemp+"/"+maxTemp);
                    weatherImg.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                }


            }
        }
    }

}
