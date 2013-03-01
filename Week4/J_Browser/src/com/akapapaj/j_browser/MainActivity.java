package com.akapapaj.j_browser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	final static String LOG_TAG = "J-Browser";
	final static int topID = Menu.FIRST + 100, backID = Menu.FIRST + 101,
			botID = Menu.FIRST + 102, addrID = Menu.FIRST + 103,
			buttBack = Menu.FIRST + 104, buttFwd = Menu.FIRST + 105,
			buttLoad = Menu.FIRST + 106, buttStop = Menu.FIRST + 107,
			buttGo = Menu.FIRST + 108;

	String j_PageName = "http://www.google.com";
	int j_HTMLsize = 0;
	WebView j_web;
	EditText j_addr;
	TextView j_tv;
	Button j_backButt, j_fwdButt, j_loadButt, j_stopButt, j_goButt;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Get the intent that started the activity and it's data Intent intent
		 * = getIntent(); Uri data = intent.getData(); URL url = null; try { url
		 * = new URL(data.getScheme(), data.getHost(), data.getPath()); } catch
		 * (Exception e) { e.printStackTrace(); }
		 * 
		 * j_PageName = url.toString();
		 **/

		// I decided that I would try out making my Relative Layout UI
		// programmically instead of all in the xml file //

		// Hide Titlebar //
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Top Header //
		RelativeLayout j_panel = new RelativeLayout(this);
		j_panel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		j_panel.setGravity(Gravity.FILL);
		j_panel.setBackground(getResources().getDrawable(R.drawable.back));
		RelativeLayout jMenu = new RelativeLayout(this);
		jMenu.setId(topID);

		jMenu.setBackground(getResources().getDrawable(R.drawable.line));
		int jMenuPadding = 6;
		jMenu.setPadding(jMenuPadding, jMenuPadding, jMenuPadding, jMenuPadding);
		RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		topParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		j_panel.addView(jMenu, topParams);

		int horText = 12;

		j_goButt = new Button(this);
		j_goButt.setId(buttGo);
		j_goButt.setOnClickListener(this);
		j_goButt.setText("Go");
		j_goButt.setTextSize(horText);
		j_goButt.setTypeface(Typeface.create("arial", Typeface.BOLD));

		RelativeLayout.LayoutParams papaJ = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		papaJ.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		papaJ.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		jMenu.addView(j_goButt);
		// addr field //
		j_addr = new EditText(this);
		j_addr.setId(addrID);
		j_addr.setText(j_PageName);
		j_addr.setBackgroundResource(R.drawable.my_editbox);
		j_addr.setTextColor(Color.BLACK);
		horText = 12;
		j_addr.setTextSize(horText);
		j_addr.setTypeface(Typeface.create("arial", Typeface.NORMAL));

		RelativeLayout.LayoutParams papaEdit = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		papaEdit.addRule(RelativeLayout.RIGHT_OF, buttGo);
		papaEdit.addRule(RelativeLayout.CENTER_VERTICAL);
		// papaEdit.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		jMenu.addView(j_addr, papaEdit);

		// ******* FOOTER ******** //
		RelativeLayout jMenuBot = new RelativeLayout(this);
		jMenuBot.setId(botID);
		jMenuBot.setBackground(getResources().getDrawable(R.drawable.line));
		jMenuBot.setPadding(jMenuPadding, jMenuPadding, jMenuPadding,
				jMenuPadding);

		RelativeLayout.LayoutParams botParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		botParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		j_panel.addView(jMenuBot, botParams);
		j_backButt = new Button(this);
		j_backButt.setId(buttBack);
		j_backButt.setOnClickListener(this);
		j_backButt.setText("Back");
		j_backButt.setTextSize(horText);
		j_backButt.setTypeface(Typeface.create("arial", Typeface.BOLD));

		RelativeLayout.LayoutParams aka = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		aka.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		aka.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		jMenuBot.addView(j_backButt, aka);

		// Forward Button //
		j_fwdButt = new Button(this);
		j_fwdButt.setId(buttFwd);
		j_fwdButt.setOnClickListener(this);
		j_fwdButt.setText("Fwd");
		j_fwdButt.setTextSize(horText);
		j_fwdButt.setTypeface(Typeface.create("arial", Typeface.BOLD));
		RelativeLayout.LayoutParams aka2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		aka2.addRule(RelativeLayout.RIGHT_OF, buttBack);
		aka2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		jMenuBot.addView(j_fwdButt, aka2);

		// Reload (Load) Button //
		j_loadButt = new Button(this);
		j_loadButt.setId(buttLoad);
		j_loadButt.setOnClickListener(this);
		j_loadButt.setText("Rld");
		j_loadButt.setTextSize(horText);
		j_loadButt.setTypeface(Typeface.create("arial", Typeface.BOLD));
		RelativeLayout.LayoutParams aka3 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		aka3.addRule(RelativeLayout.RIGHT_OF, buttFwd);
		aka3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		jMenuBot.addView(j_loadButt, aka3);

		// Stop Button //
		j_stopButt = new Button(this);
		j_stopButt.setId(buttStop);
		j_stopButt.setOnClickListener(this);
		j_stopButt.setText("Stop");
		j_stopButt.setTextSize(horText);
		j_stopButt.setTypeface(Typeface.create("arial", Typeface.BOLD));
		RelativeLayout.LayoutParams aka4 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		aka4.addRule(RelativeLayout.RIGHT_OF, buttLoad);
		aka4.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		jMenuBot.addView(j_stopButt, aka4);

		// Status //
		j_tv = new TextView(this);
		j_tv.setText("Status");
		j_tv.setTextColor(Color.rgb(255, 255, 255));
		j_tv.setTextSize(horText);
		j_tv.setTypeface(Typeface.create("arial", Typeface.BOLD));
		RelativeLayout.LayoutParams papaTV = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		papaTV.addRule(RelativeLayout.RIGHT_OF, buttStop);
		papaTV.addRule(RelativeLayout.CENTER_VERTICAL);
		jMenuBot.addView(j_tv, papaTV);

		/**
		 * Layout WebView
		 */

		j_web = new WebView(this);
		j_web.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		RelativeLayout.LayoutParams webParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		webParams.addRule(RelativeLayout.ABOVE, jMenuBot.getId());
		webParams.addRule(RelativeLayout.BELOW, jMenu.getId());
		j_panel.addView(j_web, webParams);

		/**
		 * Enable JavaScript and setup WebViewClient
		 */

		j_web.getSettings().setJavaScriptEnabled(true);
		j_web.setWebViewClient(new MyWebViewClient());

		/**
		 * Set starting page for my webView
		 */

		if (j_PageName != null)
			// IF CRASH, FIX THIS ***********
			// j_web.loadUrl(j_PageName);
			j_web.loadUrl("file:///android_asset/html/index.html");

		/**
		 * Define the JavaScript interface for the WebView and setting the HOOK
		 * to HTMLOUT
		 */
		j_web.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT");

		/**
		 * Register the WebChromeClient to assist with alerts and debugging
		 */
		j_web.setWebChromeClient(new MyWebChromeClient());

		setContentView(j_panel);

	}

	/**
	 * Build the Interface Class. The interface is a POJO object which will be
	 * accessible from the Javascript layer using HTMLOUT
	 **/

	final class JavaScriptInterface {
		@JavascriptInterface
		public String callMe(String param1, String param2) {
			// Generates a return value
			String toastValue = param1 + "," + param2;
			// Setup Toast
			Toast toast = Toast.makeText(MainActivity.this, toastValue,
					Toast.LENGTH_LONG);
			// Show Toast
			toast.show();
			return toastValue;
		}

		public void showHTML(String html) {
			j_HTMLsize = 0;
			if (html != null) {
				j_HTMLsize = html.length();
				Log.d(LOG_TAG, "HTML content is: " + html + "\nSize "
						+ j_HTMLsize + " bytes");
			}
		}
	}

	/**
	 * 
	 * My WebViewClient will handle all calls from the WebView natively instead
	 * of using Android browser
	 * 
	 **/

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(LOG_TAG, "onPageStarted");
			j_tv.setText("Loading.....");

			// Enable stop button while page is loading //
			j_stopButt.setEnabled(true);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.d(LOG_TAG, "onPageFinished");
			j_tv.setText("Ready");

			// Disable stop button if page finishes loading //
			j_stopButt.setEnabled(false);

			// Use the interface to inject html
			// j_web.loadUrl("file:///android_asset/html/index.html");
			j_web.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

			// If history exists, enable prev/next buttons //
			j_backButt.setEnabled(j_web.canGoBack());
			j_fwdButt.setEnabled(j_web.canGoForward());
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	/**
	 * Used for calling 'alert' from Javascript. Also good for debugging the
	 * javascript
	 */
	class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Log.d("JavaScript Bridge", message);
			result.confirm();
			return true;
		}
	}

	@Override
	public void onClick(View arg0) {

		int id = arg0.getId();

		// Close app if cancel is pressed //
		// if (id == backID) finish();
		switch (id) {
		case backID:
			finish();
			break;
		case buttGo:
			j_PageName = j_addr.getText().toString();
			Log.d(LOG_TAG, "Go for page:" + j_PageName);
			if (j_PageName != null)
				j_web.loadUrl(j_PageName);
			break;
		case buttBack:
			Log.d(LOG_TAG, "Go Back");
			j_web.goBack();
			break;
		case buttFwd:
			Log.d(LOG_TAG, "Go Forward");
			j_web.goForward();
			break;
		case buttLoad:
			Log.d(LOG_TAG, "Reload page");
			j_web.reload();
			break;
		case buttStop:
			Log.d(LOG_TAG, "Stop loading page");
			j_web.stopLoading();
			break;
		}

	}

}