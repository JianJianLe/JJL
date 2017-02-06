package com.xy.jjl.activity;

import com.xy.jjl.R;
import com.xy.jjl.application.ApplicationInfo;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DisclaimerActivity extends Activity {
	private ImageButton backIB;
	private TextView contentTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disclaimer);
		ApplicationInfo.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		backIB = (ImageButton) findViewById(R.id.disclaimerBackImageButton);
		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		contentTV = (TextView) findViewById(R.id.disclaimContent);
		contentTV.setMovementMethod(new ScrollingMovementMethod());
	}
}
