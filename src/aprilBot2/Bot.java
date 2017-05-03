package aprilBot2;

import java.util.List;

import javax.security.auth.login.LoginException;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;    
import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

/*
 * Main code repeats at 1 min intervals
 * Problem was scheduling java scheduler never ends
 * So after 10 repeats you have 10 processes running b/c of the scheduler in the main method
 * so if delete row for 1 main repeat after 10 repeats. The 9 other repeats will post the thing,
 * Idea was to test by having main method loop every minute and having scheduling java stop at 59 sec
 * issue was after 59 sec to 1min nothing happening. So if have to post a message it can't, so if schedule has for 1pm for instance it wouldn't show
 * Idea was to use 61 sec for scheduling java. This gives 2 repeats all the time, but better than 100s
 * must test out before implementaiton
 * 
 * update: Also made so that if time past to post thing, it wouldn't post b/c of repeat have to think about that.
 */


public class Bot {
	
	//MessageChannel channel = jda.getTextChannelById(271071244982550540L);
    //channel.sendMessage("http://i.imgur.com/W652eie.png").queue();
	
	public static JDA jda;
	
	public static final String BOT_TOKEN = "MzA2ODUxOTczNjMyNjIyNTky.C-Jxvw.7JoqH7wigk40coSs-7HHQi-rfDQ";
	
	public static void main(String[] args) throws IOException{
		
		//final Sheets service = getSheetsService();
		
		final ScheduledExecutorService scheduler =
   		     Executors.newScheduledThreadPool(1);
     final Runnable beeper = new Runnable() {
         public void run() {try {
			repeatCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
       };
       
       final ScheduledFuture<?> beeperHandle =
         scheduler.scheduleAtFixedRate(beeper, 0, 1, MINUTES);
		
		
		
		
	}
	
	public static void repeatCode() throws IOException
	{
		final Sheets service = getSheetsService();
		Record currRecord;
		//  List arranged: Nearest Date ----> Farthest Date
		List<Record> records = new LinkedList <Record>();
		
		// TODO Auto-generated method stub
		
		try {
            jda = new JDABuilder(AccountType.BOT).addEventListener(new BotListener()).setToken(BOT_TOKEN).buildBlocking();
        } catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
        }
		
		
		//Start of main quickstart code

        // Build a new authorized API client service.

        // Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        
        //String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        String spreadsheetId = "1iU-Nyka9DRNvPPsOWlIrYgEVDegJLM-_2XuPcoucnBQ";

        String range = "Class Data!A2:F";
        //String range = "Class Data!A2:D";

        ValueRange response = service.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {


          System.out.println("Name, Major");
          
          
          
          records.clear();

          for (List row : values) {
            // Print columns A and E, which correspond to indices 0 and 4.
            //System.out.printf("%s, %s\n", row.get(0), row.get(4));
            System.out.printf("%s, %s, %s, %s, %s, %s\n", row.get(0),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5));
            //System.out.println("|"+row.get(0).getClass().getName()+"***");
            boolean b = row.get(0) instanceof String;
            System.out.println(b);
            currRecord = new Record(row.get(0),row.get(1),row.get(2),row.get(3),row.get(4),row.get(5));
            //System.out.println("curr " + currRecord.tagTime);
            //currRecord = new Record(row.get(0),row.get(1),row.get(2),row.get(3), row.get(4), row.get(5));
            //currRecord = new Record(row.get(0),"b","c","d","e","f");	
            
         
            //records.add(currRecord);
            insertRecord(records,currRecord);
          }
          System.out.println("Printing List!");
          printMe(records);
          //Record tempRecord = new Record("11/12/2017 11:11:11", "Empress Ep 11", "10/11/2018", "11:11:00", "gabby", "sida");
          //records.add(2, tempRecord);
          //System.out.println("added");
          //printMe(records);
        }
        
        //String test = "mik";
        //MessageChannel.sendMessage(test);
        
        long seconds = -1;
        
        MessageChannel channel = jda.getTextChannelById(271071244982550540L);
        
        Scheduling schedule = new Scheduling(seconds,records,channel);
        schedule.activateAlarmThenStop();
        
        //MessageChannel channel = event.getChannel();
        //MessageChannel#sendMessage(String).queue();
        
        //jda.getTextChannelById(271071244982550540L);
        //MessageChannel#sendMessage(String).queue();
        MessageChannel testchannel = jda.getTextChannelById(271071244982550540L);
        testchannel.sendMessage("http://i.imgur.com/W652eie.png").queue();
	}
	
	
	private static void insertRecord(List<Record> records, Record currRecord)
	{
		int comparison;
		boolean inserted = false;
		Record iRecord;
		
		if (records.isEmpty())
		{
			records.add(currRecord);
			inserted = true;
		}
		else
		{
			for (int i = 0; i < records.size() && inserted==false; i++) {
				//System.out.println(records.get(i));
				iRecord = records.get(i); 
				comparison = currRecord.dateVar.compareTo(iRecord.dateVar);
				if (comparison < 0)
				{
					records.add(i, currRecord);
					inserted = true;
				}
			}
		}
		
		if (inserted == false) // record is the farthest away so add to end of list
		{
			records.add(currRecord);
		}
		
	}
	
	
	
	private static void printMe(List<Record> records)
	{
		for (Record currRecord : records)
		{
			System.out.println(currRecord.printRecord());
		}
			
	}
	
	/** Application name. */
    private static final String APPLICATION_NAME =
        "Google Sheets API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            Bot.class.getResourceAsStream("/client_secret_763276693697-d5st29a63e0lohfn733dl7jl6el7bu84.apps.googleusercontent.com.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    

}
