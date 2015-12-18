package de.tomsplayground.dividendenpirat.googlesheet;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
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
public class StockService implements ICacheUpdateListener {

	private static final int EARNINGS_EURO_ROW_OFFSET = 3;
	private static final int DIVIDEND_ROW_OFFSET = 5;
	private static final int FORWARD_PE_ROW_OFFSET = 8;

	private static final String DATENERFASSUNG = "Datenerfassung";

	@Autowired
	private WorksheetService worksheetService;

	@Autowired
	private CurrentPE currentPE;

	@Autowired
	private CacheUpdater cacheUpdater;

	private final Map<String, StockEntry> stockEntries = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		cacheUpdater.addListener(this);
	}

	public StockEntry getStockEntryByName(String name) {
		if (stockEntries.containsKey(name)) {
			return stockEntries.get(name);
		}

		StockEntry stockEntry = new StockEntry(name);
		try {
			SpreadsheetService service = worksheetService.getService();
			WorksheetEntry worksheet = worksheetService.findWorksheetEntryByName(service, DATENERFASSUNG);

			int rowStart = findRowStart(service, worksheet, name);
			if (rowStart <= 0) {
				return null;
			}

			CellQuery cellFeedUrl = new CellQuery(worksheet.getCellFeedUrl());
			cellFeedUrl.setMinimumRow(rowStart + EARNINGS_EURO_ROW_OFFSET);
			cellFeedUrl.setMaximumRow(rowStart + FORWARD_PE_ROW_OFFSET);
			cellFeedUrl.setMinimumCol(2);
			cellFeedUrl.setMaximumCol(25);
			cellFeedUrl.setReturnEmpty(true);
			List<CellEntry> entries = service.getFeed(cellFeedUrl, CellFeed.class).getEntries();

			// Earnings in Euro
			List<CellEntry> row = entries.stream()
				.filter(c -> c.getCell().getRow() == (rowStart+EARNINGS_EURO_ROW_OFFSET))
				.collect(Collectors.toList());
			int year = 2004;
			for (CellEntry cell : row) {
				if (cell.getCell().getNumericValue() != null) {
					BigDecimal v = Convert.convertNumber(cell.getCell().getValue());
					stockEntry.getEntry(year++).setEarningsEuro(v);
				} else {
					break;
				}
			}

			// Dividend
			row = entries.stream()
				.filter(c -> c.getCell().getRow() == (rowStart+DIVIDEND_ROW_OFFSET))
				.collect(Collectors.toList());
			year = 2004;
			for (CellEntry cell : row) {
				if (cell.getCell().getValue() == null) {
					break;
				}
				BigDecimal v = Convert.convertNumber(cell.getCell().getValue());
				stockEntry.getEntry(year++).setDividend(v);
			}

			// Forward PE
			row = entries.stream()
				.filter(c -> c.getCell().getRow() == (rowStart+FORWARD_PE_ROW_OFFSET))
				.collect(Collectors.toList());
			year = 2004;
			boolean nextValueIsMedian = false;
			for (CellEntry cell : row) {
				if (cell.getCell().getNumericValue() != null) {
					BigDecimal v = Convert.convertNumber(cell.getCell().getValue());
					if (nextValueIsMedian) {
						stockEntry.setMedianForwardPE(v);
						break;
					} else {
						stockEntry.getEntry(year++).setForwardPE(v);
					}
				} else if (StringUtils.equals(cell.getCell().getValue(), "Median:")) {
					nextValueIsMedian = true;
				}
			}

			// Add current PE
			stockEntry.getEntry(year).setForwardPE(currentPE.getCurrentForwardPe(name));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
		stockEntries.put(name, stockEntry);
		return stockEntry;
	}

	private int findRowStart(SpreadsheetService service, WorksheetEntry worksheet, String name) throws IOException, ServiceException {
		CellQuery cellFeedUrl = new CellQuery(worksheet.getCellFeedUrl());
		cellFeedUrl.setMinimumRow(2);
		cellFeedUrl.setMinimumCol(0);
		cellFeedUrl.setMaximumCol(1);
		for (CellEntry cell : service.getFeed(cellFeedUrl, CellFeed.class).getEntries()) {
			int r = cell.getCell().getRow()-3;
			if (r % 11 == 0) {
				String stockName = cell.getCell().getValue();
				if (StringUtils.equals(stockName, name)) {
					return cell.getCell().getRow();
				}
			}
		}
		return -1;
	}

	@Override
	public void flush() {
		stockEntries.clear();
	}

}
