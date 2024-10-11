package wrapper;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListWrapperTest {

  @Test
  void cloneList() {
    List<Integer> list = Arrays.asList(1, -2, 5, 0, 9, 3, -7, 2);
    ListWrapper<Integer> wrapper = new ListWrapper<Integer>(list);
    assertEquals(list, wrapper.cloneList());
  }

  @Test
  void size() {
    List<Integer> list = Arrays.asList(1, -2, 5, 0, 9, 3, -7, 2);
    ListWrapper<Integer> wrapper = new ListWrapper<Integer>(list);
    assertEquals(list.size(), wrapper.size());
  }
}