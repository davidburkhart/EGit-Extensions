package de.andrena.egit.extensions;

import org.eclipse.jgit.revwalk.RevCommit;

public interface ICommitVisitor {

	void accept(RevCommit commit);

}
