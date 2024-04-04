package main;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RemovingDuplicates {
    public static void main(String[] args) {

        String str = "1, 66, 66, 2, 5, 5, 66, 98, 6, 5, 2, 1, 4, 19, 19, 19";
        ArrayList<Integer> list = new ArrayList<>();

        StringTokenizer stringTokenizer = new StringTokenizer(str, ", ");

        while (stringTokenizer.hasMoreTokens()) {
            list.add(Integer.parseInt(stringTokenizer.nextToken()));
        }

        System.out.println(list);

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (i == j) {
                    continue;
                } else if (list.get(i).equals(list.get(j))) {
                    list.remove(j);
                    j = 0;
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                stringBuilder.append(list.get(i));
                stringBuilder.append(", ");
            } else {
                stringBuilder.append(list.get(i));
            }
        }

        String result = String.valueOf(stringBuilder);
        System.out.println(result);
    }
}