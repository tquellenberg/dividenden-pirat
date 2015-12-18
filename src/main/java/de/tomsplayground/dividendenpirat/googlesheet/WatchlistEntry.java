package de.tomsplayground.dividendenpirat.googlesheet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WatchlistEntry {

	private String name;
	private int pePosition;
	private int paybackTime;
	private int growthPosition;
	private int robustnessPosition;
	private int sizePosition;
	private int combinedValue;

	public int getPePosition() {
		return pePosition;
	}
	public void setPePosition(int pePosition) {
		this.pePosition = pePosition;
	}
	public int getGrowthPosition() {
		return growthPosition;
	}
	public void setGrowthPosition(int growthPosition) {
		this.growthPosition = growthPosition;
	}
	public int getRobustnessPosition() {
		return robustnessPosition;
	}
	public void setRobustnessPosition(int robustnessPosition) {
		this.robustnessPosition = robustnessPosition;
	}
	public int getSizePosition() {
		return sizePosition;
	}
	public void setSizePosition(int sizePosition) {
		this.sizePosition = sizePosition;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getUrlEncodedName() {
		try {
			return URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	public void setCombinedValue(int combinedValue) {
		this.combinedValue = combinedValue;
	}
	public int getCombinedValue() {
		return combinedValue;
	}
	public int getPaybackTime() {
		return paybackTime;
	}
	public void setPaybackTime(int paybackTime) {
		this.paybackTime = paybackTime;
	}
}
