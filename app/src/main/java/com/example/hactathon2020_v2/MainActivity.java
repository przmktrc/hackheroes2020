package com.example.hactathon2020_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.hactathon2020_v2.fragments.Fragment_bmi_calc;
import com.example.hactathon2020_v2.fragments.Fragment_calendar;
import com.example.hactathon2020_v2.fragments.Fragment_main_page;
import com.example.hactathon2020_v2.fragments.Fragment_settings;
import com.example.hactathon2020_v2.fragments.Fragment_statistics;
import com.example.hactathon2020_v2.fragments.MyFragment;
import com.example.hactathon2020_v2.my_calendar.MyCalendar;
import com.example.hactathon2020_v2.my_recipies.RecipeBook;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private DrawerLayout drawerLayout;
	private Toolbar toolbar;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	// I know that it is not the best way to do it, but I was unable to implement Parcelable with these
	public static MyCalendar myCalendarExercise, myCalendarMeals;
	public static RecipeBook exerciseRecipeBook, mealsRecipeBook;
	public static SettingsManager settingsManager;
	
	// onCreate override - set up the dynamic page loading
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawerLayout = findViewById(R.id.drawer);
		toolbar = findViewById(R.id.toolbar);
		NavigationView navigationView = findViewById(R.id.nav_view);
		
		toolbar.setTitle(R.string.main_page_title);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);

		navigationView.setNavigationItemSelectedListener(this);
		
		drawerLayout.addDrawerListener(toggle);
		toggle.setDrawerIndicatorEnabled(true);
		toggle.syncState();
		
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		
		fragmentTransaction.add(R.id.content_container, new Fragment_main_page());
		fragmentTransaction.commit();
		
		myCalendarExercise = new MyCalendar(getApplicationContext(), "calendar_exercise.json");
		myCalendarMeals = new MyCalendar(getApplicationContext(), "calendar_meal.json");

		exerciseRecipeBook = new RecipeBook(getApplicationContext(), "recipes_exercise.json", R.raw.recipes_exercise);
		mealsRecipeBook = new RecipeBook(getApplicationContext(), "recipes_meals.json", R.raw.recipes_meals);
		
		settingsManager = new SettingsManager(getApplicationContext(), "settings.json");
	}

	// onNavigationItemSelected override - load selected fragment
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		drawerLayout.closeDrawer(GravityCompat.START);
		switch(item.getItemId()) {
			case R.id.item_main_page:
				loadFragment(new Fragment_main_page());
				break;
			case R.id.item_calendar:
				loadFragment(new Fragment_calendar(getSupportFragmentManager()));
				break;
			case R.id.item_statistics:
				loadFragment(new Fragment_statistics());
				break;
			case R.id.item_bmi_calc:
				loadFragment(new Fragment_bmi_calc());
				break;
			case R.id.item_settings:
				loadFragment(new Fragment_settings());
				break;
		}
		
		return false;
	}
	
	// Load selected fragment
	private void loadFragment(MyFragment toLoad) {
		toolbar.setTitle(toLoad.getTitle());
		
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.replace(R.id.content_container, toLoad);

		fragmentTransaction.commit();
	}
}
