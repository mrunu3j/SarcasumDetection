package com.nlp.sarcasum_detector.ngramrules;

import java.util.LinkedList;
import java.util.List;

public class SarcasumRules {
	
	
	//global variable which holds the rule of POS tags defines negative sentiments
	public List<String> rules=new LinkedList<>();

	/**
	 * This is the default constructor which gets call after object creation of class
	 * and adds the predefine rules for the negative sentiments 
	 * into rues map
	 * @name :SarcasumRules
	 * @param : void
	 */
	
	public SarcasumRules(){
		rules.add("VB");
		rules.add("VB+VB");
		rules.add("V+RB");
		rules.add("RB+V");
		rules.add("TO+VB");
		rules.add("VB+NN");
		rules.add("VB+PRO");
		rules.add("VB+JJ");

	}//end of constructor SarcasumRules

	/**
	 * This method returns the list of rules for the negative sentiments.
	 * @name : getRules
	 * @param : void
	 * @return :  List<String>
	 */
	public List<String> getRules() {
		return rules;
	}//end of getRules

}//end of class SarcasumRules
