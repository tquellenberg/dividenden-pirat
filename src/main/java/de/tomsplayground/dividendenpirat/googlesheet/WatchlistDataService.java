package de.tomsplayground.dividendenpirat.googlesheet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class WatchlistDataService implements ICacheUpdateListener {

	private static final String WATCHLIST_SORTIERUNG = "Watchlist-Sortierung";

	@Autowired
	private WorksheetService worksheetService;

	@Autowired
	private CacheUpdater cacheUpdater;

	private static Map<Integer, WatchlistEntry> entries = new HashMap<Integer, WatchlistEntry>();

	@PostConstruct
	public void init() {
		cacheUpdater.addListener(this);
	}

	public List<WatchlistEntry> getEntries(){
		synchronized (entries) {
			if (entries.isEmpty()) {
				try {
					SpreadsheetService service = worksheetService.getService();
					WorksheetEntry worksheet = worksheetService.findWorksheetEntryByName(service, WATCHLIST_SORTIERUNG);

					// Fetch the cell feed of the worksheet.
					CellQuery cellFeedUrl = new CellQuery(worksheet.getCellFeedUrl());
					cellFeedUrl.setMinimumRow(5);
					cellFeedUrl.setMinimumCol(3);
					cellFeedUrl.setMaximumCol(23);
					for (CellEntry cell : service.getFeed(cellFeedUrl, CellFeed.class).getEntries()) {
						WatchlistEntry entryByRow = getEntryByRow(cell.getCell().getRow());
						int col = cell.getCell().getCol();
						if (col == 3) {
							entryByRow.setName(cell.getCell().getValue());
						} else if (col == 7) {
							entryByRow.setPePosition(cell.getCell().getNumericValue().intValue());
						} else if (col == 14) {
							entryByRow.setPaybackTime(cell.getCell().getNumericValue().intValue());
						} else if (col == 17) {
							entryByRow.setGrowthPosition(cell.getCell().getNumericValue().intValue());
						} else if (col == 20) {
							entryByRow.setRobustnessPosition(cell.getCell().getNumericValue().intValue());
						} else if (col == 23) {
							entryByRow.setSizePosition(cell.getCell().getNumericValue().intValue());
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
			return new ArrayList<WatchlistEntry>(entries.values());
		}
	}

	private WatchlistEntry getEntryByRow(int row) {
		if (entries.containsKey(row)) {
			return entries.get(row);
		}
		WatchlistEntry entry = new WatchlistEntry();
		entries.put(row, entry);
		return entry;
	}

	@Override
	public void flush() {
		synchronized (entries) {
			entries.clear();
		}
	}
}
