package com.numbergame.android;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
import android.view.WindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.util.Log;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.*;
import java.lang.Character;
import java.lang.Double;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import org.json.JSONObject;
import org.json.JSONException;


public class FightModeGame extends Activity {
   	
	private final String TAG = "FightModeGame";

	private String myID;
	private String rivalID;
	private int check_retry = 0;
	private String tableName;
	private String targetNumber;
	private String isFirst;

	private TextView titleBar;
	private Button ok_btn;

	private TextView number_1;
	private TextView number_2;
	private TextView number_3;
	private TextView number_4;

	private Button zero_btn;
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
	private TextView show_userID;
	private	EditText pin_code_edit;
	private ProgressBar mProgressBar;

	private int focusColumn;
	private String currentNumbers;
	private String currentResult;
	private ArrayList<Number> currentNumberList;
	private ArrayList<String> displayList;
	private ResultAdapter mAdapter;

	private final String server_domin = "http://61.228.180.251:3000/";
	private final String register_userID_URL = server_domin + "registerUserId";
	private final String check_fetched_URL = server_domin + "checkPairState";
	private final String check_table_URL = server_domin + "checkTable";
	private final String submit_Numbers_URL = server_domin + "submitNumbers";

	private int pincode_digit = 4;
	private boolean taskRunnable = false;
	private boolean gameOver = true;

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

	private OnClickListener mListener = new OnClickListener(){

		@Override
		public void onClick(View view){

			submitNumber();
		}
	};

	// listener for listen grid items
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

	// listener to listen number 1~9
	private OnClickListener mNumberListener = new OnClickListener(){
		
		@Override
		public void onClick(View view){
			
			switch(view.getId()){
				case R.id.zero:
					setNumber(0);
					break;
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


   	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Log.d(TAG, "jasper onCreate");
		// set layout and get view
        setContentView(R.layout.fightmode);
		setMyView();
		initialGame();
	}

	// get layout
	private void setMyView(){
	
		mProgressBar = (ProgressBar) findViewById(R.id.fetch_progress);

		titleBar = (TextView)findViewById(R.id.title_bar);

		number_1 = (TextView)findViewById(R.id.number1);
		number_2 = (TextView)findViewById(R.id.number2);
		number_3 = (TextView)findViewById(R.id.number3);
		number_4 = (TextView)findViewById(R.id.number4);

		ok_btn = (Button)findViewById(R.id.go);

		zero_btn = (Button)findViewById(R.id.zero);
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

		zero_btn.setOnClickListener(mNumberListener);
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

	// prepare the game before start
	private void initialGame(){

		// set default focus column to be the first one
		focusColumn = 1;

		// initial arraylist size=4
		currentNumberList = new ArrayList<Number>();
		for(int i=0; i<4; i++){
			Number tmpNumber = new Number();
			currentNumberList.add(tmpNumber);
		}

		displayList = new ArrayList<String>();
		mAdapter = new ResultAdapter(this, displayList);
		resultList.setAdapter(mAdapter);

		gameOver = false;
		setTaskRunnable(true);

		myID = IdGenerater();
		//myID = "9999";
		rivalID = "00000000";
		targetNumber = "";
		titleBar.setText("Your ID:" + myID);
		getMatchDialog().show();

	}

	private void prepareStartGame(JSONObject obj){

		Log.d(TAG, "jasper prepareStartGame");

		if(obj != null){

			try{

				targetNumber = obj.getString("TargetNumber");
				tableName = obj.getString("TableName");

			}catch(JSONException e){
				Log.e(TAG, "jasper parse json failed:" + e);
			}
		}
		else{
			Log.d(TAG, "jasper obj is NULL");
		}
	}

	// start game
	private void startGame(JSONObject obj){

		Log.d(TAG, "jasper startGame");
		boolean startFirst = false;
		resultList.setVisibility(View.VISIBLE);

		if(!isFirst(tableName)){

			Log.d(TAG, "jasper not FIRST");
			disableUI();
			pauseGame();
		}
		else{

			Log.d(TAG, "jasper FIRST");
			resumeGame();
		}
	}

	private void handleWinner(){

		Log.d(TAG, "jasper WINNER");

		gameOver = true;

		String title = "You win";
		String msg = "YOU WIN";
		myAlertDialog(title, msg).show();

	}

	private void handleLoser(){

		Log.d(TAG, "jasper LOSER");
		String title = "You lose";
		String msg = "YOU LOSE";
		myAlertDialog(title, msg).show();

	}

	private void submitNumber(){

		Log.d(TAG, "jasper submitNumber()");
		setCurrentNumber();
		//getCurrentNumber();
		startCompare();
		currentResult = getCompareResult();

		Log.d(TAG, "jasper currentResult:" + currentResult);

		currentNumbers = "";
		for(int i=0; i<4; i++){
			currentNumbers = currentNumbers + currentNumberList.get(i).no;
		}

		String myResult = currentNumbers + "    " + currentResult;

		updateListView(myResult);

		new MakePostRequestTask().execute(submit_Numbers_URL);

		if("4A0B".equals(currentResult)){
			handleWinner();
		}

	}

	private void resumeGame(){

		enableUI();
	}

	private void pauseGame(){

		disableUI();
		new MakePostRequestTask().execute(check_table_URL);
	}

	private void enableUI(){

		enableButton(ok_btn);
	}

	private void disableUI(){

		disableButton(ok_btn);
	}

	private void enableButton(Button btn){

		if(btn != null){
			btn.setBackgroundResource(R.drawable.enable_ok_button_shape);
		}
	}

	private void disableButton(Button btn){

		if(btn != null){
			btn.setBackgroundResource(R.drawable.disable_ok_button_shape);
		}
	}

	private String IdGenerater(){

		Random r = new Random();
		int rn = -1;
		String ID = "";
		for(int i=0; i<pincode_digit; i++){
			rn = r.nextInt(10);
			ID += rn;

		}
		return ID;
	}

	// Async task for making POST request
	private class MakePostRequestTask extends AsyncTask<String, Void, String>{

		protected String doInBackground(String... urls){

			String postData = "";

			if(isTaskRunnable()){

				try{
					if(register_userID_URL.equals(urls[0])){

						// Register user
						Log.d(TAG, "jasper registerUserId request");
						postData = "UserID=" + myID + "&" + "RivalID=" + rivalID;
					}
					else if(check_fetched_URL.equals(urls[0])){

						// check pair state
						Log.d(TAG, "jasper checkPairState request");
						try{
							Thread.sleep(3000);
							postData = "PollingID=" + myID;
						}catch(InterruptedException e){
							Log.d(TAG, "jasper thread sleep exception:" + e);
						}
					}
					else if(check_table_URL.equals(urls[0])){

						// check number table
						Log.d(TAG, "jasper checkTable request");
						try{
							Thread.sleep(3000);
							postData = "UserID=" + myID + "&" + "RivalID=" + rivalID + "&" + "Table=" + tableName;
						}catch(InterruptedException e){
							Log.d(TAG, "jasper thread sleep exception:" + e);
						}

					}
					else if(submit_Numbers_URL.equals(urls[0])){

						// submit user input numbers to server
						Log.d(TAG, "jasper submitNumbers request");
						postData = "UserID=" + myID + "&" + "guessNumber=" + currentNumbers
									+ "&" + "guessResult=" + currentResult
									+ "&" + "Table=" + tableName;
					}

					Log.d(TAG, "jasper postData:" + postData);
					return postRequest(urls[0], postData);
				}
				catch(IOException e){
					Log.d(TAG, "jasper unable to retrieve the URL :" + e);
				}
			}
			return "test";
		}

		protected void onPostExecute(String result){

			// check request type first
			// POST ID till success, and then keep polling the fetch state

			if(result == null){
				Log.d(TAG, "jasper server not response, returne null");
				return;
			}
			JSONObject Jobj = stringToJson(result);
			String requestType = "hahaha";
			String requestResult = "Fail";

			if(Jobj != null){

				try{
					requestType = Jobj.getString("PostType");
					requestResult = Jobj.getString("Result");
				}catch(JSONException e){
					Log.d(TAG, "jasper json parse exception:" + e);
				}
			}

			if("registerUserId".equals(requestType)){

				// handle post ID request return
				if("Success".equals(requestResult)){

					// start to check pair state
					Log.d(TAG, "jasper registerUserId return SUCCESS");
					new MakePostRequestTask().execute(check_fetched_URL);
				}
				else{

					// keep posting ID
					Log.d(TAG, "jasper registerUserId return FAIL");
					new MakePostRequestTask().execute(register_userID_URL);
				}
			}
			else if("checkPairState".equals(requestType)){

				// handle check pair state request return
				if("Success".equals(requestResult)){

					// Rival found, ready to start game
					Log.d(TAG, "jasper checkPairState return SUCCESS");
					//Log.d(TAG, "jasper rival found, ready to start game");
					Toast.makeText(getApplicationContext(), "Rival found, ready to start game", Toast.LENGTH_LONG).show();
					prepareStartGame(Jobj);
					stopProgressBar();
					startGame(Jobj);
				}
				else{

					// keep checking pair state
					Log.d(TAG, "jasper checkPairState return FAIL");
					new MakePostRequestTask().execute(check_fetched_URL);

				}
			}
			else if("checkTable".equals(requestType)){

				// handle check number table request return
				if("Success".equals(requestResult)){

					Log.d(TAG, "jasper checkTable return SUCCESS");
					String rivalStr = "";
					String rivalResult = "";

					try{
						rivalStr = Jobj.getString("RivalNumbers") + "    " + Jobj.getString("RivalResult");
						rivalResult = Jobj.getString("RivalResult");
					}catch(JSONException e){
						Log.d(TAG, "jasper json parse exception:" + e);	
					}

					updateListView(rivalStr);

					if(!"4A0B".equals(rivalResult)){
						resumeGame();
					}
					else{
						handleLoser();
					}

				}
				else{

					// keep checking table numbers
					Log.d(TAG, "jasper checkTable return FAIL");
					new MakePostRequestTask().execute(check_table_URL);
				}

			}
			else if("submitNumbers".equals(requestType)){

				if("Success". equals(requestResult)){

					Log.d(TAG, "jasper submitNumbers return SUCCESS");
					pauseGame();

					if(gameOver){
						setTaskRunnable(false);
					}
				}
				else{

					// submit again
					Log.d(TAG, "jasper submitNumbers return FAIL");
					new MakePostRequestTask().execute(submit_Numbers_URL);
				}
			}
			else{
				Log.d(TAG, "jasper improper reqestType:" + requestType);
			}

		}
	}

	private String postRequest(String myurl, String postData) throws IOException{
		
		Log.d(TAG, "jasper postRequest");
		int len = 500;
		String response = "";

		try{
			
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(postData);

			writer.flush();
			writer.close();
			os.close();
			int responseCode = conn.getResponseCode();
			if( responseCode  == HttpURLConnection.HTTP_OK){
				
				Log.d(TAG, "jasper connection success");
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					response+=line;
				}

				Log.d(TAG, "jasper response:" + response);

				return response;

			}
			else{
				response="";
				throw new Exception();
			}
		}catch(Exception e){
			Log.d(TAG, "jasper post exception:" + e);
			e.printStackTrace();
			return "Exception happened";
		}
	}
	
	private JSONObject stringToJson(String str){

		JSONObject obj = null;
		try{

			obj = new JSONObject(str);
			Log.d(TAG, "jasper json:" + obj.toString());

			return obj;

		}catch(Throwable t){

			Log.e(TAG, "jasper can not parse malformed string:" + str);
		}
		return obj;
	}

	// cehck the Id assigned from is not illegal
	private Boolean isIDLegal(String serverAssignID){
		
		try{
			
			double tmpID = Double.parseDouble(serverAssignID);
		}
		catch(NumberFormatException e){
			
			Log.d(TAG, "jasper wrong parsing string to number:" + e);
			return false;
		}
		return true;
	}
	
	// check the post request is success
	private Boolean isPostSuccess(String response){
		
		return "POST_SUCCESS".equals(response);
	}

	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
    	
		Reader reader = null;
    	reader = new InputStreamReader(stream, "UTF-8");        
    	char[] buffer = new char[len];
    	reader.read(buffer);
    	return new String(buffer);
	}

	// handle check paired GET request fail
	private void handleTimeout(){

		stopProgressBar();
		Toast.makeText(this, "Can't fectch rival", Toast.LENGTH_SHORT).show();

	}

	// the dialog to inform user there is no network connection
	private AlertDialog myAlertDialog(final String title, final String msg){
		
		Log.d(TAG, "jaspe myAlertDialog");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title)
				.setMessage(msg)
				.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int id){

						Log.d(TAG, "jasper no network click");

						String netWorkProblem = "No Network Connection";
						String Win = "You win";
						String Lose = "You lose";

						if(netWorkProblem.equals(title)){
							finish();
						}
						else if(Win.equals(title)){
							finish();
						}
						else if(Lose.equals(title)){
							finish();
						}
						else if("Improper ID".equals(title)){
							getMatchDialog().show();
						}
					}
				});
		return builder.create();
	}

	// the dialog to allow user input the rival's ID
	private Dialog getMatchDialog(){
		
		Log.d(TAG, "jasper getMatchDialog");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		View v = inflater.inflate(R.layout.pairdialog, null);
		show_userID = (TextView) v.findViewById(R.id.userID_msg);
		pin_code_edit = (EditText) v.findViewById(R.id.pin_code);

		show_userID.setText("Your ID is " + myID);

		builder.setView(v)
			   .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
			       
					@Override
				   	public void onClick(DialogInterface dialog, int id){
				   		
						Log.d(TAG, "jasper rival ID dialog click");
						rivalID = pin_code_edit.getText().toString();
						if(isOkToFindOpponent()){
							findOpponent();
						}
				   	}
			   });

		return builder.create();
	}

	private boolean isOkToFindOpponent(){
		
		// check network connection first
		// and then check input rival's ID is legal

		Log.d(TAG, "jasper isOkToFindOpponent");
		String title;
		String msg;
		if(!isNetworkAvailable()){

			title = "No Network Connection";
			msg = "Please connect to the network";
			myAlertDialog(title, msg).show();

			return false;
		}
		else{
			if(rivalID.isEmpty()){

				title = "Improper ID";
				msg = "Please input 8 digits number";
				myAlertDialog(title, msg).show();

				return false;
			}
			else{
				return true;
			}
		}

	}

	private void findOpponent(){

		Log.d(TAG, "jasper rival pin:" + rivalID);
		startProgressBar();
		new MakePostRequestTask().execute(register_userID_URL);
		//new checkPairTask().execute(check_fetched_URL);
	}

	// check the network is available
	private boolean isNetworkAvailable(){

		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			Log.d(TAG, "jasper network available");
			return true;
		}
		else{

			Log.d(TAG, "jasper network not available");
			return false;
		}

	}

	private void setTaskRunnable(boolean result){

		taskRunnable = result;
	}

	private boolean isTaskRunnable(){

		return taskRunnable;
	}

	private void startProgressBar(){

		Log.d(TAG, "jasper start progress bar");
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void stopProgressBar(){

		Log.d(TAG, "jasper stop progress bar");
		mProgressBar.setVisibility(View.GONE);

		// set container of progressbar gone
		RelativeLayout progressbar_container = (RelativeLayout)mProgressBar.getParent();
		progressbar_container.setVisibility(View.GONE);

		// set container of listview visible
		RelativeLayout listview_container = (RelativeLayout)resultList.getParent();
		listview_container.setVisibility(View.VISIBLE);

	}

	private void updateListView(String result){

		displayList.add(result);
		mAdapter.notifyDataSetChanged();

		// limit the size of ListView be 5
		/*
		if(mAdapter.getCount()>5){

			View item = mAdapter.getView(0, null, resultList);
			item.measure(0, 0);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
			resultList.setLayoutParams(params);
		}
		*/

	}
/*
	private void setListView(){

		ResultAdapter mAdapter = new ResultAdapter(this, displayList);
		resultList.setAdapter(mAdapter);
		resultList.setSelection(resultList.getAdapter().getCount()-1);
	}
*/
	private void cleanup(){

		displayList.clear();
		((BaseAdapter)resultList.getAdapter()).notifyDataSetChanged();
	}

	private String getCompareResult(){

		int A_number = 0;
		int B_number = 0;

		for(int i=0; i<4; i++){

			if(currentNumberList.get(i).match == 1){

				B_number++;
			}
			else if(currentNumberList.get(i).match == 2){

				A_number++;
			}
		}

		return "" + A_number + "A" + B_number + "B";
	}

	private void startCompare(){

		// reset current number list match flag
		for(int i=0; i<4; i++){
			currentNumberList.get(i).match = 0;
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

			if(currentNumberList.get(i).no == targetNumber.charAt(i)){
				// match number and posistion
				currentNumberList.get(i).match = 2;
			}
			else if(mTargetNumbersSet.contains(currentNumberList.get(i).no)){
				// match number only
				currentNumberList.get(i).match = 1;
			}

		}

		for(int i=0; i<4; i++){
			Log.d(TAG, "jasper " + currentNumberList.get(i).no + ":" + currentNumberList.get(i).match);
		}

	}

	// Parse table name
	private boolean isFirst(String table_name){

		if(table_name != null){

			String[] tmpArray = table_name.split("_");
			int tmpLength = tmpArray.length;
			Log.d(TAG, "jasper table array:" + tmpArray[0] + " " + tmpArray[1] + " " + tmpArray[2]);

			if(myID.equals(tmpArray[1])){
				return true;
			}
			return false;
		}
		else{
			Log.d(TAG, "jasper table_name is null");
			return false;
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
			currentNumberList.get(i).no = getFocusColumn(i+1).getText().toString().charAt(0);
		}
	}

	private void getCurrentNumber(){

		Log.d(TAG, "jasper currentNumberList:"
			+ currentNumberList.get(0).no
			+ currentNumberList.get(1).no
			+ currentNumberList.get(2).no
			+ currentNumberList.get(3).no);
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
			getFocusColumn(column).setBackgroundResource(R.drawable.border);
		}
		focusColumn = column;


	}

	private void unfocus(int column){
		
		if(focusColumn != column)return;
		
		if(getFocusColumn(column) != null){
			getFocusColumn(column).setBackgroundResource(0);
		}
	}

}
