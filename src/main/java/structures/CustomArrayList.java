package structures;

/**
 * CustomArrayList is a dynamic array-based list that implements CustomArrayList interface.
 *
 * @param <T> The type of elements in this list.
 */
public class CustomArrayList<T> implements CustomArrayListInterface<T> {
  private Object[] array;
  private int size;
  private int MaxSize;

  /**
   * Constructs an empty list with an initial size.
   */
  public CustomArrayList() {
    this.MaxSize = 16;
    this.size = 0;
    this.array = new Object[MaxSize];
  }

  /**
   * Reallocates the array by doubling the maximum size when the current size is doesn't allow adding an element.
   */

  private void reallocate() {
    Object[] NewArray = new Object[MaxSize * 2];
    System.arraycopy(this.array, 0, NewArray, 0, MaxSize);
    this.array = NewArray;
    this.MaxSize *= 2;
  }

  /**
   * Adds an element to the list.
   *
   * @param element Element to be added, must not be null.
   * @throws IllegalArgumentException if the element is null.
   */
  @Override
  public void add(T element) {
    if (element == null) {
      throw new IllegalArgumentException("Null values are not allowed");
    }
    if (size + 1 == MaxSize) {
      reallocate();
    }
    this.array[size] = element;
    this.size++;
  }

  /**
   * Removes the element at the specified index and shifts subsequent elements left.
   *
   * @param index Index of the element to remove.
   * @return The removed element.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  @Override
  public T remove(int index) {
    CheckIndex(index);
    T RemovingElement = (T) array[index];
    System.arraycopy(this.array, index + 1, this.array, index, size - index);
    this.size--;
    return RemovingElement;
  }

  /**
   * Retrieves an element at the specified index.
   *
   * @param index Index of the element to retrieve.
   * @return The element at the specified index.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  @Override
  public T get(int index) {
    CheckIndex(index);
    return (T) array[index];
  }

  /**
   * Checks if the index is valid (within range).
   *
   * @param index Index to check.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  private void CheckIndex(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("Indexes can't be negative");
    } else if (index >= size) {
      throw new IndexOutOfBoundsException("Index out of range");
    }
  }
}
