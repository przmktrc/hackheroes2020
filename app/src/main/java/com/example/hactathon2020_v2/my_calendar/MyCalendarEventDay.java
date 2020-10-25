package com.example.hactathon2020_v2.my_calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

// This class stores one day of MyCalendar
public class MyCalendarEventDay {
	private ArrayList<String> names;
	private int year, month, day;
	
	// Public constructor
	public MyCalendarEventDay(String name, Calendar date) {
		names = new ArrayList<>();
		names.add(name);
		
		year = date.get(Calendar.YEAR);
		month = date.get(Calendar.MONTH) + 1;
		day = date.get(Calendar.DAY_OF_MONTH);
	}
	
	public MyCalendarEventDay(JSONObject json) throws JSONException {
		JSONArray array = json.getJSONArray("names");
		
		names = new ArrayList<>();
		for(int i = 0; i < array.length(); i++) { names.add(array.getString(i)); }
		
		JSONObject date = json.getJSONObject("date");
		year = date.getInt("year");
		month = date.getInt("month");
		day = date.getInt("day");
	}
	
	// Public methods for manipulating MyCalendarEventDay object
	// Note that even though some of these are not used currently, they may be used later in the development
	public void addName(String name) {
		names.add(name);
	}
	
	public void removeName(int index) {
		names.remove(index);
	}

	public void removeName(String name) {
		names.remove(name);
	}
	
	public Calendar getCalendar() {
		Calendar out = Calendar.getInstance();
		out.set(Calendar.YEAR, year);
		out.set(Calendar.MONTH, month);
		out.set(Calendar.DAY_OF_MONTH, day);
		
		return out;
	}
	
	public ArrayList<String> getNames() {
		return names;
	}
	
	public String getName(int index) {
		return names.get(index);
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}

	public int compare(MyCalendarEventDay compareTo) {
		if (compareTo.getYear() == year) {
			if (compareTo.getMonth() == month) {
				return day - compareTo.getDay();
			} else {
				return month - compareTo.getMonth();
			}
		} else {
			return year - compareTo.getYear();
		}
	}
	
	public JSONObject getJSON() throws JSONException {
		JSONObject out = new JSONObject();
		
		out.put("names", new JSONArray(names));
		out.put("date", new JSONObject().put("year", year).put("month", month).put("day", day));
		
		return out;
	}
}
