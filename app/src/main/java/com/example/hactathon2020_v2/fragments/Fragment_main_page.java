package com.example.hactathon2020_v2.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hactathon2020_v2.R;
import com.example.hactathon2020_v2.activity_new.ActivityNewActivity;
import com.example.hactathon2020_v2.my_calendar.CalendarMode;

import java.util.Calendar;

public class Fragment_main_page extends MyFragment {
	
	// getTitle inherited from MyFragment. See MyFragment
	public int getTitle() {
		return R.string.main_page_title;
	}
	
	// onCreateView override - get components
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_main_page, container, false);

		Button startExercise = view.findViewById(R.id.main_page_exercise_begin);
		startExercise.setOnClickListener(v -> {
			Intent intent = new Intent(getActivity(), ActivityNewActivity.class);
			intent.putExtra("mode", CalendarMode.EXERCISE);
			intent.putExtra("selected_calendar_year", Calendar.getInstance().get(Calendar.YEAR));
			intent.putExtra("selected_calendar_month", Calendar.getInstance().get(Calendar.MONTH));
			intent.putExtra("selected_calendar_day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			startActivityForResult(intent, 1);
		});

		Button startMeal = view.findViewById(R.id.main_page_meal_begin);
		startMeal.setOnClickListener(v -> {
			Intent intent = new Intent(getActivity(), ActivityNewActivity.class);
			intent.putExtra("mode", CalendarMode.MEALS);
			intent.putExtra("selected_calendar_year", Calendar.getInstance().get(Calendar.YEAR));
			intent.putExtra("selected_calendar_month", Calendar.getInstance().get(Calendar.MONTH));
			intent.putExtra("selected_calendar_day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			startActivityForResult(intent, 1);
		});
		
		return view;
	}
}
