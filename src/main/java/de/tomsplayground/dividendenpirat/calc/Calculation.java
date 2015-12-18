package de.tomsplayground.dividendenpirat.calc;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import de.tomsplayground.dividendenpirat.googlesheet.WatchlistEntry;

@Component
public class Calculation {

	public List<WatchlistEntry> calc(List<WatchlistEntry> entries, Weight weight) {
		calculateCombinedValues(entries, weight);
		Collections.sort(entries, new Comparator<WatchlistEntry>() {
			@Override
			public int compare(WatchlistEntry o1, WatchlistEntry o2) {
				return Integer.compare(o1.getCombinedValue(), o2.getCombinedValue());
			}
		});
		return entries;
	}

	private void calculateCombinedValues(List<WatchlistEntry> entries, Weight weight) {
		for (WatchlistEntry entry : entries) {
			int value = (entry.getPePosition() * weight.getPeFactor())
					+ (entry.getGrowthPosition() * weight.getGrowthFactor())
					+ (entry.getPaybackTime() * weight.getPaybackTime())
					+ (entry.getRobustnessPosition() * weight.getRobustnessFactor())
					+ (entry.getSizePosition() * weight.getSizeFactor());
			entry.setCombinedValue(value);
		}
	}

}
