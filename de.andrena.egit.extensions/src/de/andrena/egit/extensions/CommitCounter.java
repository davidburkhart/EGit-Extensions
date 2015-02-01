package de.andrena.egit.extensions;

import org.eclipse.jgit.revwalk.RevCommit;

final class CommitCounter implements ICommitVisitor {

	private int count = 0;

	public int getCount() {
		return count;
	}

	@Override
	public void accept(RevCommit commit) {
		count++;
	}
}