package com.numbergame.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;
public class HelloWorldActivity extends Activity {
   	
	private final String TAG = "HelloWorldActivity";

	private Button ok_btn;
	private Button reset_btn;
	private TextView number_1;
	private TextView number_2;
	private TextView number_3;
	private TextView number_4;
	private Button one_btn;
	private Button two_btn;
	private Button three_btn;
	private Button four_btn;
	private Button five_btn;
	private Button six_btn;
	private Button seven_btn;
	private Button eight_btn;
	private Button nine_btn;
	
	private int focusNumber;
	


   	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
		setView();
		focusNumber = 1;
		
    }

	private void setView(){
	
		number_1 = (TextView)findViewById(R.id.number1);
		number_2 = (TextView)findViewById(R.id.number2);
		number_3 = (TextView)findViewById(R.id.number3);
		number_4 = (TextView)findViewById(R.id.number4);

		ok_btn = (Button)findViewById(R.id.go);
		reset_btn = (Button)findViewById(R.id.reset);

		one_btn = (Button)findViewById(R.id.one);
		two_btn = (Button)findViewById(R.id.one);
		three_btn = (Button)findViewById(R.id.one);
		four_btn = (Button)findViewById(R.id.one);
		five_btn = (Button)findViewById(R.id.one);
		six_btn = (Button)findViewById(R.id.one);
		seven_btn = (Button)findViewById(R.id.one);
		eight_btn = (Button)findViewById(R.id.one);
		nine_btn = (Button)findViewById(R.id.one);

		number_1.setOnClickListener(mNumberListener);
		number_2.setOnClickListener(mNumberListener);
		number_3.setOnClickListener(mNumberListener);
		number_4.setOnClickListener(mNumberListener);
		ok_btn.setOnClickListener(mListener);
		reset_btn.setOnClickListener(mListener);
		one_btn.setOnClickListener(mListener);
		two_btn.setOnClickListener(mListener);
		three_btn.setOnClickListener(mListener);
		four_btn.setOnClickListener(mListener);
		five_btn.setOnClickListener(mListener);
		six_btn.setOnClickListener(mListener);
		seven_btn.setOnClickListener(mListener);
		eight_btn.setOnClickListener(mListener);
		nine_btn.setOnClickListener(mListener);
	}

	private OnClickListener mNumberListener = new OnClickListener(){
		
		@Override
		public void onClick(View view){
			
			unfocus(focusNumber);
			switch(view.getId()){
				
				case R.id.number1:
					Log.d(TAG, "jasper number 1");
					focus(1);
					break;
				case R.id.number2:
					Log.d(TAG, "jasper number 2");
					focus(2);
					break;
				case R.id.number3:
					Log.d(TAG, "jasper number 3");
					focus(3);
					break;
				case R.id.number4:
					Log.d(TAG, "jasper number 4");
					focus(4);
					break;
			}
		}
	};

	private OnClickListener mListener = new OnClickListener(){
		
		@Override
		public void onClick(View view){
			
			switch(view.getId()){
				case R.id.go:				
					break;
				case R.id.reset:
					break;
				case R.id.one:
					break;
				case R.id.two:
					break;
				case R.id.three:
					break;
				case R.id.four:
					break;
				case R.id.five:
					break;
				case R.id.six:
					break;
				case R.id.seven:
					break;
				case R.id.eight:
					break;
				case R.id.nine:
					break;
			}
		}
	};

	private void focus(int number){
		
		if(focusNumber == number)return;
		
		switch(number){
			
			case 1:
				number_1.setBackgroundResource(R.drawable.border);
				focusNumber = 1;
				break;
			case 2:
				number_2.setBackgroundResource(R.drawable.border);
				focusNumber = 2;
				break;
			case 3:
				number_3.setBackgroundResource(R.drawable.border);
				focusNumber = 3;
				break;
			case 4:
				number_4.setBackgroundResource(R.drawable.border);
				focusNumber = 4;
				break;
		}
		

	}

	private void unfocus(int number){
		
		if(focusNumber != number)return;
		switch(number){
			
			case 1:
				number_1.setBackgroundResource(0);
				break;
			case 2:
				number_2.setBackgroundResource(0);
				break;
			case 3:
				number_3.setBackgroundResource(0);
				break;
			case 4:
				number_4.setBackgroundResource(0);
				break;
		}
	}

}
