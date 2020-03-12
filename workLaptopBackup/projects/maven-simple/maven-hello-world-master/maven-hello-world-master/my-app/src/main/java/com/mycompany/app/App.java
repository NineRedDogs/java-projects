package com.mycompany.app;

/**
 * Hello world.
 *
 */
public class App {

  /**
   * main method.
   * 
   * @param args passed in args
   */
  public static void main(String[] args) {
    System.out.println("Hello World!");

    System.out.println(
                "abcdef ghij klmn opqrs tuvw xyz abcdef ghij klmn opqrs tuvw xyz abcdef ghij klmn opqrs tuvw xyz "
                + "abcdef ghij klmn opqrs tuvw xyz abcdef ghij klmn opqrs tuvw xyz abcdef ghij klmn opqrs tuvw "
                + "xyz abcdef ghij klmn opqrs tuvw xyz ");
    Object myObject = null;

    myObject = new String("hello");
    /**
      * Checker prevents this from compiling...
      */
    System.out.println("myObject: " + myObject.toString());
    /**
      * ... which is simply fantastic. It shows:
      *
      * error: [dereference.of.nullable] dereference of possibly-null reference myObject
      *
      * http://checkerframework.org
      */
    System.out.println("... but thankfully, Checker has our back: http://checkerframework.org");
  }
}
