package com.liuym.teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeacherData{
	private static TeacherData teacherData = null;
	private String userName = null;
	private JSONObject info_list_json = null;
	private TeacherData(){

	}

	/**
	 * 获取老师登入用户名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置老师登入用户名
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean setInfoList(String str){
		boolean flag = true;
		if(str == null){
			flag =  false;
		}
		
		try {
			JSONArray array = new JSONArray(str);
			info_list_json = array.getJSONObject(0);
		} catch (JSONException e) {
			e.printStackTrace();
			flag =  false;
		}		
		return true;
	}
	
	public String getInfoListJson(String key){
		String retStr = null;
		if(info_list_json != null && info_list_json.has(key)){
			try {
				retStr = info_list_json.get(key).toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return retStr;
	}


	/**
	 * 获取老师端数据管理实例
	 */
	public static TeacherData getDefault(){
		if(teacherData == null){
			teacherData = new TeacherData();
		}
		return teacherData;
	}

	public void clearData(){
		userName = null;
		info_list_json = null;
	}
}