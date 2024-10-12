package sortings;

import java.util.Collections;
import java.util.List;

public class MergeSort extends Sorter {
  public MergeSort(int limit) {
    super(limit);
  }

  @Override
  public List<Integer> sort(List<Integer> list) throws Exception {
    super.wrap(list);
    if (wrapper.size() > elementsCountLimit) {
      throw new Exception("Превышен лимит размера для сортировки слиянием, должно быть не больше "
          + elementsCountLimit + " элементов, в переданном списке - " + wrapper.size());
    }
    List<Integer> result = wrapper.cloneList();
    Collections.sort(result);
    return result;
  }

  @Override
  public SorterType type() {
    return SorterType.MERGE;
  }
}
