package com.liuym.worker;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;

public class OrderFinishActivity extends MainActivity{
	private Navigation navi = null;
	private View asset_input_view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_orderfinish);

		asset_input_view = (View)findViewById(R.id.hardware_replase_linearlayout);
		asset_input_view.setVisibility(View.INVISIBLE);
		
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});

		navi.getBtn_right().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop(WorkerActivity.class);
			}
		});

		findViewById(R.id.software_fault_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.INVISIBLE);
			}
		});
		
		findViewById(R.id.hardware_fault_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.INVISIBLE);
			}
		});
		
		findViewById(R.id.hardware_replace_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.VISIBLE);
			}
		});
	}
}