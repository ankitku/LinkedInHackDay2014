package com.example.linker;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends LinkerActivity {

	final static String APIKEY = "75me2mjbnto3oz";
	final static String APISECRET = "pNMHHWELzQeMNJA0";
	final static String CALLBACK = "oauth://linkedin";

	WebView mWebView;
	Token mRequestToken;

	private class LinkedInAuthTask extends AsyncTask<Void, Void, String> {
		
		protected String doInBackground(Void... arg0) {

			// Temporary URL
			String authURL = "http://api.linkedin.com/";

			try {
				mRequestToken = Linker.mService.getRequestToken();
				authURL = Linker.mService.getAuthorizationUrl(mRequestToken);
			} catch (OAuthException e) {
				e.printStackTrace();
				return null;
			}

			return authURL;
		}

		@Override
		protected void onPostExecute(String authURL) {
			System.out.println(mWebView.toString());
			mWebView.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					super.shouldOverrideUrlLoading(view, url);

					if (url.startsWith("oauth")) {
						mWebView.setVisibility(WebView.GONE);

						final String url1 = url;
						Thread t1 = new Thread() {
							public void run() {
								Uri uri = Uri.parse(url1);

								String verifier = uri
										.getQueryParameter("oauth_verifier");
								Verifier v = new Verifier(verifier);
								Linker.accessToken = Linker.mService.getAccessToken(
										mRequestToken, v);
								Intent intent = new Intent();
								intent.putExtra("access_token",
										Linker.accessToken.getToken());
								intent.putExtra("access_secret",
										Linker.accessToken.getSecret());
								setResult(RESULT_OK, intent);

								finish();
							}
						};
						t1.start();
					}

					return false;
				}
			});

			mWebView.loadUrl(authURL);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Allow the title bar to show loading progress.
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.webview);
		mWebView = (WebView) findViewById(R.id.linkedin_webview);
	}

	@Override
	protected void onResume() {
		super.onResume();
		(new LinkedInAuthTask()).execute();
	}
}