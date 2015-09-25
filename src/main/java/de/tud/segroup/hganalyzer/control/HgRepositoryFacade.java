package de.tud.segroup.hganalyzer.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.hg.core.HgCallbackTargetException;
import org.tmatesoft.hg.core.HgChangeset;
import org.tmatesoft.hg.core.HgChangesetHandler;
import org.tmatesoft.hg.core.HgException;
import org.tmatesoft.hg.core.HgLogCommand;
import org.tmatesoft.hg.core.HgRepoFacade;
import org.tmatesoft.hg.core.HgRepositoryNotFoundException;
import org.tmatesoft.hg.internal.ByteArrayChannel;
import org.tmatesoft.hg.repo.HgChangelog;
import org.tmatesoft.hg.repo.HgChangelog.RawChangeset;
import org.tmatesoft.hg.repo.HgDataFile;
import org.tmatesoft.hg.repo.HgRepository;
import org.tmatesoft.hg.repo.HgRevisionMap;
import org.tmatesoft.hg.repo.HgRuntimeException;
import org.tmatesoft.hg.util.CancelledException;
import org.tmatesoft.hg.util.Path;
import org.tmatesoft.hg.util.ProgressSupport;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import de.tud.segroup.hganalyzer.entity.Change;
import de.tud.segroup.hganalyzer.entity.FileRevision;

/**
 * At the moment the main method of this class is the entry point for the
 * extraction.
 * 
 * @author Stefan
 *
 */
public class HgRepositoryFacade {

	Logger logger = LoggerFactory.getLogger(HgRepositoryFacade.class);
	
	private final HgRepoFacade hgRepo;
	private final FileDistiller fileDistiller;
	private final HgRevisionMap<HgChangelog> hgRevisionMap;

	public HgRepositoryFacade(String repoLocation) {
		File location = new File(repoLocation);
		hgRepo = new HgRepoFacade();
		try {
			hgRepo.initFrom(location);
			hgRevisionMap = new HgRevisionMap<HgChangelog>(hgRepo
					.getRepository().getChangelog()).init();
			fileDistiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		} catch (HgRepositoryNotFoundException e) {
			throw new IllegalArgumentException(
					"Location of the repository is not valid", e);
		}

	}

	public List<FileRevision> determineRevisions(String pathToFile) {
		HgRepository repository = hgRepo.getRepository();
		List<FileRevision> revisions = new ArrayList<FileRevision>();
		HgDataFile fileNode = repository.getFileNode(pathToFile);
		int latestFileRevision = fileNode.getLastRevision();
		HgRevisionMap<HgDataFile> fileMap = new HgRevisionMap<HgDataFile>(
				fileNode).init();
		File lastFile = null;
		File currentFile = null;
		for (int fileRevisionIndex = 0; fileRevisionIndex <= latestFileRevision; fileRevisionIndex++) {
			lastFile = currentFile;

			// Extract revision ids
			FileRevision fileRevision = new FileRevision();
			fileRevision.setPath(pathToFile);
			fileRevision.setIdx(fileRevisionIndex);
			fileRevision.setRevision(fileMap.revision(fileRevisionIndex)
					.toString());
			int csetIndex = fileNode
					.getChangesetRevisionIndex(fileRevisionIndex);
			fileRevision.setChangesetRevision(hgRevisionMap.revision(csetIndex)
					.toString());

			// Extract Information out of Changeset
			RawChangeset changeset = repository.getChangelog().changeset(
					hgRevisionMap.revision(csetIndex));
			fileRevision.setComment(changeset.comment());
			fileRevision.setDate(changeset.date());
			fileRevision.setBranch(changeset.branch());
			fileRevision.setExtras(changeset.extras().toString());
			fileRevision.setUser(changeset.user());
			
			// Analyze Changes
			ByteArrayChannel sink = new ByteArrayChannel();
			try {
				fileNode.content(fileRevisionIndex, sink);
			} catch (HgRuntimeException | CancelledException e) {
				logger.error("Error while fetching file contents", e);	
			}
			currentFile = createFile(fileRevision.getRevision(), sink.toArray());
			if (fileRevisionIndex == 0) {
				fileRevision.setContent(new String(sink.toArray()));
			} else {
				fileRevision.setChanges(distillChangesFromFiles(lastFile,
						currentFile));
			}
			revisions.add(fileRevision);

		}
		return revisions;
	}

	private List<Change> distillChangesFromFiles(File oldFile, File newFile) {
		List<Change> changes = new ArrayList<>();
		fileDistiller.extractClassifiedSourceCodeChanges(oldFile, newFile);
		List<SourceCodeChange> sourceCodeChanges = fileDistiller
				.getSourceCodeChanges();
		for (SourceCodeChange srcChange : sourceCodeChanges) {
			Change change = mapSourceChange(srcChange);
			changes.add(change);
		}
		return changes;
	}

	private Change mapSourceChangeGeneral(SourceCodeChange srcChange) {
		Change change = new Change();
		change.setSignificanceLevel(srcChange.getSignificanceLevel().toString());
		change.setChangeType(srcChange.getChangeType().toString());
		change.setStartPosition(srcChange.getChangedEntity().getStartPosition());
		change.setEndPosition(srcChange.getChangedEntity().getEndPosition());
		change.setCodeEntityType(srcChange.getChangedEntity().getType()
				.toString());
		change.setContent(srcChange.getChangedEntity().getUniqueName());
		return change;
	}

	private Change mapSourceChange(SourceCodeChange srcChange) {

		Change change = mapSourceChangeGeneral(srcChange);
		change.setType(srcChange.getClass().getSimpleName());
		switch (change.getType()) {
		case "Update":
			return mapSourceChange(change, (Update) srcChange);
		}
		return change;

	}

	private Change mapSourceChange(Change change, Update srcChange) {
		change.setNewContent(srcChange.getNewEntity().getUniqueName());
		return change;
	}

	public Set<Path> listAllManagedFiles() {
		final HashSet<Path> affectedFiles = new HashSet<Path>();
		HgLogCommand cmd = hgRepo.createLogCommand();
		try {
			cmd.execute(new HgChangesetHandler() {

				@Override
				public void cset(HgChangeset cset)
						throws HgCallbackTargetException, HgRuntimeException {
					affectedFiles.addAll(cset.getAffectedFiles());
				}
			});
		} catch (HgCallbackTargetException | HgException | CancelledException e) {
			e.printStackTrace();
		}
		return affectedFiles;
	}

	private File createFile(String uid, byte[] bs) {
		try {
			String fileName = "./work/" + uid + ".java";
			java.nio.file.Path writtenFile = Files.write(Paths.get(fileName),
					bs);
			return writtenFile.toFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
