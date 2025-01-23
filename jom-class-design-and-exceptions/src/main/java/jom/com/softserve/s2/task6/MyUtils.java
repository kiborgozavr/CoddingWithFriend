package jom.com.softserve.s2.task6;

import java.util.ArrayList;
import java.util.List;

public class MyUtils {
    public List<Shape> maxAreas(List<Shape> shapes) {
        List<Shape> resultList = new ArrayList<>();
        List<Shape> circleList = new ArrayList<>();
        List<Shape> rectangleList = new ArrayList<>();
        double maxCircleArea = 0;
        double maxRectangleArea = 0;

        for (Shape shape : shapes) {
            double currentArea = shape.getArea();

            if (shape instanceof Circle) {
                if (currentArea == maxCircleArea) {
                    circleList.add(shape);
                } else if (currentArea > maxCircleArea) {
                    circleList.clear();
                    maxCircleArea = currentArea;
                    circleList.add(shape);
                }
            } else if (shape instanceof Rectangle) {
                if (currentArea == maxRectangleArea) {
                    rectangleList.add(shape);
                } else if (currentArea > maxRectangleArea) {
                    rectangleList.clear();
                    maxRectangleArea = currentArea;
                    rectangleList.add(shape);
                }
            }
        }
        resultList.addAll(circleList);
        resultList.addAll(rectangleList);
        return resultList;
    }
}
