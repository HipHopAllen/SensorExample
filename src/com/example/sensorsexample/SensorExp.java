package com.example.sensorsexample;

import java.text.DecimalFormat;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.os.Build;
import android.provider.Settings.System;

public class SensorExp extends ActionBarActivity implements SensorEventListener {
	Handler handler;
	int samples = 0;
	int PERIOD = 1000; // read sensor data each 1000 ms
	boolean flag = false;

	private SensorManager sm;
	private Sensor accMtr;
	private Sensor magMtr;

	private EditText accMtrX, accMtrY, accMtrZ;
	private EditText magMtrX, magMtrY, magMtrZ;
	private EditText locAzimuth, locPitch, locRoll;
	private EditText locAzimuthD, locPitchD, locRollD;

	float[] mAcc; // data for accelerometer
	float[] mMag; // data for magnetometer
	float[] mR = new float[16];
	float[] mI = new float[16];
	float[] mLoc = new float[3];

	private final Runnable processSensors = new Runnable() {
		@Override
		public void run() {
			// Do work with the sensor values.
			flag = true;
			// The Runnable is posted to run again here:
			handler.postDelayed(this, PERIOD);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button stop = (Button) findViewById(R.id.button2);

		// gui components
		accMtrX = (EditText) findViewById(R.id.accX);
		accMtrY = (EditText) findViewById(R.id.accY);
		accMtrZ = (EditText) findViewById(R.id.accZ);
		magMtrX = (EditText) findViewById(R.id.magX);
		magMtrY = (EditText) findViewById(R.id.magY);
		magMtrZ = (EditText) findViewById(R.id.magZ);
		locAzimuth = (EditText) findViewById(R.id.locAzimuth);
		locPitch = (EditText) findViewById(R.id.locPitch);
		locRoll = (EditText) findViewById(R.id.locRoll);
		locAzimuthD = (EditText) findViewById(R.id.locAzimuthD);
		locPitchD = (EditText) findViewById(R.id.locPitchD);
		locRollD = (EditText) findViewById(R.id.locRollD);

		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);		
		handler = new Handler();
		// sensors
		accMtr = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magMtr = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);		
		stop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onStop();
				finish();

			}
		});

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (flag) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				mAcc = event.values.clone();

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
				mMag = event.values.clone();

			if (mAcc != null && mMag != null) {
				boolean success = SensorManager.getRotationMatrix(mR, mI, mAcc,
						mMag);
				if (success) {
					samples++;
					if (samples <= 10) {
						return;
					}
					samples = 1;

					SensorManager.getOrientation(mR, mLoc);

					// set text in view
					DecimalFormat df = new DecimalFormat("###.####");
					accMtrX.setText(df.format(mAcc[0]));//acceleration in x,y and z axis
					accMtrY.setText(df.format(mAcc[1]));
					accMtrZ.setText(df.format(mAcc[2]));
					magMtrX.setText(df.format(mMag[0]));//MAGNETIC_FIELD- x,y and z axis
					magMtrY.setText(df.format(mMag[1]));
					magMtrZ.setText(df.format(mMag[2]));
					locAzimuth.setText(df.format(mLoc[0]));
					locPitch.setText(df.format(mLoc[1]));
					locRoll.setText(df.format(mLoc[2]));
					locAzimuthD.setText(df.format(Math.toDegrees(mLoc[0])));
					locPitchD.setText(df.format(Math.toDegrees(mLoc[1])));
					locRollD.setText(df.format(Math.toDegrees(mLoc[2])));
					// SystemClock.sleep(500);
					flag = false;
				}
			}

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		super.onStart();
		// TODO: make delay configurable
		sm.registerListener(this, accMtr, SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, magMtr, SensorManager.SENSOR_DELAY_NORMAL);
		handler.post(processSensors);
	}

	@Override
	public void onStop() {
		super.onStop();
		sm.unregisterListener(this);
	}

}
