/**
 * 
 */
package com.akapapaj.happy_vday;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;

/**
 * @author     Joseph Malone akaPapaJ
 * @course     Mobile Dev Frameworks III
 * @project    Project #, Week # 
 * @instructor Josh Donlan
 *
 * @email      jmalone@fullsail.edu
 *
 */
public class CountDown extends CountDownTimer {
	private final Activity _act;
	private final Class<?> _cls;

	public CountDown(long millisInFuture, long countDownInterval, Activity act,
			Class<?> cls) {
		super(millisInFuture, countDownInterval);
		_act = act;
		_cls = cls;
	}

	@Override
	public void onFinish() {
		_act.startActivity(new Intent(_act, _cls));
		_act.finish();

	}

	@Override
	public void onTick(long millisUntilFinished) {

	}

}