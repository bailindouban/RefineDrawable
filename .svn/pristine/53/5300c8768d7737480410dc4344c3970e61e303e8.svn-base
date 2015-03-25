package com.asus.comparator;

import java.util.Comparator;
import java.util.regex.Matcher;

import com.asus.obj.Property;
import com.asus.util.Utils;

/**
 * Ordered by smallest width
 * @author Kim_Bai
 *
 */

public class ComparatorBySW implements Comparator<Property> {
	// Match more than zero numbers
	@Override
	public int compare(Property p1, Property p2) {
		// TODO Auto-generated method stub
		String swA = p1.smallestWidth;
		String swB = p2.smallestWidth;
		
		int swA2 = 0;
		int swB2 = 0;
		if(swA != "") {
			Matcher m1 = Utils.PATTERN_DIGITAL.matcher(swA);
			if(m1.find()) {
				swA2 = Integer.parseInt(m1.group(0));
			}
		}
		
		if(swB != "") {
			Matcher m2 = Utils.PATTERN_DIGITAL.matcher(swB);
			if(m2.find()) {
				swB2 = Integer.parseInt(m2.group(0));
			}
		}
		return swA2 == swB2 ? 0 : (swA2 > swB2 ? -1 : 1);
	}
}
