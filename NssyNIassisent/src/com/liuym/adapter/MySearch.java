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
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true); // ʵ����PopupWindow
		window.setAnimationStyle(R.style.popwin_anim_style);
		window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		window.setFocusable(true);
		window.setOutsideTouchable(true);

		//listView��ʾ����
		@SuppressWarnings("unchecked")
		final ArrayList<Map<String, Object>> listData = (ArrayList<Map<String,Object>>)arrayList.clone();
		//������������
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

				listData.clear();//��Ҫ��գ���Ȼ�����
				for(int i = 0; i < arrayList.size(); i ++){
					Map<String, Object> tMap = arrayList.get(i);
					Object object = tMap.get("object");
					if(searchInterface.changedText(object, data) == true){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("object", object);
						listData.add(map);
					}
				}
				myAdapter.notifyDataSetChanged();//����
			}
		};

		search_edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				//���Ӧ�����ڸı��ʱ������Ķ����ɣ����廹û�õ�����
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				//�����ı���ı�֮ǰ��ִ�еĶ���
			}

			@Override
			public void afterTextChanged(Editable s) {
				/**�����ı���ı�֮�� ��ִ�еĶ���
				 * ��Ϊ����Ҫ���ľ��ǣ����ı���ı��ͬʱ�����ǵ�listview������Ҳ������Ӧ�ı䶯��������һ����ʾ�ڽ����ϡ�
				 * �����������Ǿ���Ҫ�������ݵ��޸ĵĶ����ˡ�
				 */
				if(s.length() == 0){
					delete_imageview.setVisibility(View.GONE);//���ı���Ϊ��ʱ��������ʧ
				}
				else {
					delete_imageview.setVisibility(View.VISIBLE);//���ı���Ϊ��ʱ�����ֲ��
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