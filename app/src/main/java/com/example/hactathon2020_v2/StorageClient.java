package com.example.hactathon2020_v2;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Class for conveniently writing to and reading from storage, both internal and external
public class StorageClient {
	public static JSONArray readJSONFromInternal(Context parent, String source, boolean create) throws IOException, JSONException {
		FileInputStream is = null;
		try {
			is = parent.openFileInput(source);
		} catch (FileNotFoundException e) {
			if (create) {
				parent.openFileOutput(source, Context.MODE_PRIVATE);
				return new JSONArray();
			} else {
				throw new FileNotFoundException();
			}
		}
		
		StringBuilder builder = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();
		while (line != null) {
			builder.append(line);
			line = reader.readLine();
		}
		
		return new JSONArray(builder.toString());
	}
	
	public static void writeJSONToInternal(Context parent, String source, JSONArray toWrite) throws IOException {
		FileOutputStream os = parent.openFileOutput(source, Context.MODE_PRIVATE);
		
		os.write(toWrite.toString().getBytes());
	}
	
	public static void writeJSONToExternal(String dir, String source, JSONArray toWrte) throws IOException {
		File sourceDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dir);

		File out = new File(sourceDir, source);
		out.getParentFile().mkdirs();
		out.delete();
		out.createNewFile();
		
		FileOutputStream os = new FileOutputStream(out);
		
		os.write(toWrte.toString().getBytes());
	}
	
	public static JSONArray readJSONFromRaw(Context parent, int resource) throws IOException, JSONException {
		InputStream is = parent.getResources().openRawResource(resource);

		StringBuilder builder = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();
		while (line != null) {
			builder.append(line);
			line = reader.readLine();
		}

		return new JSONArray(builder.toString());
	}
}
