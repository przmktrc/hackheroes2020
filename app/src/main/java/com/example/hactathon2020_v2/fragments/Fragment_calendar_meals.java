package com.example.hactathon2020_v2.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hactathon2020_v2.my_calendar.CalendarMode;
import com.example.hactathon2020_v2.R;
import com.example.hactathon2020_v2.activity_new.ActivityNewActivity;
import com.example.hactathon2020_v2.my_calendar.MyCalendarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class Fragment_calendar_meals extends MyFragment {
	
	// getTitle inherited from MyFragment. See MyFragment
	public int getTitle() {
		return R.string.calendar_title;
	}

	// onCreateView override - get components
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_calendar_meals, container, false);

		MyCalendarView calendar = view.findViewById(R.id.calendar_meals);

		calendar.setMode(CalendarMode.MEALS);
		calendar.updateCalendar();
		
		FloatingActionButton fab = view.findViewById(R.id.calendar_meals_fab);
		fab.setOnClickListener(v -> {
			Intent intent = new Intent(getActivity(), ActivityNewActivity.class);
			intent.putExtra("mode", CalendarMode.MEALS);
			intent.putExtra("selected_calendar_year", calendar.getSelectedCalendar().get(Calendar.YEAR));
			intent.putExtra("selected_calendar_month", calendar.getSelectedCalendar().get(Calendar.MONTH));
			intent.putExtra("selected_calendar_day", calendar.getSelectedCalendar().get(Calendar.DAY_OF_MONTH));
			startActivityForResult(intent, 1);
		});

		return view;
	}
}
