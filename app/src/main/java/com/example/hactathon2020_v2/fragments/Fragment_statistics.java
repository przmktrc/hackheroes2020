package com.example.hactathon2020_v2.fragments;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Fragment_statistics extends MyFragment {
	
	private Calendar selected;
	
	private TextView month, year;
	private TextView thisMonthExercise, thisMonthMeals;
	private TextView totalExercise, totalMeals;
	private LineChart chart;
	
	private int[] thisMonth, total;
	
	// getTitle inherited from MyFragment. See MyFragment
	public int getTitle() {
		return R.string.statistics_title;
	}

	// onCreateView override - get components
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_statistics, container, false);

		selected = Calendar.getInstance();
		selected.set(Calendar.DAY_OF_MONTH, 1);
		
		chart = view.findViewById(R.id.statistics_chart);
		
		ImageView leftBttn = view.findViewById(R.id.statistics_left_bttn);
		leftBttn.setOnClickListener(v -> {
			selected.add(Calendar.MONTH, -1);
			refresh();
		});
		
		ImageView rightBttn = view.findViewById(R.id.statistics_right_bttn);
		rightBttn.setOnClickListener(v -> {
			selected.add(Calendar.MONTH, 1);
			refresh();
		});
		
		Button todayBttn = view.findViewById(R.id.statistics_today_bttn);
		todayBttn.setOnClickListener(v -> {
			selected = Calendar.getInstance();
			selected.set(Calendar.DAY_OF_MONTH, 1);
			
			refresh();
		});
		
		month = view.findViewById(R.id.statistics_textView_month);
		year = view.findViewById(R.id.statistics_textView_year);
		
		thisMonthExercise = view.findViewById(R.id.statistics_textView_this_month_exercise_number);
		thisMonthMeals = view.findViewById(R.id.statistics_textView_this_month_meals_number);
		totalExercise = view.findViewById(R.id.statistics_textView_total_exercise_number);
		totalMeals = view.findViewById(R.id.statistics_textView_total_meals_number);
		
		total = new int[]{0, 0};
		
		countTotal();
		refresh();
		
		return view;
	}
	
	// Refresh statistics upon view created or month changed
	private void refresh() {
		String[] temp = new SimpleDateFormat("LLLL,YYYY").format(selected.getTime()).split(",");
		temp[0] = temp[0].substring(0, 1).toUpperCase() + temp[0].substring(1);
		month.setText(temp[0]);
		year.setText(temp[1]);

		thisMonth = new int[]{0, 0};
		refreshChart();

		thisMonthExercise.setText(String.valueOf(thisMonth[0]));
		thisMonthMeals.setText(String.valueOf(thisMonth[1]));
		
		totalExercise.setText(String.valueOf(total[0]));
		totalMeals.setText(String.valueOf(total[1]));
	}
	
	// Subsection of the previous function separated for readability
	private void refreshChart() {
		ArrayList<ILineDataSet> dataSet = new ArrayList<>();

		dataSet.add(prepareExerciseDataset());
		dataSet.add(prepareMealDataset());

		LineData data = new LineData(dataSet);
		data.setValueFormatter(new ValueFormatter());
		
		chart.setData(data);
		
		chart.setDrawBorders(true);
		chart.setBorderColor(Color.BLACK);

		Description description = new Description();
		description.setEnabled(false);
		chart.setDescription(description);

		Legend legend = chart.getLegend();
		legend.setTextSize(17);
		legend.setFormSize(15);
		legend.setXEntrySpace(10);
		
		chart.setPinchZoom(false);
		chart.setDrawGridBackground(true);
		chart.setNoDataText(getString(R.string.statistics_chart_no_data));
		chart.invalidate();
	}
	
	// Subsections of the previous function separated for readability
	private LineDataSet prepareExerciseDataset() {
		ArrayList<Entry> entries = new ArrayList<>();
		int[] temp = new int[selected.getActualMaximum(Calendar.DAY_OF_MONTH)];
		
		int address = MainActivity.myCalendarExercise.find(selected);
		
		if (address < 0) {
			address = address * -1 - 1;
		}
		
		for (int i = address; MainActivity.myCalendarExercise.get(i) != null
				&& MainActivity.myCalendarExercise.get(i).getMonth() == selected.get(Calendar.MONTH) + 1
				&& MainActivity.myCalendarExercise.get(i).getYear() == selected.get(Calendar.YEAR); ++i) {
			thisMonth[0] += MainActivity.myCalendarExercise.get(i).getNames().size();
			temp[MainActivity.myCalendarExercise.get(i).getDay()] = MainActivity.myCalendarExercise.get(i).getNames().size();
		}
		
		for (int i = 0; i < temp.length; ++i) {
			entries.add(new Entry(i, temp[i]));
		}
		
		LineDataSet set = new LineDataSet(entries, getString(R.string.statistics_line_data_exercise_title));

		set.setLineWidth(3);
		set.setCircleRadius(5);
		try {
			set.setColor(ResourcesCompat.getColor(getContext().getResources(), R.color.color_exercise, null));
			set.setCircleColor(ResourcesCompat.getColor(getContext().getResources(), R.color.color_exercise, null));
		} catch (NullPointerException ignore) {}

		return set;
	}

	private LineDataSet prepareMealDataset() {
		ArrayList<Entry> entries = new ArrayList<>();
		int[] temp = new int[selected.getActualMaximum(Calendar.DAY_OF_MONTH)];
		
		int address = MainActivity.myCalendarMeals.find(selected);

		if (address < 0) {
			address = address * -1 - 1;
		}

		for (int i = address; MainActivity.myCalendarMeals.get(i) != null
				&& MainActivity.myCalendarMeals.get(i).getMonth() == selected.get(Calendar.MONTH) + 1
				&& MainActivity.myCalendarMeals.get(i).getYear() == selected.get(Calendar.YEAR); ++i) {
			thisMonth[1] += MainActivity.myCalendarMeals.get(i).getNames().size();
			temp[MainActivity.myCalendarMeals.get(i).getDay()] = MainActivity.myCalendarMeals.get(i).getNames().size();
		}

		for (int i = 0; i < temp.length; ++i) {
			entries.add(new Entry(i, temp[i]));
		}
		
		LineDataSet set = new LineDataSet(entries, getString(R.string.statistics_line_data_meals_title));

		set.setLineWidth(3);
		set.setCircleRadius(5);
		try {
			set.setColor(ResourcesCompat.getColor(getContext().getResources(), R.color.color_meal, null));
			set.setCircleColor(ResourcesCompat.getColor(getContext().getResources(), R.color.color_meal, null));
		} catch (NullPointerException ignore) {}
		
		return set;
	}
	
	// Count total number of exercises and meals. May be optimized later as it is linear
	private void countTotal() {
		for (int i = 0; i < MainActivity.myCalendarExercise.size(); ++i) {
			total[0] += MainActivity.myCalendarExercise.get(i).getNames().size();
		}
		for (int i = 0; i < MainActivity.myCalendarMeals.size(); ++i) {
			total[1] += MainActivity.myCalendarMeals.get(i).getNames().size();
		}
	}
	
	// Value formatter for the graph
	private static class ValueFormatter implements IValueFormatter {
		@Override
		public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
			return String.valueOf((int) value);
		}
	}
}
