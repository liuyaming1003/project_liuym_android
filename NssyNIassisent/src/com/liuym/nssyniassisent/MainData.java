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


	//������Ա��ά�޶���
    private ArrayList<Repair_Recode> repairRecodeArrayList = null;

    /***
     * ��ȡά���б�
     * @return
     */
    public ArrayList<Repair_Recode> getRepairRecodeArrayList() {
		return repairRecodeArrayList;
	}

	/***
     * ��ȡϵͳ��Ϣ�б�
     * @return
     */
	public ArrayList<System_Infomation> getSystemInfoArrayList() {
		return systemInfoArrayList;
	}

	/**
     * ��ȡ ��У�����б���Ϣ
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
	 * ��ע��ʱ��Ҫ�������
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
	 * ��ȡ�û���Ϣ�б�str��JSON�����ʽ
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
	 * ��ȡ�û���Ϣ�б�str��JSON�����ʽ
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
	 * �����豸��Ϣ�б�str��JSON�����ʽ
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
				deviceInfo.Detail_ID = device_json.getInt("Detail_ID");                         // �豸��¼ID
				deviceInfo.Model_ID = device_json.getInt("Model_ID");                           // �豸����ID
				deviceInfo.Model_Name = device_json.getString("Model_Name");                    // �豸��������
				deviceInfo.Device_Barcode = device_json.getString("Device_Barcode");            // �豸������
				deviceInfo.Device_User = device_json.getString("Device_User");                  // �豸ʹ����
				deviceInfo.DepartName = device_json.getString("DepartName");                    // ��У����
				deviceInfo.Device_Location = device_json.getString("Device_Location");          // ����λ����Ϣ
				deviceInfo.Device_Buy_Time = device_json.getString("Device_Buy_Time");          // ����ʱ��
				deviceInfo.Device_Name = device_json.getString("Device_Name");                  // �豸����
				deviceInfo.Device_MAC_Address = device_json.getString("Device_MAC_Address");    // �豸MAC��ַ
				deviceInfo.Device_IP_Address = device_json.getString("Device_IP_Address");      // �豸IP��ַ
				deviceInfo.Device_Net_UP_Port = device_json.getString("Device_Net_UP_Port");     // �豸���������˿�
				deviceInfoArrayList.add(deviceInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * ����ϵͳ��Ϣ�б�
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
				JSONObject system_info_json = new JSONObject("{\"SI_ID\":123, \"System_Information_Title\":\"��ʵ����: ���С�����Ⱥ��·�ߣ�����Ⱥ�ڹ�����ר�⽲��\", \"System_Information_Content\": \"��ʵУ԰Ѷ��ͨѶԱ ��һ�ۣ�4��29�����磬��ɽʵ������������������ѧ��ʿ����ί��У����ϲ���ڣ�����������Ϊ������Ⱥ��·�ߣ�����Ⱥ�ڹ�����ר�⸨����������������ʵ���ŵ���֧��ǡ���У�����Զ�ͬ־���֣�����ȫ�嵳Ա�ɲ�180������������\", \"System_Information_Type\":\"1\", \"System_Information_Addtime\":\"2014.06.16 10:25:53\"}");//array.getJSONObject(i);
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
	 * ���޼�¼�б�
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
	 * ����ά�޼�¼�б�
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
				repair_info_json.put("Repair_result", "δ���");
				repair_info_json.put("Repair_Domain_UserName", "h12");
				repair_info_json.put("Repair_Information", "���꼶�칫��");
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
				repair.Repair_Recode_Num = repair_info_json.getInt("Repair_Recode_Num");          // ���޵���
				repair.Device_Barcode = repair_info_json.getString("Device_Barcode");          // �豸������
				repair.Repair_result = repair_info_json.getString("Repair_result");           // ά�޽��
				repair.Repair_Domain_UserName = repair_info_json.getString("Repair_Domain_UserName");  // ���������û���
				repair.Repair_Information = repair_info_json.getString("Repair_Information");      // ������Ϣ����type==2�ò���Ϊ�༶��Ϣ
				repair.Service_UserName = repair_info_json.getString("Service_UserName");        // �ӵ���Ա
				repair.Repair_State = repair_info_json.getInt("Repair_State");               // ά��״̬
				repair.Repair_time = repair_info_json.getString("Repair_time");             // ����ʱ��
				repair.Service_time = repair_info_json.getString("Service_time");            // �ӵ�ʱ��
				repair.CaseClosed__time = repair_info_json.getString("CaseClosed__time");        // �᰸ʱ��
				repair.DepartID = repair_info_json.getInt("DepartID");                   // ��УID
				repair.RepairType = repair_info_json.getInt("RepairType");                 // ��������
				repair.Repair_RealName = repair_info_json.getString("Repair_RealName");         // ����������
				repair.Room_Name = repair_info_json.getString("Room_Name");               // �����
				repair.Mobile_Tel = repair_info_json.getString("Mobile_Tel");              // �ƶ�����
				repair.Group_Tel = repair_info_json.getString("Group_Tel");               // ���Ŷ̺�
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


	//�û���Ϣ
	public class UserInfoList{
		public String RealName;              //  ����
		public int DepartID;                 // ��УID
		public String Mobile_Tel;            // �ƶ��绰
		public String Group_Tel;             // ���Ŷ̺�
		public String Domain_UserName;       // ���û�����������ϢΪ  ���û���@sznx.com.cn
		public String Domain_Depart_Idenify; // ��У��ʾ {��ͷСѧ��nantou;����Сѧ��qilin;��̫Сѧ��dingtai������Сѧ��lilinxiaoxue;������ѧ��zhongxue;�Ϻ���ѧ��lilin}
		public String pic;                   // �û���Ƭ·����������Ա��
		public int Score;                    // ƽ�����������������Ա��
		public UserInfoList(){

		}
	}
	
	//���޼�¼�б�
	public class UserRepairRecode{
		public String Repair_time;           // ����ʱ��
		public int Repair_Recode_Num;        // ���޵���
		public int Repair_State;             // ά�޵�״̬
	}

	// ��У�����б���Ϣ
	public class Depart_Class{
		public int Room_ID;        // �༶ID
		public String Room_Type;   // �༶ classroom�������� FunctionRoom���칫�� office
		public String Room_Name;   // ��������
		public String Room_Num;    // ������Ϊ�༶ʱ���ֶ�Ϊ�༶���
	}

	//ϵͳ��Ϣ�б�
	public class System_Infomation{
		public int SI_ID;                         // ��ϢID
		public String System_Information_Title;   // ����
		public String System_Information_Content; // ����
		public String System_Information_Type;    // ����
		public String System_Information_Addtime; // ʱ��
	}

	//���޼�¼�б�
	public class Repair_Recode{
		public int Repair_Recode_Num;          // ���޵���
		public String Device_Barcode;          // �豸������
		public String Repair_result;           // ά�޽��
		public String Repair_Domain_UserName;  // ���������û���
		public String Repair_Information;      // ������Ϣ����type==2�ò���Ϊ�༶��Ϣ
		public String Service_UserName;        // �ӵ���Ա
		public int Repair_State;               // ά��״̬
		public String Repair_time;             // ����ʱ��
		public String Service_time;            // �ӵ�ʱ��
		public String CaseClosed__time;        // �᰸ʱ��
		public int DepartID;                   // ��УID
		public int RepairType;                 // ��������
		public String Repair_RealName;         // ����������
		public String Room_Name;               // �����
		public String Mobile_Tel;              // �ƶ�����
		public String Group_Tel;               // ���Ŷ̺�
	}

	//���޻ظ���Ϣ�б�
	public class Repair_Feed_Back{
		public int Feedback_ID;                // �ظ�ID
		public int Feedback_Type;              // ����
		public int Repair_Recode_Num;          // ���޵���
		public int Respone_Speed_Score;        // ��Ӧ�ٶ�����
		public int Repair_Service_Score;       // ��������
		public String Feed_Content;            // �ظ�����
		public String Domain_UserName;         // �ظ����û���
		public String Addtime;                 // �ظ�ʱ��
		public String Feed_Man;                // �ظ�������
	}

	//Ԥ��ظ���Ϣ�б�
	public class Review_Information{
		public int RI_ID;                      // ID
		public int RI_Type;                    // ����
		public String RI_Content;              // ����
		public int RI_Count;                   // ʹ�ô���
		public String RI_Tab;                  // ��ǩ
		public String RI_Addtime;              // ���ʱ��
	}

	//IP��ַ���б�
	public class IP_Section{
		public String IP_Section_D;            // IP��ַ��
	}

	//IP��ַ�б�
	public class IP_List {
		public String IP_address;              // IP��ַ
	}


	public class Device_Info{
		public int Detail_ID;                  // �豸��¼ID
		public int Model_ID;                   // �豸����ID
		public String Model_Name;              // �豸��������
		public String Device_Barcode;          // �豸������
		public String Device_User;             // �豸ʹ����
		public String DepartName;              // ��У����
		public String Device_Location;         // ����λ����Ϣ
		public String Device_Buy_Time;         // ����ʱ��
		public String Device_Name;             // �豸����
		public String Device_MAC_Address;      // �豸MAC��ַ
		public String Device_IP_Address;       // �豸IP��ַ
		public String Device_Net_UP_Port;      // �豸���������˿�
	}
}