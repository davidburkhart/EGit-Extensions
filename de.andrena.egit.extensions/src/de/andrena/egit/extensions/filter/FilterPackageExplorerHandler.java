package de.andrena.egit.extensions.filter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class FilterPackageExplorerHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		if (activePart instanceof IPackagesViewPart) {
			toggleFilter(event, (IPackagesViewPart) activePart);
		}

		return null;
	}

	private void toggleFilter(ExecutionEvent event, IPackagesViewPart packagesViewPart) throws ExecutionException {
		boolean previouslyFiltered = HandlerUtil.toggleCommandState(event.getCommand());
		if (previouslyFiltered) {
			disableFilter(packagesViewPart);
		} else {
			enableFilter(packagesViewPart);
		}
	}

	private void enableFilter(IPackagesViewPart packagesViewPart) {
		TreeViewer treeViewer = packagesViewPart.getTreeViewer();
		treeViewer.addFilter(new GitHistoryViewerFilter());
	}

	private void disableFilter(IPackagesViewPart packagesViewPart) {
		TreeViewer treeViewer = packagesViewPart.getTreeViewer();
		ViewerFilter[] filters = treeViewer.getFilters();
		for (ViewerFilter viewerFilter : filters) {
			if (viewerFilter instanceof GitHistoryViewerFilter) {
				treeViewer.removeFilter(viewerFilter);
			}
		}
	}
}
