package com.example.nabi.fragment.Healing;

import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.nabi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Healing_therapy extends Fragment implements SensorEventListener, LocationListener {

    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepCountView, stepGoalView, tv_distance;
    private FirebaseFirestore db;
    String bdiResult;
    private LocationManager locationManager;
    private Location lastKnownLocation = null;
    private Location nowLastLocation = null;
    private boolean isGPSEnable = false;

    // 현재 걸음 수
    int currentSteps = 0;
    @Nullable
    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.healing_therapy, container, false);

        db = FirebaseFirestore.getInstance();

        stepCountView = view.findViewById(R.id.tv_step);
        stepGoalView = view.findViewById(R.id.tv_goalStep);
        tv_distance = view.findViewById(R.id.tv_distance);

        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        //GPS 사용 가능 여부 확인
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, (LocationListener) this);

        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(getContext(), "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        // BDI 결과값 가져오기
        DocumentReference documentBDI = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("healing").document("BDI Result");

        // document가져오는 리스너
        Task<DocumentSnapshot> documentSnapshotTask2 = documentBDI.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("dataOutput", "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> mymap = document.getData();
                                bdiResult = (String) mymap.get("bdi_result");

                                //BDI 검사 결과에 따라 목표 걸음 수 다름

                                if (bdiResult.equals("우울하지 않은 상태")){
                                    stepGoalView.setText(" / 3000");
                                }else if (bdiResult.equals("가벼운 우울 상태")){
                                    stepGoalView.setText(" / 4000");
                                }else if (bdiResult.equals("중한 우울 상태")){
                                    stepGoalView.setText(" / 5000");
                                }else if (bdiResult.equals("심한 우울 상태")){
                                    stepGoalView.setText(" / 6000");
                                }
                            }


                        }


                    }
                });
        return view;
    }

    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            //
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){

            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                double longwork = getGPSLocation();
                if(longwork > 0.05){
                    currentSteps++;
                    stepCountView.setText(String.valueOf(currentSteps));
                }

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public double getGPSLocation(){
        double deltaTime = 0.0;
        double deltaDist = 0.0;

        if(isGPSEnable){
            if(lastKnownLocation == null){
                lastKnownLocation = nowLastLocation;
            }

            if(lastKnownLocation!=null && nowLastLocation!= null){
                double lat1 = lastKnownLocation.getLatitude();
                double lng1 = lastKnownLocation.getLongitude();
                double lat2 = nowLastLocation.getLatitude();
                double lng2 = nowLastLocation.getLongitude();

                deltaTime = (nowLastLocation.getTime() - lastKnownLocation.getTime()) / 1000.0; //시간 간격

                deltaDist = distance(lat1, lng1, lat2, lng2);
                if(deltaDist > 0.05){
                    //거리간격
                    tv_distance.setText(Double.parseDouble(String.format("%.1f",deltaDist))+"m");
                    return deltaDist;
                }
                lastKnownLocation = nowLastLocation;
            }
        }
        return 0.0;
    }

    public void onLocationChanged(Location location){
        nowLastLocation = location;

        double lng = location.getLongitude();
        double lat = location.getLatitude();
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))*Math.sin(deg2rad(lat2))+Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist*60*1.1515;
        dist = dist*1.609344*1000;

        return dist;
    }

    private static double deg2rad(double deg){
        return (deg*Math.PI/180.0);
    }

    private static double rad2deg(double rad){
        return (rad*180/Math.PI);
    }
}
