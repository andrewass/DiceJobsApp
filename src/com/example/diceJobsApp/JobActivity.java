package com.example.dicejobs;
import java.util.concurrent.CyclicBarrier;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JobActivity extends Activity {

	Button search;
	EditText searchQ;
	TextView header,pre_search,errormsg;
	String token, searchString;
	Netcom netcom;
	CyclicBarrier cb = new CyclicBarrier(2);
	JSONObject qresult;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		netcom = new Netcom();
		setContentView(R.layout.activity_job);
		search = (Button) findViewById(R.id.searchButton);
		searchQ = (EditText) findViewById(R.id.searchQ);
		header = (TextView) findViewById(R.id.header);
		pre_search = (TextView) findViewById(R.id.pre_search);

		/*display the search results based on keyword(s) on button click*/
		search.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				try{
					searchString = searchQ.getText().toString();
					new Thread(new Worker(searchString)).start();
					cb.await();	
					Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
					intent.putExtra("accToken",token);
					intent.putExtra("qresult",qresult.toString());
					startActivity(intent);
				}
				catch(Exception e){}
			}
		});
	}


	private class Worker implements Runnable {

		String search;
		
		Worker(String search){
			this.search = search;
		}
		
		public void run() {
			try {
				qresult = netcom.findJobs(search);
				cb.await();
			}
			catch (Exception e){}
		}		
	}
}


