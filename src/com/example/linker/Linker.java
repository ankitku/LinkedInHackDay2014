package com.example.linker;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import android.location.Location;

import com.android.volley.RequestQueue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Linker {

	public static RequestQueue queue;

	public static String userId;

	public static Gson gson = new GsonBuilder().setFieldNamingPolicy(
			FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	public static boolean isGooglePlayServicesAvailable;

	public static Location userLocation;
	
	public static OAuthService mService;
	public static Token accessToken;
}
