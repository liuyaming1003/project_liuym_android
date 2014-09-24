package com.liuym.destest;

import java.io.File;
import java.io.IOException;
import java.security.spec.EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpRequest{
	public HttpRequest(){
	}

	public static String httpGetRequest(String url, String hostAddr) throws ClientProtocolException, IOException{
		String result = null;
		HttpGet httpGet = new HttpGet(url);	
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
		httpGet.setHeader("Cache-Control", "max-age=0");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Host", hostAddr);

		HttpClient client = new DefaultHttpClient();
		// ����ʱ
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// ��ȡ��ʱ
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);


		HttpResponse httpResponse = client.execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			//��������ʹ��getEntity������÷��ؽ��
			result = EntityUtils.toString(httpResponse.getEntity());
		}else{
			throw new IOException("�������" + httpResponse.getStatusLine().getStatusCode());
		}		
		return result;
	}

	public static String httpPostRequest(String url) throws ClientProtocolException, IOException{
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			//��������ʹ��getEntity������÷��ؽ��
			result = EntityUtils.toString(httpResponse.getEntity());
		}else{
			throw new IOException("�������" + httpResponse.getStatusLine().getStatusCode());
		}		
		return result;
	}

	public static String httpPost(String url, String data)throws ClientProtocolException, IOException{
		String result = null;
		HttpPost httpPost = new HttpPost(url);


		//���ò�������html���ύ 
		List<NameValuePair> paramList = new ArrayList<NameValuePair>(); 
		BasicNameValuePair param = new BasicNameValuePair("param",data); 
		paramList.add(param); 
		// �����ַ���
		HttpEntity entity = new UrlEncodedFormEntity(paramList, HTTP.UTF_8);
		// ���ò���ʵ��
		httpPost.setEntity(entity);  

		HttpClient client = new DefaultHttpClient();
		// ����ʱ
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// ��ȡ��ʱ
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		HttpResponse httpResponse = client.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			//��������ʹ��getEntity������÷��ؽ��
			result = EntityUtils.toString(httpResponse.getEntity());
		}else{
			throw new IOException("�������" + httpResponse.getStatusLine().getStatusCode());
		}		
		return result;
	}

	public static String postFile(String url, String filePath, String username) throws ClientProtocolException, IOException {
		String result = null;
		//HttpClient httpclient = new DefaultHttpClient();
		//����ͨ��Э��汾
		//httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		//File path= Environment.getExternalStorageDirectory(); //ȡ��SD����·��

		//String pathToOurFile = path.getPath()+File.separator+"ak.txt"; //uploadfile
		//String urlServer = "http://192.168.1.88/test/upload.php"; 

		HttpPost httpPost = new HttpPost(url);
		File file = new File(filePath);

		MultipartEntity mpEntity = new MultipartEntity(); //�ļ�����
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart(username+".txt", cbFile); // <input type="file" name="userfile" />  ��Ӧ��


		httpPost.setEntity(mpEntity);
		HttpClient client = new DefaultHttpClient();
		// ����ʱ
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// ��ȡ��ʱ
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		HttpResponse httpResponse = client.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			//��������ʹ��getEntity������÷��ؽ��
			result = EntityUtils.toString(httpResponse.getEntity());
		}else{
			//throw new IOException("�������" + httpResponse.getStatusLine().getStatusCode());
		}		
		return result;
		
	}

}