package com.example.wiskunde;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.SyncStateContract.Columns;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity implements OnClickListener {
	private static final int INVISIBLE = 0;
	private int current_Difficulty;
	private int current_SecondsToPlay;
	public static final int SETTINGS=1;
	// private static Numbers numbers = new Numbers();
	private static int rows = 3;
	private static int columns = 5;
	private static String PLAY = "Play";
	ArrayList<TextView> NumberFields = new ArrayList<TextView>();
	ArrayList<EditText> Results = new ArrayList<EditText>();
	Button btnVerify;
	Button btnExit;
	TextView TimeCounter;
	CountDownTimer MyTimer;
	Context PlayContext;
	ProgressBar myProgressbar;

	// private static int[][] nrs = new int[rows][columns];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

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

		// Assign all objects to variables
		Log.d(PLAY, "Assign all objects");
		NumberFields.add((TextView) findViewById(R.id.Nr1_1));
		NumberFields.add((TextView) findViewById(R.id.Nr1_2));
		NumberFields.add((TextView) findViewById(R.id.Nr1_3));
		NumberFields.add((TextView) findViewById(R.id.Nr1_4));
		NumberFields.add((TextView) findViewById(R.id.Nr2_1));
		NumberFields.add((TextView) findViewById(R.id.Nr2_2));
		NumberFields.add((TextView) findViewById(R.id.Nr2_3));
		NumberFields.add((TextView) findViewById(R.id.Nr2_4));

		Results.add((EditText) findViewById(R.id.Nr3_1));
		Results.add((EditText) findViewById(R.id.Nr3_2));
		Results.add((EditText) findViewById(R.id.Nr3_3));
		Results.add((EditText) findViewById(R.id.Nr3_4));
		Results.add((EditText) findViewById(R.id.Nr3_5));

		// Results.get(Results.size()-1).clearFocus();

		// Results.get(Results.size()-1).selectAll();
		myProgressbar = (ProgressBar) findViewById(R.id.progressBar1);
		TimeCounter = (TextView) findViewById(R.id.TimeCounter);
		btnVerify = (Button) findViewById(R.id.btnVerify);
		btnExit = (Button) findViewById(R.id.btnExit);
		// End Assign all objects to variables.

		// Set Listener of Button
		btnVerify.setOnClickListener(this);
		btnExit.setOnClickListener(this);

		// Set Context
		PlayContext = this;
		myProgressbar.setProgress(0);
		
		for (int i = 0; i < Results.size(); i++)
			Results.get(i).setText("0");

		// Set all values in array to Zero.
		Log.d(PLAY, "Set All values to Zero in NumberFields");
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				// nrs[r][c] = 0;

				Numbers.setNr(r, c, 0);
			}
		}

		// Get the NumberFields to summarize
		Log.d(PLAY, "Generate random number");
		Random randomnr = new Random();
		TextView tv;
		int t = 0;
		for (int r = 0; r < rows - 1; r++) { // rows-1 as last row does not need
												// to be randomized
			for (int c = 1; c < columns; c++) // columns will start from second
												// number as the first number
												// needs to stay 0
			{

				Numbers.setNr(r, c, randomnr.nextInt(10 - 0));
				// nrs[r][c] = randomnr.nextInt(10 - 0);
				Log.d(PLAY, "View ( " + r + "," + c + ") " + t
						+ " gets number : " + Numbers.getNr(r, c));

				NumberFields.get(t)
						.setText(String.valueOf(Numbers.getNr(r, c)));
				t++;
			}
		}

		// Calculate the Result
		int result;
		for (int i = 0; i < columns; i++) {
			result = Numbers.getNr(0, columns - 1 - i)
					+ Numbers.getNr(1, columns - 1 - i)
					+ Numbers.getNr((rows - 1), (columns - 1 - i));
			Log.i(PLAY,
					"Result = " + Numbers.getNr(0, columns - 1 - i) + "+"
							+ Numbers.getNr(1, columns - 1 - i) + " + "
							+ Numbers.getNr((rows - 1), (columns - 1 - i))
							+ " = " + result);
			if (result >= 10) {
				Log.d(PLAY, "Result > 10 for " + (columns - 1 - i));
				Numbers.setNr((rows - 1), (columns - 2 - i), 1);
				// nrs[rows - 1][columns - 2 - i] = 1;
				result = result - 10;
			}
			Numbers.setNr((rows - 1), (columns - 1 - i), result);
			// nrs[rows - 1][columns - 1 - i] += result;
			Log.d(PLAY, "Result for (" + (rows - 1) + "," + (columns - 1 - i)
					+ ") = " + Numbers.getNr((rows - 1), (columns - 1 - i)));

		}

		// Start CountDown Timer
		MyTimer =  new CountDownTimer(current_SecondsToPlay * 1000, 1000) {

			public void onTick(long millisUntilFinished) {
				TimeCounter.setText("" + millisUntilFinished / 1000);
				myProgressbar.incrementProgressBy(1);
			}

			public void onFinish() {
				TimeCounter.setText("");
				myProgressbar.setProgress(30);
				Toast.makeText(PlayContext,
						R.string.TimeIsUp,
						Toast.LENGTH_SHORT).show();
				btnVerify.setEnabled(false);
				btnVerify.setVisibility(INVISIBLE);

			}
		}.start();

	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.d(PLAY, "OnResume - Request Focus for Number 3,5");
		EditText editText = (EditText) Results.get(Results.size() - 1);

		// Focus and Show numeric keyboard for field 3,5
		editText.setFocusable(true); // If not set then the keyboard will not
										// show up
		editText.setFocusableInTouchMode(true); // If not set then keyboard will
												// not show up
		editText.requestFocus(); // Set focus to the last field
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		// showTheKeyboard(this, editText);

	}

	//     /**
	//      * Method for hiding the Keyboard
	//      * @param context The context of the activity
	//      * @param editText The edit text for which we want to hide the
	// keyboard
	//      */
	public void hideTheKeyboard(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	// /**
	// * Method for showing the Keyboard
	// * @param context The context of the activity
	// * @param editText The edit text for which we want to show the keyboard
	// */
	public void showTheKeyboard(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnVerify:
			// Verify if all fields have been filled in
			Boolean isAnswerIncomplete = false;
			for (int i = 0; i < Results.size(); i++) {
				if (Results.get(i).getText().length() == 0)
					isAnswerIncomplete = true;
			}

			if (isAnswerIncomplete) {
				Toast.makeText(this, "You did not complete all numbers",
						Toast.LENGTH_SHORT).show();
				break;
			}
			// End Verify all fields have been filled in
			Boolean CorrectAnswer = true;
			String result;
			for (int t = 0; t < columns; t++) {
				result = Results.get(t).getText().toString();
				Log.d(PLAY, "Result :" + result + "==" + Numbers.getNr(2, t)
						+ "?");
				if (result.length() > 0) {
					if (Integer.valueOf(result) == Numbers.getNr(2, t))
						Results.get(t).setBackgroundColor(Color.GREEN);
					else {
						Results.get(t).setBackgroundColor(Color.RED);
						CorrectAnswer = false;
					}
				}
			}

			if (CorrectAnswer) {
				// Magnificent you have the correct answer
				btnVerify.setEnabled(false);
				MyTimer.cancel();
			} else {
				// Wrong Answer
				if (Numbers.ReduceAttemtpsLeft()) {
					// Try Again
					Toast.makeText(this, R.string.Wrong_Answer_Attempts_Left,
							Toast.LENGTH_SHORT).show();
				} else {
					// No lives left
					Toast.makeText(this,
							R.string.Wrong_Answer_No_Attempts_Left,
							Toast.LENGTH_SHORT).show();
					btnVerify.setEnabled(false);
					btnVerify.setVisibility(INVISIBLE);

				}
			}

			break;

		case R.id.btnExit:
			Log.d(PLAY, "OnExit - Disable Keyboard");
			EditText editText = (EditText) Results.get(Results.size() - 1);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

			Log.d(PLAY, "OnExit - Finish()");
			Intent intent=new Intent();
			setResult(SETTINGS,intent);  
			this.finish();
			break;
		default:
			break;
		}
	}

}
