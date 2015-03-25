package com.asus.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import asus.com.test.Test;

import com.asus.comparator.ComparatorByDensity;
import com.asus.comparator.ComparatorByMoveOrder;
import com.asus.comparator.ComparatorByOri;
import com.asus.comparator.ComparatorBySW;
import com.asus.obj.Property;

public class FileDeal {

	private Property propParam;
	private String[] strArray;

	private final static String PORT = "port";
	private final static String LAND = "land";

	/**
	 * Constructor
	 * 
	 * @param prop
	 */
	public FileDeal(Property propParam) {
		this.propParam = propParam;
		System.out.println("\npropParams:\t" + propParam.smallestWidth + "-" + propParam.density + "\t" + propParam.path);

		// get proper folder array
		strArray = readFile();

		System.out.print("Original Proper Folders(Number " + strArray.length + "): ");
		for (String s : strArray) {
			System.out.print(s + ", ");
		}
		System.out.print("\n");
	}

	/**
	 * get proper folder array
	 * 
	 * @return
	 */
	public String[] readFile() {
		
		File folder = new File(Test.FOLDER_PATH);
		List<String> folders = new ArrayList<String>();
		Set<String> multiLanSet = Utils.getMultiLanSet();

		for (String s : folder.list()) {
			if (s.startsWith("drawable") && s.split("-").length > 1) {
				if (multiLanSet.contains(s.split("-")[1]) || s.split("-")[1].equals("ldltr") || s.split("-")[1].equals("ldrtl")) {
					continue;
				}
			}

			if (s.startsWith("drawable") && !s.contains("small") && !s.contains("normal") && !s.contains("large") && !s.contains("xlarge")) {
				folders.add(s);
			}
		}

		return folders.toArray(new String[folders.size()]);
	}

	/**
	 * get Property Array with Order
	 * 
	 * @return
	 */
	public Property[] getPropertyArray() {

		Property[] propertyArray = new Property[strArray.length];
		int i = 0;
		for (String s : strArray) {

			propertyArray[i] = new Property();
			String[] splitStr = s.split("-");

			for (String ss : splitStr) {
				if (ss.startsWith("drawable")) {
					
				} else if (ss.startsWith("sw")) {
					propertyArray[i].smallestWidth = ss;
				} else if (ss.equals(PORT) || ss.equals(LAND)) {
					propertyArray[i].orientation = ss;
				} else {
					propertyArray[i].density = ss;
				}
			}
			propertyArray[i].fullname = s;
			
			i++;
		}
		
		Arrays.sort(propertyArray, new ComparatorBySW());
		Arrays.sort(propertyArray, new ComparatorByOri());
		Arrays.sort(propertyArray, new ComparatorByDensity(propParam.density));
		if (propParam.exclude != "") {
			Arrays.sort(propertyArray, new ComparatorByMoveOrder(propParam.exclude));
		}

		return propertyArray;
	}

	/**
	 * Get Delete Folder List
	 * 
	 * @return
	 */
	public List<String> getDeleteList() {
		List<String> deleteList = new ArrayList<String>();
		Matcher m2 = Utils.PATTERN_DIGITAL.matcher(propParam.smallestWidth);
		int paramSW = 0;
		if (m2.find()) {
			paramSW = Integer.parseInt(m2.group(0));
		}
		for (String s : strArray) {
			String[] splitStr = s.split("-");

			// Delete the "sw" which exceed the "sw" of the param
			for (String ss : splitStr) {
				if (ss.startsWith("sw")) {
					Matcher m1 = Utils.PATTERN_DIGITAL.matcher(ss);
					if (m1.find()) {
						if (Integer.parseInt(m1.group(0)) > paramSW) {
							deleteList.add(s);
							break;
						}
					}
				}
			}
		}
		return deleteList;
	}

	/**
	 * Write Delete Files Log
	 * 
	 * @param log
	 * @param filePath
	 * @param label
	 */
	public static void writeFile(String log, Set<String> filePath, String label) {
		FileWriter writer;
		try {
			writer = new FileWriter(log, true);
			writer.write("\r\n**************** " + label + ":\r\n");
			for (String fp : filePath) {
				writer.write(fp + "\r\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
