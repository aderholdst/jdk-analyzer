package de.tud.segroup.hganalyzer.control;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.tud.segroup.hganalyzer.entity.SourceFile;

public interface SourceFileRepository extends ElasticsearchRepository<SourceFile, String> {

    SourceFile findByPath(String path);
    
    Page<SourceFile> findByRevisionsChangesetRevision(String changeSetRevision, Pageable page);
    
    
    @Query("{\"nested\" : {" +
    		"\"path\" : \"revisions\"," +
    		"\"query\": " +
    		"{\"regexp\" : "
    		+ "{\"revisions.content\":"
    		+ " {\"value\": \"?0\"}}}}}")
    Page<SourceFile> findByRevisions(String revisionContent, Pageable pageable);
    
}
