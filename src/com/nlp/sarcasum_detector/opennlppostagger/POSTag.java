package com.nlp.sarcasum_detector.opennlppostagger;

import java.io.File;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class POSTag {
	
	/**
	 * This method takes the string as the parameter and using OpenNLP finds the POS
	 * tag for each words and returns in the form of <word,tag> hashmap.
	 * @name :post_Tag_Finder
	 * @param : String
	 * @return :  HashMap<String, String>
	 */
	public static HashMap<String, String> post_Tag_Finder(String input) throws InvalidFormatException, IOException {
		HashMap<String, String> returnMap=new LinkedHashMap<>();
		POSModel model = new POSModelLoader()	
				.load(new File("../SarcasumDetection/src/com/nlp/sarcasum_detector/static_files/en-pos-maxent.bin"));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new StringReader(input));

		perfMon.start();

		String line=lineStream.read();
		String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
				.tokenize(line);
		String[] tags = tagger.tag(whitespaceTokenizerLine);
		POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
		String[] pos=sample.toString().split(" ");
		for(int i=0;i<pos.length;i++){
			if(pos[i].startsWith("@"))
				continue;
			String[] vals=pos[i].split("_");
			if(vals[1].length()>2)
				returnMap.put(vals[0], vals[1].substring(0, 2));
			else
				returnMap.put(vals[0], vals[1]);
		}
		perfMon.incrementCounter();
		return returnMap;
	}//end of post_Tag_Finder
}//end of class POSTag
