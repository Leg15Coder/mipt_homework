package sortings;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortsManagerTest {

  @Test
  void sort() throws Exception {
    MergeSort mergeSortMax8 = new MergeSort(8);
    MergeSort mergeSortMax16 = new MergeSort(16);
    BubbleSort bubbleSortMax8 = new BubbleSort(8);
    BubbleSort bubbleSortMax32 = new BubbleSort(32);

    SortsManager manager = new SortsManager(Arrays.asList(mergeSortMax8, mergeSortMax16, bubbleSortMax8, bubbleSortMax32));

    List<Integer> list = Arrays.asList(10, 3, 8, 2, 29, 3, -3, 0, 7);
    List<Integer> listMustResult = Arrays.asList(-3, 0, 2, 3, 3, 7, 8, 10, 29);

    List<Integer> sortedList = manager.sort(list, SorterType.MERGE);

    assertEquals(sortedList, listMustResult);
  }
}