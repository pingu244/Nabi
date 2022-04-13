package com.example.nabi.fragment.Remind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Remind_result extends AppCompatActivity {

    ImageButton back;
    LineChart linechart;
    String resultDate;
    String[] sevendays = {"","","","","","",""};
    int[] seven_moods = {0,0,0,0,0,0,0};
    Integer result;
    int j = 0;

    class MyAsync extends AsyncTask<Void, Integer, Boolean> {

        private Context mContext = null ;

        public MyAsync(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            linechart = findViewById(R.id.remind_linechart);

            Intent intent = getIntent(); /*데이터 수신*/
            resultDate = intent.getExtras().getString("remindResult_date");
            try {
                sevendays[0] = resultDate;
                sevendays[1] = AddDate(resultDate,-1);
                sevendays[2] = AddDate(resultDate, -2);
                sevendays[3] = AddDate(resultDate, -3);
                sevendays[4] = AddDate(resultDate, -4);
                sevendays[5] = AddDate(resultDate, -5);
                sevendays[6] = AddDate(resultDate, -6);

            } catch (Exception e) {
                e.printStackTrace();
                Log.v("remind_dateTest", "Error");
            }
            for(int i = 0; i<7; i++)
                Log.v("remind_dateTest2", sevendays[i]+"입니다");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        boolean go = false;
        @Override
        protected Boolean doInBackground(Void... voids) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            int index = 0;
            for(index = 0; index<7; index++)
            {
                result = 0;
                DocumentReference docRef = db.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("diary").document(sevendays[index]);

                // document가져오는 리스너
                int finalIndex = index;
                Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> mymap = document.getData();
                                        result = 0;
                                        result = Integer.parseInt(mymap.get("q1_mood").toString());
                                        seven_moods[finalIndex] = Integer.parseInt(mymap.get("q1_mood").toString());
//                                        seven.add(Integer.parseInt(mymap.get("q1_mood").toString()));

                                        Log.v("remind_dateTest4242", (finalIndex)+"번째 기분은: " + mymap.get("q1_mood").toString()+"입니다");

                                    }
                                    else
                                    {
//                                        seven.add(0);
                                    }
                                    if(finalIndex == 6)
                                    {
                                        go = true;

                                    }

                                    Log.v("remind_dateTest5522", (finalIndex)+"번째 기분은: " + result+"입니다");
                                }
                            }
                        });
            }

            return go;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            ArrayList<Entry> values = new ArrayList<>();

//                values.add(new Entry(1, seven.get(0)*20));
//                values.add(new Entry(2, seven.get(1)*20));
//                values.add(new Entry(3, seven.get(2)*20));
//                values.add(new Entry(4, seven.get(3)*20));
//                values.add(new Entry(5, seven.get(4)*20));
//                values.add(new Entry(6, seven.get(5)*20));
//                values.add(new Entry(7, seven.get(6)*20));

            values.add(new Entry(1, seven_moods[6]*20));
            values.add(new Entry(2, 40));
            values.add(new Entry(3, seven_moods[4]*20));
            values.add(new Entry(4, seven_moods[3]*20));
            values.add(new Entry(5, seven_moods[2]*20));
            values.add(new Entry(6, seven_moods[1]*20));
            values.add(new Entry(7, seven_moods[0]*20));

            LineDataSet set1;
            set1 = new LineDataSet(values, "DataSet 1");

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);


            // x축 커스텀
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.RED);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
//        xAxis.setAxisMaximum(5f);
//        xAxis.setAxisMinimum(0f);

            // y축 오른쪽 label 없애기
            YAxis yAxisRight = linechart.getAxisRight();
            yAxisRight.setEnabled(false);
            YAxis yAxisLeft = linechart.getAxisLeft();
            yAxisLeft.setAxisMaximum(100f);
            yAxisLeft.setAxisMinimum(0f);
            yAxisLeft.setDrawGridLines(false);


            // 커스텀들
//        linechart.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정
            set1.setColor(Color.BLACK); // 차트의 선 색 설정
            set1.setDrawCircles(false); // point circle 안보이게 설정
            set1.setValueTextColor(Color.TRANSPARENT);
//        set1.setCircleColor(Color.BLACK); // 차트의 points 점 색 설정

//        set1.setDrawFilled(true); // 차트 아래 fill(채우기) 설정
//        set1.setFillColor(Color.GREEN); // 차트 아래 채우기 색 설정

            // 밑에 색깔보여주는 legend 안보이게 설정
            linechart.getLegend().setEnabled(false);
            // description 안보이게 설정
            linechart.getDescription().setEnabled(false);
            // 터치 안되게
            linechart.setTouchEnabled(false);
            // set data
            linechart.setData(data);

            for(int i = 0; i<7; i++)
                Log.v("remind_dateTest___", i+"번째 기분은: " + seven_moods[i]+"입니다");

            Toast.makeText(getApplicationContext(), "완료 되었습니다.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_result);

        back = findViewById(R.id.remind_result_back);

        // 뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


//        Intent intent = getIntent(); /*데이터 수신*/
//        resultDate = intent.getExtras().getString("remindResult_date");
//        String[] date_array = resultDate.split("/");
////        String date = "20200801"; //1년 후 날짜
//        Log.v("remind_dateTest_data", resultDate);
//        try {
//            sevendays[0] = resultDate;
//            sevendays[1] = AddDate(resultDate,-1);
//            sevendays[2] = AddDate(resultDate, -2);
//            sevendays[3] = AddDate(resultDate, -3);
//            sevendays[4] = AddDate(resultDate, -4);
//            sevendays[5] = AddDate(resultDate, -5);
//            sevendays[6] = AddDate(resultDate, -6);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.v("remind_dateTest", "Error");
//        }

//        for(int i = 0; i<7; i++)
//            searchInDB(sevendays[i], i);






        linechart = findViewById(R.id.remind_linechart);

        Intent intent = getIntent(); /*데이터 수신*/
        resultDate = intent.getExtras().getString("remindResult_date");
        try {
            sevendays[0] = resultDate;
            sevendays[1] = AddDate(resultDate,-1);
            sevendays[2] = AddDate(resultDate, -2);
            sevendays[3] = AddDate(resultDate, -3);
            sevendays[4] = AddDate(resultDate, -4);
            sevendays[5] = AddDate(resultDate, -5);
            sevendays[6] = AddDate(resultDate, -6);

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("remind_dateTest", "Error");
        }




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int index = 0;
        boolean go = false;
        for(index = 0; index<7; index++)
        {
            result = 0;
            DocumentReference docRef = db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("diary").document(sevendays[index]);

            // document가져오는 리스너
            int finalIndex = index;
            Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> mymap = document.getData();
                                    result = Integer.parseInt(mymap.get("q1_mood").toString());
                                    seven_moods[finalIndex] = Integer.parseInt(mymap.get("q1_mood").toString());
//                                        seven.add(Integer.parseInt(mymap.get("q1_mood").toString()));

                                    Log.v("remind_dateTest4242", (finalIndex)+"번째 기분은: " + mymap.get("q1_mood").toString()+"입니다");

                                }

                                Log.v("remind_dateTest5522", (finalIndex)+"번째 기분은: " + result+"입니다");
                                if(finalIndex == 6)
                                {
                                    ArrayList<Entry> values = new ArrayList<>();

//                values.add(new Entry(1, seven.get(0)*20));
//                values.add(new Entry(2, seven.get(1)*20));
//                values.add(new Entry(3, seven.get(2)*20));
//                values.add(new Entry(4, seven.get(3)*20));
//                values.add(new Entry(5, seven.get(4)*20));
//                values.add(new Entry(6, seven.get(5)*20));
//                values.add(new Entry(7, seven.get(6)*20));

                                    values.add(new Entry(1, seven_moods[6]*20));
                                    values.add(new Entry(2, 40));
                                    values.add(new Entry(3, seven_moods[4]*20));
                                    values.add(new Entry(4, seven_moods[3]*20));
                                    values.add(new Entry(5, seven_moods[2]*20));
                                    values.add(new Entry(6, seven_moods[1]*20));
                                    values.add(new Entry(7, seven_moods[0]*20));

                                    LineDataSet set1;
                                    set1 = new LineDataSet(values, "DataSet 1");

                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1); // add the data sets

                                    // create a data object with the data sets
                                    LineData data = new LineData(dataSets);


                                    // x축 커스텀
                                    XAxis xAxis = linechart.getXAxis();
                                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                    xAxis.setTextSize(10f);
                                    xAxis.setTextColor(Color.RED);
                                    xAxis.setDrawAxisLine(false);
                                    xAxis.setDrawGridLines(false);
//        xAxis.setAxisMaximum(5f);
//        xAxis.setAxisMinimum(0f);

                                    // y축 오른쪽 label 없애기
                                    YAxis yAxisRight = linechart.getAxisRight();
                                    yAxisRight.setEnabled(false);
                                    YAxis yAxisLeft = linechart.getAxisLeft();
                                    yAxisLeft.setAxisMaximum(100f);
                                    yAxisLeft.setAxisMinimum(0f);
                                    yAxisLeft.setDrawGridLines(false);


                                    // 커스텀들
//        linechart.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정
                                    set1.setColor(Color.BLACK); // 차트의 선 색 설정
                                    set1.setDrawCircles(false); // point circle 안보이게 설정
                                    set1.setValueTextColor(Color.TRANSPARENT);
//        set1.setCircleColor(Color.BLACK); // 차트의 points 점 색 설정

//        set1.setDrawFilled(true); // 차트 아래 fill(채우기) 설정
//        set1.setFillColor(Color.GREEN); // 차트 아래 채우기 색 설정

                                    // 밑에 색깔보여주는 legend 안보이게 설정
                                    linechart.getLegend().setEnabled(false);
                                    // description 안보이게 설정
                                    linechart.getDescription().setEnabled(false);
                                    // 터치 안되게
                                    linechart.setTouchEnabled(false);
                                    // set data
                                    linechart.setData(data);

                                    for(int i = 0; i<7; i++)
                                        Log.v("remind_dateTest___", i+"번째 기분은: " + seven_moods[i]+"입니다");

                                    Toast.makeText(getApplicationContext(), "완료 되었습니다.", Toast.LENGTH_SHORT).show();


                                }

                            }
                        }
                    });
        }





//        new MyAsync(this).execute();









    }

    // 날짜 계산 함수
    private static String AddDate(String strDate, int day) throws Exception {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        Date dt = dtFormat.parse(strDate); cal.setTime(dt);
//        cal.add(Calendar.YEAR, year);
//        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);
        return dtFormat.format(cal.getTime());
    }


    private void searchInDB(String date, int pos)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("diary").document(date);

        // document가져오는 리스너
        Task<DocumentSnapshot> documentSnapshotTask = docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> mymap = document.getData();
                                result = Integer.parseInt(mymap.get("q1_mood").toString());
                                seven_moods[pos] = result;
                            }
                        }
                    }
                });


    }

}
