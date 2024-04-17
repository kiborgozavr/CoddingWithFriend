package main.St2;

public class CyclesTest {

    public static void main(String[] args) {
        String realPassword = "5khk";

        String findPassword;

        findPassword = findPassword1Symbol(realPassword);
        if (findPassword != null) {
            System.out.println("Real password: " + findPassword);
        } else {
            findPassword = findPassword2Symbol(realPassword);
            if (findPassword != null) {
                System.out.println("Real password: " + findPassword);
            } else {
                findPassword = findPassword3Symbol(realPassword);
                if (findPassword != null) {
                    System.out.println("Real password: " + findPassword);
                } else {
                    findPassword = findPassword4Symbol(realPassword);
                    if (findPassword != null) {
                        System.out.println("Real password: " + findPassword);
                    }
                }
            }
        }
    }


    public static String findPassword1Symbol(String realPassword) {
        String[] pattern = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String resault = null;
        for (String temp : pattern) {
            if (temp.equals(realPassword)) {
                resault = temp;
            }
        }
        return resault;
    }

    public static String findPassword2Symbol(String realPassword) {
        String[] pattern = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String resault = null;
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                String temp = pattern[i] + pattern[j];
                if (temp.equals(realPassword)) {
                    resault = temp;
                }
            }
        }
        return resault;
    }

    public static String findPassword3Symbol(String realPassword) {
        String[] pattern = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String resault = null;
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                for (int k = 0; k < pattern.length; k++) {
                    String temp = pattern[i] + pattern[j] + pattern[k];
                    if (temp.equals(realPassword)) {
                        resault = temp;
                    }
                }
            }
        }
        return resault;
    }
    public static String findPassword4Symbol(String realPassword) {
        String[] pattern = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String resault = null;
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                for (int k = 0; k < pattern.length; k++) {
                    for (int l = 0; l < pattern.length; l++) {
                        String temp = pattern[i] + pattern[j] + pattern[k] + pattern[l];
                        if (temp.equals(realPassword)) {
                            resault = temp;
                        }
                    }
                }
            }
        }
        return resault;
    }
}
