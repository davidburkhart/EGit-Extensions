package de.andrena.egit.extensions.filter;

import org.eclipse.jgit.revwalk.RevCommit;

import de.andrena.egit.extensions.ICommitVisitor;

final class FindMostRecentCommit implements ICommitVisitor {

	private RevCommit mostRecentCommit = null;

	public RevCommit getMostRecentCommit() {
		return mostRecentCommit;
	}

	@Override
	public void accept(RevCommit commit) {
		if (mostRecentCommit == null) {
			mostRecentCommit = commit;
		}

		if (commit.getCommitTime() > mostRecentCommit.getCommitTime()) {
			mostRecentCommit = commit;
		}
	}
}