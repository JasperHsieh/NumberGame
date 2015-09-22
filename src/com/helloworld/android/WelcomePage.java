package com.numbergame.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;


public class WelcomePage extends Activity {
	
	final private String TAG = "WelcomePage";
	
	Button level_btn;
	Button fight_btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		level_btn = (Button) findViewById(R.id.level_mode);
		fight_btn = (Button) findViewById(R.id.fight_mode);
		
		level_btn.setOnClickListener(mListener);
		fight_btn.setOnClickListener(mListener);

	}

	private OnClickListener mListener = new OnClickListener(){
		
		@Override
		public void onClick(View view){
			
			switch(view.getId()){
				
				case R.id.level_mode:
					Log.d(TAG, "jasper level mode button");
					startLevelModeActivity();
					break;
				case R.id.fight_mode:
					Log.d(TAG, "jasper fight mode button");
					startFightModeActivity();
					break;
			}
		}
	};

	private void startFightModeActivity(){
		
		Intent intent = new Intent(this, FightModeGame.class);
		startActivity(intent);
	}

	private void startLevelModeActivity(){

		Intent intent = new Intent(this, LevelModeGame.class);
		startActivity(intent);
	}



}
