package de.hft_stuttgart.spirit;

public interface SpiritWebviewHandler {

	public void setWebviewVisible(boolean visible);
	public void setAlpha(float alpha);
	public void openUrl(String url);
	public void setWebviewSize(int x, int y);
	public int getWebviewWidth();
	public int getWebviewHeight();
	public boolean isWebviewVisible();
	public void showPicture(String data);
	public void hidePicture();
	public void endStory();
}
