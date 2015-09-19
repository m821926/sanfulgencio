package com.example.wiskunde;

import java.security.PublicKey;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRouter.VolumeCallback;
import android.util.Log;

public class Music {
	private static MediaPlayer mp = null;
	public static String MUSIC = "MusicClass";
	private static float mpVolume = 1;
	private static Boolean _isPlaying = false;
	private static Boolean _isOnResume = false;

	public static void Play(Context context, int resource) {

		stop(context);
		mp = MediaPlayer.create(context, resource);
		mp.setLooping(true);
		mp.start();
		mp.setVolume(mpVolume, mpVolume);
		Log.d(MUSIC, "Play");
		// _isPlaying = true;
	}

	public static void stop(Context context) {
		if (mp != null) {
			Log.d(MUSIC, "Stop");
			mp.stop();
			mp.release();
			mp = null;
			// _isPlaying = false;
		}
	}

	public static Boolean isPlaying() {
		return _isPlaying;
	}

	public static void setOnResume(Boolean value) {
		_isOnResume = value;
	}

	public static void setIsPlaying(Boolean value) {
		_isPlaying = value;
	}

	public static Boolean isOnResume() {
		return _isOnResume;
	}

	public static int getCurrentPosition() {
		if (mp != null) {
			return mp.getCurrentPosition();
		} else
			return 0;
	}

	public static void SetVolume(float NewVolume) {
		if (mp != null) {
			mp.setVolume(NewVolume, NewVolume);
		}
			mpVolume = NewVolume;
		
	}

	public static float GetVolume() {

		return mpVolume;
	}

	public static Boolean IsPlaying() {
		if (mp != null) {
			return true;
		} else
			return false;
	}

	public static void Resume(Context context, int resource, int musicPos) {
		// TODO Auto-generated method stub
		Log.d(MUSIC, "Resume the music at Position " + musicPos);
		stop(context);
		mp = MediaPlayer.create(context, resource);
		mp.setLooping(true);
		mp.seekTo(musicPos);
		mp.start();
		_isPlaying = true;
	}

}
