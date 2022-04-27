package com.example.nabi.fragment.Home;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nabi.GloomyWeatherFrag;
import com.example.nabi.MainActivity;
import com.example.nabi.R;
import com.example.nabi.fragment.PushNotification.PreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class FragHome extends Fragment {

    //AVD로 실행 시 구글 본사 위치

    boolean cc1, cc2;
    ProgressDialog mDialog;
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

    SwipeRefreshLayout swipeRefreshLayout;
    SwipeRefreshLayout background;
    TextView tv_weather,today_date,current_location,temp_now,tv_humidity,tv_uv,tv_rainPer,tv_feelWeather,home_weatherMessage,
    fineDust, ultra_fineDust, fineDustGrade, ultra_fineDustGrade;
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

    String nickname;
    // 우울한 날씨 변수
    Integer gloomyWeather = -1;
    int weatherToDiary = -1;
    Integer tomorrowWeather = -1;

    // 날씨에 따른 랜덤 멘트
    ArrayList<String>[] weather_message = new ArrayList[5];



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {





        view = inflater.inflate(R.layout.frag_home, container, false);

        queue= Volley.newRequestQueue(getContext());

        background = view.findViewById(R.id.home_background);
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
        home_weatherMessage = view.findViewById(R.id.home_weatherMessage);
        fineDust = view.findViewById(R.id.tv_fine_dust);
        ultra_fineDust = view.findViewById(R.id.tv_ultrafine_dust);
        fineDustGrade = view.findViewById(R.id.tv_fine_dust_grade);
        ultra_fineDustGrade = view.findViewById(R.id.tv_ultrafine_dust_grade);


        background.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(FragHome.this).attach(FragHome.this);
                ft.commit();

                Log.d(TAG,"새로고침");


                //새로고침 종료
                background.setRefreshing(false);
            }
        });

        // 랜덤 멘트 초기화
        for(int i = 0; i<5; i++)
            weather_message[i] = new ArrayList<String>();

        // 맑음
        weather_message[0].add("맑은 날, 기분좋게 웃는 일만 일어나길");
        weather_message[0].add("어느때보다 환한 햇살이 비추는 날!\n환하게 웃는 날이 되길");
        weather_message[0].add("맑은 날 맑은 웃음으로 보내봐요");
        weather_message[0].add("하늘이 푸른 맑은 날이네요\n날씨처럼 기분 좋은 하루 보내세요");
        weather_message[0].add("창문 너머로 들어오는 햇살이 기분좋은 날이에요");
        // 약간 흐림
        weather_message[1].add("흐린 날이지만 기분은 맑은 날 되세요");
        weather_message[1].add("흐린 날,\n친구들과 함께하며 기분이 흐려지지 않게 보내보는 건 어떤가요?");
        weather_message[1].add("약간 흐린 날이네요.\n따뜻한 차를 마시며 여유있게 보내봐요");
        weather_message[1].add("흐린 날이지만 새로운 에너지로 힘차게 출발해봐요");
        weather_message[1].add("약간 흐린 날이네요.\n다음 날은 맑길 기대하는 설렘으로 하루를 보내봐요!");
        // 흐림
        weather_message[2].add("흐린날이에요.\n흐린 날이 더 집중이 잘되는 장점이 있는 것 같아요.\n날은 흐리지만 더욱 파이팅해요");
        weather_message[2].add("흐리지만 즐거운 마음으로 좋은 하루 보내시길");
        weather_message[2].add("흐린 날이네요.\n유쾌한 생각으로 잘 마무리하셨으면 좋겠어요");
        weather_message[2].add("흐린 날,\n그 위로 푸른 하늘이 있듯이 마음은 푸르게 가져봐요");
        weather_message[2].add("흐리지만 짜증내지 말고 웃으면서 좋은 하루 보내세요!");
        // 비
        weather_message[3].add("내리는 비로 마음에 촉촉한 여유가 생기길");
        weather_message[3].add("시끄러운 소음이 빗소리에 묻혀\n생각을 정리하기 딱 좋은 날이네요");
        weather_message[3].add("빗소리를 들으며 편안하게 명상해보세요");
        weather_message[3].add("우산 꼭 챙기시고 빗길 조심하세요");
        weather_message[3].add("비가 와서 운치있는 날이에요");
        // 눈
        weather_message[4].add("눈이 오네요.\n따뜻하게 입으시고 포근한 하루 보내세요");
        weather_message[4].add("눈 오는 겨울의 풍경을 즐겨봐요");
        weather_message[4].add("눈이와서 풍경이 더욱 멋진 날이에요.\n좋아하는 사람들과 풍경을 즐겨봐요");
        weather_message[4].add("눈이 와서 추운 날,\n마음만은 따뜻하게 보내봐요");
        weather_message[4].add("눈 내리는 풍경을 바라보며 좋은 하루 보내세요");


        // 마이페이지 버튼
        ImageButton mypageBtn = view.findViewById(R.id.mypageBtn);
        mypageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Mypage.class);
                startActivity(intent);
            }
        });



        // 로그인해서 처음 들어왔을때만 띄우기
        if(((MainActivity)getActivity()).LoginSuccess)
        {
            // 우울설문했는지 확인 + ~님 환영합니다!
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> mymap = document.getData();
                                    nickname = (String) mymap.get("name");
                                    gloomyWeather = Integer.parseInt(mymap.get("gloomyWeather").toString());


                                        Toast.makeText(getActivity(), nickname+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                                        ((MainActivity)getActivity()).LoginSuccess = false;


                                    if(gloomyWeather == -1)
                                    {
                                        GloomyWeatherFrag g = new GloomyWeatherFrag();
                                        g.show(getParentFragmentManager(),"setGloomyWeather");
                                    }
                                    else
                                        PreferenceHelper.setGloomy(getContext(), gloomyWeather);
                                }
                            }
                        }
                    });
        }


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일"); // 날짜 포맷
        today_date.setText(sdf.format(date));

        cc1 = false; cc2 = false;
        getLocation(); //현재 위치 불러오는 함수
        new HomeAsync().execute(); // 로딩화면



//        //3시간 날씨 어댑터 연결
        hour3_list=new ArrayList<>();
        hour3_adapter=new Hour3_Adapter(hour3_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hour3_recyclerView.setLayoutManager(linearLayoutManager);
        hour3_recyclerView.setAdapter(hour3_adapter);

        //5일 날씨 어댑터 연결
        day5_list=new ArrayList<>();
        day5_adapter=new Day5_Adapter(day5_list);

        // 세로 스크롤 안되게 막는 코드
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext()){
            @Override public boolean canScrollVertically()
            { return false; }
        };
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        day5_recyclerView.setLayoutManager(layoutManager);
        day5_recyclerView.setAdapter(day5_adapter);


        return view;

    }

    public Location getLocation() {
        Geocoder geocoder = new Geocoder(getContext()); //위도, 경도 값 현재 주소로 변환 위한 지오코더
        List<Address> gList = null;
        String sido = "", gugun = "";

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
                                    sido = address.getAdminArea(); //시,도
                                    gugun = address.getSubLocality(); //구,군

                                    current_location.setText(sido + " " + gugun);
                                    Log.v("current_location1", sido + " " + gugun);
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
                                    sido = address.getAdminArea();
                                    gugun = address.getSubLocality();

                                    current_location.setText(sido + " " + gugun);
                                    Log.v("current_location2", sido + " " + gugun);
                                }


                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 미세먼지 위한 위치 변수 조정
        {
            String fineDustLoc = "";
            if (sido.contains("서울"))
                fineDustLoc = "서울";
            else if (sido.contains("부산"))
                fineDustLoc = "부산";
            else if (sido.contains("대구"))
                fineDustLoc = "대구";
            else if (sido.contains("인천"))
                fineDustLoc = "인천";
            else if (sido.contains("광주"))
                fineDustLoc = "광주";
            else if (sido.contains("대전"))
                fineDustLoc = "대전";
            else if (sido.contains("울산"))
                fineDustLoc = "울산";
            else if (sido.contains("경기"))
                fineDustLoc = "강원";
            else if (sido.contains("충청"))
                if (sido.contains("남"))
                    fineDustLoc = "충남";
                else
                    fineDustLoc = "충북";
            else if (sido.contains("전라"))
                if (sido.contains("남"))
                    fineDustLoc = "전남";
                else
                    fineDustLoc = "전북";
            else if (sido.contains("경상"))
                if (sido.contains("남"))
                    fineDustLoc = "경남";
                else
                    fineDustLoc = "경북";
            else if (sido.contains("제주"))
                fineDustLoc = "제주";
            else if (sido.contains("세종"))
                fineDustLoc = "세종";

            // 미세먼지 api 부르기
            new FineDustAsync(getContext(),
                    "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=" +
                            fineDustLoc +
                            "&pageNo=1&numOfRows=100&returnType=xml&serviceKey=pvu3pESfzwCNhaeY5CD3LAhd9mOipW2LbQZhuJK1qzZ%2F4hz3BzjzyeGgX5nalUrnush7c7s1QAElm5abVaPCAA%3D%3D&ver=1.0", gugun)
                    .execute();
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

                String unixTimeStamp = jsonObject.getString("dt");

                long timestamp = Long.parseLong(unixTimeStamp)*1000L;
                Date date = new Date();
                date.setTime(timestamp);

                DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                DateFormat formatter = new SimpleDateFormat("a h시", Locale.KOREA);

                String Datetime = parser.format(date);
                Date ddate = parser.parse(Datetime);
                item.setTime(formatter.format(ddate));
                int t = (int)(Math.round(jsonObject.getJSONObject("main").getDouble("temp")));
                item.setTemp(t);

                //main값에 따라 날씨 아이콘 적용하는 코드
                // (jsonArray.getJSONObject(i).getJSONObject("weather").getString("main"));

                String main = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
                if (main.equals("Clear")) { //맑은 날
                    item.setImageView(R.drawable.ic_mainweather_clear);
                }

                else if (main.equals("Mist")||main.equals("Smoke")||main.equals("Haze")||main.equals("Dust")){ //약간 흐린
                    item.setImageView(R.drawable.ic_mainweather_mist);
                }

                else if (main.equals("Thunderstorm") ||main.equals("Clouds")||main.equals("Fog") ||main.equals("Sand")||main.equals("Ash")||main.equals("Squall")||main.equals("Tornado")){ //흐린
                    item.setImageView(R.drawable.ic_mainweather_cloud);
                }

                else if (main.equals("Rain")||main.equals("Drizzle")){ //비
                    item.setImageView(R.drawable.ic_mainweather_rain);
                }

                else if ( main.equals("Snow")){

                    item.setImageView(R.drawable.ic_mainweather_snow);

                }


                Log.d(TAG,"3시간 날씨"+main);


                hour3_list.add(item);
                cc2 = true;

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
                int a = Math.round(Float.valueOf(daily.getJSONObject(i).getJSONObject("temp").getString("min")));
                int b = Math.round(Float.valueOf(daily.getJSONObject(i).getJSONObject("temp").getString("max")));
                minTemp = Integer.toString(a)+"°"; //일일 최소 온도
                maxTemp = Integer.toString(b)+"°"; //일일 최대 온도
                int c = (int)(Float.valueOf(jsonObject.getString("pop"))*100);
                rain = Integer.toString(c);

                long unixTime = new Long(time);

                Date date = new Date(unixTime*1000L);
                SimpleDateFormat format = new SimpleDateFormat("MM.dd");
                String dateString = format.format(date);

                SimpleDateFormat format1 = new SimpleDateFormat("E요일", Locale.KOREA);

                item.setDayOfWeek(format1.format(date));
                item.setDay(format.format(date));
                item.setTemp(minTemp+" / "+maxTemp);
                item.setRainPer1(rain+"%");



                //Log.d(TAG,main);
                Log.d(TAG,dateString);
                main = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
                if (main.equals("Clear")) { //맑은 날
                    item.setImageView(R.drawable.ic_mainweather_clear);
                    // 오늘 일기 배경바뀌게 하기위함
                    if(i==0)
                        weatherToDiary = 0;
                    else if(i==1)
                        tomorrowWeather = 0;
                }

                else if (main.equals("Mist")||main.equals("Smoke")||main.equals("Haze")||main.equals("Dust")){ //약간 흐린
                    item.setImageView(R.drawable.ic_mainweather_mist);
                    // 오늘 일기 배경바뀌게 하기위함
                    if(i==0)
                        weatherToDiary = 1;
                    else if(i == 1)
                        tomorrowWeather = 1;
                }

                else if (main.equals("Clouds")||main.equals("Fog") ||main.equals("Sand")||main.equals("Ash")||main.equals("Squall")||main.equals("Tornado")){ //흐린
                    item.setImageView(R.drawable.ic_mainweather_cloud);
                    // 오늘 일기 배경바뀌게 하기위함
                    if(i==0)
                        weatherToDiary = 2;
                    else if(i==1)
                        tomorrowWeather = 2;
                }

                else if (main.equals("Rain")||main.equals("Drizzle")||main.equals("Thunderstorm")){ //비
                    item.setImageView(R.drawable.ic_mainweather_rain);
                    // 오늘 일기 배경바뀌게 하기위함
                    if(i==0)
                        weatherToDiary = 3;
                    else if(i==1)
                        tomorrowWeather = 3;
                }

                else if ( main.equals("Snow")){
                    item.setImageView(R.drawable.ic_mainweather_snow);
                    // 오늘 일기 배경바뀌게 하기위함
                    if(i==0)
                        weatherToDiary = 4;
                    else if(i==1)
                        tomorrowWeather = 4;
                }

                day5_list.add(item);
            }

            // MainActivity public변수에 저장해줌(일기쓸때 적용 위해)
            try {
                if(((MainActivity)getActivity()).diary_weather == null)
                    ((MainActivity)getActivity()).diary_weather = weatherToDiary;
                Log.v("diaryWrite_todayWeather", "첫번째: "+ weatherToDiary);
                Log.v("diaryWrite_todayWeather", "두번째: "+ ((MainActivity)getActivity()).diary_weather);
            } catch (Exception e){e.printStackTrace();}


            // preferences로 내일 날씨 저장
            try {
                PreferenceHelper.setTomorrowWeather(getContext(), tomorrowWeather);
                Log.v("tomorrowWeather", "tomorrowWeather: "+ tomorrowWeather);
            } catch (Exception e){e.printStackTrace();Log.v("tomorrowWeather", "ERROR");}

            cc1 = true;

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
                float a = Float.valueOf(daily.getJSONObject(0).getJSONObject("temp").getString("min"));
                float b = Float.valueOf(daily.getJSONObject(0).getJSONObject("temp").getString("max"));
                int aa = Math.round(a);
                int bb = Math.round(b);

                minTemp = aa+"°"; //일일 최소 온도
                maxTemp = bb+"°"; //일일 최대 온도


                Log.d(TAG,description);


            }

            description = current.getJSONArray("weather").getJSONObject(0).getString("main"); // 현재 날씨
//            ((WritingDiary)getActivity()).today_weather = description;  // public변수에 오늘 날씨 저장 -> 다이어리에서 사용하기위함
            int c = Math.round(Float.valueOf(current.getString(("temp"))));
            nowTemp = Integer.toString(c); //현재 온도
            int f = Math.round(Float.valueOf(current.getString(("uvi"))));
//            uv = Integer.toString(f); //자외선 지수
            if (f>=0 && f<=2)
                uv = "안전";
            else if (f>2 && f<=7)
                uv = "주의";
            else if (f>7)
                uv = "각별한 주의";

            humidity = daily.getJSONObject(0).getString("humidity"); //습도
            int e = (int)(Float.valueOf(daily.getJSONObject(0).getString("pop"))*100);
            rainPer = Integer.toString(e); //강수확률
            int d = Math.round(Float.valueOf(current.getString(("feels_like"))));
            feelWeather = d+"°"; //체감온도

            temp_now.setText(nowTemp + "°");
            tv_humidity.setText(humidity+"%");
            tv_uv.setText(uv);
            tv_rainPer.setText(rainPer+"%");
            tv_feelWeather.setText(feelWeather);


            Random random = new Random();
            int randomValue = random.nextInt(4);    // 랜덤값 줘서 멘트 랜덤으로


            if (description.equals("Clear")){//맑음
                tv_weather.setText("맑음 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_mainweather_clear);
                background.setBackgroundColor(Color.parseColor("#BAD3A5"));
                home_weatherMessage.setText(weather_message[0].get(randomValue));   // 랜덤 멘트
            }
            else if (description.equals("Mist")||description.equals("Smoke")||description.equals("Haze")||description.equals("Dust")){//안개,구름 조금
                tv_weather.setText("조금 흐림 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_mainweather_mist);
                background.setBackgroundColor(Color.parseColor("#F6C97B"));
                home_weatherMessage.setText(weather_message[1].get(randomValue));   // 랜덤 멘트
            }
            else if (description.equals("Thunderstorm") ||description.equals("Clouds")||description.equals("Fog") ||description.equals("Sand")||description.equals("Ash")||description.equals("Squall")||description.equals("Tornado")){//천둥번개, 구름
                tv_weather.setText("흐림 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_mainweather_cloud);
                background.setBackgroundColor(Color.parseColor("#97ACE5"));
                home_weatherMessage.setText(weather_message[2].get(randomValue));   // 랜덤 멘트
            }
            else if (description.equals("Rain")||description.equals("Drizzle")){//비, 이슬비
                tv_weather.setText("비 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_mainweather_rain);
                background.setBackgroundColor(Color.parseColor("#8DC0D3"));
                home_weatherMessage.setText(weather_message[3].get(randomValue));   // 랜덤 멘트
            }
            else if (description.equals("Snow")){//눈
                tv_weather.setText("눈 "+minTemp+"/"+maxTemp);
                weatherImg.setImageResource(R.drawable.ic_mainweather_snow);
                background.setBackgroundColor(Color.parseColor("#B19ED8"));
                home_weatherMessage.setText(weather_message[4].get(randomValue));   // 랜덤 멘트
            }else{
                tv_weather.setText(description);
                //weatherImg.setImageResource(R.drawable.ic_baseline_ac_unit_24);
            }


        }catch (JSONException e){
            e.printStackTrace();

        }

    }


    // 로딩중 dialog 띄우기
    public class HomeAsync extends AsyncTask<Void, Location, Location> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("로딩중입니다...");
            mDialog.show();

        }

        @Override
        protected Location doInBackground(Void... voids) {
            while(!(cc1 && cc2)) {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.v("timesleep","timesleep");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Location aVoid) {
            super.onPostExecute(aVoid);

            mDialog.dismiss();
        }
    }


    // 미세먼지위한 async
    public class FineDustAsync extends AsyncTask<Void, Void, String> {

        private String receiveMsg = "";
        private String requestUrl;
        public Context mContext;
        private String gu;

        String value10, value25, grade10, grade25;

        public FineDustAsync(Context context, String requestUrl, String gu) {

            this.requestUrl = requestUrl;
            this.mContext = context;
            this.gu = gu;

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                boolean b_dataGu = false;
                boolean b_pm10Value = false, pm10Grade = false;
                boolean b_pm25Value = false, pm25Grade = false;
                String one="", two="", three="", four="";

                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {

                        case XmlPullParser.START_DOCUMENT:

                            break;

                        case XmlPullParser.END_DOCUMENT:
                            break;


                        // 시작태그를 만나는순간 (JSON으로 치면 key을 대입해서 찾음)
                        case XmlPullParser.START_TAG:

                            if (parser.getName().equals("stationName"))
                                b_dataGu = true;
                            if (parser.getName().equals("pm10Value"))
                                b_pm10Value = true;
                            if (parser.getName().equals("pm25Value"))
                                b_pm25Value = true;
                            if (parser.getName().equals("pm10Grade"))
                                pm10Grade = true;
                            if (parser.getName().equals("pm25Grade"))
                                pm25Grade = true;

                            break;

                        // 시작태그와 종료태그 사이 값을 만나는순간 (JSON으로 치면 key를 넣어서 값을 얻음)
                        case XmlPullParser.TEXT:
                            if (b_pm10Value) {
                                one = parser.getText();
                                b_pm10Value = false;
                            } else if (b_pm25Value) {
                                two = parser.getText();
                                b_pm25Value = false;
                            } else if (pm10Grade) {
                                three = parser.getText();
                                pm10Grade = false;
                            } else if (pm25Grade) {
                                four = parser.getText();
                                pm25Grade = false;
                            }
                            if (b_dataGu) {
                                b_dataGu = false;
                                if(parser.getText().equals(gu))
                                {
                                    value10 = one;
                                    value25 = two;
                                    grade10 = three;
                                    grade25 = four;
                                }
                            }
                            break;
                        // 종료태그를 만나는순간 (JSON으로 치면, value 찾고 이제 볼일없음)
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {

                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return receiveMsg;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(value10 != null){
                fineDust.setText(value10 + "㎍/m³"); // pm10Value, 미세먼지
                fineDustGrade.setText(gradeMeasure(grade10));
                ultra_fineDust.setText(value25 + "㎍/m³"); // pm25Value, 초미세먼지
                ultra_fineDustGrade.setText(gradeMeasure(grade25));
            }else {
                fineDustGrade.setText("정보없음"); // pm10Value, 미세먼지
                ultra_fineDustGrade.setText("정보없음");
            }

        }

        private String gradeMeasure(String grade){
            String result = "";
            int g;
            try{
                g = Integer.parseInt(grade);
            } catch (Exception e){g = 0;}
            switch (g){
                case 1:
                    result = "좋음"; break;
                case 2:
                    result = "보통"; break;
                case 3:
                    result = "나쁨"; break;
                case 4:
                    result = "매우나쁨"; break;

            }
            return result;
        }
    }



}