/**
 * 
 */
package com.akapapaj.happy_vday;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * @author     Joseph Malone akaPapaJ
 * @course     Mobile Dev Frameworks III
 * @project    Project #, Week # 
 * @instructor Josh Donlan
 *
 * @email      jmalone@fullsail.edu
 *
 */
public class My_Player extends Activity {

	/**
	 * Global Variables
	 */
	String j_buttText;
	String url;
	MediaPlayer myPlayer;
	Button j_button1;
	Button j_button2;
	Button j_button3;
	VideoView j_vv;
	TextView batterLevel;
	Context _this;
	/**
	 * Variables used for Shake Detection
	 */

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_player);

		
		
		batterLevel = (TextView) this.findViewById(R.id.textView1);
        batteryLevel();
		_this = this;
		/**
		 * Set MediaPlayer sound
		 */
		myPlayer = MediaPlayer.create(My_Player.this, R.raw.akapapaj1);
		
		/**
		 * Set Button 1
		 */
		j_button1 = (Button) findViewById(R.id.button1);
		j_buttText = "Play Me";
		
		/**
		 * Button 1 Listener
		 */
		j_button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				/**
				 * Start and Stop Audio
				 */
				if(j_buttText.equals("Play Me")) {
					myPlayer.start();
					j_buttText = "Pause";
					j_button1.setText(j_buttText);
				}else {
					myPlayer.pause();
					j_buttText = "Play Me";
					j_button1.setText(j_buttText);
				}
				
			}
			
			
		});
		/**
		 * Set default button text
		 */
		j_button1.setText(j_buttText);
		
		/**
		 * Set Button 2
		 */
		j_button2 = (Button) findViewById(R.id.button2);
		j_button2.setText("Play Video");
		j_button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				j_vv = (VideoView) findViewById(R.id.videoView1);
				String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.papaj2;
				j_vv.setVideoURI(Uri.parse(uriPath));
				j_vv.setMediaController(new MediaController(_this));
				j_vv.requestFocus();
				j_vv.start();
			}
			
		});
		/**
		 * Set Button 3
		 */
		j_button3 = (Button) findViewById(R.id.button3);
		j_button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// what to do when clicked //
				url = "http://www.hillbillyhotdogs.com";

				Intent intent = new Intent("com.akapapaj.j_browser.LAUNCH_TEST", Uri.parse(url));
				
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
			
		});
		}

	/**
	 * 
	 */
	private void batteryLevel() {
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				int level = -1;
				if (rawlevel >=0 && scale > 0) {
					level = (rawlevel * 100) / scale;
					if (level < 50) {
						Toast.makeText(getApplicationContext(), "Battery Level " + level + "%. Too Low to Run Video", Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(getApplicationContext(), "Battery Level " + level +"%, Running Video", Toast.LENGTH_LONG).show();
					}
				}
				batterLevel.setText("Battery Level Remaining: " + level + "%");
				
			}
			
		};
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
		
	}

}
