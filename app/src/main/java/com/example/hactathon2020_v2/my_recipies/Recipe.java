package com.example.hactathon2020_v2.my_recipies;

import org.json.JSONException;
import org.json.JSONObject;

// This class stores one recipe of RecipeBook
public class Recipe {
	
	private String name;
	private String description;

	// Public constructors
	public Recipe() {
		name = "";
		description = "";
	}
	
	public Recipe(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Recipe(JSONObject json) throws JSONException {
		name = json.getString("name");
		description = json.getString("description");
	}

	// Public methods for getting the recipe
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public JSONObject getJSON() throws JSONException {
		JSONObject out = new JSONObject();

		out.put("name", name);
		out.put("description", description);

		return out;
	}
}
