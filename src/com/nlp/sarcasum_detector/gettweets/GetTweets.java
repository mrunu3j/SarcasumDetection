package com.nlp.sarcasum_detector.gettweets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class GetTweets {

	//global variables which holds sarcastic and non non sarcastic tweet's.
	List<String> postweets=new LinkedList<>();
	List<String> negtweets=new LinkedList<>();

	/**
	 * This method reads the sarcastic tweet's from the file and 
	 * puts into postweets list.
	 * @name : getPositiveTweetsFromFile
	 * @param : void
	 * @return :  List<String>
	 */
	public List<String> getPositiveTweetsFromFile(){
		FileInputStream fstream;
		BufferedReader br;

		try {
			fstream = new FileInputStream("../SarcasumDetection/src/com/nlp/sarcasum_detector/static_files/sarcasticTweetTestData");
			br = new BufferedReader(new InputStreamReader(fstream));
			//StringBuffer brNew=new StringBuffer();
			String strLine = null;
			while (!(strLine = br.readLine()).equalsIgnoreCase("endOffile"))   {

				if(strLine.equalsIgnoreCase("\n"))
					continue;

				if(strLine.startsWith("@")){
					int index=strLine.indexOf(' ');
					strLine=strLine.substring(index+1);

				}
				if(strLine.startsWith("RT")){
					strLine=strLine.substring(3);

				}
				postweets.add(strLine);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postweets;
	}//end of getPositiveTweetsFromFile
	
	/**
	 * This method reads the sarcastic tweet's from the file and 
	 * puts into negtweets list.
	 * @name : getNegativeTweetsFromFile
	 * @param : void
	 * @return :  List<String>
	 */
	public List<String> getNegativeTweetsFromFile(){
		FileInputStream fstream;
		BufferedReader br;
		String strLine = null;
		String previousline=null;
		try {
			fstream = new FileInputStream("../SarcasumDetection/src/com/nlp/sarcasum_detector/static_files/nonSarcasticTweetTestData");
			br = new BufferedReader(new InputStreamReader(fstream));
			while (!(strLine = br.readLine()).equalsIgnoreCase("endOffile"))   {
				if(strLine.equalsIgnoreCase("\n"))
					continue;
			//	System.out.println(strLine);

				if(strLine.startsWith("@")){
					int index=strLine.indexOf(' ');
					strLine=strLine.substring(index+1);

				}
				if(strLine.startsWith("RT")){
					strLine=strLine.substring(3);

				}
				negtweets.add(strLine);
				previousline=strLine;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return negtweets;
	}//end of getNegativeTweetsFromFile
}//end of class GetTweets
