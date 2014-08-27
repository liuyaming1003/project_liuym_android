package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyViewPagerAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.R;

@SuppressLint("InflateParams") public class WorkerActivity extends MainActivity{
	private LayoutInflater inflater = null;
	private View firstView = null;
	private View secondView = null;
	private boolean secondViewIsFirst = true;
	//tableview
	private ViewPager viewPager = null;
	private View order_view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker);
		
		inflater = LayoutInflater.from(this); 
		firstView = inflater.inflate(R.layout.activity_worker, null);
		setContentView(firstView);
		secondView = inflater.inflate(R.layout.activity_asset_manager, null);
		
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				popRoot();
			}
		});
		
		findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setSecondView();
			}
		});

		findViewById(R.id.first).setEnabled(false);
		
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		initReapirListView();
		final ArrayList<View> views = new ArrayList<View>();
		views.add(order_view);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
	}
	
	private void initReapirListView(){
		order_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)order_view.findViewById(R.id.myListView);
		final MyListViewAdapter infoAdapter = new MyListViewAdapter(this, getOrderList(),  
				new ListViewInterface(){	
			@Override
			public void setCell(MyListViewAdapter adapter, View CellView, final int position) {
				TextView order_name = (TextView)CellView.findViewById(R.id.order_name);
				TextView order_info = (TextView)CellView.findViewById(R.id.order_info);
				Button order_button = (Button)CellView.findViewById(R.id.order_button);
				order_button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						push(OrderDetailActivity.class);				
					}
				});
			}

			@Override
			public View getCell(MyListViewAdapter adapter, final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.order_cell, null);
				TextView order_name = (TextView)CellView.findViewById(R.id.order_name);
				TextView order_info = (TextView)CellView.findViewById(R.id.order_info);
				Button order_button = (Button)CellView.findViewById(R.id.order_button);
				order_button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						push(OrderDetailActivity.class);				
					}
				});
				return CellView;
			}
		});
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
	      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	          long arg3){
	      }  
	    });
	}
	
	private List<Map<String, Object>> getOrderList(){  
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
		for (int i = 0; i < 30; i++) {  
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("order_name", "李丽霞");
			map.put("order_info", "2014.06.21 15:24:00\n二年级办公室"); 
			map.put("order_button", "处理该单"); 
			list.add(map);  
		}  
		return list;  
	}
	
	private void setFirstView(){
		setContentView(firstView);
	}
	
	private void setSecondView(){
		setContentView(secondView);
		if(secondViewIsFirst){
			secondViewIsFirst = false;
			findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					popRoot();
				}
			});

			findViewById(R.id.second).setEnabled(false);

			findViewById(R.id.first).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					setFirstView();
				}
			});
			
			findViewById(R.id.asset_input_btn).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					push(AssetInputActivity.class);
				}
			});
			
			findViewById(R.id.asset_query_btn).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					push(AssetQueryActivity.class);
				}
			});
		}
	}
}