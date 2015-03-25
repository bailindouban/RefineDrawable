package com.asus.obj;

public class NodeData {
	public int level;
	public String content;
	
	public NodeData() {
		
	}
	
	public NodeData(int level, String content) {
		super();
		this.level = level;
		this.content = content;
	}
	
	public void output() {
		System.out.println("Level " + level + " " + content);
	}
	
	
}
