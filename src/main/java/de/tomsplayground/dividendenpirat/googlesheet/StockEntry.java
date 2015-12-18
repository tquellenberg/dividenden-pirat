package de.tomsplayground.dividendenpirat.googlesheet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class StockEntry {

	public static class YearEntry {
		private final int year;
		private BigDecimal forwardPE = BigDecimal.ZERO;
		private BigDecimal earningsEuro = BigDecimal.ZERO;
		private BigDecimal dividend = BigDecimal.ZERO;
		private BigDecimal dividendChangePercent = null;
		private BigDecimal earningsChangePercent = null;

		public YearEntry(int year) {
			this.year = year;
		}

		public BigDecimal getForwardPE() {
			return forwardPE;
		}
		public void setForwardPE(BigDecimal forwardPE) {
			this.forwardPE = forwardPE;
		}
		public BigDecimal getEarningsEuro() {
			return earningsEuro;
		}
		public void setEarningsEuro(BigDecimal earningsEuro) {
			this.earningsEuro = earningsEuro;
		}
		public BigDecimal getDividendChangePercent() {
			return dividendChangePercent;
		}
		public void setDividendChangePercent(BigDecimal dividendChangePercent) {
			this.dividendChangePercent = dividendChangePercent;
		}
		public BigDecimal getEarningsChangePercent() {
			return earningsChangePercent;
		}
		public void setEarningsChangePercent(BigDecimal earningsChangePercent) {
			this.earningsChangePercent = earningsChangePercent;
		}
		public void setDividend(BigDecimal dividend) {
			this.dividend = dividend;
		}
		public BigDecimal getDividend() {
			return dividend;
		}
		public int getYear() {
			return year;
		}
	}

	private final String name;

	private BigDecimal medianForwardPE = BigDecimal.ZERO;
	private BigDecimal peRelation = BigDecimal.ZERO;

	private final List<YearEntry> entries = new ArrayList<>();

	public StockEntry(String name) {
		this.name = name;
	}

	public YearEntry getEntry(int year) {
		return entries.stream().filter(t -> t.year == year)
		.findFirst().orElseGet(() -> {
			YearEntry entry = new YearEntry(year);
			entries.add(entry);
			return entry;
		});
	}

	private void updateEntries() {
		if (entries.size() <= 1) {
			return;
		}
		// calculate earning changes
		for (int i=1; i < entries.size(); i++) {
			BigDecimal oldEarning = entries.get(i-1).getEarningsEuro();
			BigDecimal newEarning = entries.get(i).getEarningsEuro();
			if (newEarning.signum() == 1 && oldEarning.signum() == 1) {
				BigDecimal earningChange = newEarning
					.divide(oldEarning, new MathContext(10, RoundingMode.HALF_EVEN))
					.subtract(BigDecimal.ONE)
					.multiply(new BigDecimal("100"))
					.setScale(0, RoundingMode.HALF_EVEN);
				entries.get(i).setEarningsChangePercent(earningChange);
			}
		}
		// calculate dividend changes
		for (int i=1; i < entries.size(); i++) {
			BigDecimal oldDividend = entries.get(i-1).getDividend();
			BigDecimal newDividend = entries.get(i).getDividend();
			if (newDividend.signum() == 1 && oldDividend.signum() == 1) {
				BigDecimal change = newDividend
					.divide(oldDividend, new MathContext(10, RoundingMode.HALF_EVEN))
					.subtract(BigDecimal.ONE)
					.multiply(new BigDecimal("100"))
					.setScale(0, RoundingMode.HALF_EVEN);
				entries.get(i).setDividendChangePercent(change);
			}
		}
		// calculate PE relation
		YearEntry lastPe = entries.stream()
			.filter(e -> (e.forwardPE != null && e.forwardPE.signum() > 0))
			.reduce((previous, current) -> current)
			.get();
		if (medianForwardPE != null && medianForwardPE.signum() > 0) {
			peRelation = lastPe.forwardPE
					.divide(medianForwardPE, new MathContext(10, RoundingMode.HALF_EVEN))
					.subtract(BigDecimal.ONE)
				.multiply(new BigDecimal(100));
			peRelation = peRelation.setScale(1, RoundingMode.HALF_EVEN);
		}
	}

	public List<YearEntry> getEntries() {
		updateEntries();
		return entries;
	}

	public String getName() {
		return name;
	}

	public void setMedianForwardPE(BigDecimal medianForwardPE) {
		this.medianForwardPE = medianForwardPE;
	}

	public BigDecimal getMedianForwardPE() {
		return medianForwardPE;
	}

	public BigDecimal getPeRelation() {
		return peRelation;
	}

	public void setPeRelation(BigDecimal peRelation) {
		this.peRelation = peRelation;
	}
}
