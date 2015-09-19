package com.example.wiskunde;

import java.util.logging.Logger;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

public class SettingsActivity extends Activity implements OnClickListener {
	private Button btnOK;
	private Button btnCancel;
	private NumberPicker difficulty;
	private int current_Difficulty;
	private int current_SecondsToPlay;
	public static final int SETTINGS=1;
	private NumberPicker SecondsToPlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Log.i("Wiskunde", "SetContentView");

		// Find Screen Objects
		btnOK = (Button) findViewById(R.id.btnOK);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		difficulty = (NumberPicker) findViewById(R.id.numberPicker1);
		SecondsToPlay = (NumberPicker) findViewById(R.id.SecondsToPlay);

		// Activate Button Listeners
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		// Get the current Difficulty passed from Main Window
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			current_Difficulty = bundle.getInt("Difficulty");
			Log.i("Wiskunde", "Obtained current Difficulty:"
					+ current_Difficulty);
		} else
			current_Difficulty = 2;
		
		// Get the current Seconds to Play time
		if (bundle != null) {
			if(bundle.containsKey("SecondsToPlay"))
			{
				current_SecondsToPlay = bundle.getInt("SecondsToPlay");
			Log.i("Wiskunde", "Obtained current Seconds to Play:"
					+ current_SecondsToPlay);
			}
			else 
				current_SecondsToPlay = 30;
		} else
			current_SecondsToPlay = 30;
		
		
		// Set Number Picker (Difficulty) values
		difficulty.setMinValue(1);
		difficulty.setMaxValue(10);
		difficulty.setValue(current_Difficulty);
		
		// Set Number Picker (Seconds to Play) values
		SecondsToPlay.setMinValue(10);
		SecondsToPlay.setMaxValue(300);
		SecondsToPlay.setValue(current_SecondsToPlay);

	};

	protected void onStart() {
		super.onStart();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		
		switch (v.getId()) {

		case R.id.btnOK:
			Log.i("Wiskunde",
					"btnOK Clicked. Value Difficulty: " + difficulty.getValue()+ " ;Value Seconds to Play: " + SecondsToPlay.getValue() );
              
            intent.putExtra("Difficulty",difficulty.getValue());  
            intent.putExtra("SecondsToPlay",SecondsToPlay.getValue());  
            
            setResult(SETTINGS,intent);  
			this.finish();
			break;

		case R.id.btnCancel:
			Log.i("Wiskunde", "btnCancel Clicked.");
              
            intent.putExtra("Difficulty",current_Difficulty);  
            intent.putExtra("SecondsToPlay",current_SecondsToPlay);  
              
            setResult(SETTINGS,intent);  
			this.finish();
			break;

		default:
			break;
		}
	};

}
