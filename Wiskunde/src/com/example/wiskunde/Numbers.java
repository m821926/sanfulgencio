package com.example.wiskunde;

import java.util.ArrayList;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Numbers {

	private static int rows = 3;
	private static int columns = 5;
	private static int attempts_left=3;
	private static String logTitle = "Numbers";
	private static int[][] nrs = new int[rows][columns];

	public static int getNr(int x, int y) {
		return nrs[x][y];
	}

	public static void setNr(int x, int y, int value) {
		Log.d(logTitle, "setNr("+x+","+y+") = " + value);
		nrs[x][y] = value;
	}
	
	public static Boolean ReduceAttemtpsLeft()
	{
		// Returns True if attempts are left else returns false
		if(--attempts_left> 0)
			return true;
		else
			return false;
	}
	public static void ResetAttemptsLeft()
	{
		attempts_left = 3;
	}

}
