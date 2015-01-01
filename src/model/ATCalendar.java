package model;

import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;

public class ATCalendar {
	private ArrayList<ATDate> date_array;
	private int first_year;
	private int first_month;
	private int last_year;
	private int last_month;
	
	public ATCalendar(DateTime dt){
		this.date_array = new ArrayList<ATDate>();
		
		int firstDayOfWeek = dt.withDayOfMonth(1).getDayOfWeek();
		int lastDayOfMonth = dt.dayOfMonth().withMaximumValue().getDayOfMonth();
		this.first_month = this.last_month = dt.getMonthOfYear();
		this.first_year = this.last_year = dt.getYear();
		
		for(int i = 0; i < firstDayOfWeek; i++) {
			date_array.add(null);
		}
		
		for(int i = 0; i < lastDayOfMonth; i++) {
			date_array.add(new ATDate(first_year, last_month, i + 1));
		}
		
		for(int i = 0; ((lastDayOfMonth + firstDayOfWeek + i) % 7) != 0; i++) {
			date_array.add(null);
		}
	}

	public ATCalendar appendCalendar(ATCalendar bottom_calendar){
		this.last_year = bottom_calendar.last_year;
		this.last_month = bottom_calendar.last_month;
		
		ArrayList<ATDate> bottom_date_array = bottom_calendar.getDate_array();
		
		date_array.removeAll(Collections.singleton(null));
		bottom_date_array.removeAll(Collections.singleton(null));
		date_array.addAll(bottom_date_array);
		
		DateTime first_dt = new DateTime(first_year, first_month, 1, 0, 0);
		DateTime last_dt = new DateTime(last_year, last_month, 1, 0, 0);
		int f_f_day_of_week = first_dt.withDayOfMonth(1).getDayOfWeek();
		int l_f_day_of_week = last_dt.withDayOfMonth(1).getDayOfWeek();
		int l_l_day_of_month = last_dt.dayOfMonth().withMaximumValue().getDayOfMonth();
		
		for(int i = 0; i < (f_f_day_of_week % 7); i++) {
			date_array.add(0, null);
		}
		
		for(int i = 0; ((l_l_day_of_month + l_f_day_of_week + i) % 7) != 0; i++) {
			date_array.add(null);
		}
		
		return this;
	}
	
	public DateTime getBeforeDateTime(){
		return (new DateTime(first_year, first_month, 1, 0, 0)).minusMonths(1);
	}
	
	public DateTime getAfterDateTime(){
		return (new DateTime(last_year, last_month, 1, 0, 0)).plusMonths(1);
	}
	
	public ATDate get(int index){
		return date_array.get(index);
		
	}
	
	public boolean add(ATDate e){
		return date_array.add(e);
	}
	
	public int size(){
		return date_array.size();
	}
	
	public int getFirst_year() {
		return first_year;
	}
	public void setFirst_year(int first_year) {
		this.first_year = first_year;
	}
	
	public int getFirst_month() {
		return first_month;
	}
	public void setFirst_month(int first_month) {
		this.first_month = first_month;
	}
	
	public int getLast_year() {
		return last_year;
	}
	public void setLast_year(int last_year) {
		this.last_year = last_year;
	}
	
	public int getLast_month() {
		return last_month;
	}
	public void setLast_month(int last_month) {
		this.last_month = last_month;
	}

	public ArrayList<ATDate> getDate_array() {
		return date_array;
	}
	public void setDate_array(ArrayList<ATDate> date_array) {
		this.date_array = date_array;
	}
}
