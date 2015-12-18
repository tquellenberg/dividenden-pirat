package de.tomsplayground.dividendenpirat.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import de.tomsplayground.dividendenpirat.Manager;
import de.tomsplayground.dividendenpirat.calc.Weight;
import de.tomsplayground.dividendenpirat.googlesheet.StockEntry;
import de.tomsplayground.dividendenpirat.googlesheet.WatchlistEntry;

@Controller
public class JspController {

	@Autowired
	private Manager manager;

	@RequestMapping("/")
	public String welcome() {
		return "index";
	}

	@RequestMapping("/schwarmtabelle")
	public String schwarmtabelle(Map<String, Object> model) {
    	Weight weight = Weight.DEFAULT;
		model.put("entries", manager.getEntries(weight));
		model.put("weight", weight);
		return "schwarmtabelle";
	}

    @RequestMapping("/schwarmtabelle/alternative")
	public String alternative(Map<String, Object> model) {
    	Weight weight = Weight.ALTERNATIVE;
		model.put("entries", manager.getEntries(weight));
		model.put("weight", weight);
		return "schwarmtabelle";
	}

    @RequestMapping("/schwarmtabelle/myWeight")
	public String myWeight(Map<String, Object> model, @RequestParam int pe, @RequestParam int payback,
		@RequestParam int performance, @RequestParam int robustness, @RequestParam int size) {
    	Weight weight = new Weight("myWeight", pe, payback, performance, robustness, size);
		model.put("entries", manager.getEntries(weight));
		model.put("weight", weight);
		return "schwarmtabelle";
	}

    @RequestMapping("/aktie/{name:.*}")
    public String aktie(@PathVariable String name, Map<String, Object> model) throws NoSuchRequestHandlingMethodException {
    	String stockName;
    	try {
    		stockName = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		model.put("name", stockName);

    	StockEntry stockEntryByName = manager.getStockEntryByName(stockName);
    	if (stockEntryByName == null) {
        	throw new NoSuchRequestHandlingMethodException("aktie", JspController.class);
    	}
		model.put("entry", stockEntryByName);

		List<WatchlistEntry> entries = manager.getEntries(Weight.DEFAULT);
		entries.stream()
			.filter(e -> (stockName.equals(e.getName())))
			.findFirst()
			.ifPresent(e -> model.put("watchListEntry", e));
		model.put("watchlistSize", new Integer(entries.size()));

    	return "aktie";
    }

    @RequestMapping("/impressum")
	public String impressum() {
		return "impressum";
	}

}
