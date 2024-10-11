package wrapper;

import java.util.ArrayList;
import java.util.List;

public class ListWrapper<T> {
  private final List<T> origin;

  public ListWrapper(List<T> list) {
    this.origin = new ArrayList<T>(list);
  }

  public List<T> cloneList() {
    return new ArrayList<T>(origin);
  }

  public int size() {
    return origin.size();
  }
 }
