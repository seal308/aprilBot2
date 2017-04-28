package aprilBot2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
	String tagTime;
	String show;
	String date;
	String time;
	String name;
	String img;
	Date dateVar;
	int month;
	int year;
	int day;
	int hour;
	int min;
	int seconds;
	
	public Record(Object tagTimeObj, Object showObj, Object dateObj, Object timeObj, Object nameObj, Object imgObj)
	//public Record(Object tagTime, Object show, Object date, Object time, Object name)
	{
		tagTime = tagTimeObj.toString();
		show = showObj.toString();
		date = dateObj.toString();
		time = timeObj.toString();
		name = nameObj.toString();
		img = imgObj.toString();
	
		//SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyykk:mm:ss");
		
		
		try {
			dateVar = fmt.parse(date+time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Got a parse exception");
			e.printStackTrace();
		}
		
		month = dateVar.getMonth()+1;
		year = 1900+dateVar.getYear();
		day = dateVar.getDate();
		hour = dateVar.getHours();
		min = dateVar.getMinutes();
		seconds = dateVar.getSeconds();
		
		/*
		System.out.println("Month: " + month);
		System.out.println("Year: " + year);
		System.out.println("day: " + day);
		System.out.printf("Hour %d, Minute %d, Seconds %d\n", hour, min, temp.getSeconds());
		*/
		
	}
	
	protected String printRecord()
	{
		
		return show;
	}
}
