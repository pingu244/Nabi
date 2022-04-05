package com.example.nabi.fragment.PushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.nabi.LoginActivity;
import com.example.nabi.MainActivity;
import com.example.nabi.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.example.nabi.fragment.PushNotification.Constants.A_MORNING_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.B_MORNING_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.A_NIGHT_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.B_NIGHT_EVENT_TIME;
import static com.example.nabi.fragment.PushNotification.Constants.WORK_A_NAME;
import static com.example.nabi.fragment.PushNotification.Constants.WORK_B_NAME;
import static com.example.nabi.fragment.PushNotification.Constants.KOREA_TIMEZONE;
import static com.example.nabi.fragment.PushNotification.Constants.NOTIFICATION_CHANNEL_ID;


// https://8iggy.tistory.com/65?category=906411
// https://github.com/khygu0919/blogging-practice/tree/master/MyNotification/app/src/main/java/com/example/mynotification


public class NotificationHelper {
    private Context mContext;
    private static final Integer WORK_A_NOTIFICATION_CODE = 0;
    private static final Integer WORK_B_NOTIFICATION_CODE = 1;
    NotificationHelper(Context context) {
        mContext = context;
    }

    public static void setScheduledNotification(WorkManager workManager) {
        setANotifySchedule(workManager);
        setBNotifySchedule(workManager);
    }

    private static void setANotifySchedule(WorkManager workManager) {
        // Event 발생시 WorkerA.class 호출
        // 알림 활성화 시점에서 반복 주기 이전에 있는 가장 빠른 알림 생성
        OneTimeWorkRequest aWorkerOneTimePushRequest = new OneTimeWorkRequest.Builder(WorkerA.class).build();
        // 가장 가까운 알림시각까지 대기 후 실행, 12시간 간격 반복 5분 이내 완료
        PeriodicWorkRequest aWorkerPeriodicPushRequest =
                new PeriodicWorkRequest.Builder(WorkerA.class, 12, TimeUnit.HOURS, 5, TimeUnit.MINUTES)
                        .build();
        try {
            // workerA 정보 조회
            List<WorkInfo> aWorkerNotifyWorkInfoList = workManager.getWorkInfosForUniqueWorkLiveData(WORK_A_NAME).getValue();
            for (WorkInfo workInfo : aWorkerNotifyWorkInfoList) {
                // worker의 동작이 종료된 상태라면 worker 재등록
                if (workInfo.getState().isFinished()) {
                    workManager.enqueue(aWorkerOneTimePushRequest);
                    workManager.enqueueUniquePeriodicWork(WORK_A_NAME, ExistingPeriodicWorkPolicy.KEEP, aWorkerPeriodicPushRequest);
                }
            }
        } catch (NullPointerException nullPointerException) {
            // 알림 worker가 생성된 적이 없으면 worker 생성
            workManager.enqueue(aWorkerOneTimePushRequest);
            workManager.enqueueUniquePeriodicWork(WORK_A_NAME, ExistingPeriodicWorkPolicy.KEEP, aWorkerPeriodicPushRequest);
        }
    }

    private static void setBNotifySchedule(WorkManager workManager) {
        // Event 발생 시 WorkerB.class 호출
        OneTimeWorkRequest bWorkerOneTimePushRequest = new OneTimeWorkRequest.Builder(WorkerB.class).build();
        PeriodicWorkRequest bWorkerPeriodicPushRequest =
                new PeriodicWorkRequest.Builder(WorkerB.class, 12, TimeUnit.HOURS, 5, TimeUnit.MINUTES)
                        .build();
        try {
            List<WorkInfo> bWorkerNotifyWorkInfoList = workManager.getWorkInfosForUniqueWorkLiveData(WORK_B_NAME).getValue();
            for (WorkInfo workInfo : bWorkerNotifyWorkInfoList) {
                if (workInfo.getState().isFinished()) {
                    workManager.enqueue(bWorkerOneTimePushRequest);
                    workManager.enqueueUniquePeriodicWork(WORK_B_NAME, ExistingPeriodicWorkPolicy.KEEP, bWorkerPeriodicPushRequest);
                }
            }
        } catch (NullPointerException nullPointerException) {
            workManager.enqueue(bWorkerOneTimePushRequest);
            workManager.enqueueUniquePeriodicWork(WORK_B_NAME, ExistingPeriodicWorkPolicy.KEEP, bWorkerPeriodicPushRequest);
        }
    }

    // 현재시각이 알림 범위에 해당하지 않으면 딜레이 리턴
    public static long getNotificationDelay(String workName) {
        long pushDelayMillis = 0;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA);
        long currentMillis = cal.getTimeInMillis();
        if (workName.equals(WORK_A_NAME)) {
            // 현재 시각이 22:00보다 크면 다음 날 오전 알림, 현재 시각이 22:00 전인지 10:00 전인지에 따라 알림 딜레이 설정
            if (cal.get(Calendar.HOUR_OF_DAY) >= Constants.A_NIGHT_EVENT_TIME) {
                Calendar nextDayCal = getScheduledCalender(A_MORNING_EVENT_TIME);
                nextDayCal.add(Calendar.DAY_OF_YEAR, 1);
                pushDelayMillis = nextDayCal.getTimeInMillis() - currentMillis;

            } else if (cal.get(Calendar.HOUR_OF_DAY) >= A_MORNING_EVENT_TIME && cal.get(Calendar.HOUR_OF_DAY) < A_NIGHT_EVENT_TIME) {
                pushDelayMillis = getScheduledCalender(A_NIGHT_EVENT_TIME).getTimeInMillis() - currentMillis;

            } else if (cal.get(cal.get(Calendar.HOUR_OF_DAY)) < A_MORNING_EVENT_TIME) {
                pushDelayMillis = getScheduledCalender(A_MORNING_EVENT_TIME).getTimeInMillis() - currentMillis;
            }
//            if (cal.get(Calendar.HOUR_OF_DAY) >= Constants.A_NIGHT_EVENT_TIME) {
//                Calendar nextDayCal = getScheduledCalender(A_NIGHT_EVENT_TIME);
//                nextDayCal.add(Calendar.DAY_OF_YEAR, 1);
//                pushDelayMillis = nextDayCal.getTimeInMillis() - currentMillis;
//
//            } else if (cal.get(Calendar.HOUR_OF_DAY) < A_NIGHT_EVENT_TIME) {
//                pushDelayMillis = getScheduledCalender(A_NIGHT_EVENT_TIME).getTimeInMillis() - currentMillis;
//
//            }
        } else if (workName.equals(WORK_B_NAME)) {
            // 현재 시각이 21:00보다 크면 다음 날 오전 알림, 현재 시각이 21:00 전인지 09:00 전인지에 따라 알림 딜레이 설정
            if (cal.get(Calendar.HOUR_OF_DAY) >= B_NIGHT_EVENT_TIME) {
                Calendar nextDayCal = getScheduledCalender(B_MORNING_EVENT_TIME);
                nextDayCal.add(Calendar.DAY_OF_YEAR, 1);
                pushDelayMillis = nextDayCal.getTimeInMillis() - currentMillis;

            } else if (cal.get(Calendar.HOUR_OF_DAY) >= B_MORNING_EVENT_TIME && cal.get(Calendar.HOUR_OF_DAY) < B_NIGHT_EVENT_TIME) {
                pushDelayMillis = getScheduledCalender(B_NIGHT_EVENT_TIME).getTimeInMillis() - currentMillis;

            } else if (cal.get(cal.get(Calendar.HOUR_OF_DAY)) < B_MORNING_EVENT_TIME) {
                pushDelayMillis = getScheduledCalender(B_MORNING_EVENT_TIME).getTimeInMillis() - currentMillis;
            }
        }
        return pushDelayMillis;
    }

    public void createNotification(String workName, int randomValue) {
        // 클릭 시 LoginActivity 호출
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); // 대기열에 이미 있다면 MainActivity가 아닌 앱 활성화
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Notificatoin을 이루는 공통 부분 정의
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.btnclear) // 기본 제공되는 이미지
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true); // 클릭 시 Notification 제거

        // 매개변수가 WorkerA라면
        if (workName.equals(WORK_A_NAME)) {
            // Notification 클릭 시 동작할 Intent 입력, 중복 방지를 위해 FLAG_CANCEL_CURRENT로 설정, CODE를 다르게하면 Notification 개별 생성
            // Code가 같으면 같은 알림으로 인식하여 갱신작업 진행
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, WORK_A_NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            int gloomyWeather = PreferenceHelper.getGloomy(mContext);
            int tomorrowWeather = PreferenceHelper.getTomorrowWeather(mContext);
            boolean gopushalarm = false;

            // 사용자 우울한 날씨와 내일 날씨가 같다면 true
            if (gloomyWeather == tomorrowWeather)
                gopushalarm = true;

            if(gopushalarm)
            {
                ArrayList<String> message = new ArrayList<>();
                switch (gloomyWeather)
                {
                    case 0:
                        message.add("맑은 날, 기분좋게 웃는 일만 일어나길");
                        message.add("어느때보다 환한 햇살이 비추는 날! 환하게 웃는 날이 되길");
                        message.add("맑은 날 맑은 웃음으로 보내봐요");
                        message.add("하늘이 푸른 맑은 날이네요 날씨처럼 기분 좋은 하루 보내세요");
                        message.add("창문 너머로 들어오는 햇살이 기분좋은 날이에요");
                        break;
                    case 1:
                        message.add("흐린 날이지만 기분은 맑은 날 되세요");
                        message.add("흐린 날, 친구들과 함께하며 기분이 흐려지지 않게 보내보는 건 어떤가요?");
                        message.add("약간 흐린 날이네요. 따뜻한 차를 마시며 여유있게 보내봐요");
                        message.add("흐린 날이지만 새로운 에너지로 힘차게 출발해봐요");
                        message.add("약간 흐린 날이네요. 다음 날은 맑길 기대하는 설렘으로 하루를 보내봐요!");
                        break;
                    case 2:
                        message.add("흐린날이에요. 흐린 날이 더 집중이 잘되는 장점이 있는 것 같아요. 날은 흐리지만 더욱 파이팅해요");
                        message.add("흐리지만 즐거운 마음으로 좋은 하루 보내시길");
                        message.add("흐린 날이네요. 유쾌한 생각으로 잘 마무리하셨으면 좋겠어요");
                        message.add("흐린 날, 그 위로 푸른 하늘이 있듯이 마음은 푸르게 가져봐요");
                        message.add("흐리지만 짜증내지 말고 웃으면서 좋은 하루 보내세요!");
                        break;
                    case 3:
                        message.add("내리는 비로 마음에 촉촉한 여유가 생기길");
                        message.add("시끄러운 소음이 빗소리에 묻혀 생각을 정리하기 딱 좋은 날이네요");
                        message.add("빗소리를 들으며 편안하게 명상해보세요");
                        message.add("우산 꼭 챙기시고 빗길 조심하세요");
                        message.add("비가 와서 운치있는 날이에요");
                        break;
                    case 4:
                        message.add("눈이 오네요. 따뜻하게 입으시고 포근한 하루 보내세요");
                        message.add("눈 오는 겨울의 풍경을 즐겨봐요");
                        message.add("눈이와서 풍경이 더욱 멋진 날이에요. 좋아하는 사람들과 풍경을 즐겨봐요");
                        message.add("눈이 와서 추운 날, 마음만은 따뜻하게 보내봐요");
                        message.add("눈 내리는 풍경을 바라보며 좋은 하루 보내세요");
                        break;
                }

                // Notification 제목, 컨텐츠 설정
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message.get(randomValue)))
                        .setContentTitle("내일은").setContentText(message.get(randomValue))
                        .setContentIntent(pendingIntent);
            }

            boolean noti_boolean = PreferenceHelper.getNotification_TimeBoolean(mContext);  // 알림 한번만 가게끔 하는 장치
            if (notificationManager != null && gopushalarm && noti_boolean) {
                notificationManager.notify(WORK_A_NOTIFICATION_CODE, notificationBuilder.build());
                PreferenceHelper.setNotification_TimeBoolean(mContext, false);
            }
        } else if (workName.equals(WORK_B_NAME)) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, WORK_B_NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            notificationBuilder.setContentTitle("WorkerB Notification").setContentText("set a Notification contents")
                    .setContentIntent(pendingIntent);

            if (notificationManager != null) {
                notificationManager.notify(WORK_B_NOTIFICATION_CODE, notificationBuilder.build());
            }
        }
    }

    public static Boolean isNotificationChannelCreated(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                return notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null;
            }
            return true;
        } catch (NullPointerException nullException) {
            Toast.makeText(context, "푸시 알림 기능에 문제가 발생했습니다. 앱을 재실행해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // 푸시 알림 허용 및 사용자에 의해 알림이 꺼진 상태가 아니라면 푸시 알림 백그라운드 갱신
    public static void refreshScheduledNotification(Context context) {
        try {
            Boolean isNotificationActivated = PreferenceHelper.getBoolean(context, Constants.SHARED_PREF_NOTIFICATION_KEY);
            if (isNotificationActivated) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                boolean isNotifyAllowed;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int channelImportance = notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID).getImportance();
                    isNotifyAllowed = channelImportance != NotificationManager.IMPORTANCE_NONE;
                } else {
                    isNotifyAllowed = NotificationManagerCompat.from(context).areNotificationsEnabled();
                }
                if (isNotifyAllowed) {
                    NotificationHelper.setScheduledNotification(WorkManager.getInstance(context));
                }
            }
        } catch (NullPointerException nullException) {
            Toast.makeText(context, "푸시 알림 기능에 문제가 발생했습니다. 앱을 재실행해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }

    // 한번 실행 시 이후 재호출해도 동작 안함
    public static void createNotificationChannel(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // NotificationChannel 초기화
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel
                notificationChannel.setDescription("푸시알림");
                notificationChannel.enableLights(true); // 화면활성화 설정
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500}); // 진동패턴 설정
                notificationChannel.enableVibration(true); // 진동 설정
                notificationManager.createNotificationChannel(notificationChannel); // channel 생성
            }
        } catch (NullPointerException nullException) {
            // notificationManager null 오류 raise
            Toast.makeText(context, "푸시 알림 채널 생성에 실패했습니다. 앱을 재실행하거나 재설치해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }

    public static Calendar getScheduledCalender(Integer scheduledTime) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(KOREA_TIMEZONE), Locale.KOREA);
        cal.set(Calendar.HOUR_OF_DAY, scheduledTime);   // HOUR_OF_DAY: 현재 시간 (24시간제)
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }
}
