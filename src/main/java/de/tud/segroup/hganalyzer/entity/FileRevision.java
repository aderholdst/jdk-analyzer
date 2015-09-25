package de.tud.segroup.hganalyzer.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "file-revision", type = "file-revision", shards = 5, replicas = 1, refreshInterval = "-1")
public class FileRevision {
	@Id
	private String id;
	@Field(type=FieldType.String,index=FieldIndex.not_analyzed)
	private String path;
	private String content;
	@Field(type=FieldType.Nested)
	private List<Change> changes;
	@Field(type=FieldType.String,index=FieldIndex.not_analyzed)
	private String revision;
	@Field(type=FieldType.String,index=FieldIndex.not_analyzed)
	private String changesetRevision;
	private String comment;
	@Field(type=FieldType.Date)
	private Date date;
	private String branch;
	private String extras;
	private int idx;
	@Field(type=FieldType.String,index=FieldIndex.not_analyzed)
	private String user;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Change> getChanges() {
		return changes;
	}
	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getChangesetRevision() {
		return changesetRevision;
	}
	public void setChangesetRevision(String changesetRevision) {
		this.changesetRevision = changesetRevision;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getExtras() {
		return extras;
	}
	public void setExtras(String extras) {
		this.extras = extras;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser(){
		return user;
	}
	
	
}
