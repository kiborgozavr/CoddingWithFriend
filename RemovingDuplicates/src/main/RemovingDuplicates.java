package main;

import java.util.*;

abstract class DuplicatesRemover
{
    public abstract void remove(ArrayList<Integer> list);
}

class RemoveDuplicates extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (i == j) continue;
                else if (list.get(i).equals(list.get(j))) {
                    list.remove(j);
                    j = 0;
                }
            }
        }
    }
}
class WithArrayInPlaceRemove extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    list.remove(j);
                    --j;
                }
            }
        }
    }
}

class WithArrayInPlaceRemoveBetter extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) == null) continue;
            for (int j = i + 1; j < list.size(); j++) {
                if (Objects.equals(list.get(i), list.get(j))) {
                    list.set(j, null);
                }
            }
        }
        list.removeIf(Objects::isNull);
    }
}
class WithListInPlaceRemoveDumb extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {

        List<Integer> newList = new LinkedList<>(list);
        for (int i = 0; i < newList.size(); i++) {
            for (int j = i + 1; j < newList.size(); j++) {
                if (newList.get(i).equals(newList.get(j))) {
                    newList.remove(j);
                    --j;
                }
            }
        }
        list.clear();
        list.addAll(newList);
    }
}
@SuppressWarnings("unused")
class WithListInPlaceRemoveIterators extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {

        List<Integer> newList = new LinkedList<>(list);
        Iterator<Integer> it = newList.listIterator();
        Iterator<Integer> innerIt;
        for (int value = it.next(); it.hasNext(); value = it.next() ) {
            // TODO: This doesn't work we need a deep copy here, which doesn't seem to exist
            innerIt = it;
            for (int innerValue = innerIt.next(); innerIt.hasNext(); innerValue = innerIt.next()) {
                if (innerValue == value) {
                    innerIt.remove();
                }
            }
        }
        list.clear();
        list.addAll(newList);
    }
}
class WithSetInPlaceRemove extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {
        Set<Integer> numbers = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            if (numbers.contains(list.get(i)))
            {
                list.remove(i);
                --i;
            }
            else
            {
                numbers.add(list.get(i));
            }
        }
    }
}
class WithSetAdditionalList extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {
        Set<Integer> numbers = new HashSet<>();
        var newList = new ArrayList<Integer>();
        for (Integer integer : list) {
            if (!numbers.contains(integer)) {
                newList.add(integer);
                numbers.add(integer);
            }
        }
        list.clear();
        list.addAll(newList);

    }
}
class ConvertToSet extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {
        Set<Integer> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }
}

class ConvertToLinkedSet extends DuplicatesRemover {
    @Override
    public void remove(ArrayList<Integer> list) {
        Set<Integer> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
    }
}

public class RemovingDuplicates {

    static class Test {
        public int count;
        public float time;

        public int bound;
        ArrayList<Integer> originalList = null;
        public Test(int inCount, int inBound)
        {
            count = inCount;
            bound = inBound;
            time = 0;
        }

        @Override
        public String toString() {
            return String.format("%10d %10d", count, bound);
        }
    }
    public static void main(String[] args) {

        List<Test> testList = new ArrayList<>();
        int bound = 10;
        testList.add(new Test(10, bound));
        testList.add(new Test(100, bound));
        testList.add(new Test(1000, bound));
        testList.add(new Test(10000, bound));
        testList.add(new Test(100000, bound));
        testList.add(new Test(1000000, bound));
        testList.add(new Test(1000000, bound * 10));
        testList.add(new Test(1000000, bound * 100));



        for(var test : testList) {
            test.originalList = new ArrayList<>();
            var rand = new Random();
            for (int i = 0; i < test.count; ++i) {
                test.originalList.add(rand.nextInt(test.bound));
            }
        }

        DuplicatesRemover[] Removers = {
                new RemoveDuplicates(),
                // DOESN'T WORK new WithListInPlaceRemoveIterators(),
                new WithArrayInPlaceRemove(),
                new WithSetInPlaceRemove(),
                new WithArrayInPlaceRemoveBetter(),
                new WithSetAdditionalList(),
                new ConvertToSet(),
                new ConvertToLinkedSet(),
                new WithListInPlaceRemoveDumb(),
        };

        for (var func : Removers) {
            System.out.println(func.getClass().getSimpleName());
            for (var test : testList) {
                //System.out.println("Running test for " + test);

                var outList = new ArrayList<>(test.originalList);
                var startTime = System.nanoTime();
                func.remove(outList);
                test.time = ((System.nanoTime() - startTime) / (float)1000000) / (float)1000;

                //System.out.println("Done");
                if(!TestCorrectness(test.originalList, outList))
                {
                    System.out.println("Incorrect Solution for " + test);
                    return;
                }

            }

            for (int i = 0; i < testList.size(); ++i) {
                var test = testList.get(i);
                System.out.println(test + ":\t\t" + String.format("%.12f", test.time));
                if (i + 1 < testList.size()) {
                    var nextTest = testList.get(i + 1);
                    System.out.println(String.format("%21d:", nextTest.count / test.count) + "\t\t" + String.format("%.12f", nextTest.time / test.time));
                }
            }
            System.out.println();
        }
        System.out.println("Done");
    }



    private static boolean TestCorrectness(ArrayList<Integer> oldList, ArrayList<Integer> newList)
    {
        Set<Integer> setFromOld = new HashSet<>(oldList);
        Set<Integer> setFromNew = new HashSet<>();
        for (var i : newList)
        {
            if (setFromNew.contains(i)) return false;
            setFromNew.add(i);
        }
        return setFromOld.equals(setFromNew);
    }
}