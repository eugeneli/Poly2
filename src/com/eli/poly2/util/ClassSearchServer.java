package com.eli.poly2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
	
	private final static String JSON_DATA = "json_data";
	
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
		
		public ServerTask(HTTPMethod httpMethod, String apiMethod, String taskTag, OnResponseListener onResponse)
		{
			this.method = httpMethod;
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
				switch(method)
				{
					case GET:
			            StringBuilder getString = new StringBuilder(fullAPIPath);
						@SuppressWarnings("unchecked")
						Iterator<String> paramList = json.keys();
						boolean firstParam = true;
						while (paramList.hasNext()) {
							String paramName = paramList.next();
							try {
								if(firstParam)
								{
									getString.append('?')
									.append(paramName)
									.append('=')
									.append(json.get(paramName).toString());
									firstParam = false;
								}
								else
								{
									getString.append('&')
									.append(paramName)
									.append('=')
									.append(json.get(paramName).toString());
								}
							}
							catch (JSONException e) {
								e.printStackTrace();
							}
						}
						
						//GET to server
			            HttpGet httpGet = new HttpGet(getString.toString());
						HttpResponse getResponse = httpClient.execute(httpGet);
						System.out.println(getString.toString());
						InputStream getContentStream = getResponse.getEntity().getContent();
			            return convertStreamToString(getContentStream);
					case POST:
						HttpPost httpPost = new HttpPost(fullAPIPath);
	
			            //POST to server
			            List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(2);
			            
						//Add to POST data
			            postNameValuePairs.add(new BasicNameValuePair(JSON_DATA, json.toString()));
						
						//POST to server
			            httpPost.setEntity(new UrlEncodedFormEntity(postNameValuePairs));
			            HttpResponse postResponse = httpClient.execute(httpPost);
			            InputStream postContentStream = postResponse.getEntity().getContent();
			            return convertStreamToString(postContentStream);
					case PUT:
						HttpPut httpPut = new HttpPut(fullAPIPath);
	
			            //POST to server
			            List<NameValuePair> putNameValuePairs = new ArrayList<NameValuePair>(2);
			            
						//Add to POST data
			            putNameValuePairs.add(new BasicNameValuePair(JSON_DATA, json.toString()));
						
						//POST to server
			            httpPut.setEntity(new UrlEncodedFormEntity(putNameValuePairs));
			            HttpResponse putResponse = httpClient.execute(httpPut);
			            InputStream putContentStream = putResponse.getEntity().getContent();
			            return convertStreamToString(putContentStream);
				}
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
        	Log.i(TAG+": "+taskTag, result);
        	
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
	
	private static class CreateUserTask extends ServerTask
	{
		private static final String TASK_TAG = "CreateUser";
		
		public CreateUserTask(OnResponseListener onResponse) {
			super(HTTPMethod.POST, API_USER_PATH, TASK_TAG, onResponse);
		}
		
		public void execute(String username, String password) {
			JSONObject json = new JSONObject();
			try {
				json.put(User.JSON_USER_NAME, username);
				json.put(User.JSON_USER_PWD, password);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			execute(json);
		}
    }
	
	private static class LoginTask extends ServerTask
	{
		private static final String TASK_TAG = "Login";
		
		public LoginTask(OnResponseListener onResponse) {
			super(HTTPMethod.POST, API_USERLOGIN_PATH, TASK_TAG, onResponse);
		}
		
		public void execute(String username, String password) {
			JSONObject json = new JSONObject();
			try {
				json.put(User.JSON_USER_NAME, username);
				json.put(User.JSON_USER_PWD, password);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			execute(json);
		}
    }

	private static class CreatePingTask extends ServerTask
	{
		private static final String TASK_TAG = "CreatePing";
		
		public static final String JSON_PING_DATA = "ping_data";
		
		public CreatePingTask(OnResponseListener onResponse) {
			super(HTTPMethod.POST, API_SINGLE_PING_PATH, TASK_TAG, onResponse);
		}
		
		public void execute(User user, Ping ping) {
			JSONObject json = new JSONObject();
			try {
				json.put(User.JSON_USER_ID, user.getUserID());
				json.put(User.JSON_AUTH_TOKEN, user.getAuthToken());
			
				json.put(JSON_PING_DATA,  ping.toJSON());
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			execute(json);
		}
	}
	/*
	private static class UpdateLocationTask extends ServerTask
	{
		private static final String TASK_TAG = "UpdateLoc";
		private static final String JSON_UPDATE_LOCATION_COMMAND = "UPDATE_LOCATION";
		
		public static final String JSON_LATITUDE = "latitude";
		public static final String JSON_LONGITUDE = "longitude";
		public static final String JSON_DATETIME = "datetime";
		
		public UpdateLocationTask(OnResponseListener onResponse) {
			super(JSON_UPDATE_LOCATION_COMMAND, TASK_TAG, onResponse);
		}
		
		public void execute(double latitude, double longitude) {
			JSONObject json = new JSONObject();
			try {
				json.put(JSON_LATITUDE, latitude);
				json.put(JSON_LONGITUDE, longitude);
				json.put(JSON_DATETIME, System.currentTimeMillis());
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			execute(json);
		}
    }
	*/
	private static class VotePingTask extends ServerTask {
		private final static String TASK_TAG = "VotePing";
		public final static String JSON_VOTE_VALUE = "vote_value";
		
		public VotePingTask(OnResponseListener onResponse) {
			super(HTTPMethod.PUT, API_SINGLE_PING_PATH, TASK_TAG, onResponse);
		}
		
		public void execute(User user, Ping ping, int voteValue) {
			JSONObject json = new JSONObject();
			try {
				json.put(User.JSON_USER_ID, user.getUserID());
				json.put(User.JSON_AUTH_TOKEN, user.getAuthToken());
				json.put(Ping.SERVER_ID, ping.getServerID());
				json.put(JSON_VOTE_VALUE, Integer.signum(voteValue)); // -1, 0, or 1
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			execute(json);
		}
	}
	
	private static class GetPingsTask extends ServerTask {
		private final static String TASK_TAG = "GetPings";
		
		public static final String JSON_LATITUDE = "latitude";
		public static final String JSON_LONGITUDE = "longitude";
		public static final String JSON_RADIUS = "radius";
		public static final String JSON_HASHTAG = "tag";
		
		private static final double METERS_TO_MILES = 0.000621371;
		public GetPingsTask(OnResponseListener onResponse) {
			super(HTTPMethod.GET, API_PINGS_PATH, TASK_TAG, onResponse);
		}
		
		public void execute(double latitude, double longitude, double radius, String hashtag) {
			JSONObject json = new JSONObject();
			try {
				json.put(JSON_LATITUDE, latitude);
				json.put(JSON_LONGITUDE, longitude);
				json.put(JSON_RADIUS, radius * METERS_TO_MILES);
				if (hashtag != null) {
					json.put(JSON_HASHTAG, hashtag);
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			execute(json);
		}
		
	}
	
	private static class GetPingInfoTask extends ServerTask {
		private final static String TASK_TAG = "GetPingInfo";
		
		public GetPingInfoTask(OnResponseListener onResponse) {
			super(HTTPMethod.GET, API_SINGLE_PING_PATH, TASK_TAG, onResponse);
		}
		
		public void execute(String pingId) {
			JSONObject json = new JSONObject();
			try {
				json.put(Ping.SERVER_ID, pingId);
				
				//Append pingID to url
				apiMethod += "/"+pingId;
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
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