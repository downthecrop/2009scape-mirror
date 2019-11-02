package org.crandor.tools;

public class Lesson {


    public static void main(String[] args) {

       String text = "Beal";

        System.out.println(frontBack(text));

    }

    public boolean sleepIn(boolean weekday, boolean vaction) {
        if (!weekday || vaction) {
            return true;
        }
        return false;
    }

    public int sumDouble(int a, int b) {
        int sum = a + b;
        return  a == b ? sum * 2 : sum;
    }

    public static String frontBack(String str) {
        char first = str.charAt(0);
        char last = str.charAt(str.length() - 1);
        return str.replace(last, first).replace(first, last);
    }

    public boolean scores100(int[] scores) {
        if (scores[0] == 100) {
            return true;
        }
        for (int i = 1; i < scores.length; i++) {
            if (scores[i - 1] == 100) {
                return true;
            }
        }
        return false;
    }



}
