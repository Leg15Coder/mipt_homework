package sortings;

import java.util.List;

public class SortsManager {
  List<Sorter> listOfSorters;

  public SortsManager(List<Sorter> listOfSorters) {
    this.listOfSorters = listOfSorters;
  }

  public List<Integer> sort(List<Integer> list, SorterType type) throws Exception {
    boolean fountSorter = false;
    for (var sorter: listOfSorters) {
      if (sorter.type().equals(type)) {
        fountSorter = true;
        try {
          return sorter.sort(list);
        } catch (Exception e) {
          System.out.println("Ошибка использования сортировки " + sorter + ": " + e);
        }
      }
    }
    if (!fountSorter) {
      throw new Exception("Не найдена подходящая сортировка");
    }
    return list;
  }
}
