import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.metr.service.*;
public class Main {

	public static void main(String[] args) throws Exception {
		
		FileWorker file = new FileWorker();
		file.readFileByLines("brodcast_categories.txt");
		
		Parser parser = new Parser();
		//writing broadcasts to general file
		String allBroadcastsPage = parser.sendGet("https://sport7.site/feed?language=ru");
		file.createFile("allBroadcasts.xml",allBroadcastsPage);
	
		//parsing general file and getting only needed translation
		ArrayList <Broadcast> parsedBroadcasts = parser.generalParse("allBroadcasts.xml", "broadcastsToPublish.xml","brodcast_categories.txt","topTeams.txt");
		
		
		ArrayList<String> dbAccess = new ArrayList<String>();
		
		String siteName = "watchhd.online";
		dbAccess = file.readFileByLines("databases/"+siteName);
		MysqlDAO mysql= new MysqlDAO(dbAccess.get(0),dbAccess.get(1),dbAccess.get(2),dbAccess.get(3));
		
		String queryMaxId = "SELECT MAX(ID) FROM wp_posts";
		int maxId =mysql.selectIdByQuery(queryMaxId);
		List<String> publishedBroadcasts = new ArrayList<String>();
		
		maxId = maxId-750;
		String postNameQuery = "SELECT post_name FROM wp_posts where ID >"+maxId;
		publishedBroadcasts = mysql.selectListByQuery(postNameQuery);
	
		for (int i=0;i<parsedBroadcasts.size();i++){
			String postContentSource = parsedBroadcasts.get(i).getPostContentSource();
			String postTitle = parsedBroadcasts.get(i).getBroadcastName();
			String postLink = parsedBroadcasts.get(i).getLink();
			String postSport = parsedBroadcasts.get(i).getBroadcastSport();
			List<String> iframes = parsedBroadcasts.get(i).generateIframes();
			
			System.out.println("URLS "+parsedBroadcasts.get(i).getBroadcastUrls());
			System.out.println("aaaaa "+iframes);
			System.out.println(parsedBroadcasts.get(i).getBroadcastName());
			
			for(int published = 0;published<publishedBroadcasts.size();published++){

				if(publishedBroadcasts.get(published).equals(postLink)){
					break;
				}else if(published == publishedBroadcasts.size()-1){
					
					String idQuery = "SELECT ID FROM wp_posts where post_name ="+"'"+postLink+"';";
					
					if(mysql.getSiteType().equals("source") ){
						
						mysql.insertTranslationQuery(postContentSource, postTitle, postLink);
						
						int publishdTranslationId=mysql.selectIdByQuery(idQuery);
						
						String query = "select term_id from wp_terms where name ='"+postSport+"'";
						int termId = mysql.selectIdByQuery(query);
						System.out.println("term! "+termId);
						
						mysql.insertTerm(publishdTranslationId, termId);
						
						//adding miniature for broadcast
						String pictureIdQuery ="Select ID from wp_posts where post_excerpt = 'Прямая трансляция' and post_mime_type = 'image/png'";
						int pictureId = mysql.selectIdByQuery(pictureIdQuery);
						mysql.insertThumbnail(publishdTranslationId, String.valueOf(pictureId));
						
						
					}else{
				
						List<String> publishedPlayerUrls = new ArrayList<String>();
						
						for(int playerIframe = 0;playerIframe<iframes.size();playerIframe++){
							
							int playerIterator = playerIframe+1;
							String playerLink = postLink+"-player"+playerIterator;
							String playerTitle = "Плеер"+playerIterator +" "+postTitle;
							String playerContent = iframes.get(playerIframe);
							
							publishedPlayerUrls.add(playerLink);
							
							//get banners
							List<String> banners = file.readBannersFile("player_"+siteName);
							String playerContentWithAds = banners.get(0)+playerContent+banners.get(1);
							
							//insert player
							mysql.insertTranslationQuery(playerContentWithAds, playerTitle, playerLink);
							
							String selectPlayerId = "SELECT ID FROM wp_posts where post_name ="+"'"+playerLink+"';";
							
							//get inserted player id
							int publishdedPlayerId=mysql.selectIdByQuery(selectPlayerId);
							
							//insert publishd player to player category
							String selectPlayerTerm = "select term_id from wp_terms where name ='Плеер';";
							int termPlayerId = mysql.selectIdByQuery(selectPlayerTerm);
							System.out.println("Player term "+termPlayerId);
							mysql.insertTerm(publishdedPlayerId, termPlayerId);
						}
						
						//get main translation banners
						List<String> translationBanners = file.readBannersFile("main_"+siteName);
						
						String targetPostContent = parsedBroadcasts.get(i).setPostContentTarget(translationBanners,publishedPlayerUrls);			
						
						//insert target post broadcast
						mysql.insertTranslationQuery(targetPostContent, postTitle, postLink);
						
						int publishdTranslationId=mysql.selectIdByQuery(idQuery);
						
						//adding published broadcasting to a category
						String query = "select term_id from wp_terms where name ='"+postSport+"'";
						int termId = mysql.selectIdByQuery(query);
						mysql.insertTerm(publishdTranslationId, termId);
						
						//adding miniature for broadcast
						String pictureIdQuery ="Select ID from wp_posts where post_excerpt = 'Прямая трансляция' and post_mime_type = 'image/png'";
						int pictureId = mysql.selectIdByQuery(pictureIdQuery);
						mysql.insertThumbnail(publishdTranslationId, String.valueOf(pictureId));
					}
					
				}
						
			}
		
		}
		 
		
		long currentUnix = System.currentTimeMillis()/ 1000L;
	    
		java.util.Date timee=new java.util.Date((long)currentUnix*1000);
	
	    // the format of your date
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
	    String formattedDate = sdf.format(timee);
	    
	    System.out.println(formattedDate);
	    
	    //select term id of today's matches
	    String todayTranslQuery = "select term_id from wp_terms where name = 'Сегодняшние матчи';";
		int todaysTransTermId = mysql.selectIdByQuery(todayTranslQuery);
		
		//delete all translation from today's matches
		String deletefromTodaysQuery = "delete from wp_term_relationships where term_taxonomy_id ="+todaysTransTermId;
		mysql.executeQuery(deletefromTodaysQuery);
		
		//delete all not today's translation players
		String deletePlayers = "delete from wp_posts where post_title like '%Плеер%' and substring(post_name,1,8) <'"+formattedDate+"'";
		mysql.executeQuery(deletePlayers);
		
		//publication to main page
		String selectTodayTranslations= "select ID from wp_posts"+
				" where substring(post_name,1,8) ='"+formattedDate+"' and post_title not like '%Плеер%';";
		
	    List<Integer>todayIds = mysql.selectIntListByQuery(selectTodayTranslations);
	   
	    StringBuilder telegramContent = new StringBuilder(); 
		
	    for (int i=0;i<todayIds.size();i++){
			//insert today translations term
	    	mysql.insertTerm(todayIds.get(i), todaysTransTermId);
			
			//telegram file creator
			String currPostNameQuery = "SELECT post_name FROM wp_posts where ID ="+todayIds.get(i);
			String currPostName = mysql.selectString(currPostNameQuery);
			telegramContent.append(currPostName);
			telegramContent.append(";");
			
			String currPostTitleQuery = "SELECT post_title FROM wp_posts where ID ="+todayIds.get(i);
			String currPostTitle = mysql.selectString(currPostTitleQuery);
			telegramContent.append(currPostTitle);
			telegramContent.append(";");
			
		    Pattern r = Pattern.compile("([0-9]+-[0-9]+)");
		    Matcher m = r.matcher(currPostName);
		    String date = null; 
		    
		      if (m.find( )) {
		         date = m.group(1);
		         telegramContent.append(date);
		         telegramContent.append(";");
		       }

			telegramContent.append("\n");
		}
		file.createFile("translationtable.csv", telegramContent.toString());
	
	}
}