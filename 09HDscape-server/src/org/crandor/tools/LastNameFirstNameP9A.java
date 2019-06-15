package org.crandor.tools;

import java.util.Random;
import java.util.Scanner;

public class LastNameFirstNameP9A {

    private static final int PHYSICAL_SIZE = 100;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random generator = new Random();

        int logicalSize;
        String menuChoice;
        int[] origList = new int[PHYSICAL_SIZE];
        int[] list = new int[PHYSICAL_SIZE];
        //---------------------------Display My Information---------------------------
        System.out.println("Main Menu for Sorts and Searches\n\n");
        System.out.println("1. Generate Random Numbers For the Original Array");
        System.out.println("2. Copy the original Array");

        while (scanner.hasNext()) {


            if (scanner.next().equalsIgnoreCase("Q")) {
                System.out.println("Thank you for using the program.");
                System.exit(0);
                continue;
            }

            switch (scanner.next()) {

                case "1":
                    System.out.println("How many elements would you like to have in this array?");
                    logicalSize = scanner.nextInt();
                    break;
            }

//            if (scanner.next().equalsIgnoreCase("1")); {
//            }

        }

    }
}
