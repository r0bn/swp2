/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Interaction of a scene as quiz with question and answers.
 * 
 * @author Oliver
 * 
 */
public class Chooser implements Interaction, Serializable{
	
	private String question;
	private List<String> answers;
	private List<String> items;
	private List<String> nextScenes;

	public Chooser(){
		this.answers = new ArrayList<String>();
		this.items = new ArrayList<String>();
		this.nextScenes = new ArrayList<String>();
	}
	
	public Chooser(String question, List<String> answers, List<String> nextScenes, List<String> items){
		this.question = question;
		this.answers = answers;
		this.nextScenes = nextScenes;
		this.items = items;
	}
	
	/**
	 * 
	 * @return the scenes, as list
	 */
	public List<String> getNextScenes() {
		return nextScenes;
	}

	/**
	 * 
	 * @param nextScenes the scenes to set
	 */
	public void setNextScenes(List<String> nextScenes) {
		this.nextScenes = nextScenes;
	}

	/**
	 * @return the question, as String
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
	 * @return the answers, as list
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

	/**
	 * 
	 * @return the items, as list
	 */
	public List<String> getItems() {
		return items;
	}

	/**
	 * 
	 * @param items the items to set
	 */
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
	
	/**
	 * Print the object
	 */
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
