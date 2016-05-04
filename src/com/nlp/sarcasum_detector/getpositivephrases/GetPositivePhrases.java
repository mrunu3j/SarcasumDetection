package com.nlp.sarcasum_detector.getpositivephrases;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.nlp.sarcasum_detector.ngramrules.SarcasumRules;
import com.nlp.sarcasum_detector.opennlppostagger.POSTag;

public class GetPositivePhrases {
	/**
	 * This method reads the sarcastic tweet's from the file and calls the getpost tag method
	 * to get the,compares those tags with the predefine rules of negative sentiments and
	 * if any rules are satisfying then puts those words into positive phrase list 
	 * puts into postweets list.
	 * @param stopwords 
	 * @name : grtpositivephrases
	 * @param : void
	 * @return :  List<String>
	 */
	public static List<String> grtpositivephrases(Set<String> stopwords){
		System.out.println("Calculating the positive phrases.....");
		List<String> positivePhrases=new LinkedList<String>();
		FileInputStream fstream;
		BufferedReader br;
		POSTag posTag=new POSTag();
		SarcasumRules s=new SarcasumRules();
		HashMap<String, String> wordsPOS = null;
		try {
			fstream = new FileInputStream("../SarcasumDetection/src/com/nlp/sarcasum_detector/static_files/trainPositivePhraseData");
			br = new BufferedReader(new InputStreamReader(fstream));
			String strLine = null;
			while (!(strLine = br.readLine()).equalsIgnoreCase("endOffile"))   {
				if(strLine.equalsIgnoreCase("\n") || strLine.equalsIgnoreCase(""))
					continue;
				strLine=strLine.replaceAll("[,\\(\\)\\-\\;\\!\\:\\.\"\\?]", "");
				wordsPOS=posTag.post_Tag_Finder(strLine);
				List<String> partOfSpeech=new LinkedList(wordsPOS.values());
				int i=0;
				for(String val :wordsPOS.keySet()){
					i++;
					if(stopwords.contains(val)){
						continue;
					}
					if(i<=partOfSpeech.size()-1 && s.getRules().contains(partOfSpeech.get(i)) && 
							!positivePhrases.contains(val)){
						positivePhrases.add(val);
					}
					
					if(i<partOfSpeech.size()-2 && s.getRules().contains(partOfSpeech.get(i)+"+"+partOfSpeech.get(i+1)) 
							&& !positivePhrases.contains(val)) 
					{
						positivePhrases.add(val);
					}
					if(i<partOfSpeech.size()-3 && s.getRules().contains(partOfSpeech.get(i+1)+"+"+partOfSpeech.get(i+2))
							&&  !positivePhrases.contains(val))
					{
						positivePhrases.add(val);
					}
					 if(i<partOfSpeech.size()-3 && s.getRules().contains(partOfSpeech.get(i+1)+"+"+partOfSpeech.get(i+2))
							 && !positivePhrases.contains(val))
					{
						 positivePhrases.add(val);
					}
				}
				
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done calculating positive phrases.......!!!!");
		return positivePhrases;
	}//end of grtpositivephrases
}//end of class GetPositivePhrases
