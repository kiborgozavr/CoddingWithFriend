package jom.com.softserve.s2.task6;

import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static void main(String[] args) {

        final List<Shape> originList = new ArrayList<>();
//        originList.add(new Circle("Circle1", 2.0));
        originList.add(new Rectangle("Rectangle1", 2.0, 3.0));
        originList.add(new Circle("Circle2", 1.0));
        originList.add(new Rectangle("Rectangle2", 1.0, 2.0));

        MyUtils myUtils = new MyUtils();

        System.out.println(myUtils.maxAreas(originList));
    }
}
