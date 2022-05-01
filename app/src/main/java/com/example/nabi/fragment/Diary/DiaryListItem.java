package com.example.nabi.fragment.Diary;

import android.graphics.Movie;

// 일기 목록 리사이클러뷰 구현에 사용됨. 데이터 모델 생성
public class DiaryListItem implements Comparable<DiaryListItem> {
    private String keyword;
    private String date;
    private Integer mood, date_day;

    public DiaryListItem(String date, String keyword, Integer mood, Integer date_day){
        this.date = date;
        this.keyword = keyword;
        this.mood = mood;
        this.date_day = date_day;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMood(){ return mood; }
    public void setMood(Integer mood){ this.mood = mood; }

    // 정렬위한 함수
    @Override
    public int compareTo(DiaryListItem diaryListItem) {
        return this.date_day.compareTo(diaryListItem.date_day);
    }
}
