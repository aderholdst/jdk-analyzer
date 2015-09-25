package de.tud.segroup.hganalyzer.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.tud.segroup.hganalyzer.control.SourceFileRepository;
import de.tud.segroup.hganalyzer.entity.SourceFile;

@RestController
@RequestMapping("api/sourcefile")
public class SourceFileResource {
	
		@Autowired
		private SourceFileRepository sourceFileRepository;

		@RequestMapping(method = RequestMethod.GET)
		Page<SourceFile> findAll(Pageable page) {
			return sourceFileRepository.findAll(page);
		}
		
		@RequestMapping(value="/{id}", method = RequestMethod.GET)
		SourceFile findById(@PathVariable("id") String id) {
			return sourceFileRepository.findOne(id);
		}
		
		@RequestMapping(value="/changeset/{changeSetRevision}",method = RequestMethod.GET)
		Page<SourceFile> findByChangeset(@PathVariable("changeSetRevision") String changeSetId, Pageable page) {
			return sourceFileRepository.findByRevisionsChangesetRevision(changeSetId, page);
		}
		
		@RequestMapping(value="/search/{revisionContent}",method = RequestMethod.GET)
		Page<SourceFile> findByRevisions(@PathVariable("revisionContent") String revisionContent, Pageable page) {
			return sourceFileRepository.findByRevisions(revisionContent, page);
		}

}
