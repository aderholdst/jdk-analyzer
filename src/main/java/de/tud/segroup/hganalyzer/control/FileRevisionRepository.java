package de.tud.segroup.hganalyzer.control;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.tud.segroup.hganalyzer.entity.FileRevision;

public interface FileRevisionRepository extends ElasticsearchRepository<FileRevision, String> {

	List<FileRevision> findByPath(String path);
    
    Page<FileRevision> findByChangesetRevision(String changeSetRevision, Pageable page);
    
    
//    @Query("{\"nested\" : {" +
//    		"\"path\" : \"revisions\"," +
//    		"\"query\": " +
//    		"{\"regexp\" : "
//    		+ "{\"revisions.content\":"
//    		+ " {\"value\": \"?0\"}}}}}")
//    Page<SourceFile> findByRevisions(String revisionContent, Pageable pageable);
    
}
