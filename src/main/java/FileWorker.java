import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.Test;

public class FileWorker {

	 String getProjectPath(){
		Path currentRelativePath = Paths.get("");
		String filePath = currentRelativePath.toAbsolutePath().toString();
		return filePath;
	}
	
	ArrayList<String> readFileByLines(String filename) throws UnsupportedEncodingException{
		ArrayList<String> broadcastCategories = new ArrayList<String>();
		
		

		String projPath = getProjectPath();
		String filePath = projPath+"/publicator_files/"+filename;
		
		System.out.println("Current relative path is: " + filePath);
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String sCurrentLine;
			
			
			while ((sCurrentLine = br.readLine()) != null) {
				
				broadcastCategories.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(broadcastCategories);
	return broadcastCategories;
	}
	
	List<String> readBannersFile(String filename) throws UnsupportedEncodingException{
		
	
		String projPath = getProjectPath();
		String filePath = projPath+"/publicator_files/banners/"+filename;
		
		System.out.println("Current relative path is: " + filePath);
		StringBuilder content = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {	
				content.append(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		List<String> listOfBannersAds = new ArrayList<String>();
		StringTokenizer str = new StringTokenizer(content.toString(), "№");
		while (str.hasMoreElements()) {
			listOfBannersAds.add(str.nextElement().toString());
		}
		
		return listOfBannersAds;
	}
	
	void createFile(String filename,String content) throws IOException{
		
		
		String projPath = getProjectPath();
		String filePath = projPath+"/publicator_files/"+filename;
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			
			bw.write(content);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("File created");
	}
	
	public static void main (String [] args){
		
	}
}
