package de.andrena.egit.extensions.filter;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.internal.runtime.AdapterManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import de.andrena.egit.extensions.WalkHistory;

@SuppressWarnings("restriction")
final class GitHistoryViewerFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return isProject(element) || commitsMatchFilter(element);
	}

	private boolean commitsMatchFilter(Object element) {
		IResource resource = (IResource) AdapterManager.getDefault().getAdapter(element, IResource.class);
		if (resource == null) {
			return true;
		}

		RepositoryMapping mapping = RepositoryMapping.getMapping(resource);
		if (mapping == null) {
			return true;
		}

		Repository repository = mapping.getRepository();
		if (repository == null) {
			return true;
		}

		FindMostRecentCommit findMostRecentCommit = new FindMostRecentCommit();
		WalkHistory walkHistory = new WalkHistory(repository, resource);
		walkHistory.walk(findMostRecentCommit);

		return show(findMostRecentCommit.getMostRecentCommit());
	}

	private boolean show(RevCommit mostRecentCommit) {
		long millis = TimeUnit.SECONDS.toMillis(mostRecentCommit.getCommitTime());
		long now = System.currentTimeMillis();
		long sevenDays = TimeUnit.DAYS.toMillis(7);
		long age = now - millis;
		return age < sevenDays;
	}

	private boolean isProject(Object element) {
		return AdapterManager.getDefault().getAdapter(element, IProject.class) != null;
	}
}