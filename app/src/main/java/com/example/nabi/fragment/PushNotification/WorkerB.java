package com.example.nabi.fragment.PushNotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.example.nabi.fragment.PushNotification.Constants.B_MORNING_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.B_NIGHT_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.KOREA_TIMEZONE;

public class WorkerB extends Worker {

    public WorkerB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        long currentMillis = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA).getTimeInMillis();

        // 알림 범위에 해당하는지 기준 설정
        Calendar eventCal = NotificationHelper.getScheduledCalender(B_MORNING_EVENT_TIME);
        long morningNotifyMinRange = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY, Constants.NOTIFICATION_INTERVAL_HOUR);
        long morningNotifyMaxRange = eventCal.getTimeInMillis();

        eventCal.set(Calendar.HOUR_OF_DAY, B_NIGHT_EVENT_TIME);
        long nightNotifyMinRange = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY, Constants.NOTIFICATION_INTERVAL_HOUR);
//        eventCal.add(Calendar.HOUR_OF_DAY);
        long nightNotifyMaxRange = eventCal.getTimeInMillis();

        // 현재 시각이 알림 범위에 해당하면 알림 생성
        // 그 외의 경우 가장 B 이벤트 예정 시각까지의 notificationDelay 계산하여 딜레이 호출
        boolean isMorningNotifyRange = morningNotifyMinRange <= currentMillis && currentMillis <= morningNotifyMaxRange;
        boolean isNightNotifyRange = nightNotifyMinRange <= currentMillis && currentMillis <= nightNotifyMaxRange;
        boolean isEventANotifyAvailable = isMorningNotifyRange || isNightNotifyRange;

        if (isNightNotifyRange) {
            // 현재 시각이 알림 범위에 해당하면 알림 생성
//            mNotificationHelper.createNotification(Constants.WORK_B_NAME, -1);
//
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference docRef = db.collection("users")
//                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//            Map<String, Object> hashMap = new HashMap<>();
//            hashMap.put("test", "8시"+Calendar.getInstance().get(Calendar.MINUTE));
//            docRef.set(hashMap, SetOptions.merge())
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
////                            mNotificationHelper.createNotification(Constants.WORK_B_NAME, randomValue);
//                        }
//
//                    });

        } else {
            long notificationDelay = NotificationHelper.getNotificationDelay(Constants.WORK_B_NAME);
            OneTimeWorkRequest workRequest =
                    new OneTimeWorkRequest.Builder(WorkerA.class)
                            .setInitialDelay(notificationDelay, TimeUnit.MILLISECONDS)
                            .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
        }
        return Result.success();
    }
}
