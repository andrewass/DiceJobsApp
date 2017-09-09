package com.example.dicejobs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class JobDetails extends Activity {

	String descrip;
	TextView tv;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		tv = (TextView) findViewById(R.id.jobDesc);
		descrip = getIntent().getStringExtra("desc");
		tv.setText(convertHTML(descrip));
	}

	/*converts the html-text in the job description to plain text*/
	private String convertHTML(String input){
		Document document = Jsoup.parse(input);
		document.select("br").append("\\n");
		document.select("p").prepend("\\n\\n");
		return document.text().replaceAll("\\\\n", "\n");
	}
}
