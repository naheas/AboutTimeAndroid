package model;

import org.joda.time.DateTime;

public class ATDate {
	private int year;
	private int month;
	private int day;
	
    final String [] day_of_week_ko = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
	
	public ATDate(DateTime dt){
		this(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
	}
	
	public ATDate(int year, int month, int day){
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	public DateTime toDateTime(){
		return new DateTime(year, month, day, 0, 0);
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	public int getDayOfWeek() {
		return (new DateTime(year, month, day, 0, 0)).getDayOfWeek();
	}
	
	public String getDayOfWeekKo(){
		return day_of_week_ko[(new DateTime(year, month, day, 0, 0)).getDayOfWeek() - 1];
	}
	
	public String getDayOfWeekKoShort(){
		return day_of_week_ko[(new DateTime(year, month, day, 0, 0)).getDayOfWeek() - 1].substring(0, 1);
	}
}
