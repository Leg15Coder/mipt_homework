package structures;

/**
 * CustomArrayList interface defines a generic list structure.
 *
 * @param <T> The type of elements in this list.
 */
public interface CustomArrayListInterface<T> {
  /**
   * Adds an element to the list.
   *
   * @param element Element to be added, must not be null.
   * @throws IllegalArgumentException if the element is null.
   */
  void add(T element);

  /**
   * Removes the element at the specified index and shifts subsequent elements left.
   *
   * @param index Index of the element to remove.
   * @return The removed element.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  T remove(int index);

  /**
   * Retrieves an element at the specified index.
   *
   * @param index Index of the element to retrieve.
   * @return The element at the specified index.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  T get(int index);
}
