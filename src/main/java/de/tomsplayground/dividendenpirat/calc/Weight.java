package de.tomsplayground.dividendenpirat.calc;

public class Weight {

	public static final Weight DEFAULT = new Weight("default", 3,1,3,2,1);

	public static final Weight ALTERNATIVE = new Weight("alternative", 3,0,2,2,0);

	private final String name;

	private final int peFactor;
	private final int paybackTime;
	private final int growthFactor;
	private final int robustnessFactor;
	private final int sizeFactor;


	public Weight(String name, int peFactor, int paybackTime, int growthFactor, int robustnessFactor, int sizeFactor) {
		this.name = name;
		this.peFactor = peFactor;
		this.paybackTime = paybackTime;
		this.growthFactor = growthFactor;
		this.robustnessFactor = robustnessFactor;
		this.sizeFactor = sizeFactor;
	}

	public String getName() {
		return name;
	}

	public int getGrowthFactor() {
		return growthFactor;
	}
	public int getPaybackTime() {
		return paybackTime;
	}
	public int getPeFactor() {
		return peFactor;
	}
	public int getRobustnessFactor() {
		return robustnessFactor;
	}
	public int getSizeFactor() {
		return sizeFactor;
	}
}
