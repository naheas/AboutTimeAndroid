package com.example.abouttime;

import model.ATDate;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class EventAddActivity extends Activity {

	int position = 0;
	int selected_year = 2000;
	int selected_month = 1;
	int selected_day = 1;
	int selected_start_hour = 0;
	EditText summary;
	EditText start_date_et;
	EditText start_time_et;
	EditText end_date_et;
	EditText end_time_et;
	DateTime start_datetime;
	DateTime end_datetime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_eventadd);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.submitbar);
		Button submit_btn = (Button) findViewById(R.id.btn_submitbar_submit);				
		
		Intent intent_get = getIntent();
		position = intent_get.getIntExtra("position", position);
		selected_year = intent_get.getIntExtra("year", selected_year);
		selected_month = intent_get.getIntExtra("month", selected_month);
		selected_day = intent_get.getIntExtra("day", selected_day);
		selected_start_hour = intent_get.getIntExtra("start_hour", selected_start_hour);

		summary = (EditText) findViewById(R.id.et_eventadd_summary);
		start_date_et = (EditText) findViewById(R.id.et_eventadd_start_date);
		start_time_et = (EditText) findViewById(R.id.et_eventadd_start_time);
		end_date_et = (EditText) findViewById(R.id.et_eventadd_end_date);
		end_time_et = (EditText) findViewById(R.id.et_eventadd_end_time);
		
		start_datetime = new DateTime(selected_year, selected_month, selected_day, selected_start_hour, 0);
		end_datetime = new DateTime(selected_year, selected_month, selected_day, selected_start_hour + 1, 0);
		
		start_date_et.setText(stringOfDateFormat(start_datetime));
		start_time_et.setText(stringOfTimeFormat(start_datetime));
		end_date_et.setText(stringOfDateFormat(end_datetime));
		end_time_et.setText(stringOfTimeFormat(end_datetime));
				
		submit_btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = getIntent(); 
		        if(intent != null){
		        	intent.putExtra("position", position);
		            intent.putExtra("summary", summary.getText().toString());
		        }
		        setResult(RESULT_OK, intent);
		        finish();				
			}
		});
		
		start_date_et.setOnClickListener(new EditText.OnClickListener() {
			public void onClick(View v) {
				final DatePickerDialog mDatePickerDialog = new DatePickerDialog(EventAddActivity.this, new DatePickerDialog.OnDateSetListener() {
		            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		            	start_datetime = new DateTime(year, monthOfYear + 1, dayOfMonth, start_datetime.getHourOfDay(), start_datetime.getMinuteOfHour());
		                start_date_et.setText(stringOfDateFormat(start_datetime));
		            }
		        }, start_datetime.getYear(), start_datetime.getMonthOfYear() - 1, start_datetime.getDayOfMonth());
				
				mDatePickerDialog.setTitle("날짜 설정 (" + (new ATDate(start_datetime)).getDayOfWeekKo() + ")");
				
				DatePicker mDatePicker = mDatePickerDialog.getDatePicker();
				mDatePicker.init(start_datetime.getYear(), start_datetime.getMonthOfYear() - 1, start_datetime.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
			        public void onDateChanged(DatePicker birthDayDatePicker, int year, int monthOfYear, int dayOfMonth) {
			        	mDatePickerDialog.setTitle("날짜 설정 (" + (new ATDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0))).getDayOfWeekKo() + ")");
			        }
			    });
				
				mDatePickerDialog.show();
			}
		});
		
		start_time_et.setOnClickListener(new EditText.OnClickListener() {
			public void onClick(View v) {
				TimePickerDialog mTimePicker = new TimePickerDialog(EventAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    	start_datetime = new DateTime(start_datetime.getYear(), start_datetime.getMonthOfYear(), start_datetime.getDayOfMonth(), selectedHour, selectedMinute);
                    	start_time_et.setText(stringOfTimeFormat(start_datetime));
                    }
                }, start_datetime.getHourOfDay(), start_datetime.getMinuteOfHour(), false);
                mTimePicker.setTitle("시간 설정");
                mTimePicker.show();
			}
		});
		
		end_date_et.setOnClickListener(new EditText.OnClickListener() {
			public void onClick(View v) {
				final DatePickerDialog mDatePickerDialog = new DatePickerDialog(EventAddActivity.this, new DatePickerDialog.OnDateSetListener() {
		            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		            	end_datetime = new DateTime(year, monthOfYear + 1, dayOfMonth, end_datetime.getHourOfDay(), end_datetime.getMinuteOfHour());
		                end_date_et.setText(stringOfDateFormat(end_datetime));
		            }
		        }, end_datetime.getYear(), end_datetime.getMonthOfYear() - 1, end_datetime.getDayOfMonth());
				
				mDatePickerDialog.setTitle("날짜 설정 (" + (new ATDate(end_datetime)).getDayOfWeekKo() + ")");
				
				DatePicker mDatePicker = mDatePickerDialog.getDatePicker();
				mDatePicker.init(end_datetime.getYear(), end_datetime.getMonthOfYear() - 1, end_datetime.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
			        public void onDateChanged(DatePicker birthDayDatePicker, int year, int monthOfYear, int dayOfMonth) {
			        	mDatePickerDialog.setTitle("날짜 설정 (" + (new ATDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0))).getDayOfWeekKo() + ")");
			        }
			    });
				
				mDatePickerDialog.show();
			}
		});
		
		end_time_et.setOnClickListener(new EditText.OnClickListener() {
			public void onClick(View v) {
				TimePickerDialog mTimePicker = new TimePickerDialog(EventAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    	end_datetime = new DateTime(end_datetime.getYear(), end_datetime.getMonthOfYear(), end_datetime.getDayOfMonth(), selectedHour, selectedMinute);
                    	end_time_et.setText(stringOfTimeFormat(end_datetime));
                    }
                }, end_datetime.getHourOfDay(), end_datetime.getMinuteOfHour(), false);
                mTimePicker.setTitle("시간 설정");
                mTimePicker.show();
			}
		});
	}
	
	private String stringOfDateFormat(DateTime dt){
		return dt.getYear() + "년 " + dt.getMonthOfYear() + "월 " + dt.getDayOfMonth() + "일 " + (new ATDate(dt)).getDayOfWeekKoShort();
	}
	
	private String stringOfTimeFormat(DateTime dt){
		int hour = dt.getHourOfDay();
		String am_or_pm = "오전";
    	if(hour >= 12) am_or_pm = "오후";
		if(hour > 12) hour = hour - 12;
    	if(hour == 0) hour = hour + 12;
    	return am_or_pm + " " + hour + ":" + dt.toString("mm");
	}
}
