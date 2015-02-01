package de.andrena.egit.extensions;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.resources.IResource;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

public class WalkHistory {

	private Repository repository;
	private IResource resource;

	public WalkHistory(Repository repository, IResource resource) {
		this.repository = repository;
		this.resource = resource;
	}

	public void walk(ICommitVisitor commitVisitor) {
		try {
			walkSafely(commitVisitor);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void walkSafely(ICommitVisitor commitVisitor) throws AmbiguousObjectException,
			IncorrectObjectTypeException, IOException, MissingObjectException {
		ObjectId head = repository.resolve(Constants.HEAD);
		if (head == null) {
			return;
		}

		RevWalk walk = setupRevWalk(head);

		for (RevCommit commit : walk) {
			commitVisitor.accept(commit);
		}
	}

	private RevWalk setupRevWalk(ObjectId head) throws MissingObjectException, IncorrectObjectTypeException,
			IOException {
		RevWalk walk = new RevWalk(repository);
		RevCommit headCommit = walk.parseCommit(head);
		walk.markStart(headCommit);

		Path resourcePath = Paths.get(resource.getLocationURI());
		Path repositoryPath = repository.getWorkTree().toPath();
		String repoRelativePath = repositoryPath.relativize(resourcePath).toString();

		TreeFilter pathFilter = PathFilterGroup.createFromStrings(repoRelativePath);
		walk.setTreeFilter(AndTreeFilter.create(pathFilter, TreeFilter.ANY_DIFF));
		return walk;
	}

}
