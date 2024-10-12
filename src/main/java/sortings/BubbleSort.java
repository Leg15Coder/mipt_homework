package sortings;

import java.util.List;

public class BubbleSort extends Sorter {
  public BubbleSort(int limit) {
    super(limit);
  }

  @Override
  public List<Integer> sort(List<Integer> list) throws Exception {
    super.wrap(list);
    if (wrapper.size() > elementsCountLimit) {
      throw new Exception("Превышен лимит размера для сортировки пузырьком, должно быть не больше "
          + elementsCountLimit + " элементов, в переданном списке - " + wrapper.size());
    }
    List<Integer> result = wrapper.cloneList();
    for (int i = 0; i < result.size(); i++) {
      for (int j = i + 1; j < result.size(); j++) {
        if (result.get(i) > result.get(j)) {
          Integer temp = result.get(i);
          result.set(i, result.get(j));
          result.set(j, temp);
        }
      }
    }
    return result;
  }

  @Override
  public SorterType type() {
    return SorterType.BUBBLE;
  }
}
