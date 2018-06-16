import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {

	private final String USER_AGENT = "Mozilla/5.0";

	 String sendGet(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine+"\n");
		}
		in.close();

		return response.toString();

	}
	 
 
	 
	 ArrayList<Broadcast> generalParse(String parsefilename, String targetFilename, String ourCategories, String ourTopTeams) throws ParserConfigurationException, SAXException, IOException{
		 FileWorker fileWorker = new FileWorker();
		 StringBuilder str = new StringBuilder("<response>"+"/");
		 String projPath = fileWorker.getProjectPath();
		 File inputFile = new File(projPath+"/publicator_files/"+parsefilename);
	         
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	     Document doc = dBuilder.parse(inputFile);
	     doc.getDocumentElement().normalize();
	     //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	     NodeList nList = doc.getElementsByTagName("event");
	         System.out.println("----------------------------");
         
	         //ourCategories
	         ArrayList<String> ourBroadCastCategories = new ArrayList<String>();
	         ourBroadCastCategories = fileWorker.readFileByLines(ourCategories);
	         
	         //our top teams
	         ArrayList<String> topTeams = new ArrayList<String>();
	         topTeams = fileWorker.readFileByLines(ourTopTeams);
	         

	         ArrayList<Broadcast>  allBroadcasts = new ArrayList<Broadcast>();
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	           System.out.println("-----");
	            //System.out.println("\nCurrent Element :" + nNode.getNodeName());
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	         
	               
	              String broadcastName = eElement.getElementsByTagName("name").item(0).getTextContent();
	              String broadCastSport = eElement.getElementsByTagName("sport").item(0).getTextContent();
	              String broadcastLeague = eElement.getElementsByTagName("league").item(0).getTextContent();
	              String broadcastDate = eElement.getElementsByTagName("startDateTime").item(0).getTextContent();
	              String broadcastUrls = eElement.getElementsByTagName("urls").item(0).getTextContent();
	              String broadCastChannel = eElement.getElementsByTagName("channel").item(0).getTextContent();
	              String broadCastLanguage = eElement.getElementsByTagName("language").item(0).getTextContent();
	            
	              Broadcast b =  new Broadcast(broadcastName,broadCastSport,broadcastLeague,broadcastDate,broadcastUrls,broadCastChannel,broadCastLanguage);              
	              String currBroadcastCategory = b.getBroadcastSport();
	              String currBroadcastHeader = b.getBroadcastName();
	              
	              //write to list only top teams
	              for(int category=0;category<ourBroadCastCategories.size();category++){
	            	// System.out.println(b.getBroadcastLeague());
	            	 	for(int topTeam=0;topTeam<topTeams.size();topTeam++){
	            	 		String ourCategory = ourBroadCastCategories.get(category);
	            	 		String ourTopTeam = topTeams.get(topTeam);
	            	 		if(currBroadcastCategory.contains(ourCategory) && currBroadcastHeader.contains(ourTopTeam)){
	            	 			allBroadcasts.add(b);
	            	 		}  
	            	 	}
	              }	              
	            
	            }
	            
	         }
	         
	         for (int i = 0;i<allBroadcasts.size();i++){
	        	 String currUrl =  allBroadcasts.get(i).getBroadcastUrls();
	        	 	        	
		        	 while (i+1<allBroadcasts.size() && allBroadcasts.get(i).getBroadcastName().equals(allBroadcasts.get(i+1).getBroadcastName())){
		        		 
		        		currUrl+= ";"+allBroadcasts.get(i+1).getBroadcastUrls();
		        		allBroadcasts.remove(i+1);
		        		allBroadcasts.trimToSize();
		        	
		        	 }
	        	 
	        	 allBroadcasts.get(i).setBroadcastUrls(currUrl+";");
	        	 
	         }
	         
	         for (int i = 0;i<allBroadcasts.size();i++){
	        	 System.out.println(allBroadcasts.get(i));
	         }
	 return allBroadcasts;     
	 } 
	
	 }
	