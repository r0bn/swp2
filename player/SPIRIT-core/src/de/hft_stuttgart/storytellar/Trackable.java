package de.hft_stuttgart.storytellar;

public class Trackable {
	private String id;
	private String algorithm;
	private String enabled;
	private String src;
	private String size;
	
	public Trackable(String id, String algorithm) {
		this.setId(id);
		this.algorithm = algorithm;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String isEnabled() {
		return enabled;
	}
	public void setEnabled(String string) {
		this.enabled = string;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		String s;
		s = "ID: " + this.getId() + "\n";
		s += "Algorithm: " + this.getAlgorithm() + "\n";
		s += "enabled: " + this.isEnabled() + "\n";
		s += "src: " + this.getSrc() + "\n";
		s += "size: " + this.getSize() + "\n";
		return s;
	}
	
}
