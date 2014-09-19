package com.liuym.soap;

import java.util.HashMap;
import java.util.Map;

import com.liuym.soap.Soap.SoapInterface;

public class NssySoapMethod{
	private static NssySoapMethod nssySoap = null;
	private NssySoapMethod(){
		Soap.hostUrl = "http://systeminfo.nssy.com.cn/di.asmx";
		Soap.namespace = "http://tempuri.org/";
	}

	public static NssySoapMethod getDefault(){
		if(nssySoap == null){
			nssySoap = new NssySoapMethod();
		}
		return nssySoap;
	}

	/**
	 * ����ӿ�
	 * @param userName      �����û���
	 * @param password      ��������
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void impersonateValidUser(String userName, String password, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("userName", userName);
	map.put("password", password);
	new Soap().soapRequest("impersonateValidUser", timeout, soapInterface, map);
	}

	/**
	 * �û�Ȩ���ж�
	 * @param userName      �����û���
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Power_Judge(String UserName, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("UserName", UserName);
	new Soap().soapRequest("Power_Judge", timeout, soapInterface, map);
	}

	/**
	 * �û���Ϣ��ȡ
	 * @param userName      �û���
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Teacher_InfoList(String UserName, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("UserName", UserName);
	new Soap().soapRequest("Teacher_InfoList", timeout, soapInterface, map);
	}

	/**
	 * ��У�û���Ϣ�б�
	 * @param DepartID      ��УID
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Teacher_InfoList_Depart(String DepartID, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	new Soap().soapRequest("Teacher_InfoList_Depart", timeout, soapInterface, map);
	}

	/**
	 * ���ط�У������Ϣ�б���Ҫ���ذ༶��������
	 * @param DepartID      ��УID
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Deaprt_Room_list(int DepartID, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	new Soap().soapRequest("Deaprt_Room_list", timeout, soapInterface, map);
	}

	/**
	 * �����˱��޼�¼�ύ
	 * @param Repair_Man    ���������û���
	 * @param DepartID      ��УID
	 * @param Repair_Information ������Ϣ����type==2�ò���Ϊ�༶��Ϣ
	 * @param RepairType    �������ͣ�type==2,�༶��type==1,�칫��
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Report_Repair_Recode(String Repair_Man, int DepartID, String Repair_Information, int RepairType, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Man", Repair_Man);
	map.put("DepartID", DepartID);
	map.put("Repair_Information", Repair_Information);
	map.put("RepairType", RepairType);
	new Soap().soapRequest("Report_Repair_Recode", timeout, soapInterface, map);
	}

	/**
	 * ϵͳ��Ϣ�б�
	 * @param System_Information_Type   ϵͳ��Ϣ���ͣ�=2��������Աר��
	 * @param Recode_Count  �Żؼ�¼����, =0,���м�¼
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void System_Information_List(int System_Information_Type, int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("System_Information_Type", System_Information_Type);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("System_Information_List", timeout, soapInterface, map);
	}

	/**
	 * ��ǰ�û����޽����б�
	 * @param Domain_UserName   ��ǰ�û�
	 * @param Recode_Count      �Żؼ�¼����, =0,���м�¼
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void User_Repair_Recode(String Domain_UserName,int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Domain_UserName", Domain_UserName);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("User_Repair_Recode", timeout, soapInterface, map);
	}

	/**
	 * �豸ά�޼�¼
	 * @param Device_Barcode    �豸������
	 * @param Recode_Count      ��ȡ�б�����
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Device_Repair_Recode(String Device_Barcode, int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Device_Barcode", Device_Barcode);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("Device_Repair_Recode", timeout, soapInterface, map);
	}

	/**
	 * ��ǰ�û����ڷ�Уδ�ӵ����޽����б�
	 * @param DepartID          ��ǰ�û����ڷ�УID
	 * @param Recode_Count      �Żؼ�¼����, =0,���м�¼
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void No_Finish_Repair_Record(int DepartID, int Recode_Count, int timeout, SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("No_Finish_Repair_Record", timeout, soapInterface, map);
	}

	/**
	 * ��ǰ��Уδ�ӵ��ı��޵��б�����δ������ɵı��޵�
	 * @param DepartID          ��ǰ������Ա���ڷ�УID
	 * @param UserName          ��ǰ������Ա���û���
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Worker_Repair_List(int DepartID, String UserName, int timeout, SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	map.put("UserName", UserName);
	new Soap().soapRequest("Worker_Repair_List", timeout, soapInterface, map);
	}

	/**
	 * �ӵ�
	 * @param Repair_Recode_Num ����
	 * @param ServiceUserName   �ӵ���
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Repair_Recode_Service(int Repair_Recode_Num, String ServiceUserName, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Recode_Num", Repair_Recode_Num);
	map.put("ServiceUserName", ServiceUserName);
	new Soap().soapRequest("Repair_Recode_Service", timeout, soapInterface, map);
	}

	/**
	 * ת��
	 * @param Repair_Recode_Num ����
	 * @param Transfer_UserName ת����
	 * @param Recevice_UserName �ӵ���
	 * @param Transfer_Note     ת��ԭ��
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Repair_Recode_Transfer(int Repair_Recode_Num, String Transfer_UserName, String Recevice_UserName, String Transfer_Note, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Recode_Num", Repair_Recode_Num);
	map.put("Transfer_UserName", Transfer_UserName);
	map.put("Recevice_UserName", Recevice_UserName);
	map.put("Transfer_Note", Transfer_Note);
	new Soap().soapRequest("Repair_Recode_Transfer", timeout, soapInterface, map);
	}

	/**
	 * �᰸�´�
	 * @param Repair_Recode_Num ���޵���
	 * @param Device_Barcode    �豸����
	 * @param Malfunction_type  ��������
	 * @param Malfunction_Handle���ϴ�������
	 * @param Tab_Info          �������ݱ�ǩ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Repair_Closed(int Repair_Recode_Num, String Device_Barcode,int Malfunction_type, String Malfunction_Handle, String Tab_Info , int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Recode_Num", Repair_Recode_Num);
	map.put("Device_Barcode", Device_Barcode);
	map.put("Malfunction_type", Malfunction_type);
	map.put("Malfunction_Handle", Malfunction_Handle);
	map.put("Tab_Info", Tab_Info);
	new Soap().soapRequest("Repair_Closed", timeout, soapInterface, map);
	}

	/**
	 * ����Ԥ����Ϣ�⣨�ظ���Ϣ��
	 * @param RI_Content        Ԥ������
	 * @param RI_Type           Ԥ����������
	 * @param Tab_Info          Ԥ������Tab��Ϣ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Add_Review_Information(String RI_Content, int RI_Type, String Tab_Info, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("RI_Content", RI_Content);
	map.put("RI_Type", RI_Type);
	map.put("Tab_Info", Tab_Info);
	new Soap().soapRequest("Add_Review_Information", timeout, soapInterface, map);
	}

	/**
	 * ����Ԥ����Ϣ
	 * @param Tab_Info          = 0��ȡ���м�¼��1:������ϣ�2��Ӳ�����ϣ�3��Ӳ��������4������ʦ��ͨ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Update_Tab_Info(String Tab_Info, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Tab_Info", Tab_Info);
	new Soap().soapRequest("Update_Tab_Info", timeout, soapInterface, map);
	}

	/**
	 * ��ʦ��ͨ�����ϴ�����ȣ�������Ϣ�б�
	 * @param Repair_Recode_Num ά�޵���
	 * @param Recode_Count      ��ȡ�б����� when==0,��ȡ���м�¼
	 * @param Feed_Type         ��ȡ������,1Ϊ��ͨ��Ϣ��2Ϊ������Ϣ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Repair_Feedback_List(int Repair_Recode_Num, int Feed_Type, int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Recode_Num", Repair_Recode_Num);
	map.put("Feed_Type", Feed_Type);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("Repair_Feedback_List", timeout, soapInterface, map);
	}

	/**
	 * �༭��ʦ��ͨ�����ϴ�����ȣ�������Ϣ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	/**
	 * �༭��ʦ��ͨ�����ϴ�����ȣ�������Ϣ
	 * @param Repair_Recode_Num    ά�޵���
	 * @param Feed_Type            ��ȡ������,1Ϊ��ͨ��Ϣ��2Ϊ������Ϣ
	 * @param Respone_Speed_Score  ��Ӧ�ٶ�����
	 * @param Repair_Service_Score ��������
	 * @param Feed_content         //
	 * @param Domain_UserName      �û���
	 * @param edit_type            1��Ӽ�¼��2�༭��¼
	 * @param timeout              ��ʱʱ��
	 * @param soapInterface        �����ص�
	 */
	public void Edit_Feedback_info(int Repair_Recode_Num, int Feed_Type, int Respone_Speed_Score, int Repair_Service_Score, 
			String Feed_content, String Domain_UserName,int edit_type, 
			int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
			map.put("Repair_Recode_Num", Repair_Recode_Num);
			map.put("Feed_Type", Feed_Type);
			map.put("Respone_Speed_Score", Respone_Speed_Score);
			map.put("Repair_Service_Score", Repair_Service_Score);
			map.put("Feed_content", Feed_content);
			map.put("Domain_UserName", Domain_UserName);
			map.put("edit_type", edit_type);
			new Soap().soapRequest("Edit_Feedback_info", timeout, soapInterface, map);
	}

	/**
	 * �豸�б�
	 * @param KeyWord           ��ѯ�ؼ���
	 * @param Style             1�����ѯ;2ģ����ѯ:IP��ַ��Mac��ַ��ʹ����;3δ�����豸���ؼ���Ϊ��
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Device_Info_List(String KeyWord, int Q_Style, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("KeyWord", KeyWord);
	map.put("Q_Style", Q_Style);
	new Soap().soapRequest("Device_Info_List", timeout, soapInterface, map);
	}

	/**
	 * @param Device_Barcode                  �豸����
	 * @param Device_Location                 ��ŵص�
	 * @param Device_User_Domain_UserName     ���û���
	 * @param Device_Mac_Address              �豸MAC��ַ
	 * @param Device_IP_Address               �豸IP��ַ
	 * @param Device_Name                     �豸����
	 * @param Device_Net_UP_Information       �豸������Ϣ
	 * @param Device_User                     �豸ʹ����
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Update_Device_Info(String Device_Barcode, String Device_Location, String Device_User_Domain_UserName, String Device_Mac_Address, String Device_IP_Address, String Device_Name, String Device_Net_UP_Information, String Device_User, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Device_Barcode", Device_Barcode);
	map.put("Device_Location", Device_Location);
	map.put("Device_User_Domain_UserName", Device_User_Domain_UserName);
	map.put("Device_Mac_Address", Device_Mac_Address);
	map.put("Device_IP_Address", Device_IP_Address);
	map.put("Device_Name", Device_Name);
	map.put("Device_Net_UP_Information", Device_Net_UP_Information);
	map.put("Device_User", Device_User);
	new Soap().soapRequest("Update_Device_Info", timeout, soapInterface, map);
	}

	/**
	 * �豸���·���
	 * @param Device_Barcode    �豸����
	 * @param Device_User       �豸ǰʹ����
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Device_Redistribute(String Device_Barcode, String Device_User, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Device_Barcode", Device_Barcode);
	map.put("Device_User", Device_User);
	new Soap().soapRequest("Device_Redistribute", timeout, soapInterface, map);
	}

	/**
	 * ��־�Ǽ�
	 * @param UserName          �������û���
	 * @param IP_Address        IP��ַ����¼�Ǽ�
	 * @param Mac_Address       MAC��ַ����¼�Ǽ�
	 * @param Operation         ��������
	 * @param Useragent         �ֻ���������¼�Ǽ�
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Add_log_info(String UserName, String IP_Address, String Mac_Address, 
			String Operation, String Useragent, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
			map.put("UserName", UserName);
			map.put("IP_Address", IP_Address);
			map.put("Mac_Address", Mac_Address);
			map.put("Operation", Operation);
			map.put("Useragent", Useragent);
			new Soap().soapRequest("Add_log_info", timeout, soapInterface, map);
	}

	/**
	 * ����УIP��ַ��
	 * @param DepartID          ��УID
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void IP_Section_List(int DepartID, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	new Soap().soapRequest("IP_Section_List", timeout, soapInterface, map);
	}

	/**
	 * IP��ַ�ξ������IP��ַ�б�
	 * @param IP_S              IP��ַ��
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void IP_List_Detail(String IP_S, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("IP_S", IP_S);
	new Soap().soapRequest("IP_List_Detail", timeout, soapInterface, map);
	}

	/**
	 * �ж�IP��ַ�Ƿ����
	 * @param IP_address        IP��ַ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void IP_Exist(String IP_address, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Domain_UserName", null);
	map.put("Recode_Count", null);
	new Soap().soapRequest("IP_Exist", timeout, soapInterface, map);
	}
}