package com.dwarakarun.engine;

public class Collision {
	String n1, n2;
	public Collision (String n1, String n2) {
		this.n1 = n1;
		this.n2 = n2;
	}
	public boolean contains(String n) {
		if (this.n1.equals(n) || this.n2.equals(n)) {
			return true;
		}
		return false;
	}
}
