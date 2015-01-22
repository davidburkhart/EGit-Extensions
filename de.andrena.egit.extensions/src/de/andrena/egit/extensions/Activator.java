package de.andrena.egit.extensions;

import java.io.IOException;

import org.eclipse.jgit.events.ConfigChangedEvent;
import org.eclipse.jgit.events.ConfigChangedListener;
import org.eclipse.jgit.events.IndexChangedEvent;
import org.eclipse.jgit.events.IndexChangedListener;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.events.RepositoryEvent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	private static Activator plugin;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Repository.getGlobalListenerList().addRefsChangedListener(new RefsChangedListener() {
			@Override
			public void onRefsChanged(RefsChangedEvent event) {
				print(event);
			}
		});
		Repository.getGlobalListenerList().addConfigChangedListener(new ConfigChangedListener() {
			@Override
			public void onConfigChanged(ConfigChangedEvent event) {
				print(event);
			}
		});
		Repository.getGlobalListenerList().addIndexChangedListener(new IndexChangedListener() {
			@Override
			public void onIndexChanged(IndexChangedEvent event) {
				print(event);
			}
		});
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	private void print(RepositoryEvent<?> event) {
		try {
			Repository repository = event.getRepository();
			System.out.println(event.getClass().getSimpleName() + ": " + repository.getBranch());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Activator getDefault() {
		return plugin;
	}
}
