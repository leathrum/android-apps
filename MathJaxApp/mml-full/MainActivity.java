package edu.jsu.leathrum.android.mathjaxapp.mml.full;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.content.res.*;
import android.webkit.*;
import android.text.method.*;
import android.text.*;

public class MainActivity extends Activity
implements View.OnClickListener
{

	private String doubleEscapeTeX(String s) {
		String t="";
		for (int i=0; i < s.length(); i++) {
			if (s.charAt(i) == '\'') t += '\\';
			if (s.charAt(i) != '\n') t += s.charAt(i);
			if (s.charAt(i) == '\\') t += "\\";
		}
		return t;
	}

	private int exampleIndex = 0;

	private String getExample(int index) {
		return getResources().getStringArray(R.array.mml_examples)[index];
	}

	public void onClick(View v) {
		if (v == findViewById(R.id.button2)) {
			WebView w = (WebView) findViewById(R.id.webview);
			EditText e = (EditText) findViewById(R.id.edit);
			w.loadUrl("javascript:document.getElementById('math').innerHTML='"
			+"<math xmlns=\"http://www.w3.org/1998/Math/MathML\">"
			+"<mstyle displaystyle=\"true\">"
			    +doubleEscapeTeX(e.getText().toString())+"</mstyle></math>';");
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
			if (exampleIndex > getResources().getStringArray(R.array.mml_examples).length-1) 
				exampleIndex=0;
			w.loadUrl("javascript:document.getElementById('math').innerHTML='"
			+"<math xmlns=\"http://www.w3.org/1998/Math/MathML\">"
			+"<mstyle displaystyle=\"true\">"
			          +doubleEscapeTeX(e.getText().toString())
					  +"</mstyle></math>';");
			w.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
		}
	}

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		WebView w = (WebView) findViewById(R.id.webview);
		w.getSettings().setJavaScriptEnabled(true);
		w.getSettings().setBuiltInZoomControls(true);
		w.loadDataWithBaseURL("http://bar", "<script type='text/x-mathjax-config'>"
		                      +"MathJax.Hub.Config({ " 
							  +"showMathMenu: false, "
							  +"jax: ['input/MathML','output/HTML-CSS'], " // output/SVG
							  +"extensions: ['mml2jax.js'], " 
							  +"TeX: { extensions: ['noErrors.js','noUndefined.js'] }, "
							  //+"'SVG' : { blacker: 30, "
							  // +"styles: { path: { 'shape-rendering': 'crispEdges' } } } "
							  +"});</script>"
		                      +"<script type='text/javascript' "
							  +"src='file:///android_asset/MathJax/MathJax.js'"
							  +"></script><span id='math'></span>","text/html","utf-8","");
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
		TextView t = (TextView) findViewById(R.id.textview3);
		t.setMovementMethod(LinkMovementMethod.getInstance());
		t.setText(Html.fromHtml(t.getText().toString()));	

	}
}

