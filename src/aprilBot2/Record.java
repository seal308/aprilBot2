package aprilBot2;

public class Record {
	String tagTime;
	String show;
	String date;
	String time;
	String name;
	//String img;
	
	//public Record(Object tagTime, Object show, Object date, Object time, Object name, Object img)
	public Record(Object tagTime, Object show, Object date, Object time, Object name)
	{
		this.tagTime = tagTime.toString();
		this.show = show.toString();
		this.show = show.toString();
		this.date = date.toString();
		this.time = time.toString();
		this.name = name.toString();
		//this.img = img.toString();
	}
}
