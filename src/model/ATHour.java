package model;

import org.joda.time.DateTime;

public class ATHour extends ATDate{
	private int start_hour;
	
	public ATHour(DateTime dt, int start_hour){
		super(dt);
		this.start_hour = start_hour;
	}
	
	public ATHour(int year, int month, int day, int start_hour) {
		super(year, month, day);
		this.start_hour = start_hour;
	}

	public int getStart_hour() {
		return start_hour;
	}
	public void setStart_hour(int start_hour) {
		this.start_hour = start_hour;
	}
}
