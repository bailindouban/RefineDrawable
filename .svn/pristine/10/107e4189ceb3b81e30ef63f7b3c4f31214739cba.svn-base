package asus.com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.asus.obj.NodeData;
import com.asus.obj.Property;
import com.asus.obj.TreeNode;
import com.asus.util.FileDeal;
import com.asus.util.Utils;

/**
 * Build tree structures for only one device or both two devices
 * 
 * @author Kim_Bai
 * 
 */
public class BuildTree {
	/**
	 * 
	 * @param propertyArray
	 * @param isExceed
	 */
	private void buildBase(Property[] propertyArray, boolean isExceed) {
		NodeData root = new NodeData(-1, "root");
		TreeNode root_node = new TreeNode(-1, null, null, root);
		TreeNode last_node = root_node;
		TreeNode[] all_node = new TreeNode[propertyArray.length];
		NodeData[] all_data = new NodeData[propertyArray.length];

		Property lastP = propertyArray[0];
		all_data[0] = new NodeData(0, propertyArray[0].fullname);
		all_node[0] = new TreeNode(0, null, null, all_data[0]);
		TreeNode.insertAsChild(all_node[0], last_node);

		int level = 0;
		for (int i = 1; i < propertyArray.length; i++) {
			if (addNewLevel(lastP, propertyArray[i])) {
				level++;
				last_node = all_node[i - 1];
			}

			all_data[i] = new NodeData(level, propertyArray[i].fullname);
			all_node[i] = new TreeNode(i, null, null, all_data[i]);

			TreeNode.insertAsChild(all_node[i], last_node);

			lastP = propertyArray[i];
		}

		TreeNode.levelTraversal(root_node, isExceed);
	}

	/**
	 * 
	 * Only one param set | eg. 600-tvdpi
	 * 
	 * @param propParams
	 */
	public void build(Property propParam) {
		int paramSW = Integer.parseInt(propParam.smallestWidth);
		
		/**
		 * Tree 1: <=sw600dp
		 */
		// Get property array of proper folders
		FileDeal fileDeal = new FileDeal(propParam);
		Property[] propertyArray = fileDeal.getPropertyArray();

		List<Property> propListA = new ArrayList<Property>();
		System.out.print("\nA: All Folders(Ordered): ");
		for (Property pArray : propertyArray) {
			if (pArray.smallestWidth != "") {
				Matcher m = Utils.PATTERN_DIGITAL.matcher(pArray.smallestWidth);
				if (m.find()) {
					int propSW = Integer.parseInt(m.group(0));
					if (propSW <= paramSW) {
						propListA.add(pArray);
					}
				}
			} else {
				propListA.add(pArray);
			}
			System.out.print(pArray.fullname + ", ");
		}

		System.out.println("\n\nPropertyList A.........");
		for (Property pA : propListA) {
			System.out.print(pA.fullname + ", ");
		}
		System.out.println("\n");

		if (propListA.size() > 0) {
			buildBase(propListA.toArray(new Property[propListA.size()]), false);
		}

		/**
		 * Tree 2: >sw600dp (to be deleted)
		 */
		List<Property> propListB = new ArrayList<Property>();

		for (Property pArray : propertyArray) {
			if (pArray.smallestWidth != "") {
				Matcher m = Utils.PATTERN_DIGITAL.matcher(pArray.smallestWidth);
				if (m.find()) {
					int propSW = Integer.parseInt(m.group(0));
					if (propSW > paramSW) {
						propListB.add(pArray);
					}
				}
			}
		}

		System.out.println("\n\nPropertyList B.........");
		for (Property p : propListB) {
			System.out.print(p.fullname + ", ");
		}
		System.out.println("\n");

		if (propListB.size() > 0) {
			buildBase(propListB.toArray(new Property[propListB.size()]), true);
		}
	}

	/**
	 * 
	 * Two params set | eg. 600-tvdpi and 360-xxhdpi
	 * 
	 * @param propParams
	 */
	public void build(Property[] propParams) {
		int paramSW_large = Integer.parseInt(propParams[0].smallestWidth);
		int paramSW_small = Integer.parseInt(propParams[1].smallestWidth);

		List<String> defaultDensityList = Utils.getDefaultDensityList();
		int density_index_small = defaultDensityList.indexOf(propParams[0].density);
		int density_index_large = defaultDensityList.indexOf(propParams[1].density);

		if (density_index_small > density_index_large) {
			int temp = density_index_small;
			density_index_small = density_index_large;
			density_index_large = temp;
		}

		/**
		 * Tree 1: <= sw600dp & > sw360dp, exclude xxhdpi,xhdpi
		 */
		FileDeal fileDealA = new FileDeal(propParams[0]);
		Property[] propertyArrayA = fileDealA.getPropertyArray();

		List<Property> propListA = new ArrayList<Property>();
		System.out.print("A: All Folders (Ordered): \t");
		for (Property pArray : propertyArrayA) {
			if (pArray.smallestWidth != "") {
				Matcher m = Utils.PATTERN_DIGITAL.matcher(pArray.smallestWidth);
				if (m.find()) {
					int propSW = Integer.parseInt(m.group(0));
					if (propSW <= paramSW_large && propSW > paramSW_small) {
						propListA.add(pArray);
					}
				}
			}
			System.out.print(pArray.fullname + "\t");
		}

		System.out.println("\n\nPropertyList A.........");
		for (Property pA : propListA) {
			System.out.print(pA.fullname + ", ");
		}
		System.out.println("\n");

		if (propListA.size() > 0) {
			buildBase(propListA.toArray(new Property[propListA.size()]), false);
		}

		/**
		 * Tree 2: <=sw360dp, exclude xxhdpi,xhdpi
		 */
		FileDeal fileDealB = new FileDeal(propParams[1]);

		Property[] propertyArrayB = fileDealB.getPropertyArray();
		List<Property> propListB = new ArrayList<Property>();

		System.out.print("B: All Folders (Ordered): \t");
		
		String[] separateDensity = Utils.getSeparateDensity(density_index_small, density_index_large);
		label:
		for (Property pArray : propertyArrayB) {
			for (String sepDensity : separateDensity) {
				if(pArray.density.equals(sepDensity)) {
					continue label;
				}
			}
			
			if (pArray.smallestWidth != "") {
				Matcher m = Utils.PATTERN_DIGITAL.matcher(pArray.smallestWidth);
				if (m.find()) {
					int propSW = Integer.parseInt(m.group(0));
					if (propSW <= paramSW_small) {
						propListB.add(pArray);
					}
				}
			} else {
				propListB.add(pArray);
			}

			System.out.print(pArray.fullname + "\t");
		}

		System.out.println("\n\nPropertyList B.........");
		for (Property pB : propListB) {
			System.out.print(pB.fullname + ", ");
		}
		System.out.println("\n");

		if (propListB.size() > 0) {
			buildBase(propListB.toArray(new Property[propListB.size()]), false);
		}
		
		/**
		 * Tree 3: <=sw360dp, only separate density (xhdpi, tvdpi)
		 */
		FileDeal fileDealC = new FileDeal(propParams[1]);
		Property[] propertyArrayC = fileDealC.getPropertyArray();

		if(separateDensity != null) {
			for (String sepDensity : separateDensity) {
				List<Property> propListC = new ArrayList<Property>();
				System.out.print("\nC: All Folders (Ordered): --- " + sepDensity + "\t");

				for (Property pArray : propertyArrayC) {
					if (pArray.density.equals(sepDensity)) {
						if (pArray.smallestWidth != "") {
							Matcher m = Utils.PATTERN_DIGITAL.matcher(pArray.smallestWidth);
							if (m.find()) {
								int propSW = Integer.parseInt(m.group(0));
								if (propSW <= paramSW_small) {
									propListC.add(pArray);
								}
							}
						} else {
							propListC.add(pArray);
						}
					}

					System.out.print(pArray.fullname + "\t");
				}

				System.out.println("\nPropertyList C.........");
				for (Property pC : propListC) {
					System.out.print(pC.fullname + ", ");
				}
				System.out.println("\n");

				if (propListC.size() > 0) {
					buildBase(propListC.toArray(new Property[propListC.size()]), false);
				}
			}
		}

		/**
		 * Tree 4: >sw600dp (to be deleted)
		 */
		FileDeal fileDealD = new FileDeal(propParams[0]);

		Property[] propertyArrayD = fileDealD.getPropertyArray();
		List<Property> propListD = new ArrayList<Property>();
		System.out.print("D: All Folders (Ordered): \t");
		for (Property pArray : propertyArrayD) {
			if (pArray.smallestWidth != "") {
				Matcher m = Utils.PATTERN_DIGITAL.matcher(pArray.smallestWidth);
				if (m.find()) {
					int propSW = Integer.parseInt(m.group(0));
					if (propSW > paramSW_large) {
						propListD.add(pArray);
					}
				}
			}

			System.out.print(pArray.fullname + "\t");
		}

		System.out.println("\n\nPropertyList D.........");
		for (Property pD : propListD) {
			System.out.print(pD.fullname + ", ");
		}
		System.out.println("\n");

		if (propListD.size() > 0) {
			buildBase(propListD.toArray(new Property[propListD.size()]), true);
		}
	}

	/**
	 * 
	 * @param lastP
	 * @param curP
	 * @return
	 */
	private boolean addNewLevel(Property lastP, Property curP) {
		if (!lastP.smallestWidth.equals(curP.smallestWidth)) {
			return true;
		}
		if (lastP.orientation.equals(curP.orientation)) {
			return true;
		}
		if (lastP.orientation == "" || curP.orientation == "") {
			return true;
		}
		if (!lastP.density.equals(curP.density)) {
			return true;
		}

		return false;
	}
}
