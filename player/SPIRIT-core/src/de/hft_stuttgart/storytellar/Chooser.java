/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.util.ArrayList;
import java.util.List;

/**
 * Interaction of a scene as quiz with question and answers.
 * 
 * @author Oliver
 * 
 */
public class Chooser implements Interaction{
	
	private String question;
	private List<String> answers;
	private List<String> items; // Not available for normal quiz

	public Chooser(){
		this.answers = new ArrayList<String>();
		this.items = new ArrayList<String>();
	}
	
	public Chooser(String question, List<String> answers, List<String> nextScenes, List<String> items){
		this.question = question;
		this.answers = answers;
		this.items = items;
	}
	
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the answers
	 */
	public List<String> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		String strng;
		strng = "Type: " + this.getClass().getName() + "\n";
		strng += "Question: " + question + "\n";
		for (int i = 0; i < answers.size(); i++) {
			strng += "Answer " + String.valueOf((i + 1)) + ": \n";
			strng += "Text: " + answers.get(i);
			if(items.size() > i){
				strng += " Item: " + items.get(i) + "\n";
			} else {
				strng += " Item: [undef]\n";
			}
		}
		return strng;
	}

}
