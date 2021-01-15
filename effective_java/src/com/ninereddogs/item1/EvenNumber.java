package com.ninereddogs.item1;

import java.util.Random;

public class EvenNumber extends MyNumber {
	
    EvenNumber() {
        int newNum=0;
        Random rx = new Random();
        boolean createdEven=false;
        while (!createdEven) {
            newNum = rx.nextInt(100);
            createdEven = ((newNum % 2) == 0);
        }
        setNumber(newNum);
        System.out.println(" using EVEN: " + getNumber() + " ...");
    }

}
