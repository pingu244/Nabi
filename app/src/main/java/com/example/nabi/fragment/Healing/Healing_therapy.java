package com.example.nabi.fragment.Healing;

import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Map;

public class Healing_therapy extends Fragment implements SensorEventListener {

    SensorManager sm;
    Sensor sensor_step_detector;

    PieChart pieChart;

    TextView stepCountView, stepGoalView, tv_distance;
    private FirebaseFirestore db;
    String bdiResult;


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

        pieChart = (PieChart) view.findViewById(R.id.piechart);


        // 활동 퍼미션 체크
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }


        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sm = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);   // 센서 매니저 생성
        sensor_step_detector = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);  // 스템 감지 센서 등록


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

                                if (bdiResult.equals("우울하지 않은 상태")) {
                                    stepGoalView.setText(" / 3000");
                                } else if (bdiResult.equals("가벼운 우울 상태")) {
                                    stepGoalView.setText(" / 4000");
                                } else if (bdiResult.equals("중한 우울 상태")) {
                                    stepGoalView.setText(" / 5000");
                                } else if (bdiResult.equals("심한 우울 상태")) {
                                    stepGoalView.setText(" / 6000");
                                }
                            }


                        }


                    }
                });
        return view;
    }

    // 파이 차트 설정
    private void setPieChart() {


        pieChart.clearChart();

        pieChart.addPieSlice(new PieModel("TYPE 1", 60, Color.parseColor("#1a0c47")));

        pieChart.startAnimation();

    }



    @Override
    public void onResume() {
        super.onResume();
        sm.registerListener(this, sensor_step_detector, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // 센서 유형이 스텝감지 센서인 경우 걸음수 +1

        if(event.sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            if(event.values[0]==1.0f){
                currentSteps +=event.values[0];
                stepCountView.setText(String.valueOf(currentSteps));
                pieChart.addPieSlice(new PieModel("걸음 수", currentSteps , Color.parseColor("#1a0c47")));

                pieChart.startAnimation();

            }else{
                stepCountView.setText("감지 안됨");
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}




