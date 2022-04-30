package com.example.nabi.fragment.Diary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nabi.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

//일기 목록 어댑터
public class DiaryListViewAdapter extends RecyclerView.Adapter<DiaryListViewAdapter.ViewHolder> {

    private ArrayList<DiaryListItem> diaryList = new ArrayList<DiaryListItem>();

    @NonNull
    @Override
    public DiaryListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diarylist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(diaryList.get(position));
//        holder.keyword.setText(diaryList.get(position).getKeyword());
        holder.date.setText(diaryList.get(position).getDate());
        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public void setItems(ArrayList<DiaryListItem> items) {
        diaryList = items;
    }
    Context context;

    // 뭔지 모르겠는데 감정 단어들 넣으려면 context가 필요한데 외국인이 이러랬음
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    // 화면에 표시될 아이템뷰 저장
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutDiaryList;
        TextView date;
        Integer moods;
        FlexboxLayout mView;

        ImageView list_mood[] = new ImageView[5];
        Integer mood_ids[] = {R.id.DiaryList_one, R.id.DiaryList_two, R.id.DiaryList_three,R.id.DiaryList_four,R.id.DiaryList_five};


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutDiaryList = itemView.findViewById(R.id.layoutDiaryList);
            date = (TextView) itemView.findViewById(R.id.diaryDate);

            for(int i = 0; i<5; i++)
            {
                final int index;
                index = i;
                list_mood[index] = itemView.findViewById(mood_ids[index]);
            }


            // 클릭했을 때 이벤트
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    int pos = getAdapterPosition();
                    // 리스너 객체의 메서드 호출
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v, pos);
                    }

                }
            });

        }


        void onBind(DiaryListItem item){
            date.setText(item.getDate());

            mView = itemView.findViewById(R.id.DiaryList_selectMoods);    // 감정단어 들어가는 늘어나는 레이아웃
            mView.removeAllViews();
            String[] array = item.getKeyword().split(",");
            for(int i = 0; i<array.length; i++)
            {
                TextView txt = new TextView(context);

                // 단어 들어가는 그 하나의 타원
                FlexboxLayout.LayoutParams pm = new FlexboxLayout.LayoutParams
                        (FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                pm.setMargins(5,5,5,5);

                txt.setPadding(20,9,20,9);
                txt.setGravity(View.TEXT_ALIGNMENT_CENTER);

                txt.setText(array[i]); //view에 들어갈 텍스트를 지정(String)
                txt.setTextSize(11);
                txt.setBackgroundResource(R.drawable.q3_moodword_normal);

                txt.setLayoutParams(pm); //앞서 설정한 레이아웃파라미터를 버튼에 적용
                ViewCompat.setBackgroundTintList(txt, ColorStateList.valueOf(Color.parseColor("#686868"))); // 배경 색 지정
                txt.setTextColor(Color.parseColor("#ffffff"));  // 글씨 색 지정
                mView.addView(txt); //지정된 뷰에 셋팅완료된 textview를 추가
            }


            // 1번질문 감정정도 조절
            moods = item.getMood();
            for(int i = 0; i<moods; i++)
                list_mood[i].setImageResource(R.drawable.mood_circle);



        }

        // 아이템들을 담은 리니어레이아웃을 보여주는 메서드.
        public void setLayout(){
            layoutDiaryList.setVisibility(View.VISIBLE);
        }
    }



    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}