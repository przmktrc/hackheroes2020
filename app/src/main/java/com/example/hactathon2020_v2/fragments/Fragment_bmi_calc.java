package com.example.hactathon2020_v2.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hactathon2020_v2.R;

public class Fragment_bmi_calc extends MyFragment {
	
	// Inner class for different ranges of BMI results
	private static class PossibleResult {
		double upperLimit;
		int resourceId;
		
		PossibleResult(double upperLimit, int resourceId) {
			this.upperLimit = upperLimit;
			this.resourceId = resourceId;
		}
	}
	
	private PossibleResult[] possibleResults;
	
	private TextView BMIout, BMIoutStatus;
	private EditText massInp, heightInp;
	
	// getTitle inherited from MyFragment. See MyFragment
	public int getTitle() {
		return R.string.BMI_calc_title;
	}

	// onCreate override - get components
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bmi_calc, container, false);

		BMIout = view.findViewById(R.id.bmi_calc_textView_bmi);
		BMIoutStatus = view.findViewById(R.id.bmi_calc_textView_bmi_status);
		
		massInp = view.findViewById(R.id.bmi_calc_editText_mass);
		heightInp = view.findViewById(R.id.bmi_calc_editText_height);
		
		Button bttn = view.findViewById(R.id.bmi_calc_button);
		bttn.setOnClickListener(v -> calculateBMI());
		
		possibleResults = new PossibleResult[]{
				new PossibleResult(16, R.string.bmi_result_too_low_3),
				new PossibleResult(17, R.string.bmi_result_too_low_2),
				new PossibleResult(18.5, R.string.bmi_result_too_low_1),
				new PossibleResult(25, R.string.bmi_result_OK),
				new PossibleResult(30, R.string.bmi_result_too_high_1),
				new PossibleResult(35, R.string.bmi_result_too_high_2),
				new PossibleResult(40, R.string.bmi_result_too_high_3),
				new PossibleResult(Double.MAX_VALUE, R.string.bmi_result_too_high_4)
		};
		
		calculateBMI();
		
		return view;
	}
	
	// Calculate BMI from the given inputs and display the result and short explanation (is BMI too low, too high, etc.)
	private void calculateBMI() {
		BMIout.setVisibility(View.VISIBLE);
		BMIoutStatus.setVisibility(View.VISIBLE);
		
		double mass, height, bmi;
		
		try {
			mass = Integer.parseInt(massInp.getText().toString());
			height = Integer.parseInt(heightInp.getText().toString());
		} catch (NumberFormatException e) {
			BMIout.setVisibility(View.INVISIBLE);
			BMIoutStatus.setVisibility(View.INVISIBLE);
			return;
		}
		
		bmi = (mass * 10000) / (height * height);

		int result = R.string.WBL;
		for (PossibleResult possibleResult : possibleResults) {
			if (bmi < possibleResult.upperLimit) {
				result = possibleResult.resourceId;
				break;
			}
		}

		BMIout.setText(getString(R.string.bmi_calc_result, bmi));
		BMIoutStatus.setText(String.format(getString(R.string.bmi_calc_result_state), getString(result)));
	}
}
