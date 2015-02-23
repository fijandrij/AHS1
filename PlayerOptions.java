package com.example.ahs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import com.example.ahs.Other;
import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
	
public class PlayerOptions extends Activity implements OnClickListener{
	
	Button btnTechMistake;
	Button btnGoal;
	Button btnOK;
	Button btnFoul;
	Button btnOther;
	Button btnShot;
	Button btnWing;
	Button btn6m;
	Button btn9m;
	Button btnOutsideTheBox;
	Button btnPenalty;
	Button btnHeptameter;
	
	ImageView goal;
	ImageView ball_left_top;
	ImageView ball_left_middle;
	ImageView ball_left_bottom;
	ImageView ball_middle_top;
	ImageView ball_middle_middle;
	ImageView ball_middle_bottom;
	ImageView ball_right_top;
	ImageView ball_right_middle;
	ImageView ball_right_bottom;
	ImageView ball_left_goalpost;
	ImageView ball_top_goalpost;
	ImageView ball_right_goalpost;
	ImageView yellow_card;
	ImageView red_card;
	
	CheckBox cbxHeptameter;
	
	private static final String TAG = null;
	private Database database;
	private Manager manager;
	DatabaseHandler db;
	Map<String, Object> docContent;
	private String player_position;
	private String shot_position;
	private long time_on_click, minutes, seconds;
	private int actionId;
	private int numberOfMistake = 1, goalNumber = 1;
	private int numberOfFoul=1,playerOut=0;
	private String mistakeType;
	private String penalScore;
	private int numOfEnforcedPenal=1;
	private int numOfEnforcedYellow=1;
	private int numOfEnforcedExclusion=1;
	private int numOfEnforcedDisq=1;
	private int numOfEnforcedDisqWithoutSubs=1;
	private int numOfShots=1;
    private String penalShotPosition;
	private String penaltyType;
	private String shootingDistance = "";
	private String enforcedType;
	private View pressedButton;
	private boolean penaltyShot;
	
	TechnicalMistakes techMistakeClass;
	Foul foulMistakeClass;
	Goal goalClass;
	Shot shotClass;
	Penalty penaltyClass;
	
	//enforced (ostalo button)
	EnforcedPenalty enforcedPenalClass;
	EnforcedYellow enforcedYellowClass;
	EnforcedExclusion enforcedExclusionClass;
	EnforcedDisqualification enforcedDisqClass;
	EnforcedDisqWithoutSubs enforcedDisqWithoutSubsClass;
	
	//penalty (kazne button)
	PenaltyYellow penaltyYellowClass;
	PenaltyRed penaltyRedClass;
	PenaltyDisqualification penaltyDisqClass;
	PenaltyDisqWithoutSubs penaltyDisqWithoutSubsClass;
	
	
	int imageX, imageY;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playeroptions);
		
		btnTechMistake = (Button) findViewById(R.id.btnTechnMistake);
		btnTechMistake.setOnClickListener(this);
		btnFoul = (Button) findViewById(R.id.btnFoul);
		btnFoul.setOnClickListener(this);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnOK.setOnClickListener(this);
		btnGoal = (Button) findViewById(R.id.btnGoal);
		btnGoal.setOnClickListener(this);
		btnShot = (Button) findViewById(R.id.btnShot);
		btnShot.setOnClickListener(this);
		btnWing = (Button) findViewById(R.id.btnWing);
		btnWing.setOnClickListener(this);
		btn6m = (Button) findViewById(R.id.btn6m);
		btn6m.setOnClickListener(this);
		btn9m = (Button) findViewById(R.id.btn9m);
		btn9m.setOnClickListener(this);
		btnOutsideTheBox = (Button) findViewById(R.id.btnOutsideTheBox);
		btnOutsideTheBox.setOnClickListener(this);
		btnOther = (Button) findViewById(R.id.btnOther);
		btnOther.setOnClickListener(this);
		btnPenalty = (Button) findViewById(R.id.btnSentence);
		btnPenalty.setOnClickListener(this);
		btnHeptameter = (Button) findViewById(R.id.btnHeptameter);
		btnHeptameter.setOnClickListener(this);
		cbxHeptameter = (CheckBox) findViewById(R.id.cbxHeptameter);
		
		goal = (ImageView) findViewById(R.id.imageView1);
		ball_left_top = (ImageView) findViewById(R.id.ball_left_top);
		ball_left_middle = (ImageView) findViewById(R.id.ball_left_middle);
		ball_left_bottom = (ImageView) findViewById(R.id.ball_left_bottom);
		ball_middle_top = (ImageView) findViewById(R.id.ball_middle_top);
		ball_middle_middle = (ImageView) findViewById(R.id.ball_middle_middle);
		ball_middle_bottom = (ImageView) findViewById(R.id.ball_middle_bottom);
		ball_right_top = (ImageView) findViewById(R.id.ball_right_top);
		ball_right_middle = (ImageView) findViewById(R.id.ball_right_middle);
		ball_right_bottom = (ImageView) findViewById(R.id.ball_right_bottom);
		ball_left_goalpost = (ImageView) findViewById(R.id.ball_left_goalpost);
		ball_top_goalpost = (ImageView) findViewById(R.id.ball_top_goalpost);
		ball_right_goalpost = (ImageView) findViewById(R.id.ball_right_goalpost);
		
		yellow_card=(ImageView) findViewById(R.id.imageView2);
		red_card=(ImageView) findViewById(R.id.imageView3);
		
		Intent intent = getIntent();
		//player position
		player_position = intent.getStringExtra("PLAYER_POSITION");
		//time
		time_on_click = intent.getLongExtra("Chronometer", 1);
		seconds = (time_on_click/1000) % 60;
		minutes = time_on_click/1000/60;
		//action id
		actionId = intent.getIntExtra("actionID", 1);
		
		//stvaranje objekta za rad s bazom
		db = new DatabaseHandler(this);
		
		goal.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					int[] viewCoords = new int[2];
					goal.getLocationOnScreen(viewCoords);
					int touchX = (int) event.getX();
					int touchY = (int) event.getY();
					
					resetGoal();
					shot_position = "";


					
					if(touchY <= 45){
						shot_position = "Top goalpost";
						ball_top_goalpost.setVisibility(1);
					}
					else if(touchX <= 14){
						shot_position = "Left goalpost";
						ball_left_goalpost.setVisibility(1);
					}
					else if(touchX >= 365){
						shot_position = "Right goalpost";
						ball_right_goalpost.setVisibility(1);
					}
					else if(touchX > 15 && touchX < 120 && touchY > 45 && touchY < 105){
						shot_position = "Left top";
						ball_left_top.setVisibility(1);
					}else if(touchX > 120 && touchX < 275 && touchY > 45 && touchY < 105){
						shot_position = "Middle top";
						ball_middle_top.setVisibility(1);
					}else if(touchX > 275 && touchX < 365 && touchY > 45 && touchY < 105){
						shot_position = "Right top";
						ball_right_top.setVisibility(1);
					}else if(touchX > 15 && touchX < 120 && touchY > 105 && touchY < 160){
						shot_position = "Left middle";
						ball_left_middle.setVisibility(1);
					}else if(touchX > 120 && touchX < 275 && touchY > 105 && touchY < 160){
						shot_position = "Middle middle";
						ball_middle_middle.setVisibility(1);
					}else if(touchX > 275 && touchX < 365 && touchY > 105 && touchY < 160){
						shot_position = "Right middle";
						ball_right_middle.setVisibility(1);
					}else if(touchX > 15 && touchX < 120 && touchY > 160 && touchY < 230){
						shot_position = "Left bottom";
						ball_left_bottom.setVisibility(1);
					}else if(touchX > 120 && touchX < 275 && touchY > 160 && touchY < 230){
						shot_position = "Middle bottom";
						ball_middle_bottom.setVisibility(1);
					}else if(touchX > 275 && touchX < 365 && touchY > 160 && touchY < 230){
						shot_position = "Right bottom";
						ball_right_bottom.setVisibility(1);
					}
				}
				return true;
			}
		});
				
		//this.setFinishOnTouchOutside(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	        	enforcedType = data.getStringExtra("enforcedID");
	            resetPlayerOptionsActivityButtons();
            	btnOther.setEnabled(false); 
	        }
	        
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	    else if (requestCode == 2){
	        if(resultCode == RESULT_OK){
		        penaltyType = data.getStringExtra("penaltyID");
		        resetPlayerOptionsActivityButtons();
	           	btnPenalty.setEnabled(false); 
		       }
	        	
	        if (resultCode == RESULT_CANCELED) {
		        //Write your code if there's no result
		    }
	    }
	     else if(requestCode == 3){
	    	if(resultCode == RESULT_OK){
	            mistakeType = data.getStringExtra("mistakeID");
	            resetPlayerOptionsActivityButtons();
	           	btnTechMistake.setEnabled(false);
	    	}
	    	
	    	if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}
	
	@Override
	public void onClick(View arg0) {
		Intent intent;
		switch (arg0.getId()){
		
			case R.id.btnOther:	//if the button is OSTALO	
				pressedButton = arg0;
				
				intent = new Intent(getApplicationContext(), Other.class);
				startActivityForResult(intent, 1);
				break;
			
			case R.id.btnSentence:	//if the button is KAZNA	
				pressedButton = arg0;
				
				intent = new Intent(getApplicationContext(), PenaltyActivity.class);
				startActivityForResult(intent, 2);
				break;
				
			case R.id.btnTechnMistake:	//if the button is TEHP	
				pressedButton = arg0;
				
				intent = new Intent(getApplicationContext(), TechMistakeActivity.class);
				startActivityForResult(intent, 3);
				break;
			
            case R.id.btnFoul:	//if the button is FAUL
            	pressedButton = arg0;
				
				resetPlayerOptionsActivityButtons();
				btnFoul.setEnabled(false);								
				break;

			case R.id.btnGoal:
				pressedButton = arg0;

				resetPlayerOptionsActivityButtons();
				btnGoal.setEnabled(false);
				cbxHeptameter.setVisibility(1);
				break;
				
			case R.id.btnShot: //if the button is shot
				pressedButton = arg0;
				
				resetPlayerOptionsActivityButtons();
				btnShot.setEnabled(false);
				
				btnWing.setVisibility(1);
				btn6m.setVisibility(1);
				btn9m.setVisibility(1);
				btnHeptameter.setVisibility(1);
				btnOutsideTheBox.setVisibility(1);
				break;

			case R.id.btnWing:
				resetShootingDistanceButtons();
				btnWing.setEnabled(false);
				shootingDistance = "Wing";
				break;
				
			case R.id.btn9m:
				resetShootingDistanceButtons();
				btn9m.setEnabled(false);
				shootingDistance = "9 meters";
				break;
				
			case R.id.btn6m:
				resetShootingDistanceButtons();
				btn6m.setEnabled(false);
				shootingDistance = "6 meters";
				break;
				
			case R.id.btnOutsideTheBox:
				resetGoal();
				btnOutsideTheBox.setEnabled(false);
				shot_position = "Outside the box";
				break;
				
			case R.id.btnHeptameter:
				resetShootingDistanceButtons();
				btnHeptameter.setEnabled(false);
				shootingDistance = "Penalty";
				break;
			
			case R.id.btnOK: //if the button is OK, add data into database
				docContent = new HashMap<String, Object>();
				docContent.put("gameId", 1);
				docContent.put("actionId", actionId);
				docContent.put("time", minutes + ":" + seconds);
				docContent.put("playerId", player_position);
				
				switch(pressedButton.getId()){
				
					case R.id.btnGoal:
						
						if (cbxHeptameter.isChecked()){
							penaltyShot = true;
						}
						else{
							penaltyShot = false;
						}
						goalClass = new Goal(shot_position, goalNumber, penaltyShot);
						goalNumber++;
						docContent.put("description", goalClass);	
						break;
						
					case R.id.btnShot:
						
						shotClass = new Shot(shot_position, shootingDistance, numOfShots);
						numOfShots++;
						docContent.put("description", shotClass);
						break;
						
					case R.id.btnFoul:
						foulMistakeClass = new Foul("Fast Break", numberOfFoul);
						numberOfFoul++;
						docContent.put("description", foulMistakeClass);
						break;
						
					case R.id.btnTechnMistake:
						techMistakeClass = new TechnicalMistakes("Fast Break", numberOfMistake, mistakeType);
						numberOfMistake++;
						docContent.put("description", techMistakeClass);
						break;
						
					case R.id.btnPenalty:
			            if(penaltyType.equals("PenaltyYellowCard")){
			            	
			            	penaltyYellowClass = new PenaltyYellow(1);
							docContent.put("description", enforcedYellowClass);
			            }
			            
			            else if(penaltyType.equals("PenaltyRedCard")){
		            	
		            	penaltyRedClass = new PenaltyRed(1);
						docContent.put("description", penaltyRedClass);
			            }
			            
						else if(penaltyType.equals("PenaltyDisqualification")){
		            	
		            	penaltyDisqClass = new PenaltyDisqualification(1);
						docContent.put("description", penaltyDisqClass);
		            	}
			            
						else if(penaltyType.equals("PenaltyDisqualificationWithoutSubstitution")){
		            	
		            	penaltyDisqWithoutSubsClass = new PenaltyDisqWithoutSubs(1);
						docContent.put("description", penaltyDisqWithoutSubsClass);
		            	}
			            break;
						
					case R.id.btnOther:
						if (enforcedType.equals("EnforcedPenalty")){
							
			            	enforcedPenalClass = new EnforcedPenalty(numOfEnforcedPenal);
			            	numOfEnforcedPenal++;
							docContent.put("description", enforcedPenalClass);
			            }
			            
			            else if(enforcedType.equals("EnforcedYellowCard")){
			            	
			            	enforcedYellowClass = new EnforcedYellow(numOfEnforcedYellow);
			            	numOfEnforcedYellow++;
							docContent.put("description", enforcedYellowClass);
			            }
			            
			            else if(enforcedType.equals("EnforcedDisqualification")){
			            	
			            	enforcedDisqClass = new EnforcedDisqualification(numOfEnforcedDisq);
			            	numOfEnforcedDisq++;
							docContent.put("description", enforcedDisqClass);
			            }
			            
			            else if(enforcedType.equals("EnforcedExclusion")){
			            	
			            	enforcedExclusionClass = new EnforcedExclusion(numOfEnforcedExclusion);
			            	numOfEnforcedExclusion++;
							docContent.put("description", enforcedExclusionClass);
			            }
			            
			            else if(enforcedType.equals("EnforcedDisqualificationWithoutSubstitution")){
			            	
			            	enforcedDisqWithoutSubsClass = new EnforcedDisqWithoutSubs(numOfEnforcedDisqWithoutSubs);
			            	numOfEnforcedDisqWithoutSubs++;
							docContent.put("description", enforcedDisqWithoutSubsClass);
			            }
						break;
				}
				
				String myid = db.insertData(docContent);
				
				finish();
				break;
		}

		}
	
	private void resetGoal(){
		ball_left_top.setVisibility(View.GONE);
		ball_middle_top.setVisibility(View.GONE);
		ball_right_top.setVisibility(View.GONE);
		ball_left_middle.setVisibility(View.GONE);
		ball_middle_middle.setVisibility(View.GONE);
		ball_right_middle.setVisibility(View.GONE);
		ball_left_bottom.setVisibility(View.GONE);
		ball_middle_bottom.setVisibility(View.GONE);
		ball_right_bottom.setVisibility(View.GONE);
		ball_left_goalpost.setVisibility(View.GONE);
		ball_top_goalpost.setVisibility(View.GONE);
		ball_right_goalpost.setVisibility(View.GONE);
		btnOutsideTheBox.setEnabled(true);
	}

	private void resetPlayerOptionsActivityButtons(){
		btnGoal.setEnabled(true);
		btnShot.setEnabled(true);
		btnFoul.setEnabled(true);
		btnTechMistake.setEnabled(true);
		btnOther.setEnabled(true);
		btnPenalty.setEnabled(true);
		btn9m.setEnabled(true);
		btn6m.setEnabled(true);
		btnWing.setEnabled(true);
		btnOutsideTheBox.setEnabled(true);
		btnHeptameter.setEnabled(true);
		cbxHeptameter.setChecked(false);
		
		btn9m.setVisibility(View.GONE);
		btn6m.setVisibility(View.GONE);
		btnWing.setVisibility(View.GONE);
		btnOutsideTheBox.setVisibility(View.GONE);
		btnHeptameter.setVisibility(View.GONE);
		cbxHeptameter.setVisibility(View.GONE);
	}

	private void resetShootingDistanceButtons(){
		btnWing.setEnabled(true);
		btn9m.setEnabled(true);
		btn6m.setEnabled(true);
		btnHeptameter.setEnabled(true);
	}
}
