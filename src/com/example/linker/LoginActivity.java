package com.example.linker;

import model.User;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends LinkerActivity {
	
	private class GetUserInfoTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			// Temporary URL
			String authURL = "http://api.linkedin.com/";
			try {
				// Assume you already obtained an access token and imported the appropriate classes
				System.out.println("********A basic user profile call********");
				 
				String url = "http://api.linkedin.com/v1/people/~";
				OAuthRequest request = new OAuthRequest(Verb.GET, url);
				request.addHeader("x-li-format", "json");
				// sign the request with my access token
				Linker.mService.signRequest(Linker.accessToken, request);
				 
				// send the request and get the response
				Response response = request.send();
				 
				// print out the response body
				//System.out.println(response.getBody());
				
				Linker.user = User.getObjectFromJson(response.getBody().toString() , User.class);
				System.out.println("member id ==================" + Linker.user.getMemberId());
			} catch (OAuthException e) {
				e.printStackTrace();
			}
			return authURL;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Intent intent = new Intent(LoginActivity.this,  HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

	final int LINKEDIN_LOGIN_RESULT_CODE = 1;
	Button b;
	SharedPreferences myPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		myPrefs = getSharedPreferences("PrefData", MODE_PRIVATE);

		Button b = (Button) findViewById(R.id.btAuthenticate);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent getTokenIntent = new Intent(LoginActivity.this,
						WebViewActivity.class);
				startActivityForResult(getTokenIntent,
						LINKEDIN_LOGIN_RESULT_CODE);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK
				&& requestCode == LINKEDIN_LOGIN_RESULT_CODE) {
			String access_token = data.getStringExtra("access_token");
			String access_secret = data.getStringExtra("access_secret");

			// Store the tokens in preferences for further use
			SharedPreferences.Editor editor = myPrefs.edit();
			editor.putString("linkedin_access_token", access_token);
			editor.putString("linkedin_access_secret", access_secret);
			editor.commit();

			System.out.println(access_secret
					+ "----------------------------------" + access_token);
			// Start activity
			// Intent intent = new Intent(this, LinkedInListActivity.class);
			// startActivity(intent);
			(new GetUserInfoTask()).execute();
		}
	}

}
