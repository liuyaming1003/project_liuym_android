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
	 * 登入接口
	 * @param userName      登入用户名
	 * @param password      登入密码
	 * @param timeout       超时时间
	 * @param soapInterface 函数回调
	 */
	public void impersonateValidUser(String userName, String password, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("userName", userName);
	map.put("password", password);
	new Soap().soapRequest("impersonateValidUser", timeout, soapInterface, map);
	}

	/**
	 * 用户权限判断
	 * @param userName      登入用户名
	 * @param timeout       超时时间
	 * @param soapInterface 函数回调
	 */
	public void Power_Judge(String UserName, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("UserName", UserName);
	new Soap().soapRequest("Power_Judge", timeout, soapInterface, map);
	}

	/**
	 * 用户信息读取
	 * @param userName      用户名
	 * @param timeout       超时时间
	 * @param soapInterface 函数回调
	 */
	public void Teacher_InfoList(String UserName, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("UserName", UserName);
	new Soap().soapRequest("Teacher_InfoList", timeout, soapInterface, map);
	}

	/**
	 * 分校用户信息列表
	 * @param DepartID      分校ID
	 * @param timeout       超时时间
	 * @param soapInterface 函数回调
	 */
	public void Teacher_InfoList_Depart(String DepartID, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	new Soap().soapRequest("Teacher_InfoList_Depart", timeout, soapInterface, map);
	}

	/**
	 * 返回分校房间信息列表，主要返回班级及功能室
	 * @param DepartID      分校ID
	 * @param timeout       超时时间
	 * @param soapInterface 函数回调
	 */
	public void Deaprt_Room_list(int DepartID, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	new Soap().soapRequest("Deaprt_Room_list", timeout, soapInterface, map);
	}

	/**
	 * 报修人报修记录提交
	 * @param Repair_Man    报修人域用户名
	 * @param DepartID      分校ID
	 * @param Repair_Information 报修信息，当type==2该参数为班级信息
	 * @param RepairType    报修类型，type==2,班级；type==1,办公室
	 * @param timeout       超时时间
	 * @param soapInterface 函数回调
	 */
	public void Report_Repair_Recode(String Repair_Man, int DepartID, String Repair_Information, int RepairType, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Man", Repair_Man);
	map.put("DepartID", DepartID);
	map.put("Repair_Information", Repair_Information);
	map.put("RepairType", RepairType);
	new Soap().soapRequest("Report_Repair_Recode", timeout, soapInterface, map);
	}

	/**
	 * 系统消息列表
	 * @param System_Information_Type   系统消息类型，=2，工作人员专用
	 * @param Recode_Count  放回记录数量, =0,所有记录
	 * @param timeout       超时时间
	 * @param soapInterface 函数回调
	 */
	public void System_Information_List(int System_Information_Type, int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("System_Information_Type", System_Information_Type);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("System_Information_List", timeout, soapInterface, map);
	}

	/**
	 * 当前用户报修进度列表
	 * @param Domain_UserName   当前用户
	 * @param Recode_Count      放回记录数量, =0,所有记录
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void User_Repair_Recode(String Domain_UserName,int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Domain_UserName", Domain_UserName);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("User_Repair_Recode", timeout, soapInterface, map);
	}

	/**
	 * 设备维修记录
	 * @param Device_Barcode    设备条形码
	 * @param Recode_Count      读取列表数量
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Device_Repair_Recode(String Device_Barcode, int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Device_Barcode", Device_Barcode);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("Device_Repair_Recode", timeout, soapInterface, map);
	}

	/**
	 * 当前用户所在分校未接单报修进度列表
	 * @param DepartID          当前用户所在分校ID
	 * @param Recode_Count      放回记录数量, =0,所有记录
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void No_Finish_Repair_Record(int DepartID, int Recode_Count, int timeout, SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("No_Finish_Repair_Record", timeout, soapInterface, map);
	}

	/**
	 * 当前分校未接单的报修单列表及个人未处理完成的报修单
	 * @param DepartID          当前工作人员所在分校ID
	 * @param UserName          当前工作人员的用户名
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Worker_Repair_List(int DepartID, String UserName, int timeout, SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	map.put("UserName", UserName);
	new Soap().soapRequest("Worker_Repair_List", timeout, soapInterface, map);
	}

	/**
	 * 接单
	 * @param Repair_Recode_Num 单号
	 * @param ServiceUserName   接单人
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Repair_Recode_Service(int Repair_Recode_Num, String ServiceUserName, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Recode_Num", Repair_Recode_Num);
	map.put("ServiceUserName", ServiceUserName);
	new Soap().soapRequest("Repair_Recode_Service", timeout, soapInterface, map);
	}

	/**
	 * 转单
	 * @param Repair_Recode_Num 单号
	 * @param Transfer_UserName 转单人
	 * @param Recevice_UserName 接单人
	 * @param Transfer_Note     转单原因
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Repair_Recode_Transfer(int Repair_Recode_Num, String Transfer_UserName, String Recevice_UserName, String Transfer_Note, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Recode_Num", Repair_Recode_Num);
	map.put("Transfer_UserName", Transfer_UserName);
	map.put("Recevice_UserName", Recevice_UserName);
	map.put("Transfer_Note", Transfer_Note);
	new Soap().soapRequest("Repair_Recode_Transfer", timeout, soapInterface, map);
	}

	/**
	 * 结案陈词
	 * @param Repair_Recode_Num 报修单号
	 * @param Device_Barcode    设备条码
	 * @param Malfunction_type  故障类型
	 * @param Malfunction_Handle故障处理描述
	 * @param Tab_Info          故障内容标签
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
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
	 * 更新预设信息库（回复信息）
	 * @param RI_Content        预设内容
	 * @param RI_Type           预设内容类型
	 * @param Tab_Info          预设内容Tab信息
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Add_Review_Information(String RI_Content, int RI_Type, String Tab_Info, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("RI_Content", RI_Content);
	map.put("RI_Type", RI_Type);
	map.put("Tab_Info", Tab_Info);
	new Soap().soapRequest("Add_Review_Information", timeout, soapInterface, map);
	}

	/**
	 * 返回预设信息
	 * @param Tab_Info          = 0获取所有记录，1:软件故障；2：硬件故障；3：硬件更换，4：与老师沟通
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Update_Tab_Info(String Tab_Info, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Tab_Info", Tab_Info);
	new Soap().soapRequest("Update_Tab_Info", timeout, soapInterface, map);
	}

	/**
	 * 教师沟通、故障处理进度，评价信息列表
	 * @param Repair_Recode_Num 维修单号
	 * @param Recode_Count      读取列表数量 when==0,读取所有记录
	 * @param Feed_Type         读取的类型,1为沟通信息，2为评价信息
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Repair_Feedback_List(int Repair_Recode_Num, int Feed_Type, int Recode_Count, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Repair_Recode_Num", Repair_Recode_Num);
	map.put("Feed_Type", Feed_Type);
	map.put("Recode_Count", Recode_Count);
	new Soap().soapRequest("Repair_Feedback_List", timeout, soapInterface, map);
	}

	/**
	 * 编辑教师沟通、故障处理进度，评价信息
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	/**
	 * 编辑教师沟通、故障处理进度，评价信息
	 * @param Repair_Recode_Num    维修单号
	 * @param Feed_Type            读取的类型,1为沟通信息，2为评价信息
	 * @param Respone_Speed_Score  响应速度评分
	 * @param Repair_Service_Score 服务评分
	 * @param Feed_content         //
	 * @param Domain_UserName      用户名
	 * @param edit_type            1添加记录，2编辑记录
	 * @param timeout              超时时间
	 * @param soapInterface        函数回调
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
	 * 设备列表
	 * @param KeyWord           查询关键字
	 * @param Style             1条码查询;2模糊查询:IP地址，Mac地址，使用人;3未分配设备，关键字为空
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Device_Info_List(String KeyWord, int Q_Style, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("KeyWord", KeyWord);
	map.put("Q_Style", Q_Style);
	new Soap().soapRequest("Device_Info_List", timeout, soapInterface, map);
	}

	/**
	 * 
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Update_Device_Info(int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Domain_UserName", null);
	map.put("Recode_Count", null);
	new Soap().soapRequest("Update_Device_Info", timeout, soapInterface, map);
	}

	/**
	 * 设备重新分配
	 * @param Device_Barcode    设备条码
	 * @param Device_User       设备前使用者
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void Device_Redistribute(String Device_Barcode, String Device_User, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Device_Barcode", Device_Barcode);
	map.put("Device_User", Device_User);
	new Soap().soapRequest("Device_Redistribute", timeout, soapInterface, map);
	}

	/**
	 * 日志登记
	 * @param UserName          操作人用户名
	 * @param IP_Address        IP地址—登录登记
	 * @param Mac_Address       MAC地址—登录登记
	 * @param Operation         操作内容
	 * @param Useragent         手机参数—登录登记
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
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
	 * 各分校IP地址段
	 * @param DepartID          分校ID
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void IP_Section_List(int DepartID, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("DepartID", DepartID);
	new Soap().soapRequest("IP_Section_List", timeout, soapInterface, map);
	}

	/**
	 * IP地址段具体可用IP地址列表
	 * @param IP_S              IP地址段
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void IP_List_Detail(String IP_S, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("IP_S", IP_S);
	new Soap().soapRequest("IP_List_Detail", timeout, soapInterface, map);
	}

	/**
	 * 判断IP地址是否存在
	 * @param IP_address        IP地址
	 * @param timeout           超时时间
	 * @param soapInterface     函数回调
	 */
	public void IP_Exist(String IP_address, int timeout,  SoapInterface soapInterface){Map<String, Object> map = new HashMap<String, Object>();
	map.put("Domain_UserName", null);
	map.put("Recode_Count", null);
	new Soap().soapRequest("IP_Exist", timeout, soapInterface, map);
	}
}