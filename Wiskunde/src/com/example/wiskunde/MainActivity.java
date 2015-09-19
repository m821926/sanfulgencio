package com.example.wiskunde;

import java.io.IOException;

import com.example.wiskunde.R.string;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener, OnSeekBarChangeListener {
	private Button btnPlay;
	private Button btnSettings;
	private Button btnExit;
	private SeekBar MusicVolume;
	private ToggleButton btnMusic;
	public static final int SETTINGS = 1;
	private static final int LENGTH_SHORT = 0;
	public static final String PREFS_NAME = "WiskundePrefsFile";
	private static String DIFFICULTY = "GameDifficulty";
	private static String SECONDSTOPLAY = "SecondsToPlay";
	private static String MEDIAPLAYER_VOLUME = "mediaplayerVolume";
	private static String MEDIAPLAYER_PLAYING = "mediaplayerPlaying";
	private static String MEDIAPLAYER_POSITION = "mediaplayerPosition";
	private static String SHAREDPREF_ISMUSICPLAYING = "IsMusicPlaying";
	private static String SHAREDPREF_MUSICVOLUME = "MusicVolume";
	private static String LOG_TITLE = "Wiskunde";
	private int musicPos = 0;
	private float musicVolume = 1;
	private Boolean ISmusicPlaying;
	// Game Settings
	private int Difficulty;
	private int SecondsToPlay;

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TITLE, "onResume");
		if (Music.isOnResume()) { // Only Start music if it was playing
			if (musicPos != 0) {
				Music.Resume(this, R.raw.animals, musicPos);
				Music.SetVolume(musicVolume);
				Music.setIsPlaying(true);
			} else
				Music.Play(this, R.raw.animals);
			Music.setIsPlaying(true);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		


		// Map all Buttons
		Log.d(LOG_TITLE, "Mapp all Buttons, Views, ...");
		btnSettings = (Button) findViewById(R.id.btnSettings);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnMusic = (ToggleButton) findViewById(R.id.btnMusic);
		btnExit = (Button) findViewById(R.id.btnExit);
		MusicVolume = (SeekBar) findViewById(R.id.MusicVolume);
		btnMusic.setChecked(Music.isPlaying());

		// Program has been first opened so obtain variables from Files
		Music.setOnResume(false);
		SharedPreferences settings = getSharedPreferences("userInput", 0);
		String txtIsMusicPlaying = settings.getString(
				SHAREDPREF_ISMUSICPLAYING, "Yes");
		Music.SetVolume(settings.getFloat(SHAREDPREF_MUSICVOLUME, 1));
		Log.d(LOG_TITLE, "OnCreate : Saved Preferences, Music Volume: "+ Music.GetVolume());
		MusicVolume.setProgress((int)  (Music.GetVolume()*100f));
		Log.d(LOG_TITLE, "OnCreate : Saved Preferences, Ismusicplaying: " + txtIsMusicPlaying);
		if ( txtIsMusicPlaying.equals("Yes")) {
			Music.Play(this, R.raw.animals);
			Music.setIsPlaying(true);
			btnMusic.setChecked(true);
			Log.d(LOG_TITLE, "OnCreate : Saved Preferences, Started Music ");

		}
		else
		{
			Music.setIsPlaying(false);
			btnMusic.setChecked(false);
			Log.d(LOG_TITLE, "OnCreate : Saved Preferences, Stopped Music");
		}
			if (savedInstanceState != null) {
			// // User must have switched between vertical and horizontal
			Music.setOnResume(true);
			musicPos = savedInstanceState.getInt(MEDIAPLAYER_POSITION, 0);
			musicVolume = savedInstanceState.getFloat(MEDIAPLAYER_VOLUME, 1);
			Music.setIsPlaying(savedInstanceState.getBoolean(MEDIAPLAYER_PLAYING,
					true));

			Log.e(LOG_TITLE,
					"OnCreate - SavedInstanceState - Music Position : "
							+ musicPos + " and Volume : " + musicVolume);

			if (Music.isPlaying())
				Log.d(LOG_TITLE, "OnCreate : Music is Playing");
			else
				Log.d(LOG_TITLE, "OnCreate : Music is NOT Playing");
		}

			 // Restore preferences
		       Difficulty = settings.getInt(DIFFICULTY, 5);
		       SecondsToPlay = settings.getInt(SECONDSTOPLAY, 30);
		       
		// Set listeners of Buttons
		Log.d(LOG_TITLE, "Set Listeners of Buttons");
		btnSettings.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		MusicVolume.setOnSeekBarChangeListener(this);
		btnMusic.setOnCheckedChangeListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnPlay:
			Intent Play = new Intent(MainActivity.this, PlayActivity.class);
			//startActivity(Play);
			
			startActivityForResult(Play, SETTINGS);
			break;

		case R.id.btnSettings:
			// The Settings Button has been clicked where he can change the
			// settings
			Intent settings = new Intent(MainActivity.this,
					SettingsActivity.class);
			settings.putExtra("Difficulty", Difficulty);
			settings.putExtra(SECONDSTOPLAY, SecondsToPlay);
			startActivityForResult(settings, SETTINGS);
			break;

		case R.id.btnExit:
			// The exit button has been clicked and the application has to be
			// closed

			// Save Shared Preferences
			Log.d(LOG_TITLE, "btnExit-OnClick - Save Shared Preferences");
			String strIsMusicPlaying;
			SharedPreferences sharedprefsettings = getSharedPreferences(
					"userInput", 0);
			SharedPreferences.Editor editor = sharedprefsettings.edit();
			if (Music.isPlaying()) {
				strIsMusicPlaying = "Yes";

			} else {
				strIsMusicPlaying = "No";
			}
			Log.d(LOG_TITLE, "btnExit - IsMusicPlaying: " + strIsMusicPlaying);
			editor.putString(SHAREDPREF_ISMUSICPLAYING, strIsMusicPlaying);

			Log.d(LOG_TITLE, "btnExit - Music Volume: " + (float) Music.GetVolume());
			editor.putFloat(SHAREDPREF_MUSICVOLUME, Music.GetVolume());

		      editor.putInt(DIFFICULTY, Difficulty);
		      editor.putInt(SECONDSTOPLAY, SecondsToPlay);

			editor.commit();

			// Stop Music
			Music.stop(this);

			// Exit the program
			finish();
			System.exit(0);
			break;
		default:
			break;
		}
	}

	// Call Back method to get the Message form other Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// check if the request code is same as what is passed here it is 2
		if (requestCode == SETTINGS) {
			int answer = data.getIntExtra("Difficulty", 2);
			Difficulty = answer;
			SecondsToPlay = data.getIntExtra(SECONDSTOPLAY, 30);
			Log.i(LOG_TITLE, "We got an answer back" + answer);
			Toast.makeText(this, "Diffculty = " + answer, LENGTH_SHORT).show();
			Toast.makeText(this, "Seconds to Play = " + SecondsToPlay, LENGTH_SHORT).show();
			;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.btnMusic:
			if (isChecked) {
				// Start Music
				Music.Play(this, R.raw.animals);
				Music.SetVolume(musicVolume);
				Log.e(LOG_TITLE, "Start Music");
				Music.setIsPlaying(true);
				ISmusicPlaying = true;
			} else {
				// // Stop Music
				Music.stop(this);
				Music.setIsPlaying(false);
				ISmusicPlaying = false;
			}

			break;

		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();


	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		musicPos = Music.getCurrentPosition();
		musicVolume = Music.GetVolume();
		ISmusicPlaying = Music.isPlaying();
		Log.d(LOG_TITLE, "OnPause - Isplaying:" + ISmusicPlaying.toString());
		Log.d(LOG_TITLE, "OnPause - musicVolume:" + musicVolume);
		Log.d(LOG_TITLE, "OnPause - musicPos:" + musicPos);
		Music.stop(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		
		outState.putInt(MEDIAPLAYER_POSITION, musicPos);
		outState.putFloat(MEDIAPLAYER_VOLUME, Music.GetVolume());
		outState.putBoolean(MEDIAPLAYER_PLAYING, Music.isPlaying());
		Log.e("Wiskunde", "OnSaveInstanceState - Current Music Position : "
				+ musicPos + " volume: " + musicVolume);
		if (ISmusicPlaying)
			Log.e(LOG_TITLE, "OnSaveInstanceState - Music is Playing");
		else
			Log.e(LOG_TITLE, "OnSaveInstanceState - Music is NOT Playing");
		if (Music.isPlaying())
			Music.setOnResume(true);
		else
			Music.setOnResume(false);
		
		// Save Game Settings
				outState.putInt(DIFFICULTY, Difficulty);
				outState.putInt(SECONDSTOPLAY, SecondsToPlay);
				Log.e("Wiskunde", "OnSaveInstanceState - Current Game Difficulty : "
						+ Difficulty + " Seconds to Play: " + SecondsToPlay);
				
	}
@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onRestoreInstanceState(savedInstanceState);
	musicPos = savedInstanceState.getInt(MEDIAPLAYER_POSITION);
	Music.SetVolume(savedInstanceState.getFloat(MEDIAPLAYER_VOLUME));
	if(savedInstanceState.getBoolean(MEDIAPLAYER_PLAYING))
	{
		Music.setOnResume(true);
	}
	else
	{
		Music.setOnResume(false);
	}
		
	// Restore Game Settings
	Difficulty = savedInstanceState.getInt(DIFFICULTY);
	SecondsToPlay = savedInstanceState.getInt(SECONDSTOPLAY);
	
}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {
		switch (seekBar.getId()) {
		case R.id.MusicVolume:
			// Music Volume Button is changing value

			musicVolume = (float) progress / 100f;
			Log.d("SEEKBAR", "Music Volume is now " + musicVolume);
			Music.SetVolume(musicVolume);
			break;

		default:
			break;
		}

	}

}
