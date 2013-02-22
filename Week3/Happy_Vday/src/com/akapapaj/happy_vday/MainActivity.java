package com.akapapaj.happy_vday;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author     Joseph Malone akaPapaJ
 * @course     Mobile Dev Frameworks III
 * @project    Project 2, Week 2 
 * @instructor Josh Donlan
 *
 * @email      jmalone@fullsail.edu
 *
 */
public class MainActivity extends Activity {
	
	private TextView batterLevel;
	
    @Override
    /**
     *  Called when the activity is first created.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batterLevel = (TextView) this.findViewById(R.id.batteryLevel);
        batteryLevel();
    }

    /**
	 * Computes the battery level by registering a receiver to the intent trigger
	 * by a battery status/level change.
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

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
