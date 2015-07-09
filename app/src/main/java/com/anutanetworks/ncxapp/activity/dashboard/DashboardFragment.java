package com.anutanetworks.ncxapp.activity.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.activity.SummaryDesignFragment;

public class DashboardFragment extends Fragment {
    static final int NUM_ITEMS = 4;

    private ViewPagerAdapter mAdapter;
    private ViewPager mPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ViewPagerAdapter(getChildFragmentManager());

        mPager = (ViewPager) getView().findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
            case 0:
                return new DashboardServicesSummaryFragment();
            case 1:
                return new SummaryDesignFragment();
            case 2:
                return new DashboardRiskActivityFragment();
            case 3:
                return new DashboardDeviceSummaryFragment();
            }
            return null;
        }

        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return "Services - Top 5 in Utilization";
            case 1:
                return "System and Alarm Summary";
            case 2:
                return "At Risk Pods";
            case 3:
                return "Device summary by vendor";
            }
            return "";
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
