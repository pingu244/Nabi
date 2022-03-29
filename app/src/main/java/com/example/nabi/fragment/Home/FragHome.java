package com.example.nabi.fragment.Home;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nabi.MainActivity;
import com.example.nabi.fragment.Diary.WritingDiary;
import com.example.nabi.fragment.Home.Day5_Adapter;
import com.example.nabi.R;
import com.example.nabi.fragment.Home.Hour3_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    Hour3_Adapter hour3_adapter;
    ArrayList<Hour3_Adapter.ItemData> hour3_list;
    RecyclerView hour3_recyclerView;

    Day5_Adapter day5_adapter;
    ArrayList<Day5_Adapter.ItemData> day5_list;
    RecyclerView day5_recyclerView;

    RequestQueue queue;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.frag_home, container, false);

        queue= Volley.newRequestQueue(getContext());

        tv_weather = view.findViewById(R.id.tv_weather);
        weatherImg = view.findViewById(R.id.weatherImg);
        today_date = view.findViewById(R.id.today_date);
        current_location = view.findViewById(R.id.current_location);
        temp_now = view.findViewById(R.id.temp_now);
        tv_humidity = view.findViewById(R.id.tv_humidity);
        tv_uv = view.findViewById(R.id.tv_uv);
        tv_rainPer = view.findViewById(R.id.tv_rainPer);
        tv_feelWeather = view.findViewById(R.id.tv_feelWeather);
        hour3_recyclerView = view.findViewById(R.id.hour3_view);
        day5_recyclerView = view.findViewById(R.id.day5_view);


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일"); // 날짜 포맷
        today_date.setText(sdf.format(date));

        getLocation(); //현재 위치 불러오는 함수


        //3시간 날씨 어댑터 연결
        hour3_list=new ArrayList<>();
        hour3_adapter=new Hour3_Adapter(hour3_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hour3_recyclerView.setLayoutManager(linearLayoutManager);
        hour3_recyclerView.setAdapter(hour3_adapter);

        //5일 날씨 어댑터 연결
        day5_list=new ArrayList<>();
        day5_adapter=new Day5_Adapter(day5_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        day5_recyclerView.setLayoutManager(layoutManager);
        day5_recyclerView.setAdapter(day5_adapter);


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
                    ActivityCompat.requestPermissions((Activity) getContext(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_CODE);
                }
            });
            builder.show();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            //getWeatherData(location.getLatitude(),location.getLongitude());
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            //api 통신
            StringRequest forecastRequest = new StringRequest(Request.Method.POST, "https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lng+"&units=metric&appid=4f7af2b5be3f6ff146c79e2fdba5dc5e", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getJsonHour3Parser(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

            });

            StringRequest dailyRequest = new StringRequest(Request.Method.POST, "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lng+"&units=metric&appid=4f7af2b5be3f6ff146c79e2fdba5dc5e", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getJsonDay5Parser(response);
                    getJsonCurrentParser(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

            });

            queue.add(forecastRequest);
            queue.add(dailyRequest);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };

    // 3시간 간격 날씨 파싱 & 어댑터에 적용
    public void getJsonHour3Parser(String jsonString) {

        // 리스트 초기화
        hour3_list.clear();

        try{
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("list");

            for (int i = 0; i< 7;i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Hour3_Adapter.ItemData item = new Hour3_Adapter.ItemData();
                String time = jsonObject.getString("dt_txt");

                DateFormat parser = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
                DateFormat formatter = new SimpleDateFormat("HH:mm");

                Date date = parser.parse(time);

                item.setTime(formatter.format(date).toString());
                item.setTemp(jsonObject.getJSONObject("main").getDouble("temp"));

                //main값에 따라 날씨 아이콘 적용하는 코드
                // (jsonArray.getJSONObject(i).getJSONObject("weather").getString("main"));

                String main = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
                if (main.equals("Clear")) { //맑은 날
                    item.setImageView(R.drawable.ic_baseline_wb_sunny_24);
                }

                else if (main.equals("Mist")||main.equals("Smoke")||main.equals("Haze")||main.equals("Dust")){ //약간 흐린
                    item.setImageView(R.drawable.ic_baseline_cloud_queue_24);
                }

                else if (main.equals("Thunderstorm") ||main.equals("Clouds")||main.equals("Fog") ||main.equals("Sand")||main.equals("Ash")||main.equals("Squall")||main.equals("Tornado")){ //흐린
                    item.setImageView(R.drawable.ic_baseline_cloud_24);
                }

                else if (main.equals("Rain")||main.equals("Drizzle")){ //비
                    item.setImageView(R.drawable.ic_baseline_opacity_24);
                }

                else if ( main.equals("Snow")){

                    item.setImageView(R.drawable.ic_baseline_ac_unit_24);

                }


                Log.d(TAG,"3시간 날씨"+main);


                hour3_list.add(item);

            }
        }catch (JSONException | ParseException e){
            e.printStackTrace();
        }

    }

    //5일 날씨 데이터 파싱 & 어댑터 적용
    public void getJsonDay5Parser(String jsonString){

        day5_list.clear();

        try{
            JSONArray daily = new JSONObject(jsonString).getJSONArray("daily");
            String time="";
            String minTemp = "";
            String maxTemp = "";
            String main = "";
            String rain = "";

            for (int i = 0; i< daily.length();i++){

                JSONObject jsonObject = daily.getJSONObject(i);
                Day5_Adapter.ItemData item = new Day5_Adapter.ItemData();
                time = jsonObject.getString("dt"); //날짜
                minTemp = daily.getJSONObject(i).getJSONObject("temp").getString("min"); //일일 최소 온도
                maxTemp = daily.getJSONObject(i).getJSONObject("temp").getString("max"); //일일 최대 온도
                rain = jsonObject.getString("pop");

                long unixTime = new Long(time);

                Date date = new Date(unixTime*1000L);
                SimpleDateFormat format = new SimpleDateFormat("MM.dd");
                String dateString = format.format(date);

                item.setDay(format.format(date));
                item.setTemp(minTemp+" / "+maxTemp);
                item.setRainPer1(rain+"%");



                //Log.d(TAG,main);
                Log.d(TAG,dateString);
                main = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
                if (main.equals("Clear")) { //맑은 날
                    item.setImageView(R.drawable.ic_baseline_wb_sunny_24);
                    if(i==0)    // 오늘 일기 배경바뀌게 하기위함
                        ((MainActivity)getActivity()).diary_weather = 0;
                }

                else if (main.equals("Mist")||main.equals("Smoke")||main.equals("Haze")||main.equals("Dust")){ //약간 흐린
                    item.setImageView(R.drawable.ic_baseline_cloud_queue_24);
                    if(i==0)    // 오늘 일기 배경바뀌게 하기위함
                        ((MainActivity)getActivity()).diary_weather = 1;
                }

                else if (main.equals("Thunderstorm") ||main.equals("Clouds")||main.equals("Fog") ||main.equals("Sand")||main.equals("Ash")||main.equals("Squall")||main.equals("Tornado")){ //흐린
                    item.setImageView(R.drawable.ic_baseline_cloud_24);
                    if(i==0)    // 오늘 일기 배경바뀌게 하기위함
                        ((MainActivity)getActivity()).diary_weather = 2;
                }

                else if (main.equals("Rain")||main.equals("Drizzle")){ //비
                    item.setImageView(R.drawable.ic_baseline_opacity_24);
                    if(i==0)    // 오늘 일기 배경바뀌게 하기위함
                        ((MainActivity)getActivity()).diary_weather = 3;
                }

                else if ( main.equals("Snow")){
                    item.setImageView(R.drawable.ic_baseline_ac_unit_24);
                    if(i==0)    // 오늘 일기 배경바뀌게 하기위함
                        ((MainActivity)getActivity()).diary_weather = 4;
                }

                day5_list.add(item);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getJsonCurrentParser(String jsonString){

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
            JSONArray daily = new JSONObject(jsonString).getJSONArray("daily");
            JSONObject current = new JSONObject(jsonString).getJSONObject("current");

            for(int i=0;i<daily.length();i++) {
                minTemp = daily.getJSONObject(0).getJSONObject("temp").getString("min"); //일일 최소 온도
                maxTemp = daily.getJSONObject(0).getJSONObject("temp").getString("max"); //일일 최대 온도


                Log.d(TAG,description);


            }

            description = current.getJSONArray("weather").getJSONObject(0).getString("main"); // 현재 날씨
//            ((WritingDiary)getActivity()).today_weather = description;  // public변수에 오늘 날씨 저장 -> 다이어리에서 사용하기위함
            nowTemp = current.getString(("temp")); //현재 온도
            uv = current.getString(("uvi")); //자외선 지수
            humidity = daily.getJSONObject(0).getString("humidity"); //습도
            rainPer = daily.getJSONObject(0).getString("pop"); //강수확률
            feelWeather = current.getString(("feels_like")); //체감온도
            temp_now.setText("현재 온도 : "+nowTemp);
            tv_humidity.setText("습도 : "+humidity+"%");
            tv_uv.setText("자외선 : "+uv);
            tv_rainPer.setText("강수확률 : "+rainPer+"%");
            tv_feelWeather.setText("체감온도 : "+feelWeather);

            if (description.equals("Clear")){//맑음
                tv_weather.setText("맑음 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
            }
            else if (description.equals("Mist")||description.equals("Smoke")||description.equals("Haze")||description.equals("Dust")){//안개,구름 조금
                tv_weather.setText("조금 흐림 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_baseline_cloud_queue_24);
            }
            else if (description.equals("Thunderstorm") ||description.equals("Clouds")||description.equals("Fog") ||description.equals("Sand")||description.equals("Ash")||description.equals("Squall")||description.equals("Tornado")){//천둥번개, 구름
                tv_weather.setText("흐림 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_baseline_cloud_24);
            }
            else if (description.equals("Rain")||description.equals("Drizzle")){//비, 이슬비
                tv_weather.setText("비 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_baseline_opacity_24);
            }
            else if (description.equals("Snow")){//눈
                tv_weather.setText("눈 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_baseline_ac_unit_24);
            }else{
                tv_weather.setText(description);
                //weatherImg.setImageResource(R.drawable.ic_baseline_ac_unit_24);
            }


        }catch (JSONException e){
            e.printStackTrace();

        }

    }


}