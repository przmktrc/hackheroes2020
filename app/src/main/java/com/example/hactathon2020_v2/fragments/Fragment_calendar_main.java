package com.example.hactathon2020_v2.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hactathon2020_v2.my_calendar.CalendarMode;
import com.example.hactathon2020_v2.R;
import com.example.hactathon2020_v2.my_calendar.MyCalendarView;

public class Fragment_calendar_main extends MyFragment {
	
	// getTitle inherited from MyFragment. See MyFragment
	public int getTitle() {
		return R.string.calendar_title;
	}
	
	// onCreateView override - get components
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_calendar_main, container, false);
		
		MyCalendarView calendar = view.findViewById(R.id.calendar_main);

		calendar.setMode(CalendarMode.GENERAL);
		calendar.updateCalendar();

		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}
}
