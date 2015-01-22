package de.andrena.egit.extensions;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
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

@SuppressWarnings("restriction")
public class CommitCountDecorator implements ILightweightLabelDecorator {

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IResource) {
			decorate((IResource) element, decoration);
		}
	}

	private void decorate(IResource resource, IDecoration decoration) {
		RepositoryMapping mapping = RepositoryMapping.getMapping(resource);
		if (mapping == null) {
			return;
		}

		Repository repo = mapping.getRepository();
		if (repo == null) {
			return;
		}

		try {
			decorate(decoration, resource, mapping, repo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void decorate(IDecoration decoration, IResource resource, RepositoryMapping mapping, Repository repo)
			throws AmbiguousObjectException, IncorrectObjectTypeException, IOException, MissingObjectException {
		int count = countCommits(resource, mapping, repo);

		if (count > 0) {
			decoration.addSuffix(" [" + count + "]");
		}
	}

	private int countCommits(IResource resource, RepositoryMapping mapping, Repository repo)
			throws AmbiguousObjectException, IncorrectObjectTypeException, IOException, MissingObjectException {
		ObjectId head = repo.resolve(Constants.HEAD);
		if (head == null) {
			return 0;
		}

		RevWalk walk = new RevWalk(repo);
		RevCommit headCommit = walk.parseCommit(head);
		walk.markStart(headCommit);

		String repoRelativePath = mapping.getRepoRelativePath(resource);

		TreeFilter pathFilter = PathFilterGroup.createFromStrings(repoRelativePath);
		walk.setTreeFilter(AndTreeFilter.create(pathFilter, TreeFilter.ANY_DIFF));

		return count(walk.iterator());
	}

	private int count(Iterator<?> iterator) {
		int count = 0;
		while (iterator.hasNext() ) {
			count++;
			iterator.next();
		}
		return count;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
