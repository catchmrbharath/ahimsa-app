package com.android.locationmaps.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Utilities {
	
	public static final String TAG = "Utils";
	
	public JSONObject postData(String path,List<NameValuePair> nameValuePairs, CookieStore mCookieStore) {  

		Log.d(TAG, "postData()");
		
		Log.d(TAG + "path", "path = "+path);
		
		// Objects instantiated for HTTP post request
		HttpClient httpclient = new DefaultHttpClient();  
		HttpPost httppost = new HttpPost(path);  
		JSONObject json = new JSONObject();

		try {  

			// Create local HTTP context
			HttpContext localContext = new BasicHttpContext();
			
			// Bind custom cookie store to the local context
			localContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);

			// Add your data  
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  

			// Execute HTTP Post Request  
			HttpResponse response = httpclient.execute(httppost,localContext); 

			Log.d(TAG, "response before Converting from Stream to String : " + response.toString());
			if(response!=null)
			{
				InputStream in = response.getEntity().getContent();
				String result= convertStreamToString(in);
				Log.i(TAG+"+postData",result);
				
				json = new JSONObject(result);
				in.close();
			}
			List<Cookie> cookies = mCookieStore.getCookies();
			
			for (int i = 0; i < cookies.size(); i++) {
				Log.i(TAG,"Local cookie: " + cookies.get(i));
			}

		} catch(JSONException je){
			Log.e(TAG, "JSONException in postData() function", je);
		} catch (Exception ex) {
			Log.e(TAG, "Exception in postData() function", ex);
		} 
		return json;
	}
	
	public static JSONObject getData(String url) {
		return getData(url, new BasicCookieStore());
	}
	
	public static JSONObject getData(String url, CookieStore mCookieStore) {
		
		Log.d(TAG, "getData()");
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		JSONObject json = new JSONObject();
		
		try {
			
			HttpResponse response = client.execute(get);
			
			if(response!=null){
				
				InputStream in = response.getEntity().getContent();
				String result= convertStreamToString(in);
				Log.d(TAG+"+getData",result);

				json = new JSONObject(result);
				in.close();
			}

			List<Cookie> cookies = mCookieStore.getCookies();
			
			for (int i = 0; i < cookies.size(); i++) {
				Log.d(TAG,"Local cookie: " + cookies.get(i));
			}
			
		} catch (Exception ex) {
			Log.e(TAG, "Error in getData() function", ex);
		} 

		return json;
	}
	
	private static String convertStreamToString(InputStream is) {
		Log.d(TAG, "convertStreamToString() received");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		Log.d(TAG, "in convertStreamToString");
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
