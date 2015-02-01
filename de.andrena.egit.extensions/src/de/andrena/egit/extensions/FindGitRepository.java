package de.andrena.egit.extensions;

import org.eclipse.core.resources.IResource;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jgit.lib.Repository;

@SuppressWarnings("restriction")
public class FindGitRepository {

	private IResource resource;

	public FindGitRepository(IResource resource) {
		this.resource = resource;
	}

	public Repository getRepositoryOrNull() {
		// TODO Find repository without use of restricted API
		RepositoryMapping mapping = RepositoryMapping.getMapping(resource);
		return mapping == null ? null : mapping.getRepository();
	}
}
