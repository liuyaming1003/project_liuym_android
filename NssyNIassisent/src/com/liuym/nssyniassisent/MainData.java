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