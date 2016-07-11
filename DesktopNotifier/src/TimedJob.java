import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.TimerTask;

public class TimedJob extends TimerTask {
	
	private String Data = "";
	private URL url = null;
	URLConnection urlConn = null;
	private BufferedReader reader = null;
	private SysTrayIcon trayIcon = null;
	private PopupNotification notification = null;
	
	
	// Arrays populated with game data
	private ArrayList<Gamedeal> ReadDeals;	// Game deals data that is stored in Data.xml file, its used to tell which deals have been shown to the user = m.GetAllReadDeals();
	private ArrayList<Gamedeal> ApiDeals;	// Game deals data that is downloaded from reddit api	= m.GetAllNewDeals("nothing");
	private ArrayList<Gamedeal> NewDeals;	// Game deals data that only contains game deals that have not yet been shown to the user	= m.FilterReadDeals(ReadDeals, ApiDeals);
	
	private XmlHandler xmlHandler;			// Object that parses various xml and json data (stores and reads xml, parses json and compares arraylists)
	
	
	public TimedJob(SysTrayIcon trayicon){
		trayIcon = trayicon;
		xmlHandler = new XmlHandler();
		
		ReadDeals = new ArrayList<Gamedeal>();
		ApiDeals = new ArrayList<Gamedeal>();
		NewDeals = new ArrayList<Gamedeal>();
	}
	
	
	@Override
	public void run() {
		
		if(!main.NotificationsEnabled){
			// User does not want to be notified
			// Don't do anything
			return;
		}
		
		System.out.println("Downloading from reddit");
		Data = DownloadData();	// Download data from reddit API
		
		
		// If connection is NOT working or there has been an error downloading data
		if(Data.equals("errURL")){
			System.out.println("Error with reddit url, try again later");
			return;
		}
		
		if(Data.equals("errReader")){
			System.out.println("Error downloading data, try again later");
			return;
		}
		
		System.out.println("Download success");
		
		// Everything is fine with the data, clear the arrays
		ApiDeals.clear();
		NewDeals.clear();
		ReadDeals.clear();
		
		ApiDeals = xmlHandler.GetAllNewDealsAsArray(Data);	// Parse api json data and store it in array list
		ReadDeals = xmlHandler.GetAllReadDeals();			// Load all read deals to array list
		NewDeals = xmlHandler.FilterReadDeals(ReadDeals, ApiDeals);	// Filter all the deals that the user has seen
		
		String[] message = new String[NewDeals.size()];	// Message that is displayed to the user
	
		// Add each new deal to the message (seperate deals with a blank space)
		for(int i=0;i<NewDeals.size();i++){
			message[i] = NewDeals.get(i).GetName();
		}
		
		
		notification = new PopupNotification(message);	// Notify the user of new deals
		notification.start();
		//System.out.println(message);
			
	}
	
	
	private String DownloadData(){
		String s = "";
		String line = "";
		
		
		try {
			// Set up the url
			url = new URL("https://api.reddit.com/r/GameDeals/new.json?sort=new");
			urlConn = url.openConnection();
			urlConn.setRequestProperty("User-Agent", "Desktop notifier by /u/Multilogic");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "errURL";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while((line = reader.readLine()) != null){
				s += line;
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "errReader";
		}

		return s;
	}

}
