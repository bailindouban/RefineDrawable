package com.asus.obj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import asus.com.test.Test;

import com.asus.util.FileDeal;

/**
 * Deal with tree node
 * 
 * @author Kim_Bai
 * 
 */
public class TreeNode {
	private int id;
	private TreeNode parent;
	private ArrayList<TreeNode> children;
	private NodeData data;

	private static Set<String> deletePathRe = new LinkedHashSet<String>();
	private static Set<String> compareFilesAll = new HashSet<String>();
	private static Set<String> compareFilesPort = new HashSet<String>();
	private static Set<String> compareFilesLand = new HashSet<String>();

	private static Set<String> compareFilesAllEx = new HashSet<String>();
	private static Set<String> compareFilesPortEx = new HashSet<String>();
	private static Set<String> compareFilesLandEx = new HashSet<String>();

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param parent
	 * @param children
	 * @param data
	 */
	public TreeNode(int id, TreeNode parent, ArrayList<TreeNode> children, NodeData data) {
		super();
		this.id = id;
		this.parent = parent;
		this.children = children;
		this.data = data;
	}

	/**
	 * Is Root
	 * 
	 * @return
	 */
	public boolean isRoot() {
		if (this.parent == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Is Leaf
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		if (this.children == null || this.children.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Broad First Search
	 * 
	 * @param treeNode
	 */
	public static void levelTraversal(TreeNode treeNode, boolean isExceed) {
		if (treeNode == null) {
			return;
		}

		deletePathRe.clear();
		if (!isExceed) {
			compareFilesAll.clear();
			compareFilesPort.clear();
			compareFilesLand.clear();
		} else {
			compareFilesAll = compareFilesAllEx;
			compareFilesPort = compareFilesPortEx;
			compareFilesLand = compareFilesLandEx;
		}

		if (treeNode.isLeaf()) {
			System.out.print("Leaf: ");
			treeNode.data.output();
		} else {
			System.out.println("Deleting Redundant ..........");

			Queue<TreeNode> queue = new LinkedList<TreeNode>();
			queue.offer(treeNode);
			while (queue.size() > 0) {
				// Retrieves and removes the head of this queue, or returns null
				// if this queue is empty
				TreeNode node = queue.poll();
				//System.out.print("Not Leaf: ");
				node.data.output();

				deleteFiles(node, isExceed);

				if (!node.isLeaf()) {
					for (int i = 0; i < node.children.size(); i++) {
						// Inserts the specified element into this queue
						queue.offer(node.children.get(i));
					}
				}
			}
		}

		deletePathRe.add("File Number: " + deletePathRe.size());
		FileDeal.writeFile(Test.DELETE_LOG_PATH, deletePathRe, "Deleting Redundant");
	}
	
	/**
	 * 
	 * @param node
	 */
	private static void deleteFiles(TreeNode node, boolean isExceed) {
		// root
		if (node.data.level == -1) {
			return;
		}

		String curFolderName = node.data.content;
		ArrayList<TreeNode> parent = null;

		/**
		 * Add compare drawable resources
		 */
		if (node.data.level > 0) {
			parent = node.getParent().getParent().getChildren();

			// add only drawable (without land or port)
			if (parent.size() == 1 && (!parent.get(0).data.content.contains("land") && !parent.get(0).data.content.contains("port"))) {
				File parentFolder = new File(Test.FOLDER_PATH, parent.get(0).data.content);

				for (String pf : parentFolder.list()) {
					if (!pf.contains(".xml")) {
						compareFilesAll.add(pf);
					}
				}
			}

			// compute land parent nodes
			if (parent.get(0).data.content.contains("land")) {
				File parentFolder = new File(Test.FOLDER_PATH, node.getParent().data.content);

				for (String pf : parentFolder.list()) {
					if (!pf.contains(".xml")) {
						compareFilesLand.add(pf);
					}
				}
			} else if (parent.size() > 1 && parent.get(1).data.content.contains("land")) {
				File parentFolder = new File(Test.FOLDER_PATH, parent.get(1).data.content);

				for (String pf : parentFolder.list()) {
					if (!pf.contains(".xml")) {
						compareFilesLand.add(pf);
					}
				}
			}

			// compute port parent nodes
			if (parent.get(0).data.content.contains("port")) {
				File parentFolder = new File(Test.FOLDER_PATH, parent.get(0).data.content);

				for (String pf : parentFolder.list()) {
					if (!pf.contains(".xml")) {
						compareFilesPort.add(pf);
					}
				}
			} else if (parent.size() > 1 && parent.get(1).data.content.contains("port")) {
				File parentFolder = new File(Test.FOLDER_PATH, parent.get(1).data.content);

				for (String pf : parentFolder.list()) {
					if (!pf.contains(".xml")) {
						compareFilesPort.add(pf);
					}
				}
			}
		}

		/**
		 * Delete drawable resources
		 */
		File curFolder = new File(Test.FOLDER_PATH, curFolderName);
		if (curFolderName.contains("land")) {
			// compare and delete land folder
			for (String curF : curFolder.list()) {
				if (compareFilesLand.contains(curF) || compareFilesAll.contains(curF)) {
					File file = new File(curFolder, curF);
					deletePathRe.add(file.getPath());
					file.delete();
				}
			}

			// delete exceed xml
			if (!isExceed) {
				for (String curF : curFolder.list()) {
					if (!curF.contains(".xml")) {
						compareFilesLandEx.add(curF);
					}
				}
			}
		} else if (curFolderName.contains("port")) {
			// compare and delete port folder
			for (String curF : curFolder.list()) {
				if (compareFilesPort.contains(curF) || compareFilesAll.contains(curF)) {
					File f = new File(curFolder, curF);
					deletePathRe.add(f.getPath());
					f.delete();
				}
			}
			
			if (!isExceed) {
				for (String curF : curFolder.list()) {
					if (!curF.contains(".xml")) {
						compareFilesPortEx.add(curF);
					}
				}
			}
		} else {
			// compute jiaoji of parent nodes which are port and land
			for (String comP : compareFilesPort) {
				if (compareFilesLand.contains(comP)) {
					compareFilesAll.add(comP);
				}
			}

			// compare and delete drawable folder
			for (String curF : curFolder.list()) {
				if (compareFilesAll.contains(curF)) {
					File file = new File(curFolder, curF);
					deletePathRe.add(file.getPath());
					file.delete();
				}
			}

			// delete exceed xml
			if (!isExceed) {
				for (String curF : curFolder.list()) {
					if (!curF.contains(".xml")) {
						compareFilesAllEx.add(curF);
					}
				}
			}
		}
	}

	/**
	 * Search specific treeNode by id with the method of Broad First Search
	 * 
	 * @param id
	 * @param treeNode
	 * @return
	 */
	public static TreeNode search(int id, TreeNode treeNode) {
		if (treeNode == null) {
			return null;
		}

		if (treeNode.isLeaf()) {
			if (treeNode.id == id) {
				return treeNode;
			} else {
				return null;
			}
		} else {
			Queue<TreeNode> queue = new LinkedList<TreeNode>();
			queue.offer(treeNode);
			while (queue.size() > 0) {
				TreeNode node = queue.poll();
				if (node.id == id) {
					return node;
				}

				if (!node.isLeaf()) {
					for (int i = 0; i < node.children.size(); i++) {
						queue.offer(node.children.get(i));
					}
				}
			}
		}

		return null;
	}

	/**
	 * Insert Child
	 * 
	 * @param childNode
	 * @param currentNode
	 * @return
	 */
	public static boolean insertAsChild(TreeNode childNode, TreeNode currentNode) {
		if (childNode == null || currentNode == null) {
			return false;
		} else {
			if (currentNode.isLeaf()) {
				ArrayList<TreeNode> children = new ArrayList<TreeNode>();
				children.add(childNode);
				currentNode.children = children;
				childNode.parent = currentNode;
			} else {
				currentNode.children.add(childNode);
				childNode.parent = currentNode;
			}

			return true;
		}
	}

	/**
	 * Insert Subling
	 * 
	 * @param sublingNode
	 * @param currentNode
	 * @return
	 */
	public static boolean insertAsSubling(TreeNode sublingNode, TreeNode currentNode) {
		if (sublingNode == null || currentNode == null) {
			return false;
		} else {
			if (currentNode.isRoot()) {
				return false;
			} else {
				currentNode.parent.children.add(sublingNode);
				return true;
			}
		}
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeNode> children) {
		this.children = children;
	}

	public NodeData getData() {
		return data;
	}

	public void setData(NodeData data) {
		this.data = data;
	}

}
