package com.example.ahs;

import com.couchbase.lite.util.Log;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;

public class GameBoard extends Activity implements OnClickListener{
	
	Button btnPlayer1, btnPlayer2, btnPlayer3, btnPlayer4, btnPlayer5, btnPlayer6, btnPlayer7;
	Button btnSubstitution1, btnSubstitution2, btnSubstitution3, btnSubstitution4, btnSubstitution5, btnSubstitution6, btnSubstitution7;
	Button btnStartChrono, btnPauseChrono;
	Chronometer chrono;
	long time = 0;
	long time_on_click = 0;
	int action_counter = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameboard);	
		
		btnStartChrono = (Button) findViewById(R.id.start);
		btnPauseChrono = (Button) findViewById(R.id.pause);
		chrono = (Chronometer) findViewById(R.id.chronometer1);
		btnStartChrono.setOnClickListener(this);
		btnPauseChrono.setOnClickListener(this);
		
		btnPlayer1 = (Button) findViewById(R.id.player1);
		btnPlayer1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(getApplicationContext(), PlayerOptions.class);
				intent.putExtra("PLAYER_POSITION", btnPlayer1.getText());
				startActivity(intent);		
			}
		});
		
		btnPlayer2 = (Button) findViewById(R.id.player2);
		btnPlayer2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(getApplicationContext(), PlayerOptions.class);
				intent.putExtra("PLAYER_POSITION", btnPlayer2.getText());
				startActivity(intent);		
			}
		});
		
		btnPlayer3 = (Button) findViewById(R.id.player3);
		btnPlayer3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(getApplicationContext(), PlayerOptions.class);
				intent.putExtra("PLAYER_POSITION", btnPlayer3.getText());
				time_on_click = SystemClock.elapsedRealtime() - chrono.getBase();
				intent.putExtra("Chronometer", time_on_click);
				intent.putExtra("actionID", action_counter);
				action_counter++;
				startActivity(intent);		
			}
		});
		
		btnPlayer4 = (Button) findViewById(R.id.player4);
		btnPlayer4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(getApplicationContext(), PlayerOptions.class);
				intent.putExtra("PLAYER_POSITION", btnPlayer4.getText());
				startActivity(intent);		
			}
		});
		
		btnPlayer5 = (Button) findViewById(R.id.player5);
		btnPlayer5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(getApplicationContext(), PlayerOptions.class);
				intent.putExtra("PLAYER_POSITION", btnPlayer5.getText());
				startActivity(intent);		
			}
		});
		
		btnPlayer6 = (Button) findViewById(R.id.player6);
		
		btnPlayer6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(getApplicationContext(), PlayerOptions.class);
				intent.putExtra("PLAYER_POSITION", btnPlayer6.getText());
				startActivity(intent);		
			}
		});
		
		btnPlayer7 = (Button) findViewById(R.id.player7);
		btnPlayer7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent=new Intent(getApplicationContext(), PlayerOptions.class);
				intent.putExtra("PLAYER_POSITION", btnPlayer7.getText());
				startActivity(intent);		
			}
		});
		
		//buttons to drag
		btnSubstitution1 = (Button) findViewById(R.id.substitution1);
		btnSubstitution2 = (Button) findViewById(R.id.substitution2);
		btnSubstitution3 = (Button) findViewById(R.id.substitution3);
		btnSubstitution4 = (Button) findViewById(R.id.substitution4);
		btnSubstitution5 = (Button) findViewById(R.id.substitution5);
		btnSubstitution6 = (Button) findViewById(R.id.substitution6);
		btnSubstitution7 = (Button) findViewById(R.id.substitution7);
		
		//buttons to drop onto - btnPlayer1 till btnPlayer7
		
		
		//set touch listeners
		btnSubstitution1.setOnTouchListener(new ChoiceTouchListener());
		btnSubstitution2.setOnTouchListener(new ChoiceTouchListener());
		btnSubstitution3.setOnTouchListener(new ChoiceTouchListener());
		btnSubstitution4.setOnTouchListener(new ChoiceTouchListener());
		btnSubstitution5.setOnTouchListener(new ChoiceTouchListener());
		btnSubstitution6.setOnTouchListener(new ChoiceTouchListener());
		btnSubstitution7.setOnTouchListener(new ChoiceTouchListener());	
		
		//btnPlayer1.setOnTouchListener(new ChoiceTouchListener());	
		
		//set drag listeners
		btnPlayer1.setOnDragListener(new ChoiceDragListener());
		btnPlayer2.setOnDragListener(new ChoiceDragListener());
		btnPlayer3.setOnDragListener(new ChoiceDragListener());
		btnPlayer4.setOnDragListener(new ChoiceDragListener());
		btnPlayer5.setOnDragListener(new ChoiceDragListener());
		btnPlayer6.setOnDragListener(new ChoiceDragListener());
		btnPlayer7.setOnDragListener(new ChoiceDragListener());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View arg0) {
		//TODO Auto-generated method stub
		switch (arg0.getId()){	
		case R.id.start:	//if the button is start
			chrono.setBase(SystemClock.elapsedRealtime() + time);
			chrono.start();
			break;
			
		case R.id.pause:	//if the button is pause
			time = chrono.getBase() - SystemClock.elapsedRealtime();
			chrono.stop();
			break;		
		}
	}
	private final class ChoiceTouchListener implements OnTouchListener {
		 
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			    //setup drag
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				
				//start dragging the item touched
				view.startDrag(data, shadowBuilder, view, 0);
			    return true;
			}
			else {
			    return false;
			}
		}	
	}
	private class ChoiceDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
		    //handle drag events
			switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DRAG_ENTERED:
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DRAG_EXITED:       
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DROP:
		        
		    	//handle the dragged view being dropped over a drop view
		    	View view = (View) event.getLocalState();
		    	
		    	//stop displaying the view where it was before it was dragged
		    	//view.setVisibility(View.INVISIBLE); //kad stavim sub1 na RW, sub1 nestane
		    	
		    	
		    	
		    	//view dragged item is being dropped on
		    	Button dropTarget = (Button) v; //ovo je RW
		    	
		    	//view being dragged and dropped
		    	Button dropped = (Button) view; //ovo je sub1
		    	
		    	String tmp = (String) dropTarget.getText();
		    	
		    	//update the text in the target view to reflect the data being dropped
		    	dropTarget.setText(dropped.getText());
		    	
		    	dropped.setText(tmp);	
		    	
		    	//if an item has already been dropped here, there will be a tag
		    	Object tag = dropTarget.getTag(); 
		    	
		    	
		    	//if there is already an item here, set it back visible in its original place
		    	if(tag!=null){
		    	    //the tag is the view id already dropped here
		    	    int existingID = (Integer)tag;
		    	    //set the original view visible again
		    	    findViewById(existingID).setVisibility(View.VISIBLE);
		    	}
		    	
		    	//set the tag in the target view to the ID of the view being dropped
		    	dropTarget.setTag(dropped.getId());
		    	
		        break;
		    case DragEvent.ACTION_DRAG_ENDED:
		        //no action necessary
		        break;
		    default:
		        break;
			}
		    return true;
		}
	}
}
