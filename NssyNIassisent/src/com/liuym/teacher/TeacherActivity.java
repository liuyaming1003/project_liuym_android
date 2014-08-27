package com.liuym.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.adapter.MyViewPagerAdapter;
import com.liuym.nssyniassisent.*;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") 
public class TeacherActivity extends MainActivity {
	private LayoutInflater inflater = null;
	private View teackerView = null;
	private View ibeactonView = null;
	private boolean ibViewIsFirst = true;

	//tableview
	private ViewPager viewPager = null;
	private View order_view = null;
	private int order_select_index = -1;
	private View order_select_view = null;
	private View history_view = null;
	private int history_select_index = -1;
	private View history_select_view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_teacher);

		getIntentValues();
		
		inflater = LayoutInflater.from(this); 
		teackerView = inflater.inflate(R.layout.activity_teacher, null);
		setContentView(teackerView);
		ibeactonView = inflater.inflate(R.layout.activity_ibeacon, null);

		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				popRoot();
			}
		});

		findViewById(R.id.first).setEnabled(false);

		findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setIbeaconView();
			}
		});
		
		findViewById(R.id.teacherBtn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				push(PersonActivity.class);
			}
		});
		
		findViewById(R.id.classBtn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				push(ClassActivity.class);
			}
		});

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
		final MyListViewAdapter infoAdapter = new MyListViewAdapter(this, getOrderList(),  
				new ListViewInterface(){	
			@Override
			public void setCell(MyListViewAdapter adapter, View CellView, int position) {
				TextView info_time = (TextView)CellView.findViewById(R.id.info_time);
				TextView info_title = (TextView)CellView.findViewById(R.id.info_title);
				TextView info_detail = (TextView)CellView.findViewById(R.id.info_detail);
				info_time.setText("2014.06.16 10:25:53");				
				if(order_select_index == position){
					System.out.println("setCell ==");
					info_detail.setVisibility(View.VISIBLE);
				}else{
					System.out.println("setCell !=");
					info_detail.setVisibility(View.GONE);
				}
				info_title.setText("南实集团: 举行《践行群众路线，做好群众工作》专题讲座");
				info_detail.setText("南实校园讯（通讯员 陈一芹）4月29日下午，南山实验教育集团特邀人民大学博士、市委党校王连喜教授，" +
						"举行了主题为《践行群众路线，做好群众工作》专题辅导讲座。讲座由南实集团党总支书记、总" +
						"校长程显栋同志主持，集团全体党员干部180余人聆听讲座");
			}

			@Override
			public View getCell(MyListViewAdapter adapter, final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.info_cell, null);
				TextView info_time = (TextView)CellView.findViewById(R.id.info_time);
				TextView info_title = (TextView)CellView.findViewById(R.id.info_title);
				TextView info_detail = (TextView)CellView.findViewById(R.id.info_detail);
				info_time.setText("2014.06.16 10:25:53");
				info_title.setText("南实集团: 举行《践行群众路线，做好群众工作》专题讲座");
				info_detail.setVisibility(View.GONE); 
				info_detail.setText("南实校园讯（通讯员 陈一芹）4月29日下午，南山实验教育集团特邀人民大学博士、市委党校王连喜教授，" +
						"举行了主题为《践行群众路线，做好群众工作》专题辅导讲座。讲座由南实集团党总支书记、总" +
						"校长程显栋同志主持，集团全体党员干部180余人聆听讲座");
				return CellView;
			}
		});
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
	      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	          long arg3){  
	    	  TextView textView = (TextView)arg1.findViewById(R.id.info_detail);
	    	  if(order_select_index != arg2){
	    		  if(order_select_index != -1){
	    			  order_select_view.setVisibility(View.GONE);
	    		  }
	    		  order_select_index = arg2;	    		  
	    	  }
	    	  if(textView.getVisibility() == View.VISIBLE){
	    		  textView.setVisibility(View.GONE);
	    		  order_select_index = -1;
	    	  }else{
	    		  textView.setVisibility(View.VISIBLE);
	    		  /*textView.setMovementMethod(ScrollingMovementMethod.getInstance()); */
	    	  }
	    	  order_select_view = textView;
	      }  
	    }); 
	}

	private void initHistoryListView(){
		//init Order ListView
		history_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)history_view.findViewById(R.id.myListView);
		
		final MyListViewAdapter historyAdapter = new MyListViewAdapter(this, getOrderList(), 
				new ListViewInterface() {			
			@Override
			public void setCell(MyListViewAdapter adapter, View view, int position) {
				//设置CellView 里面的数据
				System.out.println("setCell index = " + position);
				View detailView = (View)view.findViewById(R.id.history_info);
				if(history_select_index == position){
					System.out.println("setCell ==");
					detailView.setVisibility(View.VISIBLE);
				}else{
					System.out.println("setCell !=");
					detailView.setVisibility(View.GONE);
				}
				setHistoryViewOnClick(view, position);
			}

			@Override
			public View getCell(MyListViewAdapter adapter, final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.history_cell, null);
				View detailView = (View)CellView.findViewById(R.id.history_info);
				detailView.setVisibility(View.GONE);
				setHistoryViewOnClick(CellView, position);
				return CellView;
			}
		});
		listView.setAdapter(historyAdapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
		      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
		          long arg3){
		    	  View detailView = (View)arg1.findViewById(R.id.history_info);
		    	  if(history_select_index != arg2){
		    		  if(history_select_index != -1){
		    			  history_select_view.setVisibility(View.GONE);
		    		  }
		    		  history_select_index = arg2;	    		  
		    	  }
		    	  if(detailView.getVisibility() == View.VISIBLE){
		    		  detailView.setVisibility(View.GONE);
		    		  history_select_index = -1;
		    	  }else{
		    		  detailView.setVisibility(View.VISIBLE);
		    	  }
		    	  history_select_view = detailView;
		      }  
		    }); 
	}
	
	private void setHistoryViewOnClick(View view, final int index){
		System.out.println("index = " + index);
		view.findViewById(R.id.history_btn_1).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
					toastShow("已上报 + " + index);		
			}
		});
		
		view.findViewById(R.id.history_btn_2).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
					toastShow("xxx为你服务 + " + index);		
			}
		});
		
		view.findViewById(R.id.history_btn_3).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
					toastShow("维修进行中 + " + index);		
			}
		});
		
		view.findViewById(R.id.history_btn_4).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
					toastShow("评价服务 + " + index);		
			}
		});
	}
	
	private void toastShow(String showMessage){
		Toast.makeText(this, showMessage, Toast.LENGTH_SHORT).show();
	}

	private List<Map<String, Object>> getOrderList(){  
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
		for (int i = 0; i < 30; i++) {  
			Map<String, Object> map=new HashMap<String, Object>();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
	
	private void getIntentValues(){
		Bundle bundle = getIntent().getExtras();  
		SerializableMap serializableJson = (SerializableMap) bundle  
                .get("map"); 
        System.out.println("map = " + serializableJson + "tag = " + getIntent().getStringExtra("tag"));
	}
}
