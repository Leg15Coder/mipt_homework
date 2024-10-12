package org.example;

import sortings.BubbleSort;
import sortings.MergeSort;
import sortings.SorterType;
import sortings.SortsManager;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        MergeSort mergeSortMax8 = new MergeSort(8);
        MergeSort mergeSortMax16 = new MergeSort(16);
        BubbleSort bubbleSortMax8 = new BubbleSort(8);
        BubbleSort bubbleSortMax32 = new BubbleSort(32);

        SortsManager manager = new SortsManager(Arrays.asList(mergeSortMax8, mergeSortMax16, bubbleSortMax8, bubbleSortMax32));

        List<Integer> list = Arrays.asList(10, 3, 8, 2, 29, 3, -3, 0, 7);

        List<Integer> sortedList = manager.sort(list, SorterType.MERGE);
        System.out.println(sortedList.toString());
    }
}