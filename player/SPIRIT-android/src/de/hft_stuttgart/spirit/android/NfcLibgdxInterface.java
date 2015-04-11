package de.hft_stuttgart.spirit.android;

import java.util.ArrayList;

import de.hft_stuttgart.spirit.NfcInterface;
import de.hft_stuttgart.spirit.NfcTagDetected;

public class NfcLibgdxInterface implements NfcInterface {
	ArrayList<NfcTagDetected> tags;

	public NfcLibgdxInterface() {
		tags = new ArrayList<NfcTagDetected>();
	}

	public void addNewTag(String tag){
		NfcTagDetected neu = new NfcTagDetected(tag);
		tags.add(neu);
	}
	@Override
	public boolean newTagDetected() {
		return (tags.size() > 0);
	}

	@Override
	public NfcTagDetected[] getTags() {
		NfcTagDetected returnTags[];
		returnTags = new NfcTagDetected[tags.size()];
		int i = 0;
		for (NfcTagDetected tag : tags){
			returnTags[i++] = tag;
		}
		tags.clear();
		return returnTags;
	}

}
