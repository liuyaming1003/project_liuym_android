package com.liuym.destest;

import java.io.IOException;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpRequest{
	public HttpRequest(){
	}

	public static String httpGetRequest(String url) throws ClientProtocolException, IOException{
		String result = null;
		HttpGet httpGet = new HttpGet(url);	
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
		httpGet.setHeader("Cache-Control", "max-age=0");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Host", "172.16.88.64");
		
		HttpClient client = new DefaultHttpClient();
        // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		
		
		HttpResponse httpResponse = client.execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			//第三步，使用getEntity方法活得返回结果
			result = EntityUtils.toString(httpResponse.getEntity());
		}else{
			throw new IOException("网络错误" + httpResponse.getStatusLine().getStatusCode());
		}		
		return result;
	}

	public static String httpPostRequest(String url) throws ClientProtocolException, IOException{
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			//第三步，使用getEntity方法活得返回结果
			result = EntityUtils.toString(httpResponse.getEntity());
		}else{
			throw new IOException("网络错误" + httpResponse.getStatusLine().getStatusCode());
		}		
		return result;
	}

}