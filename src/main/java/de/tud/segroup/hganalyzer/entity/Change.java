package de.tud.segroup.hganalyzer.entity;

public class Change {

	private String significanceLevel;
	private String changeType;
	private int startPosition;
	private int endPosition;
	private String codeEntityType;
	private String content;
	private String type;
	private String newContent;
	
	public String getSignificanceLevel() {
		return significanceLevel;
	}

	public void setSignificanceLevel(String significanceLevel) {
		this.significanceLevel = significanceLevel;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String setChangeType) {
		this.changeType = setChangeType;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public String getCodeEntityType() {
		return codeEntityType;
	}

	public void setCodeEntityType(String codeEntityType) {
		this.codeEntityType = codeEntityType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNewContent() {
		return newContent;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}


}
