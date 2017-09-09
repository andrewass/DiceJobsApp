package com.example.dicejobs;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Netcom {

	private String scope,tokenType,accToken,tokenString;
	private int expire;
	private boolean haveToken = false; 

	
	/*get an access token from dice.com*/
	private void getToken(){

		final String USER_ID = "diceHackathon";
		final String PW = "9fc52528-080d-4f0c-becd-45acf46bac4e";
		try{
			URI uri = new URI("https://secure.dice.com/oauth/token?grant_type=client_credentials");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(uri);
			client.getCredentialsProvider().setCredentials(
					new AuthScope(uri.getHost(), uri.getPort(),AuthScope.ANY_SCHEME),
					new UsernamePasswordCredentials(USER_ID, PW));

			HttpResponse response = client.execute(post);

			HttpEntity entity = response.getEntity();
			String result = null;
			if(entity != null){
				result = EntityUtils.toString(entity);
			}

			JSONObject jobc= new JSONObject(result);
			Iterator<String> iter = jobc.keys();	
			String key = iter.next();
			jobc.get(key);	
			key = iter.next();
			tokenType = (String) jobc.get(key);
			key = iter.next();
			scope = (String) jobc.get(key);
			iter.next();
			key = iter.next();
			accToken = (String) jobc.get(key);
			tokenString =  tokenType+" "+accToken;
		}
		catch (Exception e){}
	}

	
	/*find jobs based on the input qString, and return a JSONObject*/
	JSONObject findJobs(String qString) throws Exception{
		if(!haveToken){
			getToken();
			haveToken = true;
		}
		String url = "https://api.dice.com/jobs?";	
		DefaultHttpClient client = new DefaultHttpClient();

		List<NameValuePair> nvp = new ArrayList<NameValuePair>(3);
		nvp.add(new BasicNameValuePair("fields","company,position,id,description"));
		nvp.add(new BasicNameValuePair("q",qString));
		String params = URLEncodedUtils.format(nvp,"UTF-8");

		HttpGet get = new HttpGet(url+params);
		get.setHeader("Authorization",tokenString);
		HttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode() != 200){
			getToken();
			return findJobs(qString);
		}
		HttpEntity entity = response.getEntity();
		String result = null;
		if(entity != null)
			result = EntityUtils.toString(entity);
		return new JSONObject(result);
	}

}
