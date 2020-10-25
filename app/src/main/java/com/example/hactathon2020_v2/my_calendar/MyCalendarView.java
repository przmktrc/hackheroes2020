package com.example.hactathon2020_v2.my_calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyCalendarView extends LinearLayout {

	private TextView yearDisplay, dateDisplay, weekDisplay;
	private GridView monthDisplay;

	private CalendarMode mode;
	private int selectedTile = 0;

	private Calendar selected;
	private ArrayList<Date> days;
	
	// Public constructor
	public MyCalendarView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		selected = Calendar.getInstance();
		
		// Default mode
		mode = CalendarMode.GENERAL;
		
		setUpView(context);
	}
	
	// Set calendar mode
	public void setMode(CalendarMode mode) {
		this.mode = mode;
	}
	
	// Get components and add listeners separated from constructor for convenience and expandability
	private void setUpView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.my_calendar_view, this);
		
		days = new ArrayList<>();

		ImageView btnLeft = findViewById(R.id.my_calendar_btn_left);
		ImageView btnRight = findViewById(R.id.my_calendar_btn_right);
		
		btnLeft.setOnClickListener(v -> {
			selected.add(Calendar.MONTH, -1);
			updateCalendar();
		});

		btnRight.setOnClickListener(v -> {
			selected.add(Calendar.MONTH, 1);
			updateCalendar();
		});
		
		yearDisplay = findViewById(R.id.my_calendar_year_display);
		dateDisplay = findViewById(R.id.my_calendar_date_display);
		weekDisplay = findViewById(R.id.my_calendar_day_display);

		Button jumpToday = findViewById(R.id.my_calendar_jump_today);
		jumpToday.setOnClickListener(v -> {
			selected = Calendar.getInstance();
			updateCalendar();
		});

		monthDisplay = findViewById(R.id.my_calendar_grid);
		monthDisplay.setOnItemClickListener((parent, view, position, id) -> {
			selected.setTime(days.get(position));
			updateCalendar();

			if (selectedTile == position) {
				ArrayList<String> toAdd = new ArrayList<>();
				if (mode == CalendarMode.EXERCISE || mode == CalendarMode.GENERAL) {
					try {
						toAdd.addAll(MainActivity.myCalendarExercise.get(selected).getNames());
					} catch (NullPointerException ignore) {}
				}
				if (mode == CalendarMode.MEALS || mode == CalendarMode.GENERAL) {
					try {
						toAdd.addAll(MainActivity.myCalendarMeals.get(selected).getNames());
					} catch (NullPointerException ignore) {}
				}
				String[] a = new String[toAdd.size()];
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle(R.string.calendar_day_list)
						.setPositiveButton(R.string.WORD_OK, null)
						.setItems(toAdd.toArray(a), (dialog, which) -> {
							AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
							builder2.setTitle(R.string.WORD_DELETE)
									.setMessage(R.string.WORD_DELETE_DIALOG)
									.setPositiveButton(R.string.WORD_YES, (dialog1, which1) -> {
										if (mode == CalendarMode.EXERCISE || mode == CalendarMode.GENERAL) {
											if (which1 <= MainActivity.myCalendarExercise.size()) {
												MainActivity.myCalendarExercise.removeName(toAdd.get(which), selected);
											} else {
												MainActivity.myCalendarMeals.removeName(toAdd.get(which), selected);
											}
										} else if (mode == CalendarMode.MEALS) {
											MainActivity.myCalendarMeals.removeName(toAdd.get(which), selected);
										}
										updateCalendar();
									})
									.setNegativeButton(R.string.WORD_NO, null);
							builder2.show();
						});
				builder.show();
			}
			
			selectedTile = position;
		});
	}
	
	// Update the calendar on view created or changed state (e.g. added day)
	public void updateCalendar() {
		days = new ArrayList<>();
		
		int number = selected.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Calendar temp = Calendar.getInstance();
		temp.set(selected.get(Calendar.YEAR), selected.get(Calendar.MONTH), selected.get(Calendar.DAY_OF_MONTH));

		temp.set(Calendar.DAY_OF_MONTH, 1);

		number += (temp.get(Calendar.DAY_OF_WEEK) + 5) % 7;

		temp.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		for (int i = 0; i < number; i++) {
			days.add(temp.getTime());
			temp.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		while((temp.get(Calendar.DAY_OF_WEEK) + 5) % 7 > 0) {
			days.add(temp.getTime());
			temp.add(Calendar.DAY_OF_MONTH, 1);
		}

		MyCalendarAdapter adapter = new MyCalendarAdapter(getContext(), days, selected, mode);
		
		monthDisplay.setAdapter(adapter);

		SimpleDateFormat format = new SimpleDateFormat("EEE,d MMM,yyy");
		String[] date = format.format(selected.getTime()).split(",");
		
		weekDisplay.setText(date[0]);
		dateDisplay.setText(date[1]);
		yearDisplay.setText(date[2]);
	}
	
	// Get calendar for currently selected day
	public Calendar getSelectedCalendar() {
		return selected;
	}
}
