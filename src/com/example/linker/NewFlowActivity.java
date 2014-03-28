package com.example.linker;

import model.User;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public class NewFlowActivity extends LinkerActivity {
	
	private class GetCompanyInfoTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			// Temporary URL
			String authURL = "http://api.linkedin.com/";
			try {
				 
				String url = "http://api.linkedin.com/v1/companies/universal-name=linkedin";
				OAuthRequest request = new OAuthRequest(Verb.GET, url);
				request.addHeader("x-li-format", "json");
				// sign the request with my access token
				Linker.mService.signRequest(Linker.accessToken, request);
				 
				// send the request and get the response
				Response response = request.send();
				 
				// print out the response body
				System.out.println(response.getBody());
			} catch (OAuthException e) {
				e.printStackTrace();
			}
			return authURL;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Intent intent = new Intent(NewFlowActivity.this,  HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_newflow);
	}
	
	public void searchCompany(View v)
	{
		(new GetCompanyInfoTask()).execute();
	}
}
