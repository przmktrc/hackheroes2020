package com.example.hactathon2020_v2.fragments;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hactathon2020_v2.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

// This is a fragment that loads other fragments
public class Fragment_calendar extends MyFragment {
	
	private FragmentManager fragmentManager;
	private ViewPager pager;
	
	// getTitle inherited from MyFragment. See MyFragment
	public int getTitle() {
		return R.string.calendar_title;
	}
	
	// Constructor
	public Fragment_calendar(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	// onCreateView override - get components, initialize CalendarPagerAdapter
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calendar, container, false);

		pager = view.findViewById(R.id.calendar_viewPager);
		TabLayout tabLayout = view.findViewById(R.id.calendar_tabLayout);
		
		CalendarPagerAdapter pagerAdapter = new CalendarPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());
		
		pager.setOffscreenPageLimit(0);
		pager.setAdapter(pagerAdapter);
		
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				pager.setCurrentItem(tab.getPosition());
			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {}
		});

		pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

		return view;
	}
}
