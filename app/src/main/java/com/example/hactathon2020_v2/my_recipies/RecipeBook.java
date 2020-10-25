package com.example.hactathon2020_v2.my_recipies;

import android.content.Context;

import com.example.hactathon2020_v2.R;
import com.example.hactathon2020_v2.StorageClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// This class stores the recipe book for exercises and meals
public class RecipeBook {
	private Context parent;
	private String source;
	private int rawSource;

	private ArrayList<Recipe> recipeArray;
	private JSONArray recipeJSON;

	// Public constructor
	public RecipeBook(Context parent, String source, int rawSource) {
		recipeArray = new ArrayList<>();

		this.parent = parent;
		this.source = source;
		this.rawSource = rawSource;

		readBookFromSource();
	}

	// Public methods for manipulating the recipe book
	public Recipe get(int index) {
		try {
			return recipeArray.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public ArrayList<Recipe> getArray() {
		return recipeArray;
	}

	public int length() {
		return recipeArray.size();
	}

	public void remove(int index) {
		try {
			recipeArray.remove(index);

			writeBookToSource();
		} catch (IndexOutOfBoundsException ignore) {}
	}

	public int add(Recipe toAdd) {
		recipeArray.add(toAdd);

		writeBookToSource();
		
		return recipeArray.size() - 1;
	}
	
	public void replace(int index, Recipe recipe) {
		try {
			recipeArray.remove(index);
		} catch (IndexOutOfBoundsException ignore) {}
		
		recipeArray.add(index, recipe);
		
		writeBookToSource();
	}
	
	public boolean restoreDefault() {
		try {
			recipeJSON = StorageClient.readJSONFromRaw(parent, rawSource);
			
			convertJSONToArray();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		writeBookToSource();
		
		return true;
	}
	
	public boolean exportToExternal() {
		try {
			recipeJSON = convertArrayToJSON();
			
			StorageClient.writeJSONToExternal(parent.getResources().getString(R.string.app_name), source, recipeJSON);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// Private methods
	private void readBookFromSource() {
		recipeJSON = new JSONArray();

		try {
			recipeJSON = StorageClient.readJSONFromInternal(parent, source, false);
		} catch (FileNotFoundException e) {
			restoreDefault();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			return;
		}

		convertJSONToArray();
	}

	private void writeBookToSource() {
		try {
			recipeJSON = convertArrayToJSON();
			
			StorageClient.writeJSONToInternal(parent, source, recipeJSON);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	private void convertJSONToArray() {
		recipeArray = new ArrayList<>();
		
		try {
			for (int i = 0; i < recipeJSON.length(); i++) {
				recipeArray.add(new Recipe(recipeJSON.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private JSONArray convertArrayToJSON() throws JSONException {
		JSONArray out = new JSONArray();

		for (Recipe toAdd: recipeArray) {
			out.put(toAdd.getJSON());
		}

		return out;
	}
}
