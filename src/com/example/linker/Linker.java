package com.example.linker;

import model.User;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import android.location.Location;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Linker {

	public static RequestQueue queue;

	public static String userId;
	
	public static User user;

	public static Gson gson = new GsonBuilder().create();

	public static boolean isGooglePlayServicesAvailable;

	public static Location userLocation;
	
	public static OAuthService mService;
	public static Token accessToken;
}
