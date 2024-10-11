package sortings;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {
  Random rnd = new Random();

  @Test
  void wrap() {
    MergeSort sorter = new MergeSort(17);
    List<Integer> list = Arrays.asList(3, 9, 1, 7, -2);
    sorter.wrap(list);
    assertEquals(list, sorter.wrapper.cloneList());
  }

  @Test
  void setElementsCountLimit() {
    MergeSort sorter = new MergeSort(17);
    int n = rnd.nextInt(1024);
    sorter.setElementsCountLimit(n);
    assertEquals(n, sorter.elementsCountLimit);
  }

  @Test
  void getElementsCountLimit() {
    int n = rnd.nextInt(1024);
    MergeSort sorter = new MergeSort(n);
    assertEquals(n, sorter.getElementsCountLimit());
  }

  @Test
  void sort() throws Exception {
    List<Integer> listToSort = Arrays.asList(1, -2, 5, 0, 9, 3, -7, 2);
    List<Integer> listMustResult = Arrays.asList(-7, -2, 0, 1, 2, 3, 5, 9);
    MergeSort sorter = new MergeSort(32);
    List<Integer> sortedList = sorter.sort(listToSort);
    assertEquals(sortedList, listMustResult);
  }

  @Test
  void type() {
    SorterType answer = new MergeSort(100).type();
    assertEquals(answer, SorterType.MERGE);
  }
}