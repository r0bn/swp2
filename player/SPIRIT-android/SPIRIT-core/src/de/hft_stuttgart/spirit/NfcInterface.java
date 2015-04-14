package de.hft_stuttgart.spirit;

public interface NfcInterface {

	public boolean newTagDetected();
	public NfcTagDetected[] getTags();
}
