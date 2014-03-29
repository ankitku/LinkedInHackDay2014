package com.example.linker;

import model.User;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import util.Utils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import constants.UrlConstants;

public class LoginActivity extends LinkerActivity {

	private class GetUserInfoTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			// Temporary URL
			String authURL = "http://api.linkedin.com/";
			try {
				// Assume you already obtained an access token and imported the
				// appropriate classes
				System.out.println("********A basic user profile call********");

				String url = "http://api.linkedin.com/v1/people/~";
				OAuthRequest request = new OAuthRequest(Verb.GET, url);
				request.addHeader("x-li-format", "json");
				// sign the request with my access token
				Linker.mService.signRequest(Linker.accessToken, request);

				// send the request and get the response
				Response response = request.send();

				// print out the response body
				// System.out.println(response.getBody());

				Linker.user = User.getObjectFromJson(response.getBody()
						.toString(), User.class);
				System.out.println("member id =================="
						+ Linker.user.getMemberId());
			} catch (OAuthException e) {
				e.printStackTrace();
			}
			return authURL;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			makeServerCall();

			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

	}

	final int LINKEDIN_LOGIN_RESULT_CODE = 1;
	Button b;
	SharedPreferences myPrefs;

	final static String APIKEY = "75me2mjbnto3oz";
	final static String APISECRET = "pNMHHWELzQeMNJA0";
	final static String CALLBACK = "oauth://linkedin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		myPrefs = getSharedPreferences("PrefData", MODE_PRIVATE);
		createImageCache();
		
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
		Linker.mService = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(APIKEY).apiSecret(APISECRET).callback(CALLBACK)
				.scope("r_fullprofile").scope("rw_nus").build();

		String token = myPrefs.getString("linkedin_access_token", "");
		String secret = myPrefs.getString("linkedin_access_secret", "");
		if (!Utils.isEmpty(token)) {
			Linker.accessToken = new Token(token, secret);
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void makeServerCall() {
		JSONObject jsonObjectRequest = new JSONObject();

		try {
			JSONObject j = new JSONObject();

			j.put("member_id", Linker.user.getMemberId());
			j.put("access_token", Linker.accessToken.getToken());
			j.put("name", Linker.user.getFirstName());
			j.put("commit", "Create User");

			jsonObjectRequest.put("user", j);
			// jsonObjectRequest.put("authenticity_token","RHPPwAoJwhY7Lbw1oDJfuY+ll38zrZvLyU/McDyk1Sw=");

		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonObjectRequest jsonRegisterUserRequest = new JsonObjectRequest(
				Method.POST, UrlConstants.insertUserUrl, jsonObjectRequest,
				new com.android.volley.Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

					}

				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}

				});

		System.out.println(Linker.queue.toString());
		Linker.queue.add(jsonRegisterUserRequest);

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
