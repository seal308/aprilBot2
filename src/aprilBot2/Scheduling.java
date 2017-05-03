package aprilBot2;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.*;

public class Scheduling {
	  
	  int counter;
	  String name;
	  List<Record> LL;
	  long startTime;
	  MessageChannel channel;
	  
	  public Scheduling(long aInitialDelay, List<Record> LL, MessageChannel channel){
	    
		this.channel = channel;
	    counter = 0;
	    fInitialDelay = aInitialDelay;
	    fScheduler = Executors.newScheduledThreadPool(NUM_THREADS); 
	    //startTime = LL.get(0).date.getTime();
	    //long seconds = TimeUnit.MILLISECONDS.toSeconds(  testRecord.date.getTime() - System.currentTimeMillis()  );
	    startTime = TimeUnit.MILLISECONDS.toSeconds(LL.get(counter).dateVar.getTime() - System.currentTimeMillis());
	    System.out.println("Start of difference");
	    System.out.println(startTime);
	    
	    this.LL = LL;
	    
	    
	  }
	  
	  /** Sound the alarm for a few seconds, then stop. */
	  void activateAlarmThenStop(){
	    Runnable soundAlarmTask = new SoundAlarmTask();
	    ScheduledFuture<?> soundAlarmFuture = fScheduler.schedule(
	      soundAlarmTask, startTime, TimeUnit.SECONDS
	    );
	    Runnable stopAlarm = new StopAlarmTask(soundAlarmFuture);
	    fScheduler.schedule(stopAlarm, 61, TimeUnit.SECONDS);
	    
	    System.out.println("methodStartTime: " + startTime);
	    
	    //commented out below 2 lines, b/c I don't want this to stop!
	    //Runnable stopAlarm = new StopAlarmTask(soundAlarmFuture);
	    //fScheduler.schedule(stopAlarm, fShutdownAfter, TimeUnit.SECONDS);
	  }

	  // PRIVATE 
	  private final ScheduledExecutorService fScheduler;
	  private final long fInitialDelay;
	  //private final long fDelayBetweenRuns;
	  //private final long fShutdownAfter;
	  
	  private static void log(String aMsg){
	    System.out.println(aMsg);
	  }

	  /** If invocations might overlap, you can specify more than a single thread.*/ 
	  private static final int NUM_THREADS = 1;
	  private static final boolean DONT_INTERRUPT_IF_RUNNING = false;
	  
	  private final class SoundAlarmTask implements Runnable {
		  int comparison = -1;
	    @Override public void run() {
	      ++fCount;
	      log("beep " + fCount);
	      System.out.println("Pancakes");
	      System.out.println(LL.get(counter).show);
	      //comparison = currRecord.dateVar.compareTo(iRecord.dateVar);
	      if(LL.get(counter).dateVar.getTime()>=System.currentTimeMillis())
	      {
	    	 channel.sendMessage(LL.get(counter).show).queue();
	      }
	      //MessageChannel channel = jda.getTextChannelById(271071244982550540L);
	      //channel.sendMessage("http://i.imgur.com/W652eie.png").queue();
	      if (counter < LL.size())
	      {
	        counter++;
	        startTime = TimeUnit.MILLISECONDS.toSeconds(LL.get(counter).dateVar.getTime() - System.currentTimeMillis());
	        activateAlarmThenStop();
	        System.out.println(counter);
	      }
	      
	    }
	    private int fCount;
	  }
	  
	  private final class StopAlarmTask implements Runnable {
		    StopAlarmTask(ScheduledFuture<?> aSchedFuture){
		      fSchedFuture = aSchedFuture;
		    }
		    @Override public void run() {
		      log("Stopping alarm.");
		      fSchedFuture.cancel(DONT_INTERRUPT_IF_RUNNING);
		      /* 
		       Note that this Task also performs cleanup, by asking the 
		       scheduler to shutdown gracefully. 
		      */
		      fScheduler.shutdown();
		    }
		    private ScheduledFuture<?> fSchedFuture;
		  }

	} 
