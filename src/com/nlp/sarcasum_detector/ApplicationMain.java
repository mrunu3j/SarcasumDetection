package com.nlp.sarcasum_detector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nlp.sarcasum_detector.generateprobablisticmodel.NaiveBaseProbablisticModel;
import com.nlp.sarcasum_detector.getpositivephrases.GetPositivePhrases;
import com.nlp.sarcasum_detector.gettweets.GetTweets;
import com.nlp.sarcasum_detector.ngramrules.SarcasumRules;
import com.nlp.sarcasum_detector.opennlppostagger.POSTag;
import com.nlp.sarcasum_detector.stopwords_processing.Stopwords;

import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;

public class ApplicationMain {

	public static List<String> positivePhrases=new LinkedList<>();
	public static Set<String> stopwords = new HashSet<>();
	public static List<String> positiveTweets=new LinkedList<>();
	public static List<String> negativeTweets=new LinkedList<>();
	public static double[] positiveTweetsprobablity;
	public static double[] negativeTweetsprobablity;

	public static void main(String[] args) throws InvalidFormatException, IOException {
		// TODO Auto-generated method stub
		ApplicationMain app=new ApplicationMain();
		Stopwords st=new Stopwords();
		NaiveBaseProbablisticModel naive=new NaiveBaseProbablisticModel();
		//reads the stop-words from the file and puts into the map
		stopwords=st.buildStopWordHashSet();
		//it gose's through the sarcastic tweet's train data and using POS tags generates the list of positive phrases
		//which are following the rules written for the negative sentiments
		app.readPositivePhrases();
		GetPositivePhrases gpos=new GetPositivePhrases();
		//positivePhrases=gpos.grtpositivephrases(stopwords);
		positiveTweetsprobablity=new double[positivePhrases.size()];
		negativeTweetsprobablity=new double[positivePhrases.size()];
		//Read the positive and negative tweet's from file
		GetTweets getTwits=new GetTweets();
		positiveTweets=getTwits.getPositiveTweetsFromFile();
		negativeTweets=getTwits.getNegativeTweetsFromFile();
		//call naive base algorithm to generate probablistic model
		positiveTweetsprobablity=naive.calculateProbablity(positiveTweets,negativeTweets,positivePhrases,positiveTweetsprobablity,negativeTweetsprobablity);
		System.out.println("Probablity of the positive phrases:");
		System.out.println(Arrays.toString(positiveTweetsprobablity));
		// we calculate the accuracy of sarcastic and non-sarcastic tweet's 
		calculateAccuracyOfSarcasticTweets();
		//calculateAccuracyOfNonSarcasticTweets();
	}//end of main

	private static void calculateAccuracyOfNonSarcasticTweets() throws IOException {
		// TODO Auto-generated method stub
		POSTag posTag=new POSTag();
		HashMap<String, String> wordsPOS = null;
		System.out.println("calculating non sarcastic accuracy");
		try {
			int totalnonSarcasticTweets=0;
			for(int k=0;k<negativeTweets.size();k++){
				if(" ".equalsIgnoreCase(negativeTweets.get(k)) || "".equalsIgnoreCase(negativeTweets.get(k)))
					continue;
				wordsPOS = posTag.post_Tag_Finder(negativeTweets.get(k));
				List<String> partOfSpeech=new LinkedList(wordsPOS.values());
				double sarcastic=0,nonSarcastic=0;
				SarcasumRules s=new SarcasumRules();
				int i=0;
				for(String val :wordsPOS.keySet()){
					i++;
					
					if(positivePhrases.contains(val.toLowerCase())){
						int index=positivePhrases.indexOf(val.toLowerCase());
						if(i<=partOfSpeech.size()-1 && s.getRules().contains(partOfSpeech.get(i)))
						{
							sarcastic+=positiveTweetsprobablity[index];
							nonSarcastic+=1-positiveTweetsprobablity[index];
						}
						else if(i<partOfSpeech.size()-2 && s.getRules().contains(partOfSpeech.get(i)+"+"+partOfSpeech.get(i+1))) 
						{
							sarcastic+=positiveTweetsprobablity[index];
							nonSarcastic+=1-positiveTweetsprobablity[index];
						}
						else if(i<partOfSpeech.size()-3 && s.getRules().contains(partOfSpeech.get(i+1)+"+"+partOfSpeech.get(i+2)))
						{
							sarcastic+=positiveTweetsprobablity[index];
							nonSarcastic+=1-positiveTweetsprobablity[index];
						}
						else if(i<partOfSpeech.size()-3 && s.getRules().contains(partOfSpeech.get(i+1)+"+"+partOfSpeech.get(i+2)))
						{
							sarcastic+=positiveTweetsprobablity[index];
							nonSarcastic+=1-positiveTweetsprobablity[index];
						}
					}
				}
				if(sarcastic>nonSarcastic){
				}
				else{
					totalnonSarcasticTweets++;
				}

			}
			System.out.println("Accuracy on non sarcastic tweets ="+ (totalnonSarcasticTweets*100)/negativeTweets.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end of calculateAccuracyOfNonSarcasticTweets

	private static void calculateAccuracyOfSarcasticTweets() throws IOException {
		// TODO Auto-generated method stub
		POSTag posTag=new POSTag();
		HashMap<String, String> wordsPOS = null;
		System.out.println("calculating accuracy of sarcastic tweets:");
		try {
			int totalSarcasticTweets=0;
			for(int k=0;k<positiveTweets.size();k++){
				if(" ".equalsIgnoreCase(positiveTweets.get(k)) || "".equalsIgnoreCase(positiveTweets.get(k)))
					continue;
				wordsPOS = posTag.post_Tag_Finder(positiveTweets.get(k));
				List<String> partOfSpeech=new LinkedList(wordsPOS.values());
				double sarcastic=0,nonSarcastic=0;
				SarcasumRules s=new SarcasumRules();
				int i=0;
				String previousWord=null;
				for(String val : wordsPOS.keySet()){
					i++;
					if(positivePhrases.contains(val.toLowerCase())){
						int index=positivePhrases.indexOf(val.toLowerCase());
						if(val.equalsIgnoreCase("#sarcasm") || val.equalsIgnoreCase("#sarcastweet")){
							sarcastic+=positiveTweetsprobablity[index];
						}
						else if(i<=partOfSpeech.size()-1 && s.getRules().contains(partOfSpeech.get(i))){
							sarcastic+=positiveTweetsprobablity[index];
							nonSarcastic+=1-positiveTweetsprobablity[index];
						}
						else if(i<partOfSpeech.size()-2 && s.getRules().contains(partOfSpeech.get(i)+"+"+partOfSpeech.get(i+1))) 
						{
							sarcastic+=positiveTweetsprobablity[index];
							nonSarcastic+=1-positiveTweetsprobablity[index];
						}
						else if(i<partOfSpeech.size()-3 && s.getRules().contains(partOfSpeech.get(i+1)+"+"+partOfSpeech.get(i+2)))
						{
							sarcastic+=positiveTweetsprobablity[index];
							nonSarcastic+=1-positiveTweetsprobablity[index];
						}

					}
					previousWord=val;
				}
				if(sarcastic>nonSarcastic){
					totalSarcasticTweets++;
				}
			}
			System.out.println("Accuracy on sarcastic tweets  ="+ (totalSarcasticTweets*100)/positiveTweets.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end of calculateAccuracyOfSarcasticTweets

	public void readPositivePhrases(){
		FileInputStream fstream;
		BufferedReader br;
		String strLine = null;
		try {
			fstream = new FileInputStream("../SarcasumDetection/src/com/nlp/sarcasum_detector/static_files/finalPosTerms");
			br = new BufferedReader(new InputStreamReader(fstream));
			while (!(strLine = br.readLine()).equalsIgnoreCase("endofFile"))   {
				String m=strLine.replaceAll("[,\\(\\)\\-\\;\\!\\:\\.\"\\?]", "");
				if(!"".equalsIgnoreCase(m) && !positivePhrases.contains(m) &&!stopwords.contains(m)){
					positivePhrases.add(m.toLowerCase());
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(strLine);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(strLine);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end of readPositivePhrases
}//end of ApplicationMain
