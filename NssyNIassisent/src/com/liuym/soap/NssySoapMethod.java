package com.liuym.soap;

import com.liuym.soap.Soap.SoapInterface;

public class NssySoapMethod{
	private Soap soap = null;
	private static NssySoapMethod nssySoap = null;
	private NssySoapMethod(){
		soap = Soap.getSoap();
		soap.setHostUrl("http://systeminfo.nssy.com.cn/di.asmx");
		soap.setNamespace("http://tempuri.org/");
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
	public void impersonateValidUser(String userName, String password, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("userName", userName);
		soap.putSoapParam("password", password);
		soap.soapRequest("impersonateValidUser", timeout, soapInterface);
	}
	
	/**
	 * �û�Ȩ���ж�
	 * @param userName      �����û���
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Power_Judge(String UserName, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("UserName", UserName);
		soap.soapRequest("Power_Judge", timeout, soapInterface);
	}
	
	/**
	 * �û���Ϣ��ȡ
	 * @param userName      �û���
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Teacher_InfoList(String UserName, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("UserName", UserName);
		soap.soapRequest("Teacher_InfoList", timeout, soapInterface);
	}
	
	/**
	 * ��У�û���Ϣ�б�
	 * @param DepartID      ��УID
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Teacher_InfoList_Depart(String DepartID, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("DepartID", DepartID);
		soap.soapRequest("Teacher_InfoList_Depart", timeout, soapInterface);
	}
	
	/**
	 * ���ط�У������Ϣ�б���Ҫ���ذ༶��������
	 * @param DepartID      ��УID
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void Deaprt_Room_list(int DepartID, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("DepartID", DepartID);
		soap.soapRequest("Deaprt_Room_list", timeout, soapInterface);
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
	public void Report_Repair_Recode(String Repair_Man, int DepartID, String Repair_Information, int RepairType, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Repair_Man", Repair_Man);
		soap.putSoapParam("DepartID", DepartID);
		soap.putSoapParam("Repair_Information", Repair_Information);
		soap.putSoapParam("RepairType", RepairType);
		soap.soapRequest("Report_Repair_Recode", timeout, soapInterface);
	}
	
	/**
	 * ϵͳ��Ϣ�б�
	 * @param System_Information_Type   ϵͳ��Ϣ���ͣ�=2��������Աר��
	 * @param Recode_Count  �Żؼ�¼����, =0,���м�¼
	 * @param timeout       ��ʱʱ��
	 * @param soapInterface �����ص�
	 */
	public void System_Information_List(int System_Information_Type, int Recode_Count, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("System_Information_Type", System_Information_Type);
		soap.putSoapParam("Recode_Count", Recode_Count);
		soap.soapRequest("Report_Repair_Recode", timeout, soapInterface);
	}
	
	/**
	 * ��ǰ�û����޽����б�
	 * @param Domain_UserName   ��ǰ�û�
	 * @param Recode_Count      �Żؼ�¼����, =0,���м�¼
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void User_Repair_Recode(String Domain_UserName,int Recode_Count, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Domain_UserName", Domain_UserName);
		soap.putSoapParam("Recode_Count", Recode_Count);
		soap.soapRequest("Report_Repair_Recode", timeout, soapInterface);
	}
	
	/**
	 * �豸ά�޼�¼
	 * @param Device_Barcode    �豸������
	 * @param Recode_Count      ��ȡ�б�����
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Device_Repair_Recode(String Device_Barcode, int Recode_Count, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Device_Barcode", Device_Barcode);
		soap.putSoapParam("Recode_Count", Recode_Count);
		soap.soapRequest("Report_Repair_Recode", timeout, soapInterface);
	}
	
	/**
	 * ��ǰ�û����ڷ�Уδ�ӵ����޽����б�
	 * @param DepartID          ��ǰ�û����ڷ�УID
	 * @param Recode_Count      �Żؼ�¼����, =0,���м�¼
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void No_Finish_Repair_Record(int DepartID, int Recode_Count, int timeout, SoapInterface soapInterface){
		soap.putSoapParam("DepartID", DepartID);
		soap.putSoapParam("Recode_Count", Recode_Count);
		soap.soapRequest("No_Finish_Repair_Record", timeout, soapInterface);
	}
	
	/**
	 * ��ǰ��Уδ�ӵ��ı��޵��б�����δ������ɵı��޵�
	 * @param DepartID          ��ǰ������Ա���ڷ�УID
	 * @param UserName          ��ǰ������Ա���û���
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Worker_Repair_List(int DepartID, String UserName, int timeout, SoapInterface soapInterface){
		soap.putSoapParam("DepartID", DepartID);
		soap.putSoapParam("UserName", UserName);
		soap.soapRequest("Worker_Repair_List", timeout, soapInterface);
	}
	
	/**
	 * �ӵ�
	 * @param Repair_Recode_Num ����
	 * @param ServiceUserName   �ӵ���
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Repair_Recode_Service(int Repair_Recode_Num, String ServiceUserName, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Repair_Recode_Num", Repair_Recode_Num);
		soap.putSoapParam("ServiceUserName", ServiceUserName);
		soap.soapRequest("Repair_Recode_Service", timeout, soapInterface);
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
	public void Repair_Recode_Transfer(int Repair_Recode_Num, String Transfer_UserName, String Recevice_UserName, String Transfer_Note, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Repair_Recode_Num", Repair_Recode_Num);
		soap.putSoapParam("Transfer_UserName", Transfer_UserName);
		soap.putSoapParam("Recevice_UserName", Recevice_UserName);
		soap.putSoapParam("Transfer_Note", Transfer_Note);
		soap.soapRequest("Repair_Recode_Transfer", timeout, soapInterface);
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
	public void Repair_Closed(int Repair_Recode_Num, String Device_Barcode,int Malfunction_type, String Malfunction_Handle, String Tab_Info , int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Repair_Recode_Num", Repair_Recode_Num);
		soap.putSoapParam("Device_Barcode", Device_Barcode);
		soap.putSoapParam("Malfunction_type", Malfunction_type);
		soap.putSoapParam("Malfunction_Handle", Malfunction_Handle);
		soap.putSoapParam("Tab_Info", Tab_Info);
		soap.soapRequest("Repair_Closed", timeout, soapInterface);
	}
	
	/**
	 * ����Ԥ����Ϣ�⣨�ظ���Ϣ��
	 * @param RI_Content        Ԥ������
	 * @param RI_Type           Ԥ����������
	 * @param Tab_Info          Ԥ������Tab��Ϣ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Add_Review_Information(String RI_Content, int RI_Type, String Tab_Info, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("RI_Content", RI_Content);
		soap.putSoapParam("RI_Type", RI_Type);
		soap.putSoapParam("Tab_Info", Tab_Info);
		soap.soapRequest("Add_Review_Information", timeout, soapInterface);
	}
	
	/**
	 * ����Ԥ����Ϣ
	 * @param Tab_Info          = 0��ȡ���м�¼��1:������ϣ�2��Ӳ�����ϣ�3��Ӳ��������4������ʦ��ͨ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Update_Tab_Info(String Tab_Info, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Tab_Info", Tab_Info);
		soap.soapRequest("Update_Tab_Info", timeout, soapInterface);
	}
	
	/**
	 * ��ʦ��ͨ�����ϴ�����ȣ�������Ϣ�б�
	 * @param Repair_Recode_Num ά�޵���
     * @param Recode_Count      ��ȡ�б����� when==0,��ȡ���м�¼
	 * @param Feed_Type         ��ȡ������,1Ϊ��ͨ��Ϣ��2Ϊ������Ϣ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Repair_Feedback_List(int Repair_Recode_Num, int Feed_Type, int Recode_Count, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Repair_Recode_Num", Repair_Recode_Num);
		soap.putSoapParam("Feed_Type", Feed_Type);
		soap.putSoapParam("Recode_Count", Recode_Count);
		soap.soapRequest("Repair_Feedback_List", timeout, soapInterface);
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
								   int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Repair_Recode_Num", Repair_Recode_Num);
		soap.putSoapParam("Feed_Type", Feed_Type);
		soap.putSoapParam("Respone_Speed_Score", Respone_Speed_Score);
		soap.putSoapParam("Repair_Service_Score", Repair_Service_Score);
		soap.putSoapParam("Feed_content", Feed_content);
		soap.putSoapParam("Domain_UserName", Domain_UserName);
		soap.putSoapParam("edit_type", edit_type);
		soap.soapRequest("Edit_Feedback_info", timeout, soapInterface);
	}
	
	/**
	 * �豸�б�
	 * @param KeyWord           ��ѯ�ؼ���
     * @param Style             1�����ѯ;2ģ����ѯ:IP��ַ��Mac��ַ��ʹ����;3δ�����豸���ؼ���Ϊ��
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Device_Info_List(String KeyWord, int Q_Style, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("KeyWord", KeyWord);
		soap.putSoapParam("Q_Style", Q_Style);
		soap.soapRequest("Device_Info_List", timeout, soapInterface);
	}
	
	/**
	 * 
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Update_Device_Info(int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Domain_UserName", null);
		soap.putSoapParam("Recode_Count", null);
		soap.soapRequest("Update_Device_Info", timeout, soapInterface);
	}
	
	/**
	 * �豸���·���
	 * @param Device_Barcode    �豸����
     * @param Device_User       �豸ǰʹ����
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void Device_Redistribute(String Device_Barcode, String Device_User, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Device_Barcode", Device_Barcode);
		soap.putSoapParam("Device_User", Device_User);
		soap.soapRequest("Device_Redistribute", timeout, soapInterface);
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
			String Operation, String Useragent, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("UserName", UserName);
		soap.putSoapParam("IP_Address", IP_Address);
		soap.putSoapParam("Mac_Address", Mac_Address);
		soap.putSoapParam("Operation", Operation);
		soap.putSoapParam("Useragent", Useragent);
		soap.soapRequest("Add_log_info", timeout, soapInterface);
	}
	
	/**
	 * ����УIP��ַ��
	 * @param DepartID          ��УID
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void IP_Section_List(int DepartID, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("DepartID", DepartID);
		soap.soapRequest("IP_Section_List", timeout, soapInterface);
	}
	
	/**
	 * IP��ַ�ξ������IP��ַ�б�
	 * @param IP_S              IP��ַ��
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void IP_List_Detail(String IP_S, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("IP_S", IP_S);
		soap.soapRequest("IP_List_Detail", timeout, soapInterface);
	}
	
	/**
	 * �ж�IP��ַ�Ƿ����
	 * @param IP_address        IP��ַ
	 * @param timeout           ��ʱʱ��
	 * @param soapInterface     �����ص�
	 */
	public void IP_Exist(String IP_address, int timeout,  SoapInterface soapInterface){
		soap.putSoapParam("Domain_UserName", null);
		soap.putSoapParam("Recode_Count", null);
		soap.soapRequest("IP_Exist", timeout, soapInterface);
	}
}