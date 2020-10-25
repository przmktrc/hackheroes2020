package com.example.hactathon2020_v2.my_calendar;

import android.content.Context;

import com.example.hactathon2020_v2.R;
import com.example.hactathon2020_v2.StorageClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

// This class stores the calendar for exercises and meals
public class MyCalendar {
	
	private Context parent;
	private String source;
	
	private ArrayList<MyCalendarEventDay> calendarArray;
	private JSONArray calendarJson;
	
	// Public constructor
	public MyCalendar(Context parent, String source) {
		calendarArray = new ArrayList<>();
		
		this.parent = parent;
		this.source = source;
		
		readCalendarFromSource();
	}

	// Public methods for manipulating the calendar
	// Note that even though some of these are not used currently, they may be used later in the development
	public void add(MyCalendarEventDay toAdd) {
		int address = find(toAdd.getCalendar());
		if (address >= 0) {
			MyCalendarEventDay temp = calendarArray.get(address);
			temp.addName(toAdd.getName(0));
			
			calendarArray.remove(address);
			calendarArray.add(temp);
		} else {
			calendarArray.add(toAdd);
		}
		
		writeCalendarToSource();
	}
	
	public void add(String name, Calendar calendar) {
		int address = find(calendar);
		
		if (address >= 0) {
			MyCalendarEventDay temp = calendarArray.get(address);
			temp.addName(name);

			calendarArray.remove(address);
			calendarArray.add(temp);
		} else {
			calendarArray.add(new MyCalendarEventDay(name, calendar));
		}

		writeCalendarToSource();
	}

	public void remove(Calendar calendar) {
		int toRemove = find(calendar);
		if (toRemove >= 0) {
			calendarArray.remove(toRemove);
		}
		
		writeCalendarToSource();
	}
	
	public void removeName(String name, Calendar calendar) {
		int toRemove = find(calendar);
		
		if (toRemove >= 0) {
			calendarArray.get(toRemove).removeName(name);
			if (calendarArray.get(toRemove).getNames().isEmpty()) {
				calendarArray.remove(toRemove);
			}
			
			writeCalendarToSource();
		}
	}

	public ArrayList<MyCalendarEventDay> getArray() {
		return calendarArray;
	}

	public MyCalendarEventDay get(Calendar calendar) {
		int address = find(calendar);
		
		return get(address);
	}
	
	public MyCalendarEventDay get(int index) {
		try {
			return calendarArray.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int size() {
		return calendarArray.size();
	}
	
	public void clear() {
		calendarArray = new ArrayList<>();
		calendarJson = new JSONArray();
		
		writeCalendarToSource();
	}
	
	public boolean exportToExternal() {
		try {
			calendarJson = convertArrayToJSON();

			StorageClient.writeJSONToExternal(parent.getResources().getString(R.string.app_name), source, calendarJson);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public int find(Calendar calendar) {
		return Collections.binarySearch(calendarArray, new MyCalendarEventDay("no_name", calendar), MyCalendarEventDay::compare);
	}
	
	// Private methods
	private void readCalendarFromSource() {
		calendarJson = new JSONArray();
		
		try {
			calendarJson = StorageClient.readJSONFromInternal(parent, source, true);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			return;
		}

		try {
			for (int i = 0; i < calendarJson.length(); i++) {
				calendarArray.add(new MyCalendarEventDay(calendarJson.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		
		sort();
	}

	private void writeCalendarToSource() {
		sort();
		
		try {
			calendarJson = convertArrayToJSON();
			
			StorageClient.writeJSONToInternal(parent, source, calendarJson);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	private JSONArray convertArrayToJSON() throws JSONException {
		JSONArray out = new JSONArray();
		
		for (MyCalendarEventDay toAdd : calendarArray) {
			out.put(toAdd.getJSON());
		}

		return out;
	}

	private void sort() {
		calendarArray.sort((o1, o2) -> o1.compare(o2));
	}
}
