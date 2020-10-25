package com.example.hactathon2020_v2.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class Fragment_settings extends MyFragment {

	private String[] perms = new String[]{
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
	
	private View view;

	private RadioGroup dateFormatGroup, delimiterGroup;
	private RadioButton[] formatBttn, delimiterBttn;
	
	private int checkedFormat, checkedDelimiter;
	
	private String delimiter = "/", delimiter2 = ".";
	private String[] format = new String[]{
			"dd/MM/yyy",
			"MM/dd/yyy",
			"yyy/MM/dd"
	};
	
	// getTitle inherited from MyFragment. See MyFragment
	public int getTitle() {
		return R.string.settings_title;
	}

	// onCreateView override - get components; split into more functions below
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_settings, container, false);

		setUpButtons();
		
		formatBttn = new RadioButton[]{
				view.findViewById(R.id.settings_date_format_radioButton_1),
				view.findViewById(R.id.settings_date_format_radioButton_2),
				view.findViewById(R.id.settings_date_format_radioButton_3)
		};
		
		dateFormatGroup = view.findViewById(R.id.settings_date_format);
		dateFormatGroup.setOnCheckedChangeListener((group, checkedId) -> {
			if (checkedId == R.id.settings_date_format_radioButton_1) {
				checkedFormat = 0;
			} else if (checkedId == R.id.settings_date_format_radioButton_2) {
				checkedFormat = 1;
			} else if (checkedId == R.id.settings_date_format_radioButton_3) {
				checkedFormat = 2;
			}
			
			saveSettings();
		});

		delimiterBttn = new RadioButton[]{
				view.findViewById(R.id.settings_delimiter_radioButton_1),
				view.findViewById(R.id.settings_delimiter_radioButton_2)
		};
		
		delimiterGroup = view.findViewById(R.id.settings_delimiter);
		delimiterGroup.setOnCheckedChangeListener((group, checkedId) -> {
			if (checkedId == R.id.settings_delimiter_radioButton_1) {
				checkedDelimiter = 0;
			} else if (checkedId == R.id.settings_delimiter_radioButton_2) {
				checkedDelimiter = 1;
			}
			
			refreshRadioGroup();
		});
		
		readSettings();
		
		refreshRadioGroup();
		
		return view;
	}
	
	// Refresh RadioButtons groups on view created, date delimiter changed or default settings restored
	private void refreshRadioGroup() {
		delimiter2 = delimiter;
		if (delimiter2.equals(".")) { delimiter2 = "\\."; }
		
		delimiter = delimiterBttn[checkedDelimiter].getText().toString();
		
		for (int i = 0; i < format.length; ++i) {
			format[i] = format[i].replaceAll(delimiter2, delimiter);
			formatBttn[i].setText(format[i]);
		}
		
		saveSettings();
	}
	
	// Save settings on any change
	private void saveSettings() {
		MainActivity.settingsManager.setFormat(
				formatBttn[checkedFormat].getText().toString(),
				delimiterBttn[checkedDelimiter].getText().toString()
		);
	}
	
	// Read settings on view created
	private void readSettings() {
		String tempFormat = MainActivity.settingsManager.getFormat();
		String tempDel = MainActivity.settingsManager.getDelimiter();

		if (tempDel.equals("/")) {
			checkedDelimiter = 0;
		} else if (tempDel.equals(".")) {
			checkedDelimiter = 1;
		}

		if (tempDel.equals(".")) { tempDel = "\\."; }
		for (int i = 0; i < format.length; ++i) {
			if (format[i].equals(tempFormat.replaceAll(tempDel, "/"))) {
				checkedFormat = i;
			}
		}
		
		dateFormatGroup.check(formatBttn[checkedFormat].getId());
		delimiterGroup.check(delimiterBttn[checkedDelimiter].getId());
	}
	
	// Get buttons and add listeners
	private void setUpButtons() {
		Button exportExRecipes = view.findViewById(R.id.settings_ex_recipes_button_1);
		exportExRecipes.setOnClickListener(v -> {
			checkPermission();
			export(MainActivity.exerciseRecipeBook.exportToExternal());
		});
		
		Button restoreExRecipes = view.findViewById(R.id.settings_ex_recipes_button_2);
		restoreExRecipes.setOnClickListener(v -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle(R.string.settings_restore)
					.setMessage(R.string.settings_restore_dialog)
					.setPositiveButton("TAK", (dialog, which) -> {
						restore(MainActivity.exerciseRecipeBook.restoreDefault());
					})
					.setNegativeButton("NIE", null);
			builder.show();
		});

		Button exportMRecipes = view.findViewById(R.id.settings_m_recipes_button_1);
		exportMRecipes.setOnClickListener(v -> {
			checkPermission();
			export(MainActivity.mealsRecipeBook.exportToExternal());
		});

		Button restoreMRecipes = view.findViewById(R.id.settings_m_recipes_button_2);
		restoreMRecipes.setOnClickListener(v -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle(R.string.settings_restore)
					.setMessage(R.string.settings_restore_dialog)
					.setPositiveButton("TAK", (dialog, which) -> {
						restore(MainActivity.mealsRecipeBook.restoreDefault());
					})
					.setNegativeButton("NIE", null);
			builder.show();
		});
		
		Button exportExCalendar = view.findViewById(R.id.settings_ex_calendar_bttn_1);
		exportExCalendar.setOnClickListener(v -> {
			checkPermission();
			export(MainActivity.myCalendarExercise.exportToExternal());
		});
		
		Button clearExCalendar = view.findViewById(R.id.settings_ex_calendar_bttn_2);
		clearExCalendar.setOnClickListener(v -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle(R.string.settings_clear)
					.setMessage(R.string.settings_clear_dialog)
					.setPositiveButton("TAK", (dialog, which) -> {
						MainActivity.myCalendarExercise.clear();
						Snackbar snackbar = Snackbar.make(view.findViewById(R.id.settings_layout),
								getString(R.string.settings_clear_successful),
								Snackbar.LENGTH_SHORT);
						snackbar.show();
					})
					.setNegativeButton("NIE", null);
			builder.show();
		});

		Button exportMCalendar = view.findViewById(R.id.settings_m_calendar_bttn_1);
		exportMCalendar.setOnClickListener(v -> {
			checkPermission();
			export(MainActivity.myCalendarMeals.exportToExternal());
		});

		Button clearMCalendar = view.findViewById(R.id.settings_m_calendar_bttn_2);
		clearMCalendar.setOnClickListener(v -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle(R.string.settings_clear)
					.setMessage(R.string.settings_clear_dialog)
					.setPositiveButton("TAK", (dialog, which) -> {
						MainActivity.myCalendarMeals.clear();
						Snackbar snackbar = Snackbar.make(view.findViewById(R.id.settings_layout),
								getString(R.string.settings_clear_successful),
								Snackbar.LENGTH_SHORT);
						snackbar.show();
					})
					.setNegativeButton("NIE", null);
			builder.show();
		});
		
		Button restoreSettings = view.findViewById(R.id.settings_settings_button);
		restoreSettings.setOnClickListener(v -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle(R.string.settings_restore)
					.setMessage(R.string.settings_restore_settings_dialog)
					.setPositiveButton("TAK", (dialog, which) -> {
						if (MainActivity.settingsManager.restoreDefault()) {
							Snackbar snackbar = Snackbar.make(view.findViewById(R.id.settings_layout),
									getString(R.string.settings_restore_successful),
									Snackbar.LENGTH_SHORT);
							snackbar.show();
							refreshRadioGroup();
						} else {
							Snackbar snackbar = Snackbar.make(view.findViewById(R.id.settings_layout),
									getString(R.string.settings_restore_unsuccessful),
									Snackbar.LENGTH_SHORT);
							snackbar.show();
						}
					})
					.setNegativeButton("NIE", null);
			builder.show();
		});
	}
	
	// These two are for convenience - and so I don't repeat myself unnecessarily
	private void export(boolean succeeded) {
		if (succeeded) {
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getString(R.string.app_name));
			
			Snackbar snackbar= Snackbar.make(view.findViewById(R.id.settings_layout),
					String.format(getString(R.string.settings_export_successful), file.getAbsolutePath()),
					Snackbar.LENGTH_LONG);
			snackbar.show();
		} else {
			Snackbar snackbar= Snackbar.make(view.findViewById(R.id.settings_layout),
					getString(R.string.settings_export_unsuccessful),
					Snackbar.LENGTH_SHORT);
			snackbar.show();
		}
	}
	
	private void restore(boolean succeeded) {
		if (succeeded) {
			Snackbar snackbar = Snackbar.make(view.findViewById(R.id.settings_layout),
					getString(R.string.settings_restore_successful),
					Snackbar.LENGTH_SHORT);
			snackbar.show();
		} else {
			Snackbar snackbar = Snackbar.make(view.findViewById(R.id.settings_layout),
					getString(R.string.settings_restore_unsuccessful),
					Snackbar.LENGTH_SHORT);
			snackbar.show();
		}
	}
	
	// Check if app has necessary permission (write to external storage) upon trying to export
	private void checkPermission() {
		for (String toCheck : perms) {
			if (ContextCompat.checkSelfPermission(getContext(), toCheck) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(getActivity(), new String[]{toCheck}, 0);
			}
		}
	}
}
