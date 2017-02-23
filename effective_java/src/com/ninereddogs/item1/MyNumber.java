package com.ninereddogs.item1;

public class MyNumber implements NumberIF {

    protected int theNumber;
    
    public static NumberIF createOddNumber() {
        return new OddNumber();
    }
    
    public static NumberIF createEvenNumber() {
       return new EvenNumber(); 
    }

    @Override
    public int getNumber() {
        return theNumber;
    }

    @Override
    public void setNumber(int newNum) {
        this.theNumber = newNum;
        
    }
    
    public static void main(String[] args) {
        NumberIF oddNum = MyNumber.createOddNumber();
        System.out.println("Created odd number : " + oddNum.getClass().getName() + " [" + oddNum.getNumber() + "]");
        
        NumberIF evenNum = MyNumber.createEvenNumber();
        System.out.println("Created even number : " + evenNum.getClass().getName() + " [" + evenNum.getNumber() + "]");
        
    }
}
