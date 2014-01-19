package com.lambdai.poly.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;

public class ClassSearchServer
{
	private final static String TAG = "Server";
	private final static String SERVER_URL = "http://itseugene.li/search/api";

	private final static String API_SUBJECTS_PATH = "/classes/subjects";
	private final static String API_CLASSES_PATH = "/classes";
	
	public final static String SUBJECTS = "subjects";
	public final static String SECTIONS = "sections";
	
	
	public static interface OnResponseListener {
		public void onResponse(JSONObject response) throws JSONException;
	}
	
	public ClassSearchServer() {}
	
	/*
	 * Method: GET
	 * URL: SERVER_URL + API_SUBJECTS_PATH
	 * Data: JSONArray with all subjects
	 */
	public void startGetSubjectsTask(OnResponseListener onResponse) {
		new GetSubjectsTask(onResponse).execute();
	}
	
	/*
	 * Method: GET
	 * URL: SERVER_URL + API_CLASSES_PATH + /[Subject]/[ClassNum]
	 * Data: JSONArray with sections for the class
	 */
	public void startGetClassSectionsTask(String subj, String classNum, OnResponseListener onResponse) {
		new GetClassSectionsTask(onResponse).execute(subj, classNum);
	}
	
	private static class ServerTask extends AsyncTask<JSONObject, Void, String>
	{
		protected String apiMethod;
		private String taskTag;
		private OnResponseListener onResponse;
		
		public ServerTask(String apiMethod, String taskTag, OnResponseListener onResponse)
		{
			this.apiMethod = apiMethod;
			this.taskTag = taskTag;
			this.onResponse = onResponse;
		}
		
		@Override
		protected String doInBackground(JSONObject... params)
		{
			String fullAPIPath = SERVER_URL + apiMethod;
			
			// Create a new HttpClient
            HttpClient httpClient = new DefaultHttpClient();
            
            //Get the passed-in JSON
			JSONObject json = params[0];
			
			try
			{
	            /*StringBuilder getString = new StringBuilder(fullAPIPath);
				@SuppressWarnings("unchecked")
				Iterator<String> values = json.keys();
				while (values.hasNext()) {
					String paramName = values.next();
					try {
						getString.append('/')
						.append(json.get(paramName).toString());
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
				}*/
				
				//GET to server
	            HttpGet httpGet = new HttpGet(fullAPIPath);
				HttpResponse getResponse = httpClient.execute(httpGet);
				
				InputStream getContentStream = getResponse.getEntity().getContent();
	            return convertStreamToString(getContentStream);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
			return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
        	//Log.i(TAG+": "+taskTag, result);
        	
        	if(onResponse != null)
        	{
        		try {
        			onResponse.onResponse(new JSONObject(result));
        		}
        		catch (JSONException e) {
        			e.printStackTrace();
        		}
        	}
        }

	}
	
	private static class GetSubjectsTask extends ServerTask {
		private final static String TASK_TAG = "GetSubjects";
		
		public GetSubjectsTask(OnResponseListener onResponse) {
			super(API_SUBJECTS_PATH, TASK_TAG, onResponse);
		}
		
		public void execute() {
			JSONObject json = new JSONObject();
			execute(json);
		}
	}
	
	private static class GetClassSectionsTask extends ServerTask {
		private final static String TASK_TAG = "GetClassSections";
		
		public GetClassSectionsTask(OnResponseListener onResponse) {
			super(API_CLASSES_PATH, TASK_TAG, onResponse);
		}
		
		public void execute(String subj, String classNum) {
			JSONObject json = new JSONObject();
			
			//Quick and dirty "fix". for some reason the subj and classnums were swapped
			apiMethod += '/'+subj+'/'+classNum;
			
			execute(json);
		}
	}
	
	private static String convertStreamToString(InputStream is)
    {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append((line + "\n"));
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