package sortings;

import java.util.List;

public interface SorterInterface {
  List<Integer> sort(List<Integer> list) throws Exception;

  void setElementsCountLimit(int limit);

  int getElementsCountLimit();

  SorterType type();
}
