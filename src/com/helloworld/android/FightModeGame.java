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
	private String targetNumber;
	private ArrayList<Number> currentList;
	private ArrayList<String> displayList;

	//private final String get_user_ID_URL = "";
	private final String post_ID_URL = "http://118.166.88.157:3000/postID";
	private final String check_fetched_URL = "http://118.166.88.157:3000/checkFetched";
	private final String check_table_URL = "http://118.166.88.157:3000/checkTable";

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
		prepareStartGame();
	}

	// get layout
	private void setMyView(){
	
		mProgressBar = (ProgressBar) findViewById(R.id.fetch_progress);

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
	private void prepareStartGame(){

		// set default focus column to be the first one
		focusColumn = 1;

		// initial arraylist size=4
		currentList = new ArrayList<Number>();
		for(int i=0; i<4; i++){
			Number tmpNumber = new Number();
			currentList.add(tmpNumber);
		}

		//resetTargetNumber();
		displayList = new ArrayList<String>();

		//myID = IdGenerater();
		myID = "99999999";
		rivalID = "00000000";
		getMatchDialog().show();


	}

	// start game
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
		setListView();

	}

	private String IdGenerater(){

		Random r = new Random();
		int rn = -1;
		String ID = "";
		for(int i=0; i<8; i++){
			rn = r.nextInt(10);
			ID += rn;

		}
		return ID;
	}

	// Async task for making POST request
	private class makePostRequest extends AsyncTask<String, Void, String>{

		protected String doInBackground(String... urls){

			String postData = "";
			try{
				if(post_ID_URL.equals(urls[0])){

					postData = "UserID=" + myID + "&" + "RivalID=" + rivalID;
				}
				else if(check_fetched_URL.equals(urls[0])){

					try{
						Thread.sleep(500);
						postData = "PollingID=" + myID;
					}catch(InterruptedException e){
						Log.d(TAG, "jasper thread sleep exception:" + e);
					}
				}
				else if(check_table_URL.equals(urls[0])){
				}

				return postRequest(urls[0], postData);
			}
			catch(IOException e){

				Log.d(TAG, "jasper unable to retrieve the URL :" + e);
			}

			return "test";
		}

		protected void onPostExecute(String result){

			JSONObject Jobj = stringToJson(result);
			String requestType = "";
			String requestResult = "";

			try{
				requestType = Jobj.getString("postType");
				requestResult = Jobj.getString("result");
			}catch(JSONException e){
				Log.d(TAG, "jasper json parse exception:" + e);
			}

			if("postID".equals(requestType)){

				if("Success".equals(requestResult)){

					new makePostRequest().execute(check_fetched_URL);
				}
				else{
					new makePostRequest().execute(post_ID_URL);
				}
			}
			else if("pollingID".equals(requestType)){

				if("Success".equals(requestResult)){

					// Rival found, ready to start game
					Log.d(TAG, "jasper rival found, ready to start game");
					Toast.makeText(getApplicationContext(), "Rival found, ready to start game", Toast.LENGTH_LONG).show();

				}
				else{

					new makePostRequest().execute(check_fetched_URL);

				}
			}
			else if("checkTable".equals(requestType)){

			}

		}
	}

	private String postRequest(String myurl, String postData) throws IOException{
		
		Log.d(TAG, "jasper postID");
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

				Log.d(TAG, "jasper reponse:" + response);

				return response;

			}
			else{
				response="";
				throw new Exception();
			}
		}catch(Exception e){
			Log.d(TAG, "jasper post exception" + e);
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
						if("No Network Connection".equals(title)){
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
		new makePostRequest().execute(post_ID_URL);
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

	private void startProgressBar(){

		Log.d(TAG, "jasper start progress bar");
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void stopProgressBar(){

		Log.d(TAG, "jasper stop progress bar");
		mProgressBar.setVisibility(View.GONE);
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
