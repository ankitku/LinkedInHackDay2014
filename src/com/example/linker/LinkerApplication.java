package com.example.linker;

import android.app.Application;

import com.android.volley.toolbox.Volley;

public class LinkerApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Linker.queue = Volley.newRequestQueue(this);
	}

}
