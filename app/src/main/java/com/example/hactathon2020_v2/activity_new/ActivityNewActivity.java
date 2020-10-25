package com.example.hactathon2020_v2.activity_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.hactathon2020_v2.my_calendar.CalendarMode;
import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;
import com.example.hactathon2020_v2.my_recipies.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityNewActivity extends AppCompatActivity {

	private ListView listView;
	private BaseAdapter adapter;
	
	private CalendarMode mode;
	private Intent intent;
	
	// onCreate override - inflate view, get components
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_activity);
		
		intent = getIntent();
		
		mode = (CalendarMode) intent.getExtras().get("mode");
		
		Toolbar toolbar = findViewById(R.id.new_activity_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		int titleId = R.string.WBL;
		if (mode == CalendarMode.EXERCISE) {
			titleId = R.string.new_activity_exercise_title;
		} else if (mode == CalendarMode.MEALS) {
			titleId = R.string.new_activity_meal_title;
		}
		getSupportActionBar().setTitle(titleId);
		
		listView = findViewById(R.id.new_activity_listview);
		
		adapter = new NewActivityAdapter(getApplicationContext(), mode);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener((parent, view, position, id) -> {
			startRecipe(position);
		});
		
		FloatingActionButton fab = findViewById(R.id.new_activity_fab);
		fab.setOnClickListener(v -> startRecipe(-1));
	}
	
	// Show chosen recipe
	private void startRecipe(int index) {
		Intent intent;
		
		if (index == -1) {
			intent = new Intent(this, ActivityEditRecipe.class);
			if (mode == CalendarMode.EXERCISE) {
				index = MainActivity.exerciseRecipeBook.add(new Recipe());
			} else if (mode == CalendarMode.MEALS) {
				index = MainActivity.mealsRecipeBook.add(new Recipe());
			}
		} else {
			intent = new Intent(this, ActivityDoRecipe.class);
		}

		intent.putExtra("mode", mode);
		intent.putExtra("recipe", index);
		intent.putExtra("selected_calendar_year", this.intent.getIntExtra("selected_calendar_year", 2020));
		intent.putExtra("selected_calendar_month", this.intent.getIntExtra("selected_calendar_month", 0));
		intent.putExtra("selected_calendar_day", this.intent.getIntExtra("selected_calendar_day", 1));
		startActivityForResult(intent, 2);
	}

	// Return Activity.RESULT_OK to parent activity upon exiting
	@Override
	public boolean onSupportNavigateUp() {
		setResult(Activity.RESULT_OK, new Intent());
		finish();
		return true;
	}
	
	// Handling the result from child activity (either ActivityDoRecipe or ActivityEditRecipe)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			adapter = new NewActivityAdapter(getApplicationContext(), mode);
			listView.setAdapter(adapter);
		}
	}
}
