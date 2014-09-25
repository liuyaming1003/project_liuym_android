package com.liuym.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyListViewAdapter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private ListViewInterface listViewInterface = null;
	public MyListViewAdapter(Context context,List<Map<String, Object>> data, ListViewInterface listViewInterface){
		this.data=data;
		this.listViewInterface = listViewInterface;
	}

	public interface ListViewInterface{
		public View Cell(MyListViewAdapter adapter, View cellView, int position);
	}

	@Override
	public int getCount() {
		return data.size();
	}
	/**
	 * 获得某一位置的数据
	 */
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	
	/**
	 * 获得唯一标识
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(listViewInterface == null){
			return null;
		}
		if(convertView == null){
			convertView  = listViewInterface.Cell(this, null, position);
		}else{
			listViewInterface.Cell(this, convertView, position);
		}
		return convertView;
	}
}
