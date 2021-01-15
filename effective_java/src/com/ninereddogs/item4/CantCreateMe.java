package com.ninereddogs.item4;

public class CantCreateMe {
	
	private static int num=0;
	
	public static boolean doUtilityMethod() {
		return true;
	}
	
	public static void incrementNumber() {
		num++;
	}
	
	// Do not allow creation of a CantCreateMe instance .....
	private CantCreateMe() {
		throw new AssertionError("Attempted to create instance of CantCreateMe");
	}
	
	public static void main(String[] args) {
		CantCreateMe inst = new CantCreateMe();
	}

}
