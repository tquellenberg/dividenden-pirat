package de.tomsplayground.dividendenpirat.googlesheet;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class Convert {

	public static BigDecimal convertNumber(String value) {
		try {
			return new BigDecimal(StringUtils.replace(StringUtils.remove(value, "."), ",", "."));
		} catch (NumberFormatException e) {
			System.out.println("Convert.convertNumber(): '" + value+"'");
			return BigDecimal.ZERO;
		}
	}
}
