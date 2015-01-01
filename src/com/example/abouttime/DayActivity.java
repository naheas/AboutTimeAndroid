package com.example.abouttime;


import java.util.ArrayList;

import model.ATDate;
import model.ATHour;

import org.joda.time.DateTime;

import view.EndlessListView;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class DayActivity extends Activity {

	final int FRICTION_SCALE_FACTOR = 90;
    int item_height = 0;    
    int child_0_top = 0;
    int child_clicked_top = 0;
	
	RelativeLayout rl_day = null;
	EndlessListView lv_day = null;
	HourAdapter adapter = null;
	ArrayList<ATHour> hour_array = new ArrayList<ATHour>();
	TextView tv_year_month_day;
	int selected_year = 2000;
	int selected_month = 1;
	int selected_day = 1;
	int prev_day = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_day);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		Intent intent_get = getIntent();
		selected_year = intent_get.getIntExtra("year", selected_year);
		selected_month = intent_get.getIntExtra("month", selected_month);
		selected_day = intent_get.getIntExtra("day", selected_day);
		DateTime dt_selected = new DateTime(selected_year, selected_month, selected_day, 0, 0);

		tv_year_month_day = (TextView) findViewById(R.id.tv_titlebar);
		tv_year_month_day.setText(selected_year + "년 " + selected_month + "월 " + selected_day + "일 " + (new ATDate(dt_selected)).getDayOfWeekKo());

		for(int i = 0; i < 24; i++)	hour_array.add(new ATHour(dt_selected.minusDays(1), i));
		for(int i = 0; i < 24; i++)	hour_array.add(new ATHour(dt_selected, i));
		for(int i = 0; i < 24; i++)	hour_array.add(new ATHour(dt_selected.plusDays(1), i));
		
		lv_day = (EndlessListView) findViewById(R.id.lv_day);
		adapter = new HourAdapter(this);
		lv_day.setAdapter(adapter);
		
		lv_day.setSelection(24);
		lv_day.setFriction(ViewConfiguration.getScrollFriction() * FRICTION_SCALE_FACTOR);
		
		lv_day.setOnScrollListener(new EndlessScrollListener());
		lv_day.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {							
	            final Intent intent = new Intent(getApplicationContext(), EventAddActivity.class);
	            child_0_top = lv_day.getChildAt(0).getTop();
	            child_clicked_top = view.getTop();
	            intent.putExtra("position", position);
	            intent.putExtra("year", adapter.getItem(position).getYear());
	            intent.putExtra("month", adapter.getItem(position).getMonth());
	            intent.putExtra("day", adapter.getItem(position).getDay());
	            intent.putExtra("start_hour", adapter.getItem(position).getStart_hour());
	            startActivityForResult(intent, 1);
			}
	    });
		
		rl_day = (RelativeLayout) findViewById(R.id.rl_day);
	}
	
	class HourAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		
		public HourAdapter(Context context){
			this.context = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public int getCount() {
			return hour_array.size();
		}
		
		public ATHour getItem(int position) {
			return hour_array.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.lv_day, parent, false);
			}

			LinearLayout ll =  (LinearLayout)convertView.findViewById(R.id.ll_lv_day);
			TextView tv = (TextView)convertView.findViewById(R.id.tv_lv_day);
					
			ATHour athour = hour_array.get(position);
			
			int hour = athour.getStart_hour();				
			if(hour <= 0) hour += 12;
			if(hour > 12) hour -= 12;
			tv.setText(String.valueOf(hour));
			
			if(athour.getStart_hour() >= 12){
				tv.setTextColor(Color.parseColor("#FFFFFF"));
				tv.setBackgroundColor(Color.parseColor("#767676"));
			}else{
				tv.setTextColor(Color.parseColor("#767676"));
				tv.setBackgroundColor(Color.parseColor("#CCCCCC"));
			}
			if(athour.getDay() == prev_day){
				ll.setBackgroundResource(R.drawable.itemwhitebg);
			}else{
				ll.setBackgroundResource(R.drawable.itemgraybg);
			}
			
			return convertView;
		}
	}
	
	public class EndlessScrollListener implements OnScrollListener {

	    private final int visibleThreshold = 20;
	    private int previousTotal = 0;
	    private boolean loading = true;
	    int firstVisibleItem = 0;

	    public EndlessScrollListener() {
	    }
	    
	    public void onScroll(AbsListView view, int firstVisibleItem,
	            int visibleItemCount, int totalItemCount) {
	    	// Start - To keep relativelayout's position synchronized with listview
	    	int first_vis_pos = lv_day.getFirstVisiblePosition();
			View firstVisView = lv_day.getChildAt(0);
			int top_margin = firstVisView != null ? firstVisView.getTop() : 0;
			if(item_height == 0) item_height = firstVisView != null ? firstVisView.getHeight() + 1 : 0;
			
			if(rl_day != null && item_height != 0){
				MarginLayoutParams params = (MarginLayoutParams) rl_day.getLayoutParams();
				params.topMargin = top_margin - first_vis_pos * item_height;
				rl_day.setLayoutParams(params);
			}
	    	// End - To keep relativelayout's position synchronized with listview
	    	
	        if (loading) {
	            if (totalItemCount > previousTotal) {
	                loading = false;
	                previousTotal = totalItemCount;
	            }
	        }else{
	        	this.firstVisibleItem  = firstVisibleItem;
	        	
	        	int middle_vis_item = firstVisibleItem + view.getChildCount() / 2;
	        	ATHour athour = hour_array.get(middle_vis_item);
	        	int present_day = athour.getDay();
				if(prev_day != present_day){
					tv_year_month_day.setText(athour.getYear() + "년 " + athour.getMonth() + "월 " + athour.getDay() + "일 " + athour.getDayOfWeekKo());
					prev_day = present_day;
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
	    					
	    					int first_vis_pos = lv_day.getFirstVisiblePosition();
	    					View firstVisView = lv_day.getChildAt(0);
	    					int top_margin = firstVisView != null ? firstVisView.getTop() : 0;
	    					
	    					DateTime firstDate = hour_array.get(0).toDateTime().minusDays(1);
	    					for(int i = 23; i >= 0; i--) hour_array.add(0, new ATHour(firstDate, i));
	    					lv_day.setBlockLayoutChildren(true);
	    					adapter.notifyDataSetChanged();	 
	    					lv_day.setBlockLayoutChildren(false);
	    					lv_day.setSelectionFromTop(first_vis_pos + 24, top_margin); 
	    					
	    					rl_margin_sync(item_height); // To keep relativelayout's position synchronized with listview
	    					
	    				}else if(firstVisibleItem > (previousTotal - 2 * visibleThreshold)){
	    					loading = true;
	    					DateTime lastDate = hour_array.get(hour_array.size() - 1).toDateTime().plusDays(1);
	    					for(int i = 0; i < 24; i++)	hour_array.add(new ATHour(lastDate, i));
	    					adapter.notifyDataSetChanged();
	    				}
	    			}
    			break;
	    	}
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
        	int position = data.getIntExtra("position", 0);
        	String summary = data.getStringExtra("summary");
        	int first_vis_pos = lv_day.getFirstVisiblePosition();
        	
        	TextView tv = new TextView(this);
        	tv.setText(summary + position);
        	tv.setTextColor(Color.parseColor("#FFFFFF"));
        	tv.setBackgroundColor(Color.parseColor("#4FBCFF"));
        	tv.setGravity(Gravity.CENTER);
        	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        	params.setMargins(0, child_clicked_top - child_0_top + first_vis_pos * item_height, 0, 0);
        	rl_day.addView(tv, params);        	
        }
	}   
	
	private void rl_margin_sync(int item_height){
		for (int i = 0; i < rl_day.getChildCount(); i++) {
	        View v = rl_day.getChildAt(i);
	        if (v instanceof TextView) {
	        	MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();
				params.topMargin = params.topMargin + 24 * item_height;
				v.setLayoutParams(params);
	        }
	    }
		MarginLayoutParams params = (MarginLayoutParams) rl_day.getLayoutParams();
		params.topMargin = params.topMargin - 24 * item_height;
		rl_day.setLayoutParams(params);		
	}
}
