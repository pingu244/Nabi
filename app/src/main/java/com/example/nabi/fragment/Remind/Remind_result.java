package com.example.nabi.fragment.Remind;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Remind_result extends AppCompatActivity {

    boolean[] check = new boolean[7];
    ProgressDialog mDialog;
    ImageButton back;
    LineChart linechart;
    PieChart piechart;
    TextView one, two, three, four, five, six;
    String resultDate;
    String[] sevendays = {"","","","","","",""};
    int[] seven_moods = {0,0,0,0,0,0,0};
    String result = "";
    int j = 0;

    class MyAsync extends AsyncTask<Void, Integer, Boolean> {

        private Context mContext = null ;

        public MyAsync(Context context) {
            mContext = context;
        }


        @Override
        protected void onPreExecute() {

            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("로딩중입니다...");
            mDialog.show();


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {



            while(true) {
                try {
                    sleep(2000);
                    boolean go = true;
                    for(int i = 0; i<7; i++) {
                        if (!check[i])
                            go = false;
                        Log.v("remind___check", i+"번째: "+check[i]);
                    }
                    if(go)
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {


            {
                ArrayList<Entry> values = new ArrayList<>();

                for (int i = 0; i < 7; i++)
                    Log.v("remind____dateTest", i + "번째 기분은: " + seven_moods[i] + "입니다");

                values.add(new Entry(1, seven_moods[6] * 20));
                values.add(new Entry(2, seven_moods[5] * 20));
                values.add(new Entry(3, seven_moods[4] * 20));
                values.add(new Entry(4, seven_moods[3] * 20));
                values.add(new Entry(5, seven_moods[2] * 20));
                values.add(new Entry(6, seven_moods[1] * 20));
                values.add(new Entry(7, seven_moods[0] * 20));

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
                linechart.invalidate();
            }

            showPieChart();



            Toast.makeText(getApplicationContext(), "완료 되었습니다.", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_result);


        linechart = findViewById(R.id.remind_linechart);
        piechart = findViewById(R.id.remind_piechart);
        one = findViewById(R.id.remind_pie_one);
        two = findViewById(R.id.remind_pie_two);
        three = findViewById(R.id.remind_pie_three);
        four = findViewById(R.id.remind_pie_four);
        five = findViewById(R.id.remind_pie_five);
        six = findViewById(R.id.remind_pie_six);

        back = findViewById(R.id.remind_result_back);

        // 뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



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




        for(int i = 0; i<7; i++)
            check[i] = false;


        new MyAsync(this).execute();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(int index = 0; index<7; index++)
        {
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
                                    if(finalIndex==0)
                                        result = mymap.get("q3_todayKeyword").toString();
                                    seven_moods[finalIndex] = Integer.parseInt(mymap.get("q1_mood").toString());

                                    Log.v("remind___dateTest4242", (finalIndex)+"번째 기분은: " + mymap.get("q1_mood").toString()+"입니다");

                                }

                                Log.v("remind___dateTest5252", (finalIndex)+"번째 기분은: " + seven_moods[finalIndex]+"입니다");
                                check[finalIndex] = true;

                            }
                        }
                    });
        }














    }

    // 날짜 계산 함수
    private static String AddDate(String strDate, int day) throws Exception {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy/M/d");
        Calendar cal = Calendar.getInstance();
        Date dt = dtFormat.parse(strDate); cal.setTime(dt);
        cal.add(Calendar.DATE, day);
        return dtFormat.format(cal.getTime());
    }

    private void showPieChart(){

        int[] count = new int[6];
        for(int i = 0; i<6; i++)
            count[i] = 0;
        String[] array = result.split(",");
        for(int i = 0; i<array.length; i++)
        {
            switch (array[i]){
                case "신난":
                case "매력적인":
                case "활기찬":
                case "장난기 많은":
                case "창의적인":
                case "기쁜":
                    count[0]++; break;
                case "만족스러운":
                case "사려깊은":
                case "친한":
                case "사랑스러운":
                case "사람을 믿는":
                case "고마운":
                    count[1]++; break;
                case "신념있는":
                case "희망찬":
                case "감사한":
                case "존경하는":
                case "자랑스러운":
                case "중요한":
                    count[2]++; break;
                case "죄책감있는":
                case "부끄러운":
                case "우울한":
                case "외로운":
                case "지루한":
                case "생기없는":
                    count[3]++; break;
                case "질투나는":
                case "이기적인":
                case "화난":
                case "적대적인":
                case "짜증나는":
                case "비판적인":
                    count[4]++; break;
                case "당혹스러운":
                case "혼란스러운":
                case "무력한":
                case "순종적인":
                case "어리석은":
                case "불안한":
                    count[5]++; break;

            }
        }

        //이부분 화면에 출력하는거 아니라서 일단 주석처리했습니다. (0502 다영)
//        one.setText("기쁨 "+count[0]);
//        two.setText("평화 "+count[1]);
//        three.setText("힘찬 "+count[2]);
//        four.setText("슬픔 "+count[3]);
//        five.setText("분노 "+count[4]);
//        six.setText("두려움 "+count[5]);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("기쁨",count[0]);
        typeAmountMap.put("평화",count[1]);
        typeAmountMap.put("힘찬",count[2]);
        typeAmountMap.put("슬픔",count[3]);
        typeAmountMap.put("분노",count[4]);
        typeAmountMap.put("두려움",count[5]);

        //이거 색 분류해주시와요..! 그때그때 다르게 나옵니다
        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#EFE480"));
        colors.add(Color.parseColor("#ADC3FF"));
        colors.add(Color.parseColor("#B0E093"));
        colors.add(Color.parseColor("#CCB4FF"));
        colors.add(Color.parseColor("#FFAAA1"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#F4CA77"));

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            if(typeAmountMap.get(type).intValue() != 0)
                pieEntries.add(new PieEntry(typeAmountMap.get(type).intValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(13f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false);

        // description, legend 안보이게 설정
        piechart.getDescription().setEnabled(false);
        piechart.setDrawHoleEnabled(false);
        Legend legend = piechart.getLegend();
        legend.setEnabled(false);

        piechart.setTouchEnabled(false);    // 터치 못하게

        piechart.setData(pieData);
        piechart.invalidate();
    }




}
