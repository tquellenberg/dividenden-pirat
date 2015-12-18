package de.tomsplayground.dividendenpirat.cache;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import de.tomsplayground.dividendenpirat.googlesheet.WorksheetService;

@Component
public class CacheUpdater {

	private static final Logger log = LoggerFactory.getLogger(CacheUpdater.class);

	@Autowired
	private WorksheetService service;

	private DateTime lastUpdate = null;

	private org.joda.time.DateTime lastFlush = new org.joda.time.DateTime();

	private final List<ICacheUpdateListener> listeners = new ArrayList<>();

	@Scheduled(fixedDelay=600000)
	public void check() {
		try {
			WorksheetFeed worksheet = service.getWorksheet(service.getService());
			DateTime updated = worksheet.getUpdated();

			log.info("Worksheet updated: {}", updated);
			if (lastUpdate == null ||
				!lastUpdate.equals(updated) ||
				lastFlush.getDayOfMonth() != new org.joda.time.DateTime().getDayOfMonth()) {

				lastUpdate = updated;
				flushCaches();
			}
		} catch (GeneralSecurityException | IOException | ServiceException e) {
			log.error("", e);
		}
	}

	private void flushCaches() {
		log.info("flush all caches");
		lastFlush = new org.joda.time.DateTime();
		for (ICacheUpdateListener iCacheUpdateListener : listeners) {
			iCacheUpdateListener.flush();
		}
	}

	public void addListener(ICacheUpdateListener listener) {
		listeners.add(listener);
	}
}
