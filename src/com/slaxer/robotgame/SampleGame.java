package com.slaxer.robotgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

import com.slaxer.framework.Screen;
import com.slaxer.framework.implementation.AndroidGame;

public class SampleGame extends AndroidGame {
	
	public static String map;
	boolean firstTimeCreate = true;
	
	@Override
	public Screen getInitScreen(){
		if(firstTimeCreate){
			Assets.load(this);
			firstTimeCreate = false;
		}
		
		InputStream mapInputStream = getResources().openRawResource(R.raw.map1);
		map = convertStreamToString(mapInputStream);
		
		return new SplashLoadingScreen(this);
	}
	
	@Override
	public void onBackPressed(){
		getCurrentScreen().backButton();
	}
	
	private static String convertStreamToString(InputStream convertStream){
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(convertStream));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try{
			while((line = inputReader.readLine()) != null){
				sb.append(line + "\n");
			}
		} catch(IOException e){
			Log.w("LOG", e.getMessage());
		} finally{
			try{
				convertStream.close();
			} catch(IOException e){
				Log.w("LOG", e.getMessage());
			}
		}
		
		return sb.toString();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		Assets.theme.play();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Assets.theme.pause();
	}
}
