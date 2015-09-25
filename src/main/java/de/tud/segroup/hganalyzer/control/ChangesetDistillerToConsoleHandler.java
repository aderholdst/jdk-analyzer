package de.tud.segroup.hganalyzer.control;

import java.util.SortedMap;
import java.util.TreeMap;

import org.tmatesoft.hg.core.HgCallbackTargetException;
import org.tmatesoft.hg.core.HgCatCommand;
import org.tmatesoft.hg.core.HgChangeset;
import org.tmatesoft.hg.core.HgChangesetHandler;
import org.tmatesoft.hg.core.HgException;
import org.tmatesoft.hg.core.HgRepoFacade;
import org.tmatesoft.hg.core.Nodeid;
import org.tmatesoft.hg.internal.ByteArrayChannel;
import org.tmatesoft.hg.util.CancelledException;
import org.tmatesoft.hg.util.Path;

public class ChangesetDistillerToConsoleHandler implements HgChangesetHandler {
	
	private HgRepoFacade hgRepo;
	private String fileToInspect;
	private SortedMap<Integer, String> sortedRevisions;

	public SortedMap<Integer, String> getSortedRevisions() {
		return sortedRevisions;
	}

	public ChangesetDistillerToConsoleHandler(HgRepoFacade hgRepo, String fileToInspect){
		this.hgRepo = hgRepo;
		this.fileToInspect = fileToInspect;
		this.sortedRevisions = new TreeMap<>();
	}

	public void cset(HgChangeset changeset) throws HgCallbackTargetException {
			System.out.println("revision-index: "
					+ changeset.getRevisionIndex());
			System.out.println("date" + changeset.getDate());
			System.out.println("branch: "
					+ changeset.getBranch());
			System.out.println("comment: "
					+ changeset.getComment());
			System.out.println("user: " + changeset.getUser());
			System.out.println("manifest-revision: "
					+ changeset.getManifestRevision());
			
			//new HgFileRevision(hgRepo, changeset.getRevisionIndex(), null, new Path(fileToInspect))
			
			Nodeid ofInterest = changeset.getManifestRevision();
			Path file = Path.create(fileToInspect);
			HgCatCommand cmd = hgRepo.createCatCommand().file(file).changeset(changeset.getNodeid());
			
			ByteArrayChannel sink = new ByteArrayChannel();
			try {
				cmd.execute(sink);
			} catch (HgException | CancelledException e) {
				e.printStackTrace();
			}
			String result = new String(sink.toArray());
			sortedRevisions.put(changeset.getRevisionIndex(), result);
			System.out.println();
		}
}
