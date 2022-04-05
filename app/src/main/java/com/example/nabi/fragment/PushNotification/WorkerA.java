package com.example.nabi.fragment.PushNotification;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.example.nabi.fragment.PushNotification.Constants.A_MORNING_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.A_NIGHT_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.KOREA_TIMEZONE;

public class WorkerA extends Worker {
    public WorkerA(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        long currentMillis = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA).getTimeInMillis();

        // 알림 범위(09:00-10:00, 22:00-23:00)에 해당하는지 기준 설정
        Calendar eventCal = NotificationHelper.getScheduledCalender(A_MORNING_EVENT_TIME);
        long morningNotifyMinRange = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY, Constants.NOTIFICATION_INTERVAL_HOUR);
        long morningNotifyMaxRange = eventCal.getTimeInMillis();

        eventCal.set(Calendar.HOUR_OF_DAY, A_NIGHT_EVENT_TIME);
        long nightNotifyMinRange = eventCal.getTimeInMillis();

        eventCal.add(Calendar.HOUR_OF_DAY, Constants.NOTIFICATION_INTERVAL_HOUR);
        long nightNotifyMaxRange = eventCal.getTimeInMillis();

        // 현재 시각이 오전 알림 범위에 해당하는지
        boolean isMorningNotifyRange = morningNotifyMinRange <= currentMillis && currentMillis <= morningNotifyMaxRange;
        // 현재 시각이 오후 알림 범위에 해당하는지
        boolean isNightNotifyRange = nightNotifyMinRange <= currentMillis && currentMillis <= nightNotifyMaxRange;
        // 현재 시각이 알림 범위에 해당여부
        boolean isEventANotifyAvailable = isMorningNotifyRange || isNightNotifyRange;



        Random random = new Random();
        int randomValue = random.nextInt(4);    // 랜덤값 줘서 멘트 랜덤으로



        if (isNightNotifyRange) {
            // 현재 시각이 알림 범위에 해당하면 알림 생성
            mNotificationHelper.createNotification(Constants.WORK_A_NAME, randomValue);
        } else {
            // 그 외의 경우 가장 빠른 A 이벤트 예정 시각까지의 notificationDelay 계산하여 딜레이 호출
            long notificationDelay = NotificationHelper.getNotificationDelay(Constants.WORK_A_NAME);
            OneTimeWorkRequest workRequest =
                    new OneTimeWorkRequest.Builder(WorkerB.class)
                            .setInitialDelay(notificationDelay, TimeUnit.MILLISECONDS)
                            .build();
            WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
            PreferenceHelper.setNotification_TimeBoolean(getApplicationContext(), true);
        }
        return Result.success();
    }
}
