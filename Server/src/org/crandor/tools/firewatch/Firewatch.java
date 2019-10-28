package org.crandor.tools.firewatch;

import java.util.*;

public class Firewatch {

    private static Map<String, Boolean> firewatch = new HashMap<>(4);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        String[] names = {"Willams", "Fabiano", "Martinez", "Popocamartinez", "Millard", "Wilson", "Young", "Dennis"};
        for (int i = 0; i < names.length; i++) {
            boolean hasWatch = new Random().nextInt(1) == 0;
            firewatch.put(names[i++], hasWatch);
        }
        System.out.println("Enter the name of the Marine you wish to check:");
        String name = scanner.nextLine();

        int time = new Random().nextInt(4);

        for (Map.Entry<String, Boolean> marine : firewatch.entrySet()) {
            if (name.equalsIgnoreCase(marine.getKey())) {
                System.out.println(name + ( marine.getValue() ? " does " : " does not ") + "have firewatch.");
                System.out.println("From " + time + " to " + (time + 4) + ".");
            }
        }
    }
}
