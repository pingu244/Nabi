package com.example.nabi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    String sql;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "naviDB.db";
    private static final String TAG = "DBHelper";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        //데이터베이스 -> 테이블 -> 컬럼 -> 값

        // 회원 테이블
        db.execSQL("CREATE TABLE user_inform (user_no INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT UNIQUE NOT NULL, user_pw TEXT NOT NULL," +
                "user_name TEXT NOT NULL, user_bdi TEXT NOT NULL, user_sad TEXT, user_weather TEXT)");

        // 다이어리 테이블
        db.execSQL("CREATE TABLE diary_post (post_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, diary_title TEXT NOT NULL, content_1 TEXT, content_2 TEXT, " +
                "content_3 TEXT, content_4 TEXT, diary_mood INTEGER, diary_keyword TEXT, diary_weather INTEGER, reporting_date CHAR(20) NOT NULL" +
                ",FOREIGN KEY (user_id) REFERENCES user_inform (user_id) on DELETE CASCADE )");

        // 산책 테이블
        db.execSQL("CREATE TABLE walking_report (walking_date TEXT PRIMARY KEY, user_id TEXT, walking_record INTEGER NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES user_inform (user_id) on DELETE CASCADE)");

        // 명상 테이블
        db.execSQL("CREATE TABLE meditation_report (meditation_date TEXT PRIMARY KEY, user_id TEXT, meditation_record INTEGER NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES user_inform (user_id) on DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE user_inform;");
        db.execSQL("DROP TABLE diary_post;");
        db.execSQL("DROP TABLE walking_report;");
        db.execSQL("DROP TABLE meditation_report;");


    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}