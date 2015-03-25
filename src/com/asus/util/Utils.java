package com.asus.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Utils {
	private static String[] multiLan = { "hr", "zh", "ro", "tl", "ca", "rm", "vi", "tr", "hu", "lv", "hi", "lt", "th", "de", "id", "fi",
			"sv", "fr", "bg", "sl", "sk", "uk", "da", "it", "sr", "iw", "ko", "fa", "ar", "cs", "el", "nb", "pt", "pl", "en", "ru", "es",
			"ja", "nl" };
	private static final String[] DENSITY_STR_DEFAULT = { "xxhdpi", "xhdpi", "hdpi", "tvdpi", "mdpi", "", "ldpi", "nodpi" };
	public static Pattern PATTERN_DIGITAL = Pattern.compile("\\d+");

	/**
	 * get multi-language set
	 * 
	 * @return
	 */
	public static Set<String> getMultiLanSet() {
		Set<String> multiLanSet = new HashSet<String>();
		for (String ml : multiLan) {
			multiLanSet.add(ml);
		}

		return multiLanSet;
	}

	public static List<String> getDefaultDensityList() {
		return Arrays.asList(DENSITY_STR_DEFAULT);
	}

	/**
	 * get density list
	 * 
	 * @return
	 */
	public static List<String> getDensityList(String paramDensity) {
		// Order
		String[] DENSITY_STR;
		if (paramDensity.equals("xhdpi")) {
			String[] densityStr = { "xhdpi", "xxhdpi", "hdpi", "tvdpi", "mdpi", "", "ldpi", "nodpi" };
			DENSITY_STR = densityStr;
		} else if (paramDensity.equals("hdpi")) {
			String[] densityStr = { "hdpi", "tvdpi", "xhdpi", "xxhdpi", "mdpi", "", "ldpi", "nodpi" };
			DENSITY_STR = densityStr;
		} else if (paramDensity.equals("tvdpi")) {
			String[] densityStr = { "tvdpi", "hdpi", "xhdpi", "xxhdpi", "mdpi", "", "ldpi", "nodpi" };
			DENSITY_STR = densityStr;
		} else if (paramDensity.equals("mdpi")) {
			String[] densityStr = { "mdpi", "", "tvdpi", "hdpi", "xhdpi", "xxhdpi", "ldpi", "nodpi" };
			DENSITY_STR = densityStr;
		} else if (paramDensity.equals("ldpi")) {
			String[] densityStr = { "ldpi", "mdpi", "", "tvdpi", "hdpi", "xhdpi", "xxhdpi", "nodpi" };
			DENSITY_STR = densityStr;
		} else {
			DENSITY_STR = DENSITY_STR_DEFAULT;
		}

		return Arrays.asList(DENSITY_STR);
	}
	
	/**
	 * get separate density
	 * 
	 * @param density_index_small
	 * @param density_index_large
	 * @return
	 */
	public static String[] getSeparateDensity(int density_index_small, int density_index_large) {
		String[] separateDensity = null;
		
		switch(density_index_small) {
			// xxhdpi
			case 0: 
				switch(density_index_large) {
					case 1:
						// xxhdpi, xhdpi
						separateDensity = new String[]{ "xxhdpi" };
						break;
					case 2:
						// Device A80: -sw 800,360 -density hdpi,xxhdpi
						separateDensity = new String[]{ "xxhdpi", "xhdpi" };
						break;
					case 3:
						// xxhdpi, tvdpi
						separateDensity = new String[]{ "xxhdpi", "xhdpi", "hdpi" };
						break;
					case 4:
						// xxhdpi, mdpi
						separateDensity = new String[]{ "xxhdpi", "xhdpi", "hdpi", "tvdpi" };
						break;
					default:
						break;
				}
				break;
			// xhdpi
			case 1:
				switch(density_index_large) {
					// xhdpi, hdpi
					case 2:
						separateDensity = new String[]{ "hdpi", "tvdpi" };
						break;
					// xhdpi, tvdpi
					case 3:
						separateDensity = new String[]{ "hdpi", "tvdpi" };
						break;
					// xhdpi, mdpi A68 -sw 800,360 -density mdpi,xhdpi
					case 4:
						separateDensity = new String[]{ "hdpi", "tvdpi", "mdpi", "" };
						break;
					default:
						break;
				}
			// hdpi
			case 2:
				switch(density_index_large) {
					case 3:
						// Device A11: -sw 600,360 -density tvdpi,hdpi
						separateDensity = new String[]{ "tvdpi" };
						break;
					case 4:
						// hdpi, mdpi, exclude xxhdpi,xhdpi
						separateDensity = new String[]{ "hdpi", "tvdpi"};
						break;
					default:
						break;
				}
			// tvdpi
			case 3:
				switch(density_index_large) {
					// tvdpi, mdpi
					case 4:
						separateDensity = new String[]{ "mdpi", "" };
						break;
					default:
						break;
			}
			default:
				break;
		}
		
		return separateDensity;
	}
}
