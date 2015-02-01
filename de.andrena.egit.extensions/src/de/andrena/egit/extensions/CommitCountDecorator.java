package de.andrena.egit.extensions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jgit.lib.Repository;

public class CommitCountDecorator implements ILightweightLabelDecorator {

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IResource) {
			decorate((IResource) element, decoration);
		}
	}

	private void decorate(IResource resource, IDecoration decoration) {
		Repository repository = new FindGitRepository(resource).getRepositoryOrNull();
		decorate(decoration, resource, repository);
	}

	private void decorate(IDecoration decoration, IResource resource, Repository repo) {
		int count = countCommits(resource, repo);

		if (count > 0) {
			decoration.addSuffix(" [" + count + "]");
		}
	}

	private int countCommits(IResource resource, Repository repository) {
		CommitCounter counter = new CommitCounter();
		new WalkHistory(repository, resource).walk(counter);
		return counter.getCount();
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
