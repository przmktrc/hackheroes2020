package com.example.hactathon2020_v2.my_calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

// This is an adapter for MyCalendarView
public class MyCalendarAdapter extends ArrayAdapter<Date> {
	private LayoutInflater inflater;
	
	private CalendarMode mode;
	
	private Calendar selected;
	private Calendar toGet;
	
	// Constructor
	MyCalendarAdapter(@NonNull Context context, ArrayList<Date> days, Calendar selected, CalendarMode mode) {
		super(context, R.layout.my_calendar_day, days);

		this.selected = selected;
		this.mode = mode;
		
		toGet = Calendar.getInstance();
		
		inflater = LayoutInflater.from(context);
	}
	
	// getView override - executed for each calendar day in selected month; checks if the given date should be highlighted and, if so, what color should it be
	@NonNull
	public View getView(int position, View view, @NonNull ViewGroup parent) {
		toGet.setTime(Objects.requireNonNull(getItem(position)));
		
		if (view == null) { view = inflater.inflate(R.layout.my_calendar_day, parent, false); }

		TextView dayText = view.findViewById(R.id.my_calendar_day_textView);
		
		dayText.setTypeface(null, Typeface.NORMAL);
		dayText.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.color_in_month, null));
		
		int[] contain = {-1, -1};
		int color = 0;
		
		if (mode == CalendarMode.EXERCISE || mode == CalendarMode.GENERAL) {
			contain[0] = MainActivity.myCalendarExercise.find(toGet);
		} // Check if has exercise
		if (mode == CalendarMode.MEALS || mode == CalendarMode.GENERAL) {
			contain[1] = MainActivity.myCalendarMeals.find(toGet);
		} // Check if has meal
		
		if (contain[0] >= 0) {
			color += 1;
		}
		if (contain[1] >= 0) {
			color += 2;
		}
		
		if (toGet.get(Calendar.MONTH) != selected.get(Calendar.MONTH)) {
			color += 4;
		} // Check if outside of month
		else if (toGet.get(Calendar.DAY_OF_MONTH) == selected.get(Calendar.DAY_OF_MONTH)) {
			color = 8;
		} // Check if today

		int colorOut;
		switch (color) {
			case 1:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_exercise, null);
				break;
			case 2:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_meal, null);
				break;
			case 3:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_both, null);
				break;
			case 4:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_out_month, null);
				break;
			case 5:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_exercise_out_month, null);
				break;
			case 6:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_meal_out_month, null);
				break;
			case 7:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_both_out_month, null);
				break;
			case 8:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_today, null);
				break;
			default:
				colorOut = ResourcesCompat.getColor(getContext().getResources(), R.color.color_in_month, null);
				break;
		}
		
		dayText.setTextColor(colorOut);
		dayText.setText(String.valueOf(toGet.get(Calendar.DAY_OF_MONTH)));
		
		return view;
	}
}
