package de.tomsplayground.dividendenpirat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.tomsplayground.dividendenpirat.calc.Calculation;
import de.tomsplayground.dividendenpirat.calc.Weight;
import de.tomsplayground.dividendenpirat.googlesheet.StockEntry;
import de.tomsplayground.dividendenpirat.googlesheet.StockService;
import de.tomsplayground.dividendenpirat.googlesheet.WatchlistDataService;
import de.tomsplayground.dividendenpirat.googlesheet.WatchlistEntry;

@Component
public class Manager {

	@Autowired
	private WatchlistDataService watchlistDataService;

	@Autowired
	private Calculation calculation;
	
	@Autowired
	private StockService stockService;

	public List<WatchlistEntry> getEntries(Weight weight) {
    	return calculation.calc(watchlistDataService.getEntries(), weight);
	}
	
	public StockEntry getStockEntryByName(String stockName) {
		return stockService.getStockEntryByName(stockName);
	}
}
