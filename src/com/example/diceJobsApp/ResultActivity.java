package com.example.dicejobs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ResultActivity extends Activity implements OnItemClickListener {
	
	TextView tv;
	ListView lv;
	CustomAdapter adap;
	JSONObject result;
	int count;
	ArrayList<Job> objects;
	String token;

	/*show job details when a specific job is clicked in the ListView*/
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String htmlstring = objects.get(position).description;
		Intent intent = new Intent(this,JobDetails.class);
		intent.putExtra("desc",htmlstring);
		startActivity(intent);
	}
	
	
	protected void onCreate(Bundle savedInstanceState){
		
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_result);
			objects = new ArrayList<Job>();
			try {
				result = new JSONObject(getIntent().getStringExtra("qresult"));
				token = getIntent().getStringExtra("accToken");
			} catch (Exception e) {}
			fillList();
			tv = (TextView) findViewById(R.id.header);
			lv = (ListView) findViewById(R.id.listV);
			adap = new CustomAdapter(this,objects);
			count = 0;
			lv.setAdapter(adap);
			lv.setOnItemClickListener(this);
			
	}

	private CharSequence toString(int size) {
		// TODO Auto-generated method stub
		return null;
	}

	/*fill an ArrayList wit Job objects*/
	private void fillList(){
		try{
			JSONArray jarray = result.getJSONArray("searchResults");
			for(int i=0; i<jarray.length(); i++){
				Job temp = new Job(); 
				JSONObject searchRes = jarray.getJSONObject(i);
				JSONObject company = searchRes.getJSONObject("company");
				JSONObject position = searchRes.getJSONObject("position");
				JSONObject location = position.getJSONObject("location");
				temp.id = searchRes.getString("id");
				temp.description = searchRes.getString("description");
				temp.title = position.getString("title");
				temp.company = company.getString("name");
				temp.city = location.getString("city");
				temp.state = location.getString("region");
				objects.add(temp);
			}
		}
		catch(Exception e){}
	}


	/*an adapter used to display multiple TextViews in a ListView row*/
	private class CustomAdapter extends BaseAdapter{

		private ArrayList<Job> objects;
		Context context;

		CustomAdapter(Context context,ArrayList<Job> objects){
			this.context = context;
			this.objects = objects;
		}

		private class ViewHolder{
			public TextView title;
			public TextView company;
			public TextView location;
		}

		public int getCount(){
			return objects.size();
		}

		public Job getItem(int position) {
			return objects.get(position);
		}

		public long getItemId(int position) {
			return objects.indexOf(getItem(position));
		}

		public View getView(int position, View convertView, ViewGroup parent){

			ViewHolder vHold = null;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			if(convertView == null){
				convertView  = inflater.inflate(R.layout.list_views,null);
				vHold = new ViewHolder();
				vHold.title = (TextView) convertView.findViewById(R.id.titleView);
				vHold.company = (TextView) convertView.findViewById(R.id.companyView);
				vHold.location = (TextView) convertView.findViewById(R.id.locationView);
				convertView.setTag(vHold);
			}
			else 
				vHold = (ViewHolder) convertView.getTag();

			Job j = (Job) getItem(position);
			vHold.title.setText(j.title);
			vHold.company.setText(j.company);
			vHold.location.setText(j.city+", "+j.state);
			return convertView;
		}
	}
}
