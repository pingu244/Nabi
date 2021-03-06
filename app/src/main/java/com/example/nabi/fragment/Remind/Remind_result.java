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

    String nabi1 = "", nabi2="", nabi3=null;  // ?????? ?????????


    // ?????? ????????? ??????????????? ????????? ?????????
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
            mDialog.setMessage("??????????????????...");
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // ?????? ??? ??? ???????????? ?????????
            while(true) {
                try {
                    sleep(2000);
                    boolean go = true;
                    for(int i = 0; i<7; i++) {
                        if (!check[i])
                            go = false;
                        Log.v("remind___check", i+"??????: "+check[i]);
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
            // ?????? ??? ?????? ??? ????????? ????????? ?????????
            {
                // ??? ?????????
                ArrayList<Entry> values = new ArrayList<>();

                for (int i = 0; i < 7; i++)
                    Log.v("remind____dateTest", i + "?????? ?????????: " + seven_moods[i] + "?????????");

                // x??? ?????????
                XAxis xAxis = linechart.getXAxis();

                // XAxis??? ????????? String ???????????? (??????)
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



                // y??? ????????? label ?????????
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


                // ????????????
//        linechart.setBackgroundColor(Color.WHITE); // ????????? ?????? ??? ??????
                set1.setColor(Color.parseColor("#FFA6A6")); // ????????? ??? ??? ??????
                set1.setLineWidth(3f);
                set1.setMode(LineDataSet.Mode.LINEAR);
                set1.setCubicIntensity(0f);
                set1.setDrawCircles(false); // point circle ???????????? ??????
                set1.setValueTextColor(Color.TRANSPARENT);

                // ?????? ????????? ??????????????? ????????? ?????????
                linechart.setXAxisRenderer(new ColoredLabelXAxisRenderer(linechart.getViewPortHandler(), linechart.getXAxis(), linechart.getTransformer(YAxis.AxisDependency.LEFT)));

                // ?????? ?????????????????? legend ???????????? ??????
                linechart.getLegend().setEnabled(false);
                // description ???????????? ??????
                linechart.getDescription().setEnabled(false);
                // ?????? ?????????
                linechart.setTouchEnabled(false);
                // set data
                linechart.setData(data);
                linechart.invalidate();
            }

            // ????????????(????????????) ????????? ??????
            showPieChart();

            // ?????? ??????
            step.setText(steps+"??????");
//            walkingProgress.setMax(walkingMax);
            walkingProgress.setProgressMax(walkingMax);
            walkingProgress.setProgress(steps);
            if(steps>=walkingMax)
                walkSuccess.setVisibility(View.VISIBLE);

            // ?????? ??????
            meditateProgress.setProgressMax(10);
            meditate = (meditate / (1000*60)) % 60;
            meditateTime.setText(meditate+"???");
            meditateProgress.setProgress(meditate);
            if(meditate>=10)
                meditateSuccess.setVisibility(View.VISIBLE);

            // ?????? ?????????
            nabi_word();
            if(nabi3 == null)
                nabi_word.setText(nabi1+"\n\n"+nabi2);
            else
                nabi_word.setText(nabi1+"\n\n"+nabi3+"\n\n"+nabi2);


//            Toast.makeText(getApplicationContext(), "?????? ???????????????.", Toast.LENGTH_SHORT).show();
            try {
                sleep(1000);    // 1?????? ????????????
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

        // ???????????? ??????
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        Intent intent = getIntent(); /*????????? ??????*/
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
            Log.v("remind_dateTest2", sevendays[i]+"?????????");



        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // BDI ????????? ???????????? -> ?????? max??? ????????? ??????
        DocumentReference documentBDI = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("healing").document("BDI Result");

        // document???????????? ?????????
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

                                //BDI ?????? ????????? ?????? ?????? ?????? ??? ??????

                                if (bdiResult.equals("???????????? ?????? ??????")) {
                                    walkingMax = 3000;
                                } else if (bdiResult.equals("????????? ?????? ??????")) {
                                    walkingMax = 4000;
                                } else if (bdiResult.equals("?????? ?????? ??????")) {
                                    walkingMax = 5000;
                                } else if (bdiResult.equals("?????? ?????? ??????")) {
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

            // document???????????? ?????????
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

//                                    Log.v("remind___dateTest4242", (finalIndex)+"?????? ?????????: " + mymap.get("q1_mood").toString()+"?????????");

                                }

                                Log.v("remind___dateTest5252", (finalIndex)+"?????? ?????????: " + seven_moods[finalIndex]+"?????????");
                                check[finalIndex] = true;

                            }
                        }
                    });
        }



    }

    // ?????? ?????? ??????
    private static String AddDate(String strDate, int day) throws Exception {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy/M/d");
        Calendar cal = Calendar.getInstance();
        Date dt = dtFormat.parse(strDate); cal.setTime(dt);
        cal.add(Calendar.DATE, day);
        return dtFormat.format(cal.getTime());
    }

    // ???????????? ??????
    private static float Milliseconds(String strDate) throws Exception {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy/M/d");
        Date dt = dtFormat.parse(strDate);
        return dt.getTime();
    }

    // ??? ????????? ??????
    private void showPieChart(){

        int[] count = new int[6];
        for(int i = 0; i<6; i++)
            count[i] = 0;
        String[] array = result.split(",");
        for(int i = 0; i<array.length; i++)
        {
            switch (array[i]){
                case "??????":
                case "????????????":
                case "?????????":
                case "????????? ??????":
                case "????????????":
                case "??????":
                    count[0]++; break;
                case "???????????????":
                case "????????????":
                case "??????":
                case "???????????????":
                case "????????? ??????":
                case "?????????":
                    count[1]++; break;
                case "????????????":
                case "?????????":
                case "?????????":
                case "????????????":
                case "???????????????":
                case "?????????":
                    count[2]++; break;
                case "???????????????":
                case "????????????":
                case "?????????":
                case "?????????":
                case "?????????":
                case "????????????":
                    count[3]++; break;
                case "????????????":
                case "????????????":
                case "??????":
                case "????????????":
                case "????????????":
                case "????????????":
                    count[4]++; break;
                case "???????????????":
                case "???????????????":
                case "?????????":
                case "????????????":
                case "????????????":
                case "?????????":
                    count[5]++; break;

            }
        }



        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("??????",count[0]);
        typeAmountMap.put("??????",count[1]);
        typeAmountMap.put("??????",count[2]);
        typeAmountMap.put("??????",count[3]);
        typeAmountMap.put("??????",count[4]);
        typeAmountMap.put("?????????",count[5]);

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
        // ??? ???????????? ?????????????????? (??????0507)
        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i<pieDataSet.getEntryCount(); i++){
            if (pieDataSet.getEntryForIndex(i).getLabel().equals("??????"))
                colors.add(Color.parseColor("#EFE480"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("??????"))
                colors.add(Color.parseColor("#ADC3FF"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("??????"))
                colors.add(Color.parseColor("#B0E093"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("??????"))
                colors.add(Color.parseColor("#CCB4FF"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("??????"))
                colors.add(Color.parseColor("#FFAAA1"));
            else if(pieDataSet.getEntryForIndex(i).getLabel().equals("?????????"))
                colors.add(Color.parseColor("#ff5f67"));
        }
        pieDataSet.setColors(colors);


        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(false);

        // ????????? textview??? ?????????
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
                        keywordPercentage[i].setText("?????? "+percentage[i]+"%"); break;
                    case 1:
                        keywordPercentage[i].setText("?????? "+percentage[i]+"%"); break;
                    case 2:
                        keywordPercentage[i].setText("?????? "+percentage[i]+"%"); break;
                    case 3:
                        keywordPercentage[i].setText("?????? "+percentage[i]+"%"); break;
                    case 4:
                        keywordPercentage[i].setText("?????? "+percentage[i]+"%"); break;
                    case 5:
                        keywordPercentage[i].setText(percentage[i]+"%"); break;
                }



        // description, legend ???????????? ??????
        piechart.getDescription().setEnabled(false);
        piechart.setDrawHoleEnabled(false);
        Legend legend = piechart.getLegend();
        legend.setEnabled(false);
        piechart.setDrawEntryLabels(false); // ????????? ?????? ?????? ?????????

        piechart.setTouchEnabled(false);    // ?????? ?????????

        piechart.setData(pieData);
        piechart.invalidate();
    }



    // ????????? ???????????? ????????????....
    // ????????? ???????????? ?????? ?????????..
    void nabi_word(){

        // ?????? ?????? ????????? ?????? ?????? ?????? ???????????? ?????????
        Integer todayMood = seven_moods[0];
        Integer yesterdayMood = seven_moods[1];
        switch (todayMood){
            case 0:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "?????? ?????? ?????? ????????????? ??? ????????????."; break;
                    case 1:
                        nabi1 = "?????? ?????? ???????????? ????????????. ???????????? ??? ???????????????"; break;
                    case 2:
                        nabi1 = "?????? ???????????? ?????????, ?????? ?????? ???????????????. ????????? ????????? ?????? ??? ?????????. ????????? ???????????????"; break;
                    case 3:
                        nabi1 = "????????? ??? ?????????. ????????????. ????????? ???????????????"; break;
                    case 4:
                        nabi1 = "????????? ???????????? ?????? ?????????. ????????? ????????? ?????? ??? ?????????. ????????? ???????????????"; break;
                    case 5:
                        nabi1 = "?????? ?????? ?????? ??????. ????????? ????????? ????????? ????????? ??????????????? ????????? ??????. ????????? ???????????? ????????????."; break;
                } break;
            case 1:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "????????? ??????????????? ?????????. ??? ????????? ?????????!"; break;
                    case 1:
                        nabi1 = "????????????. ??????????????? ?????????. ????????? ????????? ???????????????"; break;
                    case 2:
                        nabi1 = "?????? ?????? ???????????? ????????????. ???????????? ??? ???????????????"; break;
                    case 3:
                        nabi1 = "?????? ???????????? ?????????. ????????? ???????????? ?????? ?????? ??????????????????! :)"; break;
                    case 4:
                        nabi1 = "?????? ????????? ?????? ?????????. ????????????. ????????? ?????????"; break;
                    case 5:
                        nabi1 = "???????????? ???????????????. ??? ???????????????!"; break;
                } break;
            case 2:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "???????????? ????????? ?????? ???????????????. ???????????? ?????????!"; break;
                    case 1:
                        nabi1 = "??????????????? ???????????????! ?????????. ????????? ???????????????????"; break;
                    case 2:
                        nabi1 = "????????? ??? ?????? ??????! ????????? ?????? ???????????? ?????????"; break;
                    case 3:
                        nabi1 = "?????? ????????? ?????? ????????????? ????????? ???????????? ?????????. ?????? ???????????????."; break;
                    case 4:
                        nabi1 = "????????????. ????????? ??? ????????? ?????? ???????????????. ??? ???????????? ????????????"; break;
                    case 5:
                        nabi1 = "???????????? ????????? ??? ????????? ????????? ????????????. ????????? ???????????????."; break;
                } break;
            case 3:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "?????? ?????? ?????? ????????????? ????????? ??????????????? ??? ??????????????? ?????? ?????? ????????? ??? ?????????~"; break;
                    case 1:
                        nabi1 = "???????????? ????????? ?????? ??????????????? ???????????? ????????? ?????? ??????!"; break;
                    case 2:
                        nabi1 = "??? ?????? ??? ?????? ?????? ??????, ???????????? ????????? ??????????????? ?????????. ????????? ???????????????!"; break;
                    case 3:
                        nabi1 = "????????? ~ ! ???????????? ????????? ????????????."; break;
                    case 4:
                        nabi1 = "?????? ?????? ???????????? ????????????. ????????? ???????????? ????????????."; break;
                    case 5:
                        nabi1 = "?????? ?????? ????????? ???????????????~ ????????? ??? ????????? ?????????! ????????? ?????? ????????? ?????? ?????????????????? :)"; break;
                } break;
            case 4:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "????????? ?????? ???????????????! ?????? ???????????? ????????? ???????????????~"; break;
                    case 1:
                        nabi1 = "??? ???????????? ?????????. ????????? ????????? ????????? ???????????? ??? ?????????."; break;
                    case 2:
                        nabi1 = "???????????? ????????? ?????? ??????????????? ???????????? ?????????!"; break;
                    case 3:
                        nabi1 = "??????????????? ???????????????! ?????? ????????? ?????????!"; break;
                    case 4:
                        nabi1 = "????????? ~ ! ???????????? ????????? ????????????."; break;
                    case 5:
                        nabi1 = "?????? ?????? ???????????? ????????????. ???????????? ??? ???????????????."; break;
                } break;
            case 5:
                switch (yesterdayMood){
                    case 0:
                        nabi1 = "?????? ???????????? ????????? ????????? ???????????????. ?????? ????????????!"; break;
                    case 1:
                        nabi1 = "?????? ???????????? ????????? ???????????????. ?????? ????????????!"; break;
                    case 2:
                        nabi1 = "???????????? ????????? ?????? ???????????????. ????????? ?????? ??? ???????????????!"; break;
                    case 3:
                        nabi1 = "?????????! ????????? ?????? ??? ??????????????????. ???????????? ?????? ??????."; break;
                    case 4:
                        nabi1 = "?????????! ????????? ?????? ??? ??????????????????. ???????????? ?????? ??????."; break;
                    case 5:
                        nabi1 = "????????? ~ ! ???????????? ????????? ????????????."; break;
                } break;
        }

        // ??????, ?????? ?????????????????? ?????????
        // ?????? ????????? ??????
        Integer walkPercent = (steps/walkingMax)*100;
        if(walkPercent == 0){
            if(meditate == 0){
                nabi2 = "????????? ????????? ?????? ????????????. ????????? ????????? ?????????!";
            }else if(meditate <= 2){
                nabi2 = "????????? ?????????????????? ????????? ?????? ?????????";
            }else if(meditate <= 4){
                nabi2 = "????????? ??? ??? ?????????! ????????? ?????? ?????????.";
            }else if(meditate <= 6){
                nabi2 = "????????? ????????? ????????? ??? ?????? ?????? ?????????. ????????? ?????? ??????????";
            }else if(meditate <= 8){
                nabi2 = "????????? ?????? ?????????! ????????? ?????? ????????? ????????? ?????????. ????????? ?????? ??? ?????????????";
            }else if(meditate >= 10){
                nabi2 = "?????? ???????????? ??????! ????????? ???????????? ?????????????";
            }
        }
        else if(walkPercent<=20){
            if(meditate == 0){
                nabi2 = "????????? ?????????????????? ????????? ?????? ?????????.";
            }else if(meditate <= 2){
                nabi2 = "?????????! ????????? ??? ??? ????????? ???????????????????";
            }else if(meditate <= 4){
                nabi2 = "????????? ????????? ????????? ?????????????????? ??? ??? ?????????!";
            }else if(meditate <= 6){
                nabi2 = "????????? ????????? ????????? ??? ?????? ?????? ?????????. ????????? ????????? ?????? ?????????????";
            }else if(meditate <= 8){
                nabi2 = "????????? ?????? ?????????! ????????? ?????? ????????? ????????? ?????????. ????????? ?????? ??? ?????????????";
            }else if(meditate >= 10){
                nabi2 = "?????? ???????????? ??????! ?????? ??? ???????????????.";
            }
        }
        else if(walkPercent <= 40){
            if(meditate == 0){
                nabi2 = "????????? ????????? ?????? ???????????? ??????????";
            }else if(meditate <= 2){
                nabi2 = "?????????????????? ????????? ?????? ?????????. ????????? ?????? ??????????";
            }else if(meditate <= 4){
                nabi2 = "?????????????????? ????????? ?????? ?????????";
            }else if(meditate <= 6){
                nabi2 = "???????????? ????????? ?????? ?????????. ?????? ??????????????????. ????????? ??? ????????? ?????????!";
            }else if(meditate <= 8){
                nabi2 = "?????? ???????????????! ????????? ?????? ????????? ???  ????????????~";
            }else if(meditate >= 10){
                nabi2 = "?????? ???????????? ??????! ????????? ??? ????????????????";
            }
        }
        else if(walkPercent <= 60){
            if(meditate == 0){
                nabi2 = "????????? ??? ???????????????? ????????? ?????? ??????";
            }else if(meditate <= 2){
                nabi2 = "????????? ??? ?????? ???????????? ??? ??? ??????. ????????? ????????? ????????? ??? ????????????. ?????? ?????????~";
            }else if(meditate <= 4){
                nabi2 = "????????? ??? ?????? ???????????? ??? ??? ??????.  ????????? ?????? ??????????";
            }else if(meditate <= 6){
                nabi2 = "????????? ??????, ????????? ?????? ?????????? ????????? ????????? ?????????. ????????? ??? ????????? ?????????!";
            }else if(meditate <= 8){
                nabi2 = "????????? ????????? ?????? ????????? ?????????. ????????? ????????? ??? ?????? ?????????????";
            }else if(meditate >= 10){
                nabi2 = "?????? ???????????? ??????! ????????? ?????????!";
            }
        }
        else if(walkPercent <= 80){
            if(meditate == 0){
                nabi2 = "????????? ?????? ?????????! ????????? ?????? ?????????.";
            }else if(meditate <= 2){
                nabi2 = "????????? ?????? ?????????! ????????? ?????? ?????????.";
            }else if(meditate <= 4){
                nabi2 = "????????? ??? ?????? ?????? ????????? ??????! ????????? ????????? ?????????~";
            }else if(meditate <= 6){
                nabi2 = "????????? ?????? ?????????! ????????? ????????? ?????????.";
            }else if(meditate <= 8){
                nabi2 = "???????????? ?????? ?????????????  ?????? ????????? ???????????????.";
            }else if(meditate >= 10){
                nabi2 = "?????? ???????????? ??????! ????????? ????????? ?????????.";
            }
        }
        else if(walkPercent >= 100){
            if(meditate == 0){
                nabi2 = "?????? ????????? ??????! ????????? ?????? ?????????.";
            }else if(meditate <= 2){
                nabi2 = "?????? ????????? ??????! ????????? ????????? ?????????.";
            }else if(meditate <= 4){
                nabi2 = "?????? ????????? ??????! ????????? ????????? ???????????????????";
            }else if(meditate <= 6){
                nabi2 = "?????? ????????? ??????! ????????? ?????? ???????????????.";
            }else if(meditate <= 8){
                nabi2 = "?????? ????????? ??????! ????????? ????????? ?????????.";
            }else if(meditate >= 10){
                nabi2 = "?????? ???????????? ????????? ???????????? ???????????????! ????????????!";
            }
        }


        // ????????? ???????????????, ????????????????????? ?????????
        Integer gloomyWeather = PreferenceHelper.getGloomy(this);
        if(gloomyWeather==today_weather){
            switch (todayMood){
                case 0:
                    nabi3 = "????????? ??????????????????. ???????????? ????????? ?????? ???????????????????"; break;
                case 1:
                    nabi3 = "????????? ??????????????????. ?????? ?????? ??? ???????????????."; break;
                case 2:
                    nabi3 = "????????? ?????????????????? ?????? ?????? ??? ????????????. ????????????."; break;
                case 3:
                    nabi3 = "????????? ??????????????? ??? ???????????????. ???????????? ?????? ?????? ???????????????."; break;
                case 4:
                    nabi3 = "????????? ????????? ??? ???????????????. ?????? ????????? ?????? ????????????!"; break;
                case 5:
                    nabi3 = "?????? ?????? ????????? ???????????????, ???????????? ??????????????????! ????????? ????????????~ ????????? ?????? ??????????????????!"; break;
            }
        }



    }




}
