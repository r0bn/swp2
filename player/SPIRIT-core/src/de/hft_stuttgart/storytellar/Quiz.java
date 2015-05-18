/**
 * 
 */
package de.hft_stuttgart.storytellar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oliver
 * Interaction of a scene as quiz with question and answers.
 */
public class Quiz implements Interaction{
	
	private String question;
	private List<String> answers;
	private List<String> nextScenes;

	public Quiz(){
		this.answers = new ArrayList<String>();
		this.nextScenes = new ArrayList<String>();
	}
	
	public Quiz(String question, List<String> answers, List<String> nextScenes){
		this.question = question;
		this.answers = answers;
		this.nextScenes = nextScenes;
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

	/**
	 * @param nextScenes the nextScenes to set
	 */
	public void setNextScenes(List<String> nextScenes) {
		this.nextScenes = nextScenes;
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
		strng = "Question: " + question + "\n";
		for (int i = 0; i < answers.size(); i++) {
			strng += "Answer " + String.valueOf((i + 1)) + ": " + answers.get(i) + " " + nextScenes.get(i) + "\n";
		}
		return strng;
	}

}
