package de.tomsplayground.dividendenpirat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.tomsplayground.dividendenpirat.Manager;
import de.tomsplayground.dividendenpirat.calc.Weight;
import de.tomsplayground.dividendenpirat.googlesheet.WatchlistEntry;

@RestController
public class JsonService {

	@Autowired
	private Manager manager;

    @RequestMapping("/entries")
    public @ResponseBody List<WatchlistEntry> getEntries() {
    	return manager.getEntries(Weight.DEFAULT);
    }

    @RequestMapping("/entries/alternative")
    public @ResponseBody List<WatchlistEntry> getEntriesAlternative() {
    	return manager.getEntries(Weight.ALTERNATIVE);
    }

}
