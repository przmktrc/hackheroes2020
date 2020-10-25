package com.example.hactathon2020_v2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

// This class stores settings
public class SettingsManager {
	
	private Context parent;
	private String source;
	
	private JSONObject settingsJSON;
	
	private String format, delimiter;
	
	// Public constructor
	public SettingsManager(Context parent, String source) {
		this.parent = parent;
		this.source = source;
		
		readSettings();
	}
	
	// Public methods for manipulating settings
	public String getFormat() {
		return format;
	}
	
	public String getDelimiter() {
		return delimiter;
	}

	public void setFormat(String format, String delimiter) {
		this.format = format;
		this.delimiter = delimiter;
		
		writeSettings();
	}

	public boolean restoreDefault() {
		try {
			settingsJSON = StorageClient.readJSONFromRaw(parent, R.raw.settings).getJSONObject(0);
			
			format = settingsJSON.getJSONObject("format").getString("format");
			delimiter = settingsJSON.getJSONObject("format").getString("delimiter");
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			return false;
		}

		writeSettings();
		
		return true;
	}
	
	// Private methods
	private void readSettings() {
		try {
			settingsJSON = StorageClient.readJSONFromInternal(parent, source, false).getJSONObject(0);
		} catch (FileNotFoundException e) {
			restoreDefault();
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			JSONObject formatJSON = settingsJSON.getJSONObject("format");
			format = formatJSON.getString("format");
			delimiter = formatJSON.getString("delimiter");
		} catch (JSONException e) {
			e.printStackTrace();
			restoreDefault();
		}
	}

	private void writeSettings() {
		try {
			settingsJSON = convertToJSON();
			
			JSONArray temp = new JSONArray();
			temp.put(settingsJSON);

			StorageClient.writeJSONToInternal(parent, source, temp);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject convertToJSON() throws JSONException {
		JSONObject out = new JSONObject(), formatJSON = new JSONObject();
		
		formatJSON.put("format", format);
		formatJSON.put("delimiter", delimiter);
		
		out.put("format", formatJSON);
		
		return out;
	}
}
