package de.tud.segroup.hganalyzer.control;

import java.util.List;

import org.tmatesoft.hg.core.HgCallbackTargetException;
import org.tmatesoft.hg.core.HgChangeset;
import org.tmatesoft.hg.core.HgChangesetHandler;
import org.tmatesoft.hg.core.HgFileRevision;

public class ChangesetToConsoleHandler implements HgChangesetHandler {
	

	public ChangesetToConsoleHandler(){
	}

	public void cset(HgChangeset changeset) throws HgCallbackTargetException {
			changeset.getAffectedFiles();
			System.out.println("revision-index: "
					+ changeset.getRevisionIndex());
			System.out.println("date" + changeset.getDate());
			System.out.println("branch: "
					+ changeset.getBranch());
			System.out.println("comment: "
					+ changeset.getComment());
			// System.out.println("added-files-count: " +
			// changeset.getAddedFiles().size());
			System.out.println("user: " + changeset.getUser());
			System.out.println("manifest-revision: "
					+ changeset.getManifestRevision());
			System.out.println("modified-files-count: "
					+ changeset.getModifiedFiles().size());
			List<HgFileRevision> modifiedFiles = changeset
					.getModifiedFiles();
			for (HgFileRevision modifiedFile : modifiedFiles) {
				System.out.println("wasCopied: "
						+ modifiedFile.wasCopied());
				System.out.println("path: "
						+ modifiedFile.getPath());
				System.out.println("revision: "
						+ modifiedFile.getRevision());
				System.out.println("flags: "
						+ modifiedFile.getFileFlags());
				System.out.println("parents: " + modifiedFile.getParents());
			}
			System.out.println();
	}
}
