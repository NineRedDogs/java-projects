package com.ninereddogs.item3;

import java.util.Random;

/**
 * Singleton implemented as enum - preferred pattern for singleton
 * 
 * @author agrahame
 *
 */
public enum Elvis {
	INSTANCE;
	
	private final int age;
	private final String address;
	
	private Elvis() {
		System.out.println("private ctor for Elvis ....");
		age = new Random().nextInt(20);
		address="Graceland" + new Random().nextInt(20);
	}
	
	@Override
	public String toString() {
		return "[ " + address + " - " + age + " - " + Integer.toHexString(System.identityHashCode(this)) + " ]";
	}

	public static void main(String[] args) {
		System.out.println("start ...");
		
		Elvis elvis1 = Elvis.INSTANCE;
		System.out.println("Elvis1:" + elvis1);
		
		Elvis elvis2 = Elvis.INSTANCE;
		System.out.println("Elvis2:" + elvis2);
		
		System.out.println("end ...");
	}

}
