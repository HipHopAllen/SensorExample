package com.example.sensorsexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		Button start = (Button) findViewById(R.id.button3);
		Button Close = (Button) findViewById(R.id.button4);
		start.setOnClickListener(new OnClickListener() {
			

			public void onClick(View v) {
			
			//sendIntent = new Intent();
				Intent sendIntent = new Intent(v.getContext(),SensorExp.class);				
				startActivity(sendIntent);
				
			}
		});
     Close.setOnClickListener(new OnClickListener() {
			

			public void onClick(View v) {
			
			finish();
				
			}
		});
	}

}
