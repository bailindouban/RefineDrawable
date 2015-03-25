package asus.com.test;

import com.asus.obj.Property;

/**
 * Main enter
 * 
 * @author Kim_Bai
 * 
 */
public class Test {
	// res folder path
	public static String FOLDER_PATH;
	// delete log file path
	public static String DELETE_LOG_PATH;

	public static void main(String[] args) {
		
		// only print version
		if(args.length == 1 && (args[0].equalsIgnoreCase("-v") || args[0].equalsIgnoreCase("-version"))) {
			System.out.println("Drawable Resource Tool: Version 0.0.1");
		} 
		// parse user input params into property array
		else {
			Test test = new Test();
			Property[] propParams = test.parseCommand(args);
			if (propParams != null) {
				FOLDER_PATH = propParams[0].path;
				DELETE_LOG_PATH = FOLDER_PATH + "Delete.log";

				BuildTree bt = new BuildTree();

				switch (numberSW) {
				case 1:
					bt.build(propParams[0]);
					break;
				case 2:
					// Adjust parameters order in order to set larger sw before smaller sw.
					int paramSW1 = Integer.parseInt(propParams[0].smallestWidth);
					int paramSW2 = Integer.parseInt(propParams[1].smallestWidth);
					
					if (paramSW1 < paramSW2) {
						Property temp = propParams[0];
						propParams[0] = propParams[1];
						propParams[1] = temp;
					}

					bt.build(propParams);
					break;
				default:
					System.out.println("You should input no more than two params, like: -n 2 -sw 800,360 -density hdpi,xxhdpi -path testpath");
					break;
				}

				System.out.println("\nFinished!");
			} else {
				System.out.println("You may like to input two params, like: -n 2 -sw 800,360 -density hdpi,xxhdpi -path testpath");
			}
		}
	}

	private static int numberSW = 0;

	/**
	 * parse user input params into property array
	 * 
	 * @param params
	 * @return
	 */
	private Property[] parseCommand(String[] params) {
		// decide whether the param numbers of sw and density are equal
		int numberDensity = 0;
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			if (param.equals("-sw")) {
				numberSW = params[++i].split(",").length;
			} else if (param.equals("-density")) {
				numberDensity = params[++i].split(",").length;
			}
		}

		if (numberSW != numberDensity) {
			return null;
		}

		// init property array
		Property[] propParams = new Property[numberSW];
		for (int i = 0; i < numberSW; i++) {
			propParams[i] = new Property();
		}

		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			if (param.equals("-sw")) {
				int j = 0;
				for (String s : params[++i].split(",")) {
					propParams[j++].smallestWidth = s;
				}
			} else if (param.equals("-density")) {
				int j = 0;
				for (String s : params[++i].split(",")) {
					propParams[j++].density = s;
				}
			} else if (param.equals("-exclude")) {
				i++;
				for(int j = 0; j < numberSW; j++) {
					propParams[j].exclude = params[i];
				}
			} else if (param.equals("-path")) {
				i++;
				for(int j = 0; j < numberSW; j++) {
					propParams[j].path = params[i];
				}
			}
		}

		return propParams;
	}

}