import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.metr.service.DateFormator;


public class MysqlDAO {
	
	private static Connection connection;
	private String url;
	private String login;
	private String password;
	private String siteType;
	final private static String INSERT_TRANSLATION_WITHOUT_MINIATURE = "INSERT INTO wp_posts (post_author, post_date, post_date_gmt, post_content,"
			+ " post_excerpt,post_title, post_status, comment_status, ping_status,post_name,to_ping,pinged,"
			+ "post_modified, post_modified_gmt,post_content_filtered, guid,post_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	final private static String TERM_POST = "INSERT into wp_term_relationships(object_id,term_taxonomy_id) VALUES (?,?);";
	final private static String THUMBNAIL_POST = "INSERT into wp_postmeta(post_id,meta_key,meta_value) VALUES (?,?,?);";
	
	
	MysqlDAO(String url,String login,String password, String siteType) {
		this.setUrl(url);
		this.setLogin(login);
		this.setPassword(password);
		this.siteType = siteType;
		try {
			connection = DriverManager.getConnection(url, login, password);
		} catch (SQLException ex) {
			ex.printStackTrace();

		}
	}
	
	 void insertTranslationQuery( String postContent,
				String postTitle, String postName) throws ParseException {
			try {
				PreparedStatement preparedStatement = null;
				String currentDate = DateFormator.currentDateDAO();
				String currentGmtDate = DateFormator.currentDateGrinvichTime();
				preparedStatement = getConnection().prepareStatement(INSERT_TRANSLATION_WITHOUT_MINIATURE);
				preparedStatement.setInt(1, 2);
				preparedStatement.setString(2, currentDate);
				preparedStatement.setString(3, currentGmtDate);
				preparedStatement.setString(4, postContent);
				preparedStatement.setString(5, "");
				preparedStatement.setString(6, postTitle);
				preparedStatement.setString(7, "publish");
				preparedStatement.setString(8, "open");
				preparedStatement.setString(9, "open");
				preparedStatement.setString(10, postName);
				preparedStatement.setString(11, "");
				preparedStatement.setString(12, "");
				preparedStatement.setString(13, currentDate);
				preparedStatement.setString(14, currentGmtDate);
				preparedStatement.setString(15, "");
				preparedStatement.setString(16, "");
				preparedStatement.setString(17, "post");
				preparedStatement.execute();
				System.err.println("Трансляцію додано");
			} catch (SQLException e) {
				System.err.println("Трансляцію НЕ Додано. Помилка!");
				e.printStackTrace();
			}
		}
	 
	 int selectIdByQuery(String query) {
			//String query="SELECT MAX(ID) FROM wp_posts ";
			int id = 0;
			try {
				PreparedStatement preparedStatement = null;
				preparedStatement = getConnection().prepareStatement(query);
				ResultSet set = preparedStatement.executeQuery();
					while (set.next()) {
					id = set.getInt(1);
					}
				preparedStatement.execute();
				System.err.println("Макс id вибрано");
			} catch (SQLException e) {
				System.err.println("Макс id не вбирано");
				e.printStackTrace();
			}
				return id;
		}
	 
	 List<String> selectListByQuery(String query) {
			//String query="SELECT MAX(ID) FROM wp_posts ";
		
		 List<String> elements = new ArrayList<String>();
			try {
				PreparedStatement preparedStatement = null;
				preparedStatement = getConnection().prepareStatement(query);
				ResultSet set = preparedStatement.executeQuery();
				String element;	
				while (set.next()) {
					element = set.getString(1);
					elements.add(element);
					}
				preparedStatement.execute();
				System.err.println("Макс id вибрано");
			} catch (SQLException e) {
				System.err.println("Макс id не вбирано");
				e.printStackTrace();
			}
				return elements;
		}
	 
	 List<Integer> selectIntListByQuery(String query) {
			//String query="SELECT MAX(ID) FROM wp_posts ";
		
		 List<Integer> elements = new ArrayList<Integer>();
			try {
				PreparedStatement preparedStatement = null;
				preparedStatement = getConnection().prepareStatement(query);
				ResultSet set = preparedStatement.executeQuery();
				int element;	
				while (set.next()) {
					element = set.getInt(1);
					elements.add(element);
					}
				preparedStatement.execute();
				System.err.println("Макс id вибрано");
			} catch (SQLException e) {
				System.err.println("Макс id не вбирано");
				e.printStackTrace();
			}
				return elements;
		}
	 

		void insertTerm(int objectId,int termId)throws ParseException {
			try {
				PreparedStatement preparedStatement = null;
				preparedStatement = getConnection().prepareStatement(TERM_POST);
				preparedStatement.setInt(1, objectId);
				preparedStatement.setInt(2, termId);
				preparedStatement.execute();
				System.err.println("В терм додано");
			} catch (SQLException e) {
				
				System.err.println("В терм не додано");
				e.printStackTrace();
			}

		}

		void insertThumbnail(int idCounter, String metaValue)
				throws ParseException {
			try {
				PreparedStatement preparedStatement = null;
				preparedStatement = getConnection().prepareStatement(THUMBNAIL_POST);
				preparedStatement.setFloat(1, idCounter);
				preparedStatement.setString(2, "_thumbnail_id");
				preparedStatement.setString(3, metaValue);
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		void executeQuery(String query) throws SQLException{
			PreparedStatement preparedStatement = null;
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.executeUpdate();

		}
		
	public Connection getConnection() {
		return connection;
	}
	
	
	
	public String getSiteType() {
		return siteType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
