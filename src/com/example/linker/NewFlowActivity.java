package com.example.linker;

import model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NewFlowActivity extends LinkerActivity {
	
	EditText et;
	String Companiesresponse;
	String logoUrl, websiteUrl, compname;
	ImageView iv;
	TextView tv1,tv2 ; 
	Handler handler = new Handler();
	
	private class GetCompanyInfoTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			// Temporary URL
			String authURL = "http://api.linkedin.com/";
			try {
				 
				String url = "http://api.linkedin.com/v1/company-search:(companies:(id,name,universal-name,website-url,logo-url))?keywords={" + et.getText().toString() +"}";
				OAuthRequest request = new OAuthRequest(Verb.GET, url);
				request.addHeader("x-li-format", "json");
				// sign the request with my access token
				Linker.mService.signRequest(Linker.accessToken, request);
				 
				// send the request and get the response
				Response response = request.send();
				Companiesresponse = response.getBody();
				// print out the response body
				System.out.println(Companiesresponse);
				
			} catch (OAuthException e) {
				e.printStackTrace();
			}
			return authURL;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
				
		try {
			JSONObject resp = new JSONObject(Companiesresponse);
			JSONObject ff = resp.getJSONObject("companies");
			JSONArray companies = ff.getJSONArray("values");
			logoUrl = ((JSONObject) companies.get(0)).getString("logoUrl");
			 websiteUrl =  ((JSONObject) companies.get(0)).getString("websiteUrl");
			 compname =  ((JSONObject) companies.get(0)).getString("name");
			 
			if (logoUrl != null && !logoUrl.equalsIgnoreCase("null")
					&& !logoUrl.equalsIgnoreCase("")) {
				loadImage(logoUrl, iv);
				tv1.setText(compname);
				tv1.setText(websiteUrl);

				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						loadImage(logoUrl, iv);						
					}
				}, 5000);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_newflow);
		et= (EditText) findViewById(R.id.editText1);
		iv= (ImageView) findViewById(R.id.imageView1);
		tv1= (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
	}
	
	public void searchCompany(View v)
	{
		(new GetCompanyInfoTask()).execute();
	}
}
