package com.example.nabi.fragment.Remind;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nabi.R;
import com.example.nabi.fragment.PushNotification.PreferenceHelper;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Remind_result extends AppCompatActivity {

    boolean[] check = new boolean[7];
    ProgressDialog mDialog;
    ImageButton back;
    LineChart linechart;
    PieChart piechart;
    TextView one, two, three, four, five, six, step, meditateTime, nabi_word;
    TextView keywordPercentage[];
    CircularProgressBar walkingProgress, meditateProgress;
    ImageView walkSuccess, meditateSuccess;
    Integer steps, meditate, walkingMax;
    String resultDate, bdiResult;
    String[] sevendays = {"","","","","","",""};
    int[] seven_moods = {0,0,0,0,0,0,0};
    String result = "";
    Integer today_weather;

    String nabi1 = "", nabi2="", nabi3=null;  // 나비 한마디


    // 오늘 날짜만 검정색으로 나오게 하는것
    public class ColoredLabelXAxisRenderer extends XAxisRenderer {

        List<Integer> labelColors;

        public ColoredLabelXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
            labelColors = Collections.EMPTY_LIST;
        }

        @Override
        protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
            final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
            boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

            float[] positions = new float[mXAxis.mEntryCount * 2];

            for (int i = 0; i < positions.length; i += 2) {

                // only fill x values
                if (centeringEnabled) {
                    positions[i] = mXAxis.mCenteredEntries[i / 2];
                } else {
                    positions[i] = mXAxis.mEntries[i / 2];
                }
            }

            mTrans.pointValuesToPixel(positions);

            for (int i = 0; i < positions.length; i += 2) {

                float x = positions[i];

                if (mViewPortHandler.isInBoundsX(x)) {

                    String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
                    int color = Color.parseColor("#9D9D9D");
                    if(i==positions.length-2)
                        color = Color.BLACK;

                    mAxisLabelPaint.setColor(color);

                    if (mXAxis.isAvoidFirstLastClippingEnabled()) {

                        // avoid clipping of the last
                        if (i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                            float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                            if (width > mViewPortHandler.offsetRight() * 2
                                    && x + width > mViewPortHandler.getChartWidth())
                                x -= width / 2;

                            // avoid clipping of the first
                        } else if (i == 0) {

                            float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                            x += width / 2;
                        }
                    }

                    drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
                }
            }
        }


    }


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
        protected Boolean doInBackground(Void... voids) {
            // 시킨 일 다 완료되길 기다림
            while(true) {
                try {
                    sleep(2000);
                    boolean go = true;
                    for(int i = 0; i<7; i++) {
                        if (!check[i])
                            go = false;
                        Log.v("remind___check", i+"번째: "+check[i]);
                    }
                    if(go){
                        publishProgress(0);
                        break;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);
            // 시킨 일 끝난 후 화면에 보이는 작업들
            {
                // 선 그래프
                ArrayList<Entry> values = new ArrayList<>();

                for (int i = 0; i < 7; i++)
                    Log.v("remind____dateTest", i + "번째 기분은: " + seven_moods[i] + "입니다");

                // x축 커스텀
                XAxis xAxis = linechart.getXAxis();

                // XAxis에 원하는 String 설정하기 (날짜)
                xAxis.setValueFormatter(new ValueFormatter() {

                    @Override
                    public String getFormattedValue(float value) {
                        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy/M/d");
                        long emissionsMilliSince1970Time = 0;
                        try {
                            Date dt = dtFormat.parse(sevendays[7-(int)value]);
                            emissionsMilliSince1970Time = dt.getTime();
                        } catch (ParseException e) {}
                        // Show time in local version
                        Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("M/d");

                        return dateTimeFormat.format(timeMilliseconds);
                    }
                });
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextSize(11f);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(false);
                linechart.getXAxis().setSpaceMin(0.5f);
                linechart.getXAxis().setSpaceMax(0.5f);



                // y축 오른쪽 label 없애기
                YAxis yAxisRight = linechart.getAxisRight();
                yAxisRight.setEnabled(false);
                YAxis yAxisLeft = linechart.getAxisLeft();
                yAxisLeft.setTextSize(11f);
                yAxisLeft.setAxisMaximum(101f);
                yAxisLeft.setAxisMinimum(-1f);
                yAxisLeft.setDrawGridLines(false);
                yAxisLeft.setDrawAxisLine(true);
                yAxisLeft.setSpaceBottom(1f);


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


                // 커스텀들
//        linechart.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정
                set1.setColor(Color.parseColor("#FFA6A6")); // 차트의 선 색 설정
                set1.setLineWidth(3f);
                set1.setMode(LineDataSet.Mode.LINEAR);
                set1.setCubicIntensity(0f);
                set1.setDrawCircles(false); // point circle 안보이게 설정
                set1.setValueTextColor(Color.TRANSPARENT);

                // 오늘 날짜만 검정색으로 나오게 하는것
                linechart.setXAxisRenderer(new ColoredLabelXAxisRenderer(linechart.getViewPortHandler(), linechart.getXAxis(), linechart.getTransformer(YAxis.AxisDependency.LEFT)));

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

            // 원그래프(감정단어) 띄우는 함수
            showPieChart();

            // 산책 부분
            step.setText(steps+"걸음");
//            walkingProgress.setMax(walkingMax);
            walkingProgress.setProgressMax(walkingMax);
            walkingProgress.setProgress(steps);
            if(steps>=walkingMax)
                walkSuccess.setVisibility(View.VISIBLE);

            // 명상 부분
            meditateProgress.setProgressMax(10);
            meditate = (meditate / (1000*60)) % 60;
            meditateTime.setText(meditate+"분");
            meditateProgress.setProgress(meditate);
            if(meditate>=10)
                meditateSuccess.setVisibility(View.VISIBLE);

            // 나비 한마디
            nabi_word();
            if(nabi3 == null)
                nabi_word.setText(nabi1+"\n\n"+nabi2);
            else
                nabi_word.setText(nabi1+"\n\n"+nabi3+"\n\n"+nabi2);


//            Toast.makeText(getApplicationContext(), "완료 되었습니다.", Toast.LENGTH_SHORT).show();
            try {
                sleep(1000);    // 1초만 기다리자
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
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
        six = findViewById(R.id.remind_pie_six_per);
        keywordPercentage = new TextView[]{one, two, three, four, five, six};
        step = findViewById(R.id.remind_step);
        meditateTime = findViewById(R.id.remind_meditate);

        back = findViewById(R.id.remind_result_back);

        walkingProgress = findViewById(R.id.remind_walk_progressbar);
        meditateProgress = findViewById(R.id.remind_meditate_progressbar);
        walkSuccess = findViewById(R.id.remind_walk_success);
        meditateSuccess = findViewById(R.id.remind_meditate_success);

        nabi_word = findViewById(R.id.nabi_word);

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



        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // BDI 결과값 가져오기 -> 산책 max값 정하기 위해
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
                                    walkingMax = 3000;
                                } else if (bdiResult.equals("가벼운 우울 상태")) {
                                    walkingMax = 4000;
                                } else if (bdiResult.equals("중한 우울 상태")) {
                                    walkingMax = 5000;
                                } else if (bdiResult.equals("심한 우울 상태")) {
                                    walkingMax = 6000;
                                }
                            }
                        }
                    }
                });



        for(int i = 0; i<7; i++)
            check[i] = false;

        new MyAsync(this).execute();

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
                                    if(finalIndex==0){
                                        result = mymap.get("q3_todayKeyword").toString();
                                        today_weather = Integer.valueOf(mymap.get("weather").toString());
                                        try{
                                            steps = Integer.valueOf(mymap.get("walk").toString());
                                            meditate = Integer.valueOf(mymap.get("meditate").toString());
                                        } catch (Exception e){ steps = 0; meditate = 0;}

                                    }
                                    try{

                                        seven_moods[finalIndex] = Integer.parseInt(mymap.get("q1_mood").toString());
                                    }catch (Exception e) {
                                        Log.v("erorrororo", finalIndex + "");
                                    }

//                                    Log.v("remind___dateTest4242", (finalIndex)+"번째 기분은: " + mymap.get("q1_mood").toString()+"입니다");

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

    // 밀리초로 반환
    private static float Milliseconds(String strDate) throws Exception {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy/M/d");
        Date dt = dtFormat.parse(strDate);
        return dt.getTime();
    }

    // 원 그래프 함수
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
        // 색 지정되게 수정했습니다 (희애0507)
        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i<pieDataSet.getEntryCount(); i++){
            if (pieDataSet.getEntryForIndex(i).getLabel().equals("기쁨"))
                colors.add(Color.parseColor("#EFE480"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("평화"))
                colors.add(Color.parseColor("#ADC3FF"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("힘찬"))
                colors.add(Color.parseColor("#B0E093"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("슬픔"))
                colors.add(Color.parseColor("#CCB4FF"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("분노"))
                colors.add(Color.parseColor("#FFAAA1"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("두려움"))
                colors.add(Color.parseColor("#ff5f67"));
        }
        pieDataSet.setColors(colors);


        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false);

        // 퍼센트 textview에 보이기
        int[] percentage = new int[6];
        for(int i = 0; i<6; i++)
            percentage[i] = (int)(count[i] / pieData.getYValueSum()*100);
        for(int i = 0; i<6; i++)
            if(percentage[i] == 0)
                if(i==5)
                    findViewById(R.id.remind_pie_six).setVisibility(View.GONE);
                else
                    keywordPercentage[i].setVisibility(View.GONE);
            else
                switch (i){
                    case 0:
                        keywordPercentage[i].setText("기쁨 "+percentage[i]+"%"); break;
                    case 1:
                        keywordPercentage[i].setText("평화 "+percentage[i]+"%"); break;
                    case 2:
                        keywordPercentage[i].setText("힘찬 "+percentage[i]+"%"); break;
                    case 3:
                        keywordPercentage[i].setText("슬픔 "+percentage[i]+"%"); break;
                    case 4:
                        keywordPercentage[i].setText("분노 "+percentage[i]+"%"); break;
                    case 5:
                        keywordPercentage[i].setText(percentage[i]+"%"); break;
                }



        // description, legend 안보이게 설정
        piechart.getDescription().setEnabled(false);
        piechart.setDrawHoleEnabled(false);
        Legend legend = piechart.getLegend();
        legend.setEnabled(false);
        piechart.setDrawEntryLabels(false); // 그래프 위에 글자 없애기

        piechart.setTouchEnabled(false);    // 터치 못하게

        piechart.setData(pieData);
        piechart.invalidate();
    }



    // 엄청난 노가다가 시작된다....
    // 나비의 한마디가 아닌 한마디..
    void nabi_word(){

        // 어제 기분 정도와 오늘 기분 정도 비교하여 한마디
        Integer todayMood = seven_moods[0];
        Integer yesterdayMood = seven_moods[1];
        switch (todayMood){
            case 0:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "요즘 슬픈 일이 많은가요? 다 괜찮아요."; break;
                    case 1:
                        nabi1 = "오늘 하루 슬픈일이 있었군요. 괜찮아요 다 잘될거에요"; break;
                    case 2:
                        nabi1 = "너무 슬퍼하지 말아요, 이런 날도 있는거지요. 내일은 내일의 해가 뜰 거예요. 나비가 도와줄게요"; break;
                    case 3:
                        nabi1 = "우울할 수 있어요. 괜찮아요. 나비가 도와줄게요"; break;
                    case 4:
                        nabi1 = "마음껏 슬퍼하는 것도 좋아요. 내일은 내일의 해가 뜰 거예요. 나비가 도와줄게요"; break;
                    case 5:
                        nabi1 = "항상 맑을 수는 없죠. 어제의 당신이 오늘의 당신을 위로해주는 하루가 되길. 나비가 진심으로 바랍니다."; break;
                } break;
            case 1:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "조금씩 괜찮아지고 있어요. 잘 해내고 있어요!"; break;
                    case 1:
                        nabi1 = "괜찮아요. 조급해하지 말아요. 나비가 옆에서 기다릴게요"; break;
                    case 2:
                        nabi1 = "오늘 하루 슬픈일이 있었군요. 괜찮아요 다 잘될거에요"; break;
                    case 3:
                        nabi1 = "너무 주눅둘지 마세요. 당신을 사랑하는 것이 제일 중요하답니다! :)"; break;
                    case 4:
                        nabi1 = "항상 완벽할 수는 없어요. 괜찮아요. 잘하고 있어요"; break;
                    case 5:
                        nabi1 = "힘들었던 하루였군요. 잘 버텨냈어요!"; break;
                } break;
            case 2:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "어제보다 기분이 많이 좋아졌어요. 앞으로도 파이팅!"; break;
                    case 1:
                        nabi1 = "만족스러운 하루였군요! 좋아요. 내일도 기대되는데요?"; break;
                    case 2:
                        nabi1 = "조금만 더 힘을 내요! 나비가 항상 응원하고 있어요"; break;
                    case 3:
                        nabi1 = "오늘 우울한 일이 있었나요? 마음껏 우울해도 좋아요. 함께 이겨내봐요."; break;
                    case 4:
                        nabi1 = "괜찮아요. 모두가 한 번쯤은 겪는 감정이에요. 다 지나가게 될거에요"; break;
                    case 5:
                        nabi1 = "오늘보다 내일은 더 행복한 하루가 될거에요. 나비가 도와줄게요."; break;
                } break;
            case 3:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "오늘 좋은 일이 있었나요? 당신이 행복해하니 곧 나비에게도 좋은 일이 찾아올 것 같아요~"; break;
                    case 1:
                        nabi1 = "어제보다 기분이 많이 좋아졌어요 앞으로도 나비와 함께 해요!"; break;
                    case 2:
                        nabi1 = "한 걸음 한 걸음 걷다 보면, 언젠가는 목표에 도달해있을 거예요. 나비가 응원합니다!"; break;
                    case 3:
                        nabi1 = "나이스 ~ ! 이렇게만 꾸준히 해나가요."; break;
                    case 4:
                        nabi1 = "오늘 하루 슬픈일이 있었군요. 오늘은 슬퍼해도 괜찮아요."; break;
                    case 5:
                        nabi1 = "오늘 하루 일진이 사나웠나요~ 나비가 다 물리쳐 줄게요! 당신은 웃는 모습이 제일 아름답답니다 :)"; break;
                } break;
            case 4:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "어제의 나를 이겨냈군요! 한층 단단해진 당신을 칭찬합니다~"; break;
                    case 1:
                        nabi1 = "잘 해나가고 있어요. 이러한 발전은 소중한 밑거름이 될 거에요."; break;
                    case 2:
                        nabi1 = "어제보다 기분이 많이 좋아졌어요 앞으로도 파이팅!"; break;
                    case 3:
                        nabi1 = "만족스러운 하루였군요! 오늘 하루도 화이팅!"; break;
                    case 4:
                        nabi1 = "나이스 ~ ! 이렇게만 꾸준히 해나가요."; break;
                    case 5:
                        nabi1 = "오늘 하루 슬픈일이 있었군요. 괜찮아요 다 잘될거에요."; break;
                } break;
            case 5:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "어제 힘들었던 감정을 완벽히 이겨냈군요. 정말 대단해요!"; break;
                    case 1:
                        nabi1 = "어제 힘들었던 감정을 이겨냈군요. 정말 대단해요!"; break;
                    case 2:
                        nabi1 = "어제보다 기분이 많이 좋아졌어요. 앞으로 함께 더 노력해봐요!"; break;
                    case 3:
                        nabi1 = "좋아요! 행복에 한발 더 가까워졌어요. 앞으로도 함께 해요."; break;
                    case 4:
                        nabi1 = "좋아요! 행복에 한발 더 가까워졌어요. 앞으로도 함께 해요."; break;
                    case 5:
                        nabi1 = "나이스 ~ ! 이렇게만 꾸준히 해나가요."; break;
                } break;
        }

        // 산책, 명상 성취도따라서 한마디
        // 산책 성취도 계산
        Integer walkPercent = (steps/walkingMax)*100;
        if(walkPercent == 0){
            if(meditate == 0){
                nabi2 = "오늘은 활동을 하지 않았네요. 내일은 힘내서 해봐요!";
            }else if(meditate <= 2){
                nabi2 = "하나씩 이루어나가는 모습이 보기 좋아요";
            }else if(meditate <= 4){
                nabi2 = "명상을 좀 더 해봐요! 산책도 잊지 말아요.";
            }else if(meditate <= 6){
                nabi2 = "명상은 마음을 단련할 수 있는 좋은 기회죠. 내일은 산책 어때요?";
            }else if(meditate <= 8){
                nabi2 = "명상을 많이 했군요! 산책은 몸과 마음의 건강에 좋아요. 산책을 조금 더 해볼까요?";
            }else if(meditate >= 10){
                nabi2 = "목표 명상시간 달성! 산책도 해보는건 어떨까요?";
            }
        }
        else if(walkPercent<=20){
            if(meditate == 0){
                nabi2 = "하나씩 이루어나가는 모습이 보기 좋아요.";
            }else if(meditate <= 2){
                nabi2 = "좋아요! 내일은 좀 더 활동에 집중해볼까요?";
            }else if(meditate <= 4){
                nabi2 = "산책과 명상을 하려고 노력하셨네요 좀 더 해봐요!";
            }else if(meditate <= 6){
                nabi2 = "명상은 마음을 단련할 수 있는 좋은 기회죠. 내일은 산책에 힘을 내볼까요?";
            }else if(meditate <= 8){
                nabi2 = "명상을 많이 했군요! 산책은 몸과 마음의 건강에 좋아요. 산책을 조금 더 해볼까요?";
            }else if(meditate >= 10){
                nabi2 = "목표 명상시간 달성! 조금 더 산책해봐요.";
            }
        }
        else if(walkPercent <= 40){
            if(meditate == 0){
                nabi2 = "내일은 명상도 함께 해보는건 어때요?";
            }else if(meditate <= 2){
                nabi2 = "발전해나가는 모습이 보기 좋아요. 내일은 명상 어때요?";
            }else if(meditate <= 4){
                nabi2 = "발전해나가는 모습이 보기 좋아요";
            }else if(meditate <= 6){
                nabi2 = "시도하는 모습이 보기 좋아요. 많이 노력하셨네요. 조금만 더 화이팅 합시다!";
            }else if(meditate <= 8){
                nabi2 = "명상 수고했어요! 하지만 우리 조금만 더  걸어봐요~";
            }else if(meditate >= 10){
                nabi2 = "목표 명상시간 달성! 조금만 더 걸어볼까요?";
            }
        }
        else if(walkPercent <= 60){
            if(meditate == 0){
                nabi2 = "조금만 더 걸어볼까요? 명상도 함께 해요";
            }else if(meditate <= 2){
                nabi2 = "산책은 내 몸을 건강하게 할 수 있죠. 명상은 마음을 단련할 수 있습니다. 함께 해봐요~";
            }else if(meditate <= 4){
                nabi2 = "산책은 내 몸을 건강하게 할 수 있죠.  내일은 명상 어때요?";
            }else if(meditate <= 6){
                nabi2 = "산책과 명상, 동시에 하기 힘들죠? 하지만 잘하고 있어요. 조금만 더 노력해 봅시다!";
            }else if(meditate <= 8){
                nabi2 = "오늘은 명상에 많은 시간을 썼네요. 내일은 산책을 더 많이 해볼까요?";
            }else if(meditate >= 10){
                nabi2 = "목표 명상시간 달성! 산책도 화이팅!";
            }
        }
        else if(walkPercent <= 80){
            if(meditate == 0){
                nabi2 = "산책을 많이 했네요! 명상도 함께 해봐요.";
            }else if(meditate <= 2){
                nabi2 = "산책을 많이 했네요! 명상도 함께 해봐요.";
            }else if(meditate <= 4){
                nabi2 = "조금만 더 하면 목표 산책량 달성! 명상도 빼먹지 말아요~";
            }else if(meditate <= 6){
                nabi2 = "산책을 많이 했네요! 명상도 꾸준히 해봐요.";
            }else if(meditate <= 8){
                nabi2 = "이렇게만 계속 해볼까요?  좋은 결과가 있을거에요.";
            }else if(meditate >= 10){
                nabi2 = "목표 명상시간 달성! 산책도 조금만 힘내요.";
            }
        }
        else if(walkPercent >= 100){
            if(meditate == 0){
                nabi2 = "목표 산책량 달성! 명상도 함께 해봐요.";
            }else if(meditate <= 2){
                nabi2 = "목표 산책량 달성! 명상도 꾸준히 해봐요.";
            }else if(meditate <= 4){
                nabi2 = "목표 산책량 달성! 내일은 명상에 집중해볼까요?";
            }else if(meditate <= 6){
                nabi2 = "목표 산책량 달성! 명상도 함께 완료해봐요.";
            }else if(meditate <= 8){
                nabi2 = "목표 산책량 달성! 명상도 조금만 힘내요.";
            }else if(meditate >= 10){
                nabi2 = "일일 산책량도 채우고 명상까지 완료했네요! 잘했어요!";
            }
        }


        // 우울한 날씨였을때, 기분정도따라서 한마디
        Integer gloomyWeather = PreferenceHelper.getGloomy(this);
        if(gloomyWeather==today_weather){
            switch (todayMood){
                case 0:
                    nabi3 = "우울한 날씨였잖아요. 다음에는 나비와 함께 이겨내볼까요?"; break;
                case 1:
                    nabi3 = "우울한 날씨였잖아요. 오늘 하루 잘 이겨냈어요."; break;
                case 2:
                    nabi3 = "우울한 날씨였는데도 오늘 하루 잘 버텼어요. 대단해요."; break;
                case 3:
                    nabi3 = "우울한 날씨였지만 잘 이겨냈어요. 앞으로는 좋은 일이 있을거에요."; break;
                case 4:
                    nabi3 = "우울한 날씨도 잘 이겨냈어요. 오늘 하루도 수고 많았어요!"; break;
                case 5:
                    nabi3 = "사실 오늘 우울한 날씨였는데, 완벽하게 극복하셨네요! 당신이 대견해요~ 나비는 너무 행복하답니다!"; break;
            }
        }



    }




}
