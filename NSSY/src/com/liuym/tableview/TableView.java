package com.liuym.tableview;

import java.util.ArrayList;
import java.util.List;

import com.liuym.nssy.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TableView extends LinearLayout{
	private TableViewInterface tableViweInterface;
	private int cellCount = 0;
	
	private LayoutInflater mInflater;
	private LinearLayout mMainContainer;
	private LinearLayout mListContainer;
	private List<IListItem> mItemList;
	//private ClickListener mClickListener;
	
	public interface IListItem {

		public boolean isClickable();
		
		public void setClickable(boolean clickable);
		
	}

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mItemList = new ArrayList<IListItem>();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainContainer = (LinearLayout)  mInflater.inflate(R.layout.tableview, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		addView(mMainContainer, params);				
		mListContainer = (LinearLayout) mMainContainer.findViewById(R.id.buttonsContainer);
		
		//initCell();
	}
	
	public void setCallback(TableViewInterface tableViewInterface){
		this.tableViweInterface = tableViewInterface;
		initCell();
	}
	
	public void reloadData(){
		initCell();
	}
	
	private void initCell(){
		cellCount = tableViweInterface.numberOfCell();			
		
		for(int i = 0; i < cellCount; i++){
			View view = tableViweInterface.setCell(this, i);
			mListContainer.addView(view);
		}
	}
	
	public int getCellCount() {
		return cellCount;
	}
	public void setCellCount(int cellCount) {
		this.cellCount = cellCount;
	}
	
	
}