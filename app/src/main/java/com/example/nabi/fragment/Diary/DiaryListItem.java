package com.example.nabi.fragment.Diary;
// 일기 목록 리사이클러뷰 구현에 사용됨. 데이터 모델 생성
public class DiaryListItem {
    private String keyword;
    private String date;
    private Integer mood;

    public DiaryListItem(String date, String keyword, Integer mood){
        this.date = date;
        this.keyword = keyword;
        this.mood = mood;
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
}
