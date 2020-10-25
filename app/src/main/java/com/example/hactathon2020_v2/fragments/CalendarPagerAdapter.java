package com.example.hactathon2020_v2.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

// This is adapter for ViewPager in Fragment_calendar
public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

	private int tabNum;

	// Constructor
	CalendarPagerAdapter(@NonNull FragmentManager fm, int behavior, int tabNum) {
		super(fm, behavior);

		this.tabNum = tabNum;
	}

	// getItem override
	@NonNull
	@Override
	public Fragment getItem(int position) {
		switch(position) {
			default:
			case 0:
				return new Fragment_calendar_main();
			case 1:
				return new Fragment_calendar_exercise();
			case 2:
				return new Fragment_calendar_meals();
		}
	}

	// getCount override
	@Override
	public int getCount() {
		return tabNum;
	}
}
