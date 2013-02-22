/**
 * 
 */
package com.akapapaj.happy_vday;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * @author Joseph Malone akaPapaJ
 * @course Mobile Dev Frameworks III
 * @project Project 3, Week 3
 * @instructor Josh Donlan
 * 
 * @email jmalone@fullsail.edu
 * 
 */
public class My_Player extends Activity implements SensorEventListener {

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
	/**
	 * Variables for Battery Level
	 */
	TextView batterLevel;
	boolean good2Go;
	static Context _this;

	/**
	 * Variables used for Shake Detection
	 */
	private float mLastX, mLastY, mLastZ;
	private boolean mInit;
	private SensorManager sm;
	private Sensor sensor;
	private final float NOISE = (float) 4.0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_player);

		/**
		 * Force the app to show overflow menu. WARNING: This is very much
		 * frowned upon in real world applications. But, the directions say to
		 * make at least one action show in the OVERFLOW menu ... this is the
		 * best work around I could find.
		 */
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}

		/**
		 * Get the default ActionBar
		 */
		ActionBar actionBar = getActionBar();
		actionBar.show();
		// set app icon as home button
		// actionBar.setDisplayHomeAsUpEnabled(true);

		/**
		 * Setup imageView for use with Shake Sensor
		 */
		ImageView iv = (ImageView) findViewById(R.id.image);
		iv.setBackgroundColor(Color.WHITE);
		// iv.setImageResource(R.drawable.eyesfront);

		/**
		 * Initialize sensor variables and setup listener
		 */
		mInit = false;
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

		/**
		 * Setup textView for battery level
		 */
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
				if (j_buttText.equals("Play Me")) {
					myPlayer.start();
					j_buttText = "Pause";
					j_button1.setText(j_buttText);
				} else {
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
				/**
				 * Check to see if battery level is sufficient to play the video
				 * file (it is, considering this is just a small video file ...
				 * but in the case that it should be a larger file this would
				 * prevent playback if the battery was too low.
				 */
				if (good2Go = true) {
					// If enough batter, play video //
					j_vv = (VideoView) findViewById(R.id.videoView1);
					String uriPath = "android.resource://" + getPackageName()
							+ "/" + R.raw.papaj2;
					j_vv.setVideoURI(Uri.parse(uriPath));
					j_vv.setMediaController(new MediaController(_this));
					j_vv.requestFocus();
					j_vv.start();

				} else {
					// If battery low, show toast and do not run video //
					Toast.makeText(getApplicationContext(),
							"Battery Level Too Low \n" + "to Safely Run Video",
							Toast.LENGTH_LONG).show();
				}

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

				Intent intent = new Intent(
						"com.akapapaj.j_browser.LAUNCH_TEST", Uri.parse(url));

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
				int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,
						-1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

				/**
				 * Check battery level, if too low - don't allow video to play
				 * This is not real-world application of usage, just demo of how
				 * to check level and control app flow with results. Best use
				 * would be for activities such as accessing large data from
				 * internet, loading large files such as updates, or playing a
				 * full length streaming movie that will drain the battery
				 * quickly.
				 **/

				int level = -1;
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
					if (level < 20) {
						good2Go = false;
					} else {
						good2Go = true;
					}
				}
				batterLevel.setText("Battery Level Remaining: " + level + "%");

			}

		};
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);

	}

	/**
	 * Methods for Shake Sensor
	 */
	@Override
	protected void onResume() {
		// on Resume, register Listener
		super.onResume();
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// on Pause, unregister Listener to save battery
		super.onPause();
		sm.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * What to do when phone detects shakes
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		ImageView iv = (ImageView) findViewById(R.id.image);

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		if (!mInit) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInit = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE)
				deltaX = (float) 0.0;
			if (deltaY < NOISE)
				deltaY = (float) 0.0;
			if (deltaZ < NOISE)
				deltaZ = (float) 0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;

			iv.setVisibility(View.VISIBLE);
			if (deltaX > deltaY) {
				// if shake is left and right
				iv.setImageResource(R.drawable.eyesleft);
			} else if (deltaY > deltaX) {
				// if shake is up and down
				iv.setImageResource(R.drawable.eyesright);
			} else {
				// if no shake, eyes forward
				iv.setImageResource(R.drawable.eyesfront);
				// iv.setVisibility(View.INVISIBLE);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// use inflater to populate ActionBar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_refresh:
			makeToast("Refreshing...");
			break;
		case R.id.item_save:
			makeToast("Saving...");
			break;
		case R.id.play_music:
			makeToast("Playing Song...");
			myPlayer.start();
			break;
		case R.id.show_video:
			if (good2Go = true) {
				// If enough batter, play video //
				j_vv = (VideoView) findViewById(R.id.videoView1);
				String uriPath = "android.resource://" + getPackageName() + "/"
						+ R.raw.papaj2;
				j_vv.setVideoURI(Uri.parse(uriPath));
				j_vv.setMediaController(new MediaController(_this));
				j_vv.requestFocus();
				j_vv.start();
				makeToast("Playing...");
				break;
			}
		case R.id.about:
			makeToast("ME: Some people call me the space cowboy,\n"
					+ "yeah Some call me the gangster of love\n"
					+ "Some people call me Maurice\n"
					+ "Cause I speak of the pompitous of love");
			break;

		}
		return true;
	}

	public void makeToast(String message) {
		Toast.makeText(_this, message, Toast.LENGTH_SHORT).show();

	}

}
