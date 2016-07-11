
public class Gamedeal {
	
	private String DealID;
	private String DealName;
	private String DealURL;
	
	
	public Gamedeal(String id, String Name, String weburl){
		DealID = id;
		DealName = Name;
		DealURL = weburl;
	}
	
	
	public String GetID(){
		return DealID;
	}
	
	public String GetName(){
		return DealName;
	}
	
	public String GetURL(){
		return DealURL;
	}
	
	

}
