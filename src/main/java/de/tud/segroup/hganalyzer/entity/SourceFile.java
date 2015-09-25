package de.tud.segroup.hganalyzer.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

@Document(indexName = "file", type = "file", shards = 5, replicas = 1, refreshInterval = "-1")
public class SourceFile {
	
	public SourceFile() {
	}
	
	public SourceFile(String path) {
		this.path = path;
	}

	@Id
	private String id;
	
	private String path;
	
	private List<FileRevision> revisions;  

	@Override
	public String toString() {
		return String.format("SourceFile [id=%s, path=%s]", id, path);
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

	public List<FileRevision> getRevisions() {
		return revisions;
	}

	public void setRevisions(List<FileRevision> revisions) {
		this.revisions = revisions;
	}

	
	
	
	
	
}
