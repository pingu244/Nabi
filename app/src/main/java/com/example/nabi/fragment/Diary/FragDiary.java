package com.example.nabi.fragment.Diary;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nabi.MainActivity;
import com.example.nabi.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

// Diary 탭 fragment
public class FragDiary extends Fragment {

    @Nullable
    View view;

    TabLayout tabLayout;
    ViewPager viewPager;
    MainAdapter adapter;
    DiaryList_Clear clearDiary;

    FragDiary_cal fragDiary_cal;
    FragDiary_list fragDiary_list;

    FragmentManager childFragmentManager;
    boolean start = true;


    @Override
    public void onStart() {
        super.onStart();
        start = true;

//        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
//        t.setReorderingAllowed(false);
//        t.detach(this).attach(this).commitAllowingStateLoss();

//        getFragmentManager().beginTransaction().detach(this).commit();
//        getFragmentManager().beginTransaction().attach(this).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clearDiary = new DiaryList_Clear();

        tabLayout = getActivity().findViewById(R.id.Diary_tab_layout);
        viewPager = getActivity().findViewById(R.id.Diary_view_pager);

        fragDiary_cal = new FragDiary_cal();
        fragDiary_list = new FragDiary_list();
        childFragmentManager = getChildFragmentManager();


        // adapter 초기화
        adapter = new MainAdapter(childFragmentManager);
        // add fragments
        adapter.AddFragment(fragDiary_cal, "캘린더");
        adapter.AddFragment(fragDiary_list,"날씨 일기");
        //set adapter
        viewPager.setAdapter(adapter);
        //connect tab layout with view pager
        tabLayout.setupWithViewPager(viewPager);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(start)
                {
                    if(position==0)
                        getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.diarybg);
                    else
                        getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_clear);
                    start = false;
                }

            }

            @Override
            public void onPageSelected(int position) { //페이지 넘어갈 때

                switch (position) {
                    case 0:
                        getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.diarybg);
                        break;
                    case 1:
                        switch (fragDiary_list.diaryList_bg)
                        {
                            case 0:
                                getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_clear);
                                break;
                            case 1:
                                getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_littlecloud);
                                break;
                            case 2:
                                getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_cloud);
                                break;
                            case 3:
                                getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_rain);
                                break;
                            case 4:
                                getActivity().findViewById(R.id.diaryBg).setBackgroundResource(R.drawable.bg_snow);
                                break;
                        }
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            private void refresh() {
                adapter.notifyDataSetChanged();
            }

        });



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_diary, container,false);

        return view;
    }






    // viewpager 관련 클래스
    public static class MainAdapter extends FragmentPagerAdapter{
        // arrayList 초기화
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        public void AddFragment(Fragment fragment, String s){
            // add fragment
            fragmentArrayList.add(fragment);
            //add string
            stringArrayList.add(s);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //return fragment position
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            //return fragment list size
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            // return tab title
            return stringArrayList.get(position);
        }

        public int getItemPosition(@NonNull Object object) {

            return POSITION_NONE;

        }

    }

}