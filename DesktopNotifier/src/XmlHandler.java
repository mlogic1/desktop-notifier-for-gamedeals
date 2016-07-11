import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlHandler {

	
	public XmlHandler(){
		
	}
	
	public void AddIdToXml(String redditID, String DealName, String url){
		
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse("Data.xml");
			Element root = document.getDocumentElement();
			
			
			Element entry = document.createElement("entry");
			entry.setAttribute("ID", redditID);

			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(DealName));
			
			Element webURL = document.createElement("url");
			webURL.appendChild(document.createTextNode(url));
			
			
			entry.appendChild(name);
			entry.appendChild(webURL);
			root.appendChild(entry);
			
			
			DOMSource source = new DOMSource(document);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        StreamResult result = new StreamResult("Data.xml");
	        transformer.transform(source, result);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public ArrayList<Gamedeal> GetAllReadDeals(){
		ArrayList<Gamedeal> list = new ArrayList<Gamedeal>();
		
		try {
			String filename = "Data.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filename);
			
			
			NodeList nlist = doc.getElementsByTagName("entry");

			for(int i=0;i<nlist.getLength();i++){
				Node node = nlist.item(i);
				String dealID, name, url;
				dealID = node.getAttributes().getNamedItem("ID").getNodeValue();
				
				Element e = (Element)node;
				name = e.getElementsByTagName("name").item(0).getTextContent();
				url = e.getElementsByTagName("url").item(0).getTextContent();
				
				list.add(new Gamedeal(dealID, name, url));
				
			}
		}catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<Gamedeal> GetAllNewDealsAsArray(String jsonData){
		ArrayList<Gamedeal> list = new ArrayList<Gamedeal>();
		
		JSONObject obj = null;
		JSONArray posts = null;
		
		JSONObject postData = null;
		
		int ArraySize;
		
		try {
			obj = new JSONObject(jsonData);	
			
			obj = obj.getJSONObject("data");
			posts = obj.getJSONArray("children");	
			ArraySize = posts.length();

			
			String dealID, name, url;
			
			for(int i=0;i<ArraySize;i++){
				postData = posts.getJSONObject(i).getJSONObject("data");
				dealID = postData.get("id").toString();
				name = postData.get("title").toString();
				url = postData.get("url").toString();
				list.add(new Gamedeal(dealID, name, url));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	
	public ArrayList<Gamedeal> FilterReadDeals(ArrayList<Gamedeal> Readdeals, ArrayList<Gamedeal> Newdeals){
		ArrayList<Gamedeal> list = new ArrayList<Gamedeal>();
		
		// For each new deal, check if ID matches with read deal, if it does NOT it means user has not
		// seen that deal yet, so add it to the unread list and return the list at the end
		
		
		for(int i=0;i<Newdeals.size();i++){
			boolean newDeal = false;
			for(int j=0;j<Readdeals.size();j++){
				if(Newdeals.get(i).GetID().equals(Readdeals.get(j).GetID())){
					// This ID exists in the read deals db
					newDeal = false;
					break;
				}else{
					// This is an unseen deal id so far
					newDeal = true;
				}	
			}
			if(newDeal){
				//System.out.println(Newdeals.get(i).GetID());
				String dealID, name, url;
				dealID = Newdeals.get(i).GetID();
				name = Newdeals.get(i).GetName();
				url = Newdeals.get(i).GetURL();
				list.add(new Gamedeal(dealID, name, url));
			}
		}
		
		return list;
	}
}
