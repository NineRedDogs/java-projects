package com.ninereddogs.item1;

import java.util.Random;

public class OddNumber extends MyNumber {
	
    OddNumber() {
        int newNum=0;
        Random rx = new Random();
        boolean createdOdd=false;
        while (!createdOdd) {
            newNum = rx.nextInt(100);
            createdOdd = ((newNum % 2) == 1);
        }
        setNumber(newNum);
        System.out.println(" using ODD: " + getNumber() + " ...");
    }
}
