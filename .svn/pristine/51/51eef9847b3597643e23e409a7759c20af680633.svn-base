package com.asus.comparator;

import java.util.Comparator;
import java.util.List;

import com.asus.obj.Property;
import com.asus.util.Utils;

/**
 * Ordered by density
 * @author Kim_Bai
 *
 */

public class ComparatorByDensity implements Comparator<Property> {
	private List<String> densityList;
	
	/**
	 * Constructor
	 * @param paramDensity
	 */
	public ComparatorByDensity(String paramDensity) {
		densityList = Utils.getDensityList(paramDensity);
	}
	
	@Override
	public int compare(Property p1, Property p2) {
		// TODO Auto-generated method stub
		int densityA = densityList.indexOf(p1.density);
		int densityB = densityList.indexOf(p2.density);
		
		if (p1.smallestWidth.equals(p2.smallestWidth) 
				&& (p1.orientation.equals(p2.orientation) || (p1.orientation != "" && p2.orientation != ""))) {
			return densityA < densityB ? -1 : 1;
		} 
		
		return 0;
	}
}
