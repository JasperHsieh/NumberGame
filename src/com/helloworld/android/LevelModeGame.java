package com.numbergame.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.util.Log;
import android.content.Context;

import java.util.*;
import java.lang.Character;

public class LevelModeGame extends Activity {
   	
	private final String TAG = "LevleModeGame";

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
	private ListView resultList;


	private int focusColumn;
	private String targetNumber;
	private ArrayList<Number> currentList;
	private ArrayList<String> displayList;

	// number object to store number and match level
	// 0: no match anything
	// 1: match number only
	// 2: match nunmber and postion
	public class Number{
		char no;
		int match = 0;
	}

	public class Result{
		String result_number;
		String result_hint;
	}

	public class ResultAdapter extends ArrayAdapter<String>{

		public ResultAdapter(Context context, ArrayList<String> testResults){
			super(context, 0, testResults);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){

			String test_result = getItem(position);

			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_result, parent, false);
			}

			TextView tv = (TextView) convertView.findViewById(R.id.display_result);
			tv.setText(test_result);

			return convertView;
		}

	}

   	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Log.d(TAG, "jasper onCreate");
		// set layout and get view
        setContentView(R.layout.levelmode);
		setMyView();

		// set default focus column to be the first one
		focusColumn = 1;
		
		// initial arraylist size=4
		currentList = new ArrayList<Number>();
		for(int i=0; i<4; i++){
			Number tmpNumber = new Number();
			currentList.add(tmpNumber);
		}

		resetTargetNumber();
		displayList = new ArrayList<String>();
	}



	private void setMyView(){
	
		number_1 = (TextView)findViewById(R.id.number1);
		number_2 = (TextView)findViewById(R.id.number2);
		number_3 = (TextView)findViewById(R.id.number3);
		number_4 = (TextView)findViewById(R.id.number4);

		ok_btn = (Button)findViewById(R.id.go);

		one_btn = (Button)findViewById(R.id.one);
		two_btn = (Button)findViewById(R.id.two);
		three_btn = (Button)findViewById(R.id.three);
		four_btn = (Button)findViewById(R.id.four);
		five_btn = (Button)findViewById(R.id.five);
		six_btn = (Button)findViewById(R.id.six);
		seven_btn = (Button)findViewById(R.id.seven);
		eight_btn = (Button)findViewById(R.id.eight);
		nine_btn = (Button)findViewById(R.id.nine);

		resultList = (ListView)findViewById(R.id.result_list);

		number_1.setOnClickListener(mColumnListener);
		number_2.setOnClickListener(mColumnListener);
		number_3.setOnClickListener(mColumnListener);
		number_4.setOnClickListener(mColumnListener);

		ok_btn.setOnClickListener(mListener);

		one_btn.setOnClickListener(mNumberListener);
		two_btn.setOnClickListener(mNumberListener);
		three_btn.setOnClickListener(mNumberListener);
		four_btn.setOnClickListener(mNumberListener);
		five_btn.setOnClickListener(mNumberListener);
		six_btn.setOnClickListener(mNumberListener);
		seven_btn.setOnClickListener(mNumberListener);
		eight_btn.setOnClickListener(mNumberListener);
		nine_btn.setOnClickListener(mNumberListener);
	}

	private OnClickListener mListener = new OnClickListener(){

		@Override
		public void onClick(View view){

			switch(view.getId()){

				case R.id.go:
					startGame();
					break;
				case R.id.reset:
					resetTargetNumber();
					cleanup();
					break;
			}
		}
	};

	private OnClickListener mColumnListener = new OnClickListener(){
		
		@Override
		public void onClick(View view){
			
			unfocus(focusColumn);
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

	private OnClickListener mNumberListener = new OnClickListener(){
		
		@Override
		public void onClick(View view){
			
			switch(view.getId()){
				case R.id.one:
					setNumber(1);
					break;
				case R.id.two:
					setNumber(2);
					break;
				case R.id.three:
					setNumber(3);
					break;
				case R.id.four:
					setNumber(4);
					break;
				case R.id.five:
					setNumber(5);
					break;
				case R.id.six:
					setNumber(6);
					break;
				case R.id.seven:
					setNumber(7);
					break;
				case R.id.eight:
					setNumber(8);
					break;
				case R.id.nine:
					setNumber(9);
					break;
			}
			focusNext();
		}
	};

	private void startGame(){

		setCurrentNumber();
		getCurrentNumber();
		startCompare();
		String compareResult = getCompareResult();

		Log.d(TAG, "jasper " + compareResult);

		String currentResult = "";
		for(int i=0; i<4; i++){
			currentResult = currentResult + currentList.get(i).no;
		}

		currentResult = currentResult + "    " + compareResult;
		displayList.add(currentResult);
		/*
		ArrayAdapter mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayList);
		resultList.setAdapter(mArrayAdapter);

		resultList.setSelection(resultList.getAdapter().getCount()-1);
		*/
		setListView();

	}

	private void setListView(){

		ResultAdapter mAdapter = new ResultAdapter(this, displayList);
		resultList.setAdapter(mAdapter);
		resultList.setSelection(resultList.getAdapter().getCount()-1);
	}

	private void cleanup(){

		displayList.clear();
		((BaseAdapter)resultList.getAdapter()).notifyDataSetChanged();
	}

	private String getCompareResult(){

		int A_number = 0;
		int B_number = 0;

		for(int i=0; i<4; i++){

			if(currentList.get(i).match == 1){

				B_number++;
			}
			else if(currentList.get(i).match == 2){

				A_number++;
			}
		}

		return "" + A_number + "A" + B_number + "B";
	}

	private void startCompare(){

		// reset current number list match flag
		for(int i=0; i<4; i++){
			currentList.get(i).match = 0;
		}

		// initial target number set
		Set<Character> mTargetNumbersSet = new HashSet();
		for(int i=0; i<4; i++){
			mTargetNumbersSet.add(targetNumber.charAt(i));
		}
		for(char c:mTargetNumbersSet){
			//Log.d(TAG, "jasper mTargetNumbersSet:" + c);
		}

		// start to compare
		for(int i=0; i<4; i++){

			if(currentList.get(i).no == targetNumber.charAt(i)){
				// match number and posistion
				currentList.get(i).match = 2;
			}
			else if(mTargetNumbersSet.contains(currentList.get(i).no)){
				// match number only
				currentList.get(i).match = 1;
			}

		}

		for(int i=0; i<4; i++){
			Log.d(TAG, "jasper " + currentList.get(i).no + ":" + currentList.get(i).match);
		}

	}

	// can optimize
	private void resetTargetNumber(){

		targetNumber = "";
		Set<String> mNumbers = new HashSet();
		Random rn = new Random();
		String tmp = String.valueOf(rn.nextInt(9) + 1);
		mNumbers.add(tmp);
		//Log.d(TAG, "jasper tmp:" + tmp);
		while(mNumbers.size() < 4){

			tmp = String.valueOf(rn.nextInt(9) + 1);
			if(!mNumbers.contains(tmp)){
				mNumbers.add(tmp);
				//Log.d(TAG, "jasper tmp:" + tmp);
			}
		}

		for(String s: mNumbers){
			targetNumber = targetNumber + s;
		}
		Log.d(TAG, "jasper targetNumber:" + targetNumber);
	}

	private void setCurrentNumber(){

		for(int i=0; i<4; i++){

			//Log.d(TAG,"jasper add:" + getFocusColumn(i+1).getText().toString());
			currentList.get(i).no = getFocusColumn(i+1).getText().toString().charAt(0);
		}
	}

	private void getCurrentNumber(){

		Log.d(TAG, "jasper currentList:"
			+ currentList.get(0).no
			+ currentList.get(1).no
			+ currentList.get(2).no
			+ currentList.get(3).no);
	}

	private void focusNext(){
		
		Log.d(TAG, "jasper focusNext");
		if(focusColumn == 4)return;

		unfocus(focusColumn);
		focus(++focusColumn);
	}

	private void setNumber(int number){
		
		Log.d(TAG, "jasper f:" + focusColumn + " n:" + number);
		String s = "" + number;
		getFocusColumn(focusColumn).setText(s);
	}
	
	private TextView getFocusColumn(int column){
		
		switch(column){
			
			case 1:
				return number_1;
			case 2:
				return number_2;
			case 3:
				return number_3;
			case 4:
				return number_4;
			default:
				Log.d(TAG, "jasper wrong column number");
				return null;
		}
	}

	private void focus(int column){
		
		
		if(getFocusColumn(column) != null){
			getFocusColumn(column).setBackgroundResource(R.drawable.focus_grid);
		}
		focusColumn = column;


	}

	private void unfocus(int column){
		
		if(focusColumn != column)return;
		
		if(getFocusColumn(column) != null){
			getFocusColumn(column).setBackgroundResource(R.drawable.unfocus_grid);
		}
	}

}
