import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
public class Broadcast {
	private String broadcastName ;
    private String broadcastLeague;
    private String broadcastDate;
    private String broadcastUrls;
    private String broadcastSport ;
    private String broadCastChannel;
    private String broadCastLanguage;
    private String link;
    private String postContentTarget;
    private String postContentSource; 
    
    String  targetSite = "http://www.matchttv.ru/";

    public Broadcast(String broadcastName, String broadCastSport, String broadcastLeague, String broadcastDate,
			String broadcastUrls, String broadCastChannel, String broadCastLanguage) {
			
		this.broadcastLeague = broadcastLeague;
		this.broadcastName = setBroadcastName(broadcastName);
		this.broadcastDate = broadcastDate;
		this.broadcastUrls = broadcastUrls;
		this.broadcastSport = broadCastSport;
		this.broadCastChannel = broadCastChannel;
		this.broadCastLanguage = broadCastLanguage;
		this.link =setLink();
		this.postContentSource = setPostContentSource();
		
    }
    
    String urlToIframe(String url){
		 StringBuilder player = new StringBuilder("<iframe src=\"");
		 player.append(url);
		 player.append("\" width=\"640\" height=\"480\" frameborder=\"0\" scrolling=\"no\" allowfullscreen></iframe>");
		 return player.toString();	 
	 }

	public List<String> generateIframes() {
		
		List<String> convertedToIframes = new ArrayList<String>();
		StringTokenizer stUrls = new StringTokenizer(broadcastUrls, ";");
		
		while (stUrls.hasMoreElements()) {
			convertedToIframes.add(urlToIframe(stUrls.nextElement().toString()));
		}
		
		System.out.println("List "+convertedToIframes);
		return convertedToIframes;
	}

	public String getLink() {
		return link;
	}

	public String setLink() {

		  long longBroadcastDate = Long.parseLong(this.broadcastDate);
		   java.util.Date timee=new java.util.Date((long)longBroadcastDate*1000);
		
		    // the format of your date
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		   String formattedDate = sdf.format(timee);
		
		//StringBuilder finalUrl= new StringBuilder(this.broadcastDate);
		StringBuilder finalUrl= new StringBuilder();
		finalUrl.append(formattedDate);
		finalUrl.append("-");
		
		String urlText = this.broadcastName.toLowerCase();
		urlText = urlText.replaceAll("\\s+", "");
		urlText = urlText.replace(".", "-");
		
		
		char[] english = { 'a', 'b', 'v', 'g', 'd', 'e', 'e', 'j', 'z', 'i',
				'k', 'l', 'm', 'n', 'o', 'p', 'r', 's', 't', 'h', 'f', 'u',
				's', 's', 'q', 'u', 'a', 'i', 'c', 'j', 'c', 'o' };

		char[] russian = { 'а', 'б', 'в', 'г', 'д', 'е', 'э', 'ж', 'з', 'и',
				'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'х', 'ф', 'у',
				'ш', 'щ', 'ь', 'ю', 'я', 'ы', 'ч', 'й', 'ц', 'ё' };
		
		for (int i = 0; i < urlText.length(); i++) {
			for (int j = 0; j < russian.length; j++) {
				if (urlText.charAt(i) == russian[j]) {
					urlText = urlText.replace(russian[j], english[j]);

				}
			}
		}
		finalUrl.append(urlText).toString();
		return finalUrl.toString();
		
	}

	public String getPostContentTarget() {
		return postContentTarget;
	}

	public String setPostContentTarget(List<String> ads,List<String> publishedPlayerLinks) {
		
		String translationText = "<h4><em><span style=\"color: #ff0000;\"><strong>"+
				"Ссылки на плеера с трансляциями к матчу " +
				"будут доступны за 15-20 минут до начала матча.\n" +
				"</strong></span></em></h4>\n"+
				"<span style=\"color: #ff0000;\"><em><strong>Выберите плеер. Трансляция откроется в новом" +
				" окне:</strong></em></span>\n" +
				"<div id=\"streams1\">";
		
		StringBuilder players = new StringBuilder();
		for (int i=0;i<publishedPlayerLinks.size();i++){
			players.append("[button color=\"green\" size=\"medium\" link=\"");
			players.append(targetSite);
			players.append(publishedPlayerLinks.get(i));
			players.append("/\" target=\"blank\" ]");
			players.append("Канал ");
			players.append(i);
			players.append("[/button]");	
		}
		
		String adsAbovePlayer = ads.get(0);
		String adsUnderPlayer = ads.get(1);
		StringBuilder translationFinalText = new StringBuilder();
		translationFinalText.append(adsAbovePlayer);
		translationFinalText.append(translationText);
		translationFinalText.append(players);
		translationFinalText.append("</div>");
		translationFinalText.append(adsUnderPlayer);
		
		return translationFinalText.toString();
	}

	public String getPostContentSource() {
		return postContentSource;
	}

	public String setPostContentSource() {
		String playerButton =		
				"<meta http-equiv=\"refresh\" content=\"2;URL="+this.targetSite+this.link+"\"/>"+"\n" +
				"<a href=\""+this.targetSite+this.link+"\"><img src=\"/wp-content/uploads/watch.jpg\" alt=\"Смотреть\" width=\"634\" height=\"355\" /></a>";
		
		String ads=
				playerButton+
				"<div id=\"MIXADV_1531\" class=\"MIXADVERT_NET\"></div>"+
				"<script type=\"text/javascript\" src=\"https://s.mixadvert.com/show/?id=1531\" async></script>";

		String mainText=ads+
				"<em><span style=\"color: #ff0000;\"><strong>Страница с плеерами откроется автоматически."+"</strong></span></em>"+
		" [button color=\"red\" size=\"medium\" link=\""+targetSite+this.link+"\" target=\"blank\" ]Смотреть трансляцию матча![/button]"+"\n";
		
		return mainText;
		
	}


	public String getBroadcastName() {
		return broadcastName;
	}

	String setBroadcastName(String broadcastName) {
		StringBuilder finalHeader = new StringBuilder(broadcastName);
		finalHeader.append(". ");
		finalHeader.append(this.broadcastLeague);
		finalHeader.append(". ");
		finalHeader.append("Прямая трансляция");
		System.out.println(finalHeader.toString());
		//return finalHeader.toString();
		return this.broadcastName = finalHeader.toString();
	}

	public String getBroadcastLeague() {
		return broadcastLeague;
	}

	public void setBroadcastLeague(String broadcastLeague) {
		this.broadcastLeague = broadcastLeague;
	}

	public String getBroadcastDate() {
		return broadcastDate;
	}

	public void setBroadcastDate(String broadcastDate) {
		this.broadcastDate = broadcastDate;
	}

	public String getBroadcastUrls() {
		return broadcastUrls;
	}

	public void setBroadcastUrls(String broadcastUrls) {
		this.broadcastUrls = broadcastUrls;
	}

	public String getBroadcastSport() {
		return broadcastSport;
	}

	public void setBroadcastSport(String broadcastSport) {
		this.broadcastSport = broadcastSport;
	}

	public String getBroadCastChannel() {
		return broadCastChannel;
	}

	public void setBroadCastChannel(String broadCastChannel) {
		this.broadCastChannel = broadCastChannel;
	}

	public String getBroadCastLanguage() {
		return broadCastLanguage;
	}

	public void setBroadCastLanguage(String broadCastLanguage) {
		this.broadCastLanguage = broadCastLanguage;
	}


	@Override
	public String toString() {
		return "Broadcast [broadcastName=" + broadcastName + ", broadcastLeague=" + broadcastLeague + ", broadcastDate="
				+ broadcastDate + ", broadcastUrls=" + broadcastUrls + ", broadcastSport=" + broadcastSport
				+ ", broadCastChannel=" + broadCastChannel + ", broadCastLanguage=" + broadCastLanguage + ", link="
				+ link + ", postContentTarget=" + postContentTarget + ", postContentSource=" + postContentSource + "]";
	}
	
}
