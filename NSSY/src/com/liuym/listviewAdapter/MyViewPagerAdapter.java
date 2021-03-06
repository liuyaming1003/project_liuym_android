package com.liuym.listviewAdapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPagerAdapter extends PagerAdapter {
	private List<View> viewList = null;
	public MyViewPagerAdapter(List<View> viewList) {  
		this.viewList = viewList;  
	} 

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)viewList.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";//viewList.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView((View) viewList.get(position));
		return viewList.get(position);
	}
}
