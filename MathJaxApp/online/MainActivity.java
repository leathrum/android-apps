package edu.jsu.leathrum.android.mathjaxapp.online;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.webkit.*;
import android.text.method.*;
import android.text.*;
import android.content.*;
import android.net.*;
import java.net.*;

// support library v4 classes FragmentActivity and DialogFragment used here
// with method getSupportFragmentManager() in FragmentActivity
// to keep dialog display compatible with older API devices
// for Honeycomb or later only compile, use Activity, standard DialogFragment
// and method getFragmentManager() in Activity

public class MainActivity extends android.support.v4.app.FragmentActivity // Activity
implements View.OnClickListener
{
	
	private String doubleEscapeTeX(String s) {
		String t="";
		for (int i=0; i<s.length(); i++) {
			if (s.charAt(i) == '\'') t += '\\';
			if (s.charAt(i) != '\n') t += s.charAt(i);
			if (s.charAt(i) == '\\') t += "\\";
		}
		return t;
	}
	
	private int exampleIndex = 0;
	
	private String getExample(int index) {
		return getResources().getStringArray(R.array.tex_examples)[index];
	}

	public void onClick(View v) {
		if (v == findViewById(R.id.button2)) {
			WebView w = (WebView) findViewById(R.id.webview);
			EditText e = (EditText) findViewById(R.id.edit);
			w.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["+doubleEscapeTeX(e.getText().toString())+"\\\\]';");
			w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
		}
		else if (v == findViewById(R.id.button3)) {
			WebView w = (WebView) findViewById(R.id.webview);
			EditText e = (EditText) findViewById(R.id.edit);
			e.setText("");
			w.loadUrl("javascript:document.getElementById('math').innerHTML='';");
			w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
		}
		else if (v == findViewById(R.id.button4)) {
			WebView w = (WebView) findViewById(R.id.webview);
			EditText e = (EditText) findViewById(R.id.edit);
			e.setText(getExample(exampleIndex++));
			if (exampleIndex>getResources().getStringArray(R.array.tex_examples).length-1) exampleIndex=0;
			w.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["+doubleEscapeTeX(e.getText().toString())+"\\\\]';");
			w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
		}
		else if (v == findViewById(R.id.button5)) {
			// MJURLDialog.show(getFragmentManager(),"mj_url");
			MJURLDialog.show(getSupportFragmentManager(),"mj_url");			
		}		
	}
	
	SharedPreferences prefs;
	final int MJ_DIALOG_ID = 1;
	
	public void setURL(String newurl) {
		SharedPreferences.Editor editprefs = prefs.edit();
		editprefs.putString("MathJaxURL", newurl);	
		editprefs.commit();
		WebView w = (WebView) findViewById(R.id.webview);
		w.loadDataWithBaseURL("http://bar","<script type='text/javascript' "
							  +"src='"+newurl+"'"
							  +"></script><span id='math'></span>","text/html","utf-8","");
	}
	
	public void setDefaultURL() {
		setURL(getResources().getString(R.string.mjurl));
	}
	
	public class URLDialog extends android.support.v4.app.DialogFragment { // DialogFragment

	    public URLDialog() { }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        final View view = inflater.inflate(R.layout.url_dialog, container);
	        getDialog().setTitle(R.string.urltitle);
	        
			EditText e = (EditText) view.findViewById(R.id.editurl);
			e.setText(prefs.getString("MathJaxURL",getResources().getString(R.string.mjurl)));
			Button b = (Button) view.findViewById(R.id.okbutton);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					EditText e = (EditText) view.findViewById(R.id.editurl);
					setURL(e.getText().toString());
					getDialog().dismiss();
				}
			});
			b = (Button) view.findViewById(R.id.defaultbutton);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					EditText e = (EditText) view.findViewById(R.id.editurl);
					e.setText(getResources().getString(R.string.mjurl));
					setDefaultURL();
				}
			});			
			b = (Button) view.findViewById(R.id.localbutton);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					EditText e = (EditText) view.findViewById(R.id.editurl);
					e.setText(getResources().getString(R.string.loopback));
					setURL(getResources().getString(R.string.loopback));
				}
			});						

	        return view;
	    }
	}
	
	URLDialog MJURLDialog = new URLDialog();

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		WebView w = (WebView) findViewById(R.id.webview);
		w.getSettings().setJavaScriptEnabled(true);
		w.getSettings().setBuiltInZoomControls(true);
		EditText e = (EditText) findViewById(R.id.edit);
		e.setBackgroundColor(Color.LTGRAY);
		e.setTextColor(Color.BLACK);
		e.setText("");
		Button b = (Button) findViewById(R.id.button2);
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.button3);
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.button4);
		b.setOnClickListener(this);
		b = (Button) findViewById(R.id.button5);
		b.setOnClickListener(this);
		TextView t = (TextView) findViewById(R.id.textview3);
		t.setMovementMethod(LinkMovementMethod.getInstance());
		t.setText(Html.fromHtml(t.getText().toString()));
		prefs = getPreferences(MODE_PRIVATE);
		if (prefs.getString("MathJaxURL","").length() == 0) {
			SharedPreferences.Editor editprefs = prefs.edit();
			editprefs.putString("MathJaxURL", getResources().getString(R.string.mjurl));	
			editprefs.commit();
		}
		setURL(prefs.getString("MathJaxURL", getResources().getString(R.string.mjurl)));
		
	}
}

