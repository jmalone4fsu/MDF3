/**
 * 
 */
package com.akapapaj.happy_vday;


import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author     Joseph Malone akaPapaJ
 * @course     Mobile Dev Frameworks III
 * @project    Project 2-Splash, Week 2 
 * @instructor Josh Donlan
 *
 * @email      jmalone@fullsail.edu
 *
 */
public class SplashActivity extends Activity {
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		CountDown _tik;
		_tik = new CountDown(6000, 1000, this, My_Player.class);
		_tik.start();
		StartAnimation();
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
	}

	private void StartAnimation() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.reset();
		LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
		l.clearAnimation();
		l.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim.reset();
		ImageView iv = (ImageView) findViewById(R.id.logo);
		iv.clearAnimation();
		iv.startAnimation(anim);
	}

}