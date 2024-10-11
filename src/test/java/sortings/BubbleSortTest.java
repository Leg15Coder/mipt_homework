package sortings;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BubbleSortTest {
  @Test
  void sort() throws Exception {
    List<Integer> listToSort = Arrays.asList(1, -2, 5, 0, 9, 3, -7, 2);
    List<Integer> listMustResult = Arrays.asList(-7, -2, 0, 1, 2, 3, 5, 9);
    BubbleSort sorter = new BubbleSort(32);
    List<Integer> sortedList = sorter.sort(listToSort);
    assertEquals(sortedList, listMustResult);
  }

  @Test
  void type() {
    SorterType answer = new BubbleSort(100).type();
    assertEquals(answer, SorterType.BUBBLE);
  }
}