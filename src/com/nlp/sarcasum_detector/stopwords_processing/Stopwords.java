package com.nlp.sarcasum_detector.stopwords_processing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Stopwords {

	//global variable which holds the stopword's after reading from file
	public  Set<String> stopwords = new HashSet<>();
	
	/**
	 * This method reads the stopword's from the file and 
	 * stores it into stopwords hashmap
	 * @name :buildStopWordHashSet
	 * @param : void
	 * @return :  Set<String>
	 */
	public Set<String> buildStopWordHashSet() {
		// TODO Auto-generated method stub

		String fileName = "../SarcasumDetection/src/com/nlp/sarcasum_detector/static_files/stopWords";
		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				stopwords.add(line);
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		return stopwords;
	}// end of buildStopWordHashSet
}//end of class Stopwords
