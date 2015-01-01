package com.example.abouttime;


import model.ATCalendar;
import model.ATDate;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import view.EndlessGridView;

import com.example.abouttime.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	final int FRICTION_SCALE_FACTOR = 100;
	
	EndlessGridView gv_calendar;
	DateAdapter adapter;
	ATCalendar date_array = null;
	DateTime dt_today;
	TextView tv_year_month;
    int prev_month = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		TextView title = (TextView) findViewById(R.id.tv_titlebar);
		title.setText(R.string.app_name);

		/* Dont know if we need this */
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		dt_today = new DateTime();
		
		prev_month = dt_today.getMonthOfYear();
				
		tv_year_month = (TextView) findViewById(R.id.tv_titlebar_right);
		tv_year_month.setText(dt_today.getYear() + "년 " + dt_today.getMonthOfYear() + "월");

		date_array = (new ATCalendar(dt_today.minusMonths(2))).appendCalendar(new ATCalendar(dt_today.minusMonths(1))).appendCalendar(new ATCalendar(dt_today)).appendCalendar(new ATCalendar(dt_today.plusMonths(1))).appendCalendar(new ATCalendar(dt_today.plusMonths(2)));		
		
		gv_calendar = (EndlessGridView) findViewById(R.id.gv_main);
		adapter = new DateAdapter(this);
		gv_calendar.setAdapter(adapter);
		
		gv_calendar.setSelection(68);
		gv_calendar.setFriction(ViewConfiguration.getScrollFriction() * FRICTION_SCALE_FACTOR);
		
		gv_calendar.setOnScrollListener(new EndlessScrollListener());
		gv_calendar.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {							
	            final Intent intent = new Intent(getApplicationContext(), DayActivity.class);
	            intent.putExtra("year", adapter.getItem(position).getYear());
	            intent.putExtra("month", adapter.getItem(position).getMonth());
	            intent.putExtra("day", adapter.getItem(position).getDay());
	            startActivity(intent);
			}
	    });
	}
	
	class DateAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		
		public DateAdapter(Context context){
			this.context = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public int getCount() {
			return date_array.size();
		}
		
		public ATDate getItem(int position) {
			return date_array.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gv_main, parent, false);
			}

			LinearLayout ll =  (LinearLayout)convertView.findViewById(R.id.ll_gv_main);
			TextView tv = (TextView)convertView.findViewById(R.id.tv_gv_main);
			
			ATDate date = date_array.get(position);
			if(date != null){
				tv.setText(String.valueOf(date.getDay()));
				if(DateTimeComparator.getDateOnlyInstance().compare(dt_today, date.toDateTime()) == 0){
					tv.setTextColor(Color.parseColor("#4FBCFF"));
					tv.setTypeface(null, Typeface.BOLD);
				}else{
					tv.setTextColor(Color.parseColor("#767676"));
					tv.setTypeface(null, Typeface.NORMAL);
				}
				if(date.getMonth() == prev_month){
					ll.setBackgroundResource(R.drawable.itemwhitebg);
				}else{
					ll.setBackgroundResource(R.drawable.itemgraybg);
				}
			}else{
				tv.setText("");
				ll.setBackgroundColor(Color.parseColor("#ABABAB"));
			}
			
			return convertView;
		}
		
		private void dataChange(){
			adapter.notifyDataSetChanged();
	    }
	}
	
	public class EndlessScrollListener implements OnScrollListener {

	    private final int visibleThreshold = 60;
	    private int previousTotal = 0;
	    private boolean loading = true;
	    int firstVisibleItem = 0;

	    public EndlessScrollListener() {
	    }
	    
	    public void onScroll(AbsListView view, int firstVisibleItem,
	            int visibleItemCount, int totalItemCount) {
	        if (loading) {
	            if (totalItemCount > previousTotal) {
	                loading = false;
	                previousTotal = totalItemCount;
	            }
	        }else{
	        	this.firstVisibleItem  = firstVisibleItem;
	        	
	        	int middle_vis_item = firstVisibleItem + view.getChildCount() / 2;
	        	int present_month = date_array.get(middle_vis_item).getMonth();
				if(prev_month != present_month){
					tv_year_month.setText(date_array.get(middle_vis_item).getYear() + "년 " + present_month + "월");
					prev_month = present_month;
					adapter.notifyDataSetChanged();
				}
	        }
	    }

	    public void onScrollStateChanged(AbsListView view, int scrollState) {
	    	switch(scrollState){
	    		case SCROLL_STATE_IDLE:
	    			if(!loading){
	    				if(firstVisibleItem < visibleThreshold){
	    					loading = true;
	    					
	    					int first_vis_pos = gv_calendar.getFirstVisiblePosition();
	    					View firstVisView = gv_calendar.getChildAt(0);
	    					int top_margin = firstVisView != null ? firstVisView.getTop() : 0;
	    					int prev_date_array_size = date_array.size();
	    					
	    					date_array = (new ATCalendar(date_array.getBeforeDateTime())).appendCalendar(date_array);
	    					gv_calendar.setBlockLayoutChildren(true, first_vis_pos + date_array.size() - prev_date_array_size, top_margin);
	    					adapter.notifyDataSetChanged();	 
	    				}else if(firstVisibleItem > (previousTotal - 2 * visibleThreshold)){
	    					loading = true;
	    					
	    					date_array.appendCalendar(new ATCalendar(date_array.getAfterDateTime()));
	    					adapter.notifyDataSetChanged();
	    				}
	    			}
    			break;
	    	}
	    }
	}
}


