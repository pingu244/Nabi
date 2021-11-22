package com.example.nabi;
// 일기 목록 리사이클러뷰 구현에 사용됨. 데이터 모델 생성
public class DiaryListItem {
    private String title;
    private String date;

    public DiaryListItem(String date, String title){
        this.date = date;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
