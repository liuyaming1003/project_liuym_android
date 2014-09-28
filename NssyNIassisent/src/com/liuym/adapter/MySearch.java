package com.liuym.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

public class MySearch{
	public MySearch(){
	}

	public interface SearchInterface{
		public View Cell(View cellView, Object object);
		public void selectCell(Object object);
		public boolean changedText(Object object, String data);
	}

	@SuppressLint("InflateParams")
	public void showSearch(Context context, View rootView, final ArrayList<Map<String, Object>> arrayList, final SearchInterface searchInterface){
		LayoutInflater inflater = LayoutInflater.from(context); 
		View view = inflater.inflate(R.layout.search_view, null);
		final Handler myhandler = new Handler();
		final EditText search_edittext = (EditText)view.findViewById(R.id.search_edittext);
		final ImageView delete_imageview = (ImageView)view.findViewById(R.id.delete_imageview);

		final PopupWindow window = new PopupWindow(view,  
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true); // 实例化PopupWindow
		window.setAnimationStyle(R.style.popwin_anim_style);
		window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		window.setFocusable(true);
		window.setOutsideTouchable(true);

		//listView显示数据
		@SuppressWarnings("unchecked")
		final ArrayList<Map<String, Object>> listData = (ArrayList<Map<String,Object>>)arrayList.clone();
		//查找内容数据
		final ListView listView = (ListView)view.findViewById(R.id.listView);

		final MyListViewAdapter myAdapter = new MyListViewAdapter(context, listData, new ListViewInterface() {
			@SuppressWarnings("unchecked")
			@Override
			public View Cell(MyListViewAdapter adapter, View cellView, int position) {
				Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
				Object object = map.get("object");
				cellView = searchInterface.Cell(cellView, object);
				return cellView;
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){
				Map<String, Object> map = (Map<String, Object>) myAdapter.getItem(arg2);
				Object object = map.get("object");
				searchInterface.selectCell(object);
				window.dismiss();
			}
		});
		listView.setAdapter(myAdapter);



		final Runnable eChanged = new Runnable() {

			@Override
			public void run() {
				String data = search_edittext.getText().toString();

				listData.clear();//先要清空，不然会叠加
				for(int i = 0; i < arrayList.size(); i ++){
					Map<String, Object> tMap = arrayList.get(i);
					Object object = tMap.get("object");
					if(searchInterface.changedText(object, data) == true){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("object", object);
						listData.add(map);
					}
				}
				myAdapter.notifyDataSetChanged();//更新
			}
		};

		search_edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				//这个应该是在改变的时候会做的动作吧，具体还没用到过。
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				//这是文本框改变之前会执行的动作
			}

			@Override
			public void afterTextChanged(Editable s) {
				/**这是文本框改变之后 会执行的动作
				 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
				 * 所以这里我们就需要加上数据的修改的动作了。
				 */
				if(s.length() == 0){
					delete_imageview.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
				}
				else {
					delete_imageview.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
				}

				myhandler.post(eChanged);
			}
		});



		delete_imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				search_edittext.setText("");
			}
		});


		view.findViewById(R.id.cancel_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				window.dismiss();
			}
		});
	}
}