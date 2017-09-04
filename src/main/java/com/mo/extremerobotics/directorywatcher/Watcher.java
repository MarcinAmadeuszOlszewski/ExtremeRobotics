package com.mo.extremerobotics.directorywatcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.log4j.Logger;

public class Watcher {
	final static Logger logger = Logger.getLogger(Watcher.class);
	private WatchService watcher;
	private Path directory;
	private FromFileToDb fromFileToDb = new FromFileToDb();

	public Watcher(Path dir) {
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			dir.register(watcher, ENTRY_CREATE);
			directory = dir;
		} catch (IOException e) {
			this.watcher = null;
			logger.error("Watcher: ERROR IN INITIATING WATCHER: ", e);
		}
	}

	public void processEvents() {
		while (true) {

			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();

				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW) {
					continue;
				}

				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = directory.resolve(name);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					logger.error("processEvents: ", e);
				}
				fromFileToDb.file(child);
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}
}
