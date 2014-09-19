package com.liuym.nssyniassisent;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainData{
	private static MainData mainData = null;
	private UserInfoList userInfoList = null;
	private Repair_Recode repair_Recode = null;
	private Repair_Feed_Back repair_Feed_Back = null;
	private Review_Information review_Information = null;
	private IP_Section ip_Section = null;
	private IP_List ip_List = null;
	private Device_Info device_Info = null;
	private String userName = null;
    private ArrayList<Depart_Class> roomArrayList = null;
    private ArrayList<System_Infomation> systemInfoArrayList = null;
    public  int order_select_index;
    public ArrayList<UserRepairRecode> repairHistoryArrayList = null;
    public ArrayList<Device_Info> deviceInfoArrayList = null;
    
    public ArrayList<Device_Info> getDeviceInfoArrayList() {
		return deviceInfoArrayList;
	}

	public ArrayList<UserRepairRecode> getRepairHistoryArrayList() {
		return repairHistoryArrayList;
	}


	//工作人员端维修订单
    private ArrayList<Repair_Recode> repairRecodeArrayList = null;

    /***
     * 获取维修列表
     * @return
     */
    public ArrayList<Repair_Recode> getRepairRecodeArrayList() {
		return repairRecodeArrayList;
	}

	/***
     * 获取系统消息列表
     * @return
     */
	public ArrayList<System_Infomation> getSystemInfoArrayList() {
		return systemInfoArrayList;
	}

	/**
     * 获取 分校房间列表信息
     * @return
     */
	public ArrayList<Depart_Class> getRoomArrayList() {
		return roomArrayList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private MainData(){
		roomArrayList = new ArrayList<MainData.Depart_Class>();
		systemInfoArrayList = new ArrayList<MainData.System_Infomation>();
		repairHistoryArrayList = new ArrayList<MainData.UserRepairRecode>();
		repairRecodeArrayList = new ArrayList<MainData.Repair_Recode>();
		deviceInfoArrayList = new ArrayList<MainData.Device_Info>();
	}

	public static MainData GetDefault(){
		if(mainData == null){
			mainData = new MainData();
		}
		return mainData;
	}
	
	/**
	 * 当注销时需要清楚数据
	 */
	public void clearData(){
		userInfoList = null;
		roomArrayList = null;
		repair_Recode = null;
		repair_Feed_Back = null;
		review_Information = null;
		ip_Section = null;
		ip_List = null;
		device_Info = null;
	}

	/**
	 * 获取用户信息列表，str是JSON数组格式
	 * @param str
	 * @return
	 */
	public boolean setUserInfoList(String str){
		boolean flag = true;
		if(str == null){
			flag =  false;
		}

		try {
			JSONArray array = new JSONArray(str);
			JSONObject info_list_json = array.getJSONObject(0);
			if(userInfoList == null){
				userInfoList = new UserInfoList();
			}
			userInfoList.RealName = info_list_json.get("RealName").toString();
			userInfoList.DepartID = info_list_json.getInt("DepartID");
			userInfoList.Mobile_Tel = info_list_json.get("Mobile_Tel").toString();
			userInfoList.Group_Tel = info_list_json.get("Group_Tel").toString();
			userInfoList.Domain_UserName = info_list_json.get("Domain_UserName").toString();
			userInfoList.Domain_Depart_Idenify = info_list_json.get("Domain_Depart_Idenify").toString();
			userInfoList.pic = info_list_json.get("pic").toString();
			userInfoList.Score = info_list_json.getInt("Score");
		} catch (JSONException e) {
			e.printStackTrace();
			flag =  false;
		}		
		return flag;
	}
	
	/**
	 * 获取用户信息列表，str是JSON数组格式
	 * @param str
	 * @return
	 */
	public boolean setRoomList(String str){
		boolean flag = true;
		roomArrayList.clear();
		if(str == null){
			flag = false;
		}
		
		try {
			JSONArray array = new JSONArray(str);
			for(int i = 0; i < array.length(); i++){
				JSONObject room_json = array.getJSONObject(i);
				Depart_Class depart = new Depart_Class();
				depart.Room_ID = room_json.getInt("Room_ID");
				depart.Room_Name = room_json.getString("Room_Name");
				depart.Room_Type = room_json.getString("Room_Type");
				depart.Room_Num = room_json.getString("Room_Num");
				roomArrayList.add(depart);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 设置设备信息列表，str是JSON数组格式
	 * @param str
	 * @return
	 */
	public boolean setDeviceInfoList(String str){
		boolean flag = true;
		deviceInfoArrayList.clear();
		if(str == null){
			flag = false;
		}
		
		try {
			JSONArray array = new JSONArray(str);
			for(int i = 0; i < array.length(); i++){
				JSONObject device_json = array.getJSONObject(i);
				Device_Info deviceInfo = new Device_Info();
				deviceInfo.Detail_ID = device_json.getInt("Detail_ID");                         // 设备记录ID
				deviceInfo.Model_ID = device_json.getInt("Model_ID");                           // 设备类型ID
				deviceInfo.Model_Name = device_json.getString("Model_Name");                    // 设备类型名称
				deviceInfo.Device_Barcode = device_json.getString("Device_Barcode");            // 设备条形码
				deviceInfo.Device_User = device_json.getString("Device_User");                  // 设备使用人
				deviceInfo.DepartName = device_json.getString("DepartName");                    // 分校名称
				deviceInfo.Device_Location = device_json.getString("Device_Location");          // 具体位置信息
				deviceInfo.Device_Buy_Time = device_json.getString("Device_Buy_Time");          // 购买时间
				deviceInfo.Device_Name = device_json.getString("Device_Name");                  // 设备名称
				deviceInfo.Device_MAC_Address = device_json.getString("Device_MAC_Address");    // 设备MAC地址
				deviceInfo.Device_IP_Address = device_json.getString("Device_IP_Address");      // 设备IP地址
				deviceInfo.Device_Net_UP_Port = device_json.getString("Device_Net_UP_Port");     // 设备网络上联端口
				deviceInfoArrayList.add(deviceInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 设置系统消息列表
	 * @param str
	 * @return
	 */
	public boolean setSystemInfoList(String str){
		boolean flag = true;
		systemInfoArrayList.clear();
		if(str == null){
			flag = false;
		} 
		
		
		try {
			//JSONArray array = new JSONArray(str);
			//for(int i = 0; i < array.length(); i++){
			for(int i = 0; i < 5; i++){				
				JSONObject system_info_json = new JSONObject("{\"SI_ID\":123, \"System_Information_Title\":\"南实集团: 举行《践行群众路线，做好群众工作》专题讲座\", \"System_Information_Content\": \"南实校园讯（通讯员 陈一芹）4月29日下午，南山实验教育集团特邀人民大学博士、市委党校王连喜教授，举行了主题为《践行群众路线，做好群众工作》专题辅导讲座。讲座由南实集团党总支书记、总校长程显栋同志主持，集团全体党员干部180余人聆听讲座\", \"System_Information_Type\":\"1\", \"System_Information_Addtime\":\"2014.06.16 10:25:53\"}");//array.getJSONObject(i);
				System_Infomation systemInfo = new System_Infomation();
				systemInfo.SI_ID = system_info_json.getInt("SI_ID");
				systemInfo.System_Information_Title = system_info_json.getString("System_Information_Title");
				systemInfo.System_Information_Content = system_info_json.getString("System_Information_Content");
				systemInfo.System_Information_Type = system_info_json.getString("System_Information_Type");
				systemInfo.System_Information_Addtime = system_info_json.getString("System_Information_Addtime");
				systemInfoArrayList.add(systemInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 报修记录列表
	 * @param str
	 * @return
	 */
	public boolean setRepairHistoryList(String str){
		boolean flag = true;
		repairHistoryArrayList.clear();
		if(str == null){
			flag = false;
		} 
		
		
		try {
			JSONArray array = new JSONArray(str);
			for(int i = 0; i < array.length(); i++){			
				JSONObject repair_history_json = array.getJSONObject(i);
				UserRepairRecode userRepair = new UserRepairRecode();
				userRepair.Repair_Recode_Num = repair_history_json.getInt("Repair_Recode_Num");
				userRepair.Repair_State = repair_history_json.getInt("Repair_State");
				userRepair.Repair_time = repair_history_json.getString("Repair_time");
				repairHistoryArrayList.add(userRepair);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 设置维修记录列表
	 * @param str
	 * @return
	 */
	public boolean setRepairRecodeList(String str){
		boolean flag = true;
		repairRecodeArrayList.clear();
		if(str == null){
			flag = false;
		} 
		
		
		try {
			//JSONArray array = new JSONArray(str);
			//for(int i = 0; i < array.length(); i++){
			for(int i = 0; i < 5; i++){				
				JSONObject repair_info_json = new JSONObject();//array.getJSONObject(i);
				repair_info_json.put("Repair_Recode_Num", 345436565);
				repair_info_json.put("Device_Barcode", "4433223456");
				repair_info_json.put("Repair_result", "未完成");
				repair_info_json.put("Repair_Domain_UserName", "h12");
				repair_info_json.put("Repair_Information", "二年级办公室");
				repair_info_json.put("Service_UserName", "");
				repair_info_json.put("Repair_State", 0);
				repair_info_json.put("Repair_time", "2014.09.18 10:12:45");
				repair_info_json.put("Service_time", "");
				repair_info_json.put("CaseClosed__time", "");
				repair_info_json.put("DepartID", 3);
				repair_info_json.put("RepairType", 1);
				repair_info_json.put("Repair_RealName", "h12");
				repair_info_json.put("Room_Name", "null");
				repair_info_json.put("Mobile_Tel", "18603036536");
				repair_info_json.put("Group_Tel", "754323");
				Repair_Recode repair = new Repair_Recode();
				repair.Repair_Recode_Num = repair_info_json.getInt("Repair_Recode_Num");          // 报修单号
				repair.Device_Barcode = repair_info_json.getString("Device_Barcode");          // 设备条形码
				repair.Repair_result = repair_info_json.getString("Repair_result");           // 维修结果
				repair.Repair_Domain_UserName = repair_info_json.getString("Repair_Domain_UserName");  // 报修人域用户名
				repair.Repair_Information = repair_info_json.getString("Repair_Information");      // 报修信息，当type==2该参数为班级信息
				repair.Service_UserName = repair_info_json.getString("Service_UserName");        // 接单人员
				repair.Repair_State = repair_info_json.getInt("Repair_State");               // 维修状态
				repair.Repair_time = repair_info_json.getString("Repair_time");             // 报修时间
				repair.Service_time = repair_info_json.getString("Service_time");            // 接单时间
				repair.CaseClosed__time = repair_info_json.getString("CaseClosed__time");        // 结案时间
				repair.DepartID = repair_info_json.getInt("DepartID");                   // 分校ID
				repair.RepairType = repair_info_json.getInt("RepairType");                 // 报修类型
				repair.Repair_RealName = repair_info_json.getString("Repair_RealName");         // 报修人姓名
				repair.Room_Name = repair_info_json.getString("Room_Name");               // 房间号
				repair.Mobile_Tel = repair_info_json.getString("Mobile_Tel");              // 移动号码
				repair.Group_Tel = repair_info_json.getString("Group_Tel");               // 集团短号
				repairRecodeArrayList.add(repair);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}

	public UserInfoList getUserInfo(){
		return userInfoList;
	}


	//用户信息
	public class UserInfoList{
		public String RealName;              //  姓名
		public int DepartID;                 // 分校ID
		public String Mobile_Tel;            // 移动电话
		public String Group_Tel;             // 集团短号
		public String Domain_UserName;       // 域用户名，邮箱信息为  域用户名@sznx.com.cn
		public String Domain_Depart_Idenify; // 分校标示 {南头小学：nantou;麒麟小学：qilin;鼎太小学：dingtai；荔林小学：lilinxiaoxue;麒麟中学：zhongxue;南海中学：lilin}
		public String pic;                   // 用户照片路径（工作人员）
		public int Score;                    // 平均服务分数（工作人员）
		public UserInfoList(){

		}
	}
	
	//报修记录列表
	public class UserRepairRecode{
		public String Repair_time;           // 报修时间
		public int Repair_Recode_Num;        // 报修单号
		public int Repair_State;             // 维修单状态
	}

	// 分校房间列表信息
	public class Depart_Class{
		public int Room_ID;        // 班级ID
		public String Room_Type;   // 班级 classroom，功能室 FunctionRoom，办公室 office
		public String Room_Name;   // 房间名称
		public String Room_Num;    // 当类型为班级时该字段为班级班号
	}

	//系统信息列表
	public class System_Infomation{
		public int SI_ID;                         // 信息ID
		public String System_Information_Title;   // 标题
		public String System_Information_Content; // 内容
		public String System_Information_Type;    // 类型
		public String System_Information_Addtime; // 时间
	}

	//报修记录列表
	public class Repair_Recode{
		public int Repair_Recode_Num;          // 报修单号
		public String Device_Barcode;          // 设备条形码
		public String Repair_result;           // 维修结果
		public String Repair_Domain_UserName;  // 报修人域用户名
		public String Repair_Information;      // 报修信息，当type==2该参数为班级信息
		public String Service_UserName;        // 接单人员
		public int Repair_State;               // 维修状态
		public String Repair_time;             // 报修时间
		public String Service_time;            // 接单时间
		public String CaseClosed__time;        // 结案时间
		public int DepartID;                   // 分校ID
		public int RepairType;                 // 报修类型
		public String Repair_RealName;         // 报修人姓名
		public String Room_Name;               // 房间号
		public String Mobile_Tel;              // 移动号码
		public String Group_Tel;               // 集团短号
	}

	//报修回复信息列表
	public class Repair_Feed_Back{
		public int Feedback_ID;                // 回复ID
		public int Feedback_Type;              // 类型
		public int Repair_Recode_Num;          // 报修单号
		public int Respone_Speed_Score;        // 反应速度评分
		public int Repair_Service_Score;       // 服务评分
		public String Feed_Content;            // 回复内容
		public String Domain_UserName;         // 回复人用户名
		public String Addtime;                 // 回复时间
		public String Feed_Man;                // 回复人姓名
	}

	//预设回复信息列表
	public class Review_Information{
		public int RI_ID;                      // ID
		public int RI_Type;                    // 类型
		public String RI_Content;              // 内容
		public int RI_Count;                   // 使用次数
		public String RI_Tab;                  // 标签
		public String RI_Addtime;              // 添加时间
	}

	//IP地址段列表
	public class IP_Section{
		public String IP_Section_D;            // IP地址段
	}

	//IP地址列表
	public class IP_List {
		public String IP_address;              // IP地址
	}


	public class Device_Info{
		public int Detail_ID;                  // 设备记录ID
		public int Model_ID;                   // 设备类型ID
		public String Model_Name;              // 设备类型名称
		public String Device_Barcode;          // 设备条形码
		public String Device_User;             // 设备使用人
		public String DepartName;              // 分校名称
		public String Device_Location;         // 具体位置信息
		public String Device_Buy_Time;         // 购买时间
		public String Device_Name;             // 设备名称
		public String Device_MAC_Address;      // 设备MAC地址
		public String Device_IP_Address;       // 设备IP地址
		public String Device_Net_UP_Port;      // 设备网络上联端口
	}
}