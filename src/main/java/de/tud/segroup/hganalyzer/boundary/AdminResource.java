package de.tud.segroup.hganalyzer.boundary;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tmatesoft.hg.internal.PathGlobMatcher;
import org.tmatesoft.hg.util.Path;

import de.tud.segroup.hganalyzer.config.Constants;
import de.tud.segroup.hganalyzer.control.FileRevisionRepository;
import de.tud.segroup.hganalyzer.control.HgRepositoryFacade;
import de.tud.segroup.hganalyzer.entity.FileRevision;
import de.tud.segroup.hganalyzer.entity.SourceFile;

@RestController
@RequestMapping("/api/admin")
public class AdminResource {

	Logger logger = LoggerFactory.getLogger(AdminResource.class);
	
	@Autowired
	private FileRevisionRepository fileRevisionRepository;

	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public void importFilesFromRepository() {
		logger.info("Beginning import of files");
		prepareImport();
		HgRepositoryFacade hgRepositoryFacade = new HgRepositoryFacade("C:\\dev-data\\jdk7u\\jdk\\");
		Set<Path> managedFiles = hgRepositoryFacade.listAllManagedFiles();
		//PathGlobMatcher globMatcher = new PathGlobMatcher("src/share/classes/com/sun/beans/finder/**/*.java");
		PathGlobMatcher globMatcher = new PathGlobMatcher("src/**/*.java");
		for (Path filePath : managedFiles) {
			if (globMatcher.accept(filePath)) {
				SourceFile sourceFile = new SourceFile(filePath.toString());
				List<FileRevision> fileRevisions = hgRepositoryFacade.determineRevisions(filePath.toString());
				this.fileRevisionRepository.save(fileRevisions);
				logger.debug("Processing {}", filePath);
			}
		}
		logger.info("Finished import of files");
	}

	private void prepareImport() {
		this.fileRevisionRepository.deleteAll();
		File f = new File(Constants.DIRECTORY_WORK);
		try {
			if(f.exists()){
				FileUtils.cleanDirectory(f);
			} else {
				FileUtils.forceMkdir(f);
			}
		} catch (IOException e) {
			logger.error("Error while preparing work directory", e);
		}
	}
}
