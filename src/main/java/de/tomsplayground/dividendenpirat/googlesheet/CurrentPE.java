package de.tomsplayground.dividendenpirat.googlesheet;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import de.tomsplayground.dividendenpirat.cache.CacheUpdater;
import de.tomsplayground.dividendenpirat.cache.ICacheUpdateListener;

@Component
public class CurrentPE implements ICacheUpdateListener {

	private static final String WORKSHEET_NAME = "KGV-Berechnung";

	@Autowired
	private WorksheetService worksheetService;

	@Autowired
	private CacheUpdater cacheUpdater;

	public static class Entry {
		private final String name;
		private final BigDecimal forwardPe;

		public Entry(String name, BigDecimal forwardPe) {
			this.name = name;
			this.forwardPe = forwardPe;
		}
	}

	private final Map<String, Entry> entries = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		cacheUpdater.addListener(this);
		initCache();
	}

	public void initCache() {
		try {
			SpreadsheetService service = worksheetService.getService();
			WorksheetEntry worksheetEntry = worksheetService.findWorksheetEntryByName(service, WORKSHEET_NAME);

			CellQuery cellFeedUrl = new CellQuery(worksheetEntry.getCellFeedUrl());
			cellFeedUrl.setMinimumRow(7);
			cellFeedUrl.setMinimumCol(1);
			cellFeedUrl.setMaximumCol(17);
			String name ="";
			BigDecimal forwardPe = BigDecimal.ZERO;
			for (CellEntry cell : service.getFeed(cellFeedUrl, CellFeed.class).getEntries()) {
				int col = cell.getCell().getCol();
				String value = cell.getCell().getValue();
				if (col == 1) {
					name = value;
				}
				if (col == 17) {
					if (cell.getCell().getNumericValue() != null) {
						forwardPe = Convert.convertNumber(value);
						entries.put(name, new Entry(name, forwardPe));
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	public BigDecimal getCurrentForwardPe(String name) {
		Entry entry = entries.get(name);
		if (entry == null) {
			return BigDecimal.ZERO;
		}
		return entry.forwardPe;
	}

	@Override
	public void flush() {
		initCache();
	}
}
