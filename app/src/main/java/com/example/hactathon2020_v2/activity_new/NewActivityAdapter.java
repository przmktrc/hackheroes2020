package com.example.hactathon2020_v2.activity_new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hactathon2020_v2.my_calendar.CalendarMode;
import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;

// This is adapter for the ListView in ActivityNewActivity
public class NewActivityAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	
	private CalendarMode mode;
	
	// Constructor
	NewActivityAdapter(Context context, CalendarMode mode) {
		inflater = LayoutInflater.from(context);
		
		this.mode = mode;
	}
	
	// getItem override
	@Override
	public Object getItem(int position) {
		if (mode == CalendarMode.EXERCISE) {
			return MainActivity.exerciseRecipeBook.get(position);
		} else if (mode == CalendarMode.MEALS) {
			return MainActivity.exerciseRecipeBook.get(position);
		} else {
			return null;
		}
	}

	// getId override - does nothing
	@Override
	public long getItemId(int position) {
		return 0;
	}

	// getView override - executed for each ListView element
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_view_holder, parent, false);
		}
		
		if (mode == CalendarMode.EXERCISE) {
			((TextView) convertView.findViewById(R.id.listview_textView_1)).setText(MainActivity.exerciseRecipeBook.get(position).getName());
		} else if (mode == CalendarMode.MEALS) {
			((TextView) convertView.findViewById(R.id.listview_textView_1)).setText(MainActivity.mealsRecipeBook.get(position).getName());
		}
		
		return convertView;
	}

	// getCount override
	@Override
	public int getCount() {
		if (mode == CalendarMode.EXERCISE) {
			return MainActivity.exerciseRecipeBook.length();
		} else if (mode == CalendarMode.MEALS) {
			return MainActivity.mealsRecipeBook.length();
		} else {
			return 0;
		}
	}
}
