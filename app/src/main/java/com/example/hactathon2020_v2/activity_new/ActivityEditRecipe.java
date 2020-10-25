package com.example.hactathon2020_v2.activity_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.hactathon2020_v2.my_calendar.CalendarMode;
import com.example.hactathon2020_v2.MainActivity;
import com.example.hactathon2020_v2.R;
import com.example.hactathon2020_v2.my_recipies.Recipe;
import com.google.android.material.snackbar.Snackbar;

public class ActivityEditRecipe extends AppCompatActivity {
	
	private EditText editName, editDescription;
	
	private CalendarMode mode;
	private int recipe;
	
	// onCreate override - get components
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);

		Intent intent = getIntent();

		mode = (CalendarMode) intent.getExtras().get("mode");
		recipe = intent.getIntExtra("recipe", 0);
		
		Toolbar toolbar = findViewById(R.id.browse_activity_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Button saveBtn = findViewById(R.id.edit_activity_save_button);
		saveBtn.setOnClickListener(v -> saveRecipe());
		
		Button deleteBtn = findViewById(R.id.edit_activity_delete_button);
		deleteBtn.setOnClickListener(v -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.WORD_DELETE)
					.setMessage(R.string.WORD_DELETE_DIALOG)
					.setPositiveButton(R.string.WORD_YES, ((dialog, which) -> removeRecipe()))
					.setNegativeButton(R.string.WORD_NO, null);
			builder.show();
		});
		
		editName = findViewById(R.id.edit_activity_editText_name);
		editDescription = findViewById(R.id.edit_activity_editText_description);
		
		updateEditText();
	}
	
	// Update EditTexts on activity create. This is separate from onCreate for expandability and readability
	private void updateEditText() {
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
	}
	
	// Delete the edited recipe and return Activty.RESULT_OK with an extra of "deleted" with a value of true
	private void removeRecipe() {
		if (mode == CalendarMode.EXERCISE) {
			MainActivity.exerciseRecipeBook.remove(recipe);
		} else if (mode == CalendarMode.MEALS) {
			MainActivity.mealsRecipeBook.remove(recipe);
		}

		setResult(Activity.RESULT_OK, new Intent().putExtra("deleted", true));
		finish();
	}
	
	// Save the edited recipe
	private void saveRecipe() {
		String name, description;
		
		name = editName.getText().toString();
		description = editDescription.getText().toString();
		
		name = name.replaceAll("\n", "\\\\n");
		description = description.replaceAll("\n", "\\\\n");
		
		if (mode == CalendarMode.EXERCISE) {
			MainActivity.exerciseRecipeBook.replace(recipe, new Recipe(name, description));
		} else if (mode == CalendarMode.MEALS) {
			MainActivity.mealsRecipeBook.replace(recipe, new Recipe(name, description));
		}

		Snackbar snackbar = Snackbar.make(findViewById(R.id.browse_layout), R.string.edit_activity_save_successful, Snackbar.LENGTH_SHORT);
		snackbar.show();
	}
	
	// Return Activity.RESULT_OK to the parent activity upon exiting
	@Override
	public boolean onSupportNavigateUp() {
		if (editName.getText().toString().equals("") && editDescription.getText().toString().equals("")) {
			removeRecipe();
		}
		
		setResult(Activity.RESULT_OK, new Intent().putExtra("deleted", false));
		finish();
		return true;
	}
}
