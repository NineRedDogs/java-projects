package com.ninereddogs.item5;

import java.util.Date;

public class AvoidObjectCreation {
	
	public static void main(String[] args) {
		
		Long sum = 0L;
		System.out.println("start (1) : " + new Date().toString());
		
		for (long i =0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
		System.out.println(sum);

		System.out.println("end (1)   : " +new Date().toString());
		
		// note the following is not an autoboxed Long, so we don't create ~ 2^31 instances of a Long object ...
		long sum2 = 0L;
		System.out.println("start (2) : " + new Date().toString());
		
		for (long i =0; i < Integer.MAX_VALUE; i++) {
			sum2 += i;
		}
		System.out.println(sum2);

		System.out.println("end   (2) : " + new Date().toString());

	}

}
