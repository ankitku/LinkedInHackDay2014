package com.example.linker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.ListItemWithIcon;
import model.ListviewAdapter;
import util.ImageCacheManager;
import util.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import constants.LinkerConstants;

public class LinkerActivity extends SherlockFragmentActivity {

	protected LinearLayout fullLayout;
	protected FrameLayout actContent;

	protected ProgressDialog progressDialog;
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private String childActivities[] = { "LoginActivity", "WebViewActivity"};
	private List<String> childActivitiesList = Arrays.asList(childActivities);

	protected static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
	protected static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	protected static int DISK_IMAGECACHE_QUALITY = 100; // PNG is lossless so
														// quality is ignored
														// but must be provided
	private Handler handler = new Handler();

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public String getActivityLabel() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void setContentView(final int layoutResID) {
		fullLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.parent_layout, null); // Your base layout here
		actContent = (FrameLayout) fullLayout.findViewById(R.id.act_content);
		// Setting content of layout your provided to the act_content frame
		getLayoutInflater().inflate(layoutResID, actContent, true);
		super.setContentView(fullLayout);
		// here you can get your drawer buttons and define how they should
		// behave and what must they do, so you won't be needing to repeat it in
		// every activity class

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);

		populateDrawer();
	}

	private void populateDrawer() {
		mTitle = getSupportActionBar().getTitle();
		mDrawerTitle = "Linker";
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		final List<ListItemWithIcon> tabs = new ArrayList<ListItemWithIcon>();
		tabs.add(new ListItemWithIcon(0, "Current Applications", null));
		tabs.add(new ListItemWithIcon(0, "New Application", null));
		tabs.add(new ListItemWithIcon(0, "Feedback", null));
		ListviewAdapter menuAdapter = new ListviewAdapter(LinkerActivity.this,
				tabs);

		mDrawerList.setAdapter(menuAdapter);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				mDrawerLayout.closeDrawer(mDrawerList);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						startActivityWithName(LinkerConstants.fromString(tabs
								.get(position).getTitle()));
					}
				}, 250);
			}
		});

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(LinkerActivity.this,
				mDrawerLayout, R.drawable.ic_drawer, R.string.app_name,
				R.string.app_name) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerToggle.setDrawerIndicatorEnabled(true);

		if (childActivitiesList.contains(getActivityLabel())) {
			mDrawerToggle.setDrawerIndicatorEnabled(false);
			mDrawerLayout
					.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void startActivityWithName(LinkerConstants name) {
		switch (name) {
		case NEWAPP: {
			// inviteFriends();
			break;
		}
		}
		
		if (!"HomeActivity".equals(getActivityLabel().toString()))
			finish();
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	protected void createImageCache() {
		ImageCacheManager.getInstance().init(this, this.getPackageCodePath(),
				DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT,
				DISK_IMAGECACHE_QUALITY);
	}

	protected void loadImage(final String imageUrl, final ImageView iv) {
		ImageCacheManager.getInstance().getImage(imageUrl, new ImageListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (iv == null)
					return;

				iv.setImageResource(R.drawable.placeholder_contact);
			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				if (iv == null)
					return;

				iv.setImageBitmap(response.getBitmap());
			}
		});
	}

}
