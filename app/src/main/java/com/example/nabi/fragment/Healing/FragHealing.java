package com.example.nabi.fragment.Healing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.nabi.R;
import com.example.nabi.fragment.Diary.DiaryList_Clear;
import com.example.nabi.fragment.Diary.FragDiary;
import com.example.nabi.fragment.Diary.FragDiary_cal;
import com.example.nabi.fragment.Diary.FragDiary_list;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FragHealing extends Fragment {
    @Nullable
    View view;
    TabLayout tabLayout;
    ViewPager viewPager;
    MainAdapter adapter;
    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = getActivity().findViewById(R.id.healing_tab_layout);
        viewPager = getActivity().findViewById(R.id.healing_view_pager);


        // adapter 초기화
        adapter = new FragHealing.MainAdapter(getChildFragmentManager());
        // add fragments
        adapter.AddFragment(new Healing_heal(), "힐링");
        adapter.AddFragment(new Healing_therapy(),"셀프 테라피");
        //set adapter
        viewPager.setAdapter(adapter);
        //connect tab layout with view pager
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) { //페이지 넘어갈 때
                switch (position) {
                    case 0:
                        getActivity().findViewById(R.id.diaryBg).setBackgroundDrawable(getResources().getDrawable(R.drawable.diarybg));
                        break;
                    case 1:

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

        view = inflater.inflate(R.layout.frag_healing, container,false);

        return view;
    }


    // viewpager 관련 클래스
    private class MainAdapter extends FragmentPagerAdapter {
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



