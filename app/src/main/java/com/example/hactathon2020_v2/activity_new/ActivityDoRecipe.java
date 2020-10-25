package com.example.hactathon2020_v2.activity_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.hactathon2020_v2.my_calendar.CalendarMode;
import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityDoRecipe extends AppCompatActivity {
	
	private TextView editName, editDescription, editDate;
	
	private SimpleDateFormat format;
	private Calendar selected;
	private int recipe;
	private CalendarMode mode;
	
	// onCreate override - get components
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_do_recipe);
		
		Intent intent = getIntent();
		
		mode = (CalendarMode) intent.getExtras().get("mode");
		recipe = intent.getIntExtra("recipe", 0);
		selected = Calendar.getInstance();
		selected.set(Calendar.YEAR, intent.getIntExtra("selected_calendar_year", 2020));
		selected.set(Calendar.MONTH, intent.getIntExtra("selected_calendar_month", 1));
		selected.set(Calendar.DAY_OF_MONTH, intent.getIntExtra("selected_calendar_day", 1));
		
		Toolbar toolbar = findViewById(R.id.do_activity_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		int titleId = R.string.WBL;
		if (mode == CalendarMode.EXERCISE) {
			titleId = R.string.do_activity_exercise_title;
		} else if (mode == CalendarMode.MEALS) {
			titleId = R.string.do_activity_meal_title;
		}
		getSupportActionBar().setTitle(titleId);
		
		Button saveBtn = findViewById(R.id.do_activity_btn_done);
		saveBtn.setOnClickListener(v -> {
			Date toAdd = Calendar.getInstance().getTime();
			try {
				toAdd = format.parse(editDate.getText().toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toAdd);
			
			if (mode == CalendarMode.EXERCISE) {
				String name = MainActivity.exerciseRecipeBook.get(recipe).getName();
				MainActivity.myCalendarExercise.add(name, calendar);
			} else if (mode == CalendarMode.MEALS) {
				String name = MainActivity.mealsRecipeBook.get(recipe).getName();
				MainActivity.myCalendarMeals.add(name, calendar);
			}

			Snackbar snackbar = Snackbar.make(findViewById(R.id.browse_layout), R.string.do_activity_add_successful, Snackbar.LENGTH_SHORT);
			snackbar.show();
		});
		
		editName = findViewById(R.id.do_activity_editText_name);
		editDescription = findViewById(R.id.do_activity_editText_description);
		editDate = findViewById(R.id.do_activity_editText_date);
		
		format = new SimpleDateFormat(MainActivity.settingsManager.getFormat());
		
		updateTextViews();
	}

	// Update TextViews when activity started loaded or result from ActivityEditRecipe gotten
	private void updateTextViews() {
		String name = "", description = "";

		if (mode == CalendarMode.EXERCISE) {
			name = MainActivity.exerciseRecipeBook.get(recipe).getName();

			description = MainActivity.exerciseRecipeBook.get(recipe).getDescription();
		} else if (mode == CalendarMode.MEALS) {
			name = MainActivity.mealsRecipeBook.get(recipe).getName();

			description = MainActivity.mealsRecipeBook.get(recipe).getDescription();
		}

		name = name.replaceAll("\\\\n", "\n");
		description = description.replaceAll("\\\\n", "\n");

		editName.setText(name);
		editDescription.setText(description);
		
		editDate.setText(format.format(selected.getTime()));
	}
	
	// Handle results from ActivityEditRecipe
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 3) {
			if (data.getBooleanExtra("deleted", true)) {
				setResult(Activity.RESULT_OK, new Intent());
				finish();
			} else {
				updateTextViews();
			}
		}
	}

	// Return Activity.RESULT_OK to parent activity upon exiting
	@Override
	public boolean onSupportNavigateUp() {
		setResult(Activity.RESULT_OK, new Intent());
		finish();
		return true;
	}
	
	// Add functionality to the "Edit" button in the toolbar
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_edit_recipe:
				Intent intent = new Intent(this, ActivityEditRecipe.class);
				intent.putExtra("recipe", recipe);
				intent.putExtra("mode", mode);

				startActivityForResult(intent, 3);
		}

		return super.onOptionsItemSelected(item);
	}
	
	// Inflate menu to add the "Edit" button to the toolbar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_browse, menu);
		
		return true;
	}
}
