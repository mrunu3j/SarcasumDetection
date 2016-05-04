package com.nlp.sarcasum_detector.generateprobablisticmodel;

import java.io.IOException;
import java.util.List;

import opennlp.tools.tokenize.WhitespaceTokenizer;

public class NaiveBaseProbablisticModel {

	/**
	 * This method reads the sarcastic and non-sarcastic tweet's from list's and 
	 * calculates its count of occurrence in both categories. finly apply byes rule to 
	 * calculate the probability.
	 * @name : calculateProbablity
	 * @param : List<String> positiveTweets
	 * @param : List<String> negativeTweets
	 * @param : List<String> positivePhrases
	 * @param : double[] positiveTweetsprobablity
	 * @param : double[] negativeTweetsprobablity
	 * @return :  double[] 
	 */
	public static double[] calculateProbablity(List<String> positiveTweets,List<String> negativeTweets,
			List<String> positivePhrases,double[] positiveTweetsprobablity,double[] negativeTweetsprobablity)
					throws IOException {
		// TODO Auto-generated method stub
		//calculate the count of positive phases occurring in sarcastic tweet's
		for(int i=0;i<positiveTweets.size();i++){
			String valuePos=positiveTweets.get(i);
			if(" ".equalsIgnoreCase(valuePos) || "".equalsIgnoreCase(valuePos))
				continue;
			String[] valuePosArray=WhitespaceTokenizer.INSTANCE.tokenize(valuePos);
			for(int k=0;k<valuePosArray.length;k++){
				if(positivePhrases.contains(valuePosArray[k])){
					double count=positiveTweetsprobablity[positivePhrases.indexOf(valuePosArray[k])];
					positiveTweetsprobablity[positivePhrases.indexOf(valuePosArray[k])]=count+1;
				}
			}//end of for k
		}//end of i
		//calculate the count of positive phases occurring in non-sarcastic tweet's
		for(int j=0;j<negativeTweets.size();j++){
			String valueNeg=negativeTweets.get(j);
			if(" ".equalsIgnoreCase(valueNeg) || "".equalsIgnoreCase(valueNeg))
				continue;
			String[] valuePosArray=WhitespaceTokenizer.INSTANCE.tokenize(valueNeg);
			for(int k=0;k<valuePosArray.length;k++){
				if(positivePhrases.contains(valuePosArray[k].toLowerCase())){
					double count=negativeTweetsprobablity[positivePhrases.indexOf(valuePosArray[k].toLowerCase())];
					negativeTweetsprobablity[positivePhrases.indexOf(valuePosArray[k].toLowerCase())]=count+1;
				}
			}//end of for k
		}//end of j
		
		//apply baye's rule to calculate the probablity of positive phrase in both sarcastic and non-sarcastic categories 
		for(int i=0;i<positiveTweetsprobablity.length;i++){
			Double probablity=(positiveTweetsprobablity[i]+1)/(positiveTweetsprobablity[i]+negativeTweetsprobablity[i]+2);
			positiveTweetsprobablity[i]=probablity;
		}//end of for i
		return positiveTweetsprobablity;
	}//end of calculateProbablity
}//end of class NaiveBaseProbablisticModel
