package com.asus.comparator;

import java.util.Comparator;

import com.asus.obj.Property;

/**
 * Ordered by density
 * @author Kim_Bai
 *
 */

public class ComparatorByMoveOrder implements Comparator<Property> {
	private String excludeDensity;
	
	/**
	 * Constructor
	 * @param paramDensity
	 */
	public ComparatorByMoveOrder(String excludeDensity) {
		this.excludeDensity = excludeDensity;
	}
	
	@Override
	public int compare(Property p1, Property p2) {
		// TODO Auto-generated method stub
		for(String ed : excludeDensity.split(",")) {
			if (p1.density.equals(ed)) {
				return 1;
			} else if(p2.density.equals(ed)) {
				return -1;
			}
		}
		
		return 0;
	}
}
