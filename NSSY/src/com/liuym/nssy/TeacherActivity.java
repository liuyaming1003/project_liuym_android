package com.liuym.nssy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuym.listviewAdapter.MyListViewAdapter;
import com.liuym.listviewAdapter.MyListViewAdapter.ListViewInterface;
import com.liuym.listviewAdapter.MyViewPagerAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TeacherActivity extends Activity{
	private LayoutInflater inflater = null;
	private Navigation navi = null;
	private View teackerView = null;
	private View ibeactonView = null;
	private boolean ibViewIsFirst = true;
	private ViewPager viewPager = null;
	private View order_view = null;
	private View history_view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_teacher);

		inflater = LayoutInflater.from(this); 
		teackerView = inflater.inflate(R.layout.activity_teacher, null);
		ibeactonView = inflater.inflate(R.layout.activity_ibeacon, null);
		setTeackerView();
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});

		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				popRoot();
			}
		});

		findViewById(R.id.first).setEnabled(false);

		findViewById(R.id.teacherBtn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				push(TeacherRepair.class);
			}
		});

		findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setIbeaconView();
			}
		});

		/*order_tableView = (TableView)findViewById(R.id.order_tableView);
		order_tableView.setCallback(new TableViewInterface() {			
			@Override
			public View setCell(TableView tableView, final int callIndex) {
				LayoutInflater inflater = LayoutInflater.from(tableView.getContext()); 
				View cellView = inflater.inflate(R.layout.order_cell, null);
				cellView.findViewById(R.id.cell_button).setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View arg0) {
						System.out.println("button is index " + callIndex);
					}
				});
				return cellView;
			}

			@Override
			public int numberOfCell() {
				System.out.println("return 10 cell");
				return 10;
			}
		});*/

		viewPager = (ViewPager)findViewById(R.id.viewPager);
		initOrderListView();
		initHistoryListView();
		final ArrayList<View> views = new ArrayList<View>();
		views.add(order_view);
		views.add(history_view);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
	}

	private void initOrderListView(){
		//init Order ListView
		order_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)order_view.findViewById(R.id.myListView);
		listView.setAdapter(new MyListViewAdapter(this, getOrderList(), 
				new ListViewInterface() {			
			@Override
			public void setCell(View view, int position) {
				//设置CellView 里面的数据
				System.out.println("setCell index = " + position);
				Button button = (Button)view.findViewById(R.id.cell_button);
				button.setText("按钮 " + position);
			}

			@Override
			public View getCell(final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.order_cell, null);
				Button button = (Button)CellView.findViewById(R.id.cell_button);
				button.setText("按钮 " + position);
				button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						System.out.println("button index = " + position);					
					}
				});
				return CellView;
			}
		}));
	}
	
	private void initHistoryListView(){
		//init Order ListView
		history_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)history_view.findViewById(R.id.myListView);
		listView.setAdapter(new MyListViewAdapter(this, getOrderList(), 
				new ListViewInterface() {			
			@Override
			public void setCell(View view, int position) {
				//设置CellView 里面的数据
				System.out.println("setCell index = " + position);
				Button button = (Button)view.findViewById(R.id.cell_button);
				button.setText("按钮 " + position);
			}

			@Override
			public View getCell(final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.order_cell, null);
				Button button = (Button)CellView.findViewById(R.id.cell_button);
				button.setText("按钮 " + position);
				button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						System.out.println("button index = " + position);					
					}
				});
				return CellView;
			}
		}));
	}

	private List<Map<String, Object>> getOrderList(){  
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
		for (int i = 0; i < 30; i++) {  
			Map<String, Object> map=new HashMap<String, Object>();  
			map.put("image", R.drawable.ic_launcher);  
			map.put("title", "这是一个标题"+i);  
			map.put("info", "这是一个详细信息"+i);  
			list.add(map);  
		}  
		return list;  
	}

	private void setTeackerView(){
		setContentView(teackerView);
	}

	private void setIbeaconView(){
		setContentView(ibeactonView);
		if(ibViewIsFirst){
			ibViewIsFirst = false;
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
					setTeackerView();
				}
			});
		}		
	}

	private void push(Class<?> inClass){
		Intent intent = new Intent(TeacherActivity.this, inClass);		
		navi.pushNavigation(this, intent);
	}

	private void pop(){
		navi.popNavigation(this);
	}

	private void popRoot(){
		navi.popRootNavigation(this, new Intent(TeacherActivity.this, MainActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//pop
			navi.popRootNavigation(this, new Intent(TeacherActivity.this, MainActivity.class));
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
}
