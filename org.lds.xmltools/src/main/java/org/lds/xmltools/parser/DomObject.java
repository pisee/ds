package org.lds.xmltools.parser;

public class DomObject {

	private String id;
	private String text;
	private String koName;
	private String enName;
	
	public DomObject(String id, String text, String koName, String enName) {
		this.id = id;
		this.text = text;
		this.koName = koName;
		this.enName = enName;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getKoName() {
		return koName;
	}
	public void setKoName(String koName) {
		this.koName = koName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
}
