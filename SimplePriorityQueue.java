package lab06;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A priority queue that can accomodate any type of item. Limits access to
 * highest
 * priority item.
 * 
 * @author Arnaud Cavalletto and Eric Agar
 * @version Feb 1, 2024
 */

public class SimplePriorityQueue<E> implements PriorityQueue<E>, Iterable<E> {
    private E[] arr;
    private Comparator<? super E> cmp = null;
    private int arrLength;

    /**
     * Creates a new priority queue assuming elements use natural ordering
     */
    @SuppressWarnings("unchecked")
    public SimplePriorityQueue() {
        this.arr = (E[]) new Object[10];
        this.arrLength = 0;
    }

    /**
     * Creates a new priority queue assuming elements use a Comparator for ordering
     * 
     * @param cmp comparator used to order elements
     */
    @SuppressWarnings("unchecked")
    public SimplePriorityQueue(Comparator<? super E> cmp) {
        this.arrLength = 0;
        this.arr = (E[]) new Object[10];
        this.cmp = cmp;

    }

    /**
     * Retrieves, but does not remove, the maximum element in this priority
     * queue.
     * 
     * @return the maximum element
     * @throws NoSuchElementException if the priority queue is empty
     */
    public E findMax() throws NoSuchElementException {
        if (arrLength == 0)
            throw new NoSuchElementException("The queue is empty.");
        return arr[arrLength - 1];
    }

    /**
     * Retrieves and removes the maximum element in this priority queue.
     * 
     * @return the maximum element
     * @throws NoSuchElementException if the priority queue is empty
     */
    public E deleteMax() throws NoSuchElementException {
        // check what we need to return. New max element after deletion or deleted
        // element
        if (arrLength == 0)
            throw new NoSuchElementException("The queue is empty.");
        E deletedElement = arr[arrLength - 1];
        arr[arrLength - 1] = null;
        arrLength--;
        return deletedElement;
    }

    /**
     * Inserts the specified element into this priority queue.
     * 
     * @param item - the element to insert
     */
    @SuppressWarnings("unchecked")
    public void insert(E item) {

        if (arrLength == 0) {
            arr[arrLength] = item;
            arrLength++;
            return;
        }

        if (arrLength == arr.length)
            doubleArray();

        int insertIndex = binarySearch(item);

        if (compare(arr[insertIndex], item) < 0) {
            arrLength++;
            if (arrLength == arr.length)
                doubleArray();
            for (int i = arrLength - 1; i >= insertIndex; i--) {
                arr[i + 1] = arr[i];
            }
            arr[insertIndex + 1] = item;
        } else if (compare(arr[insertIndex], item) == 0 && compare(findMax(), item) == 0) {
            arr[insertIndex + 1] = item;
            arrLength++;

        } else {
            E[] tempArr = (E[]) new Object[arrLength];
            for (int i = insertIndex; i < arrLength; i++)
                tempArr[i] = arr[i];
            arr[insertIndex] = item;
            arrLength++;
            if (arrLength == arr.length) {
                doubleArray();
            }

            for (int i = insertIndex; i < tempArr.length; i++) {
                arr[i + 1] = tempArr[i];
            }

        }

    }

    /**
     * Inserts the specified elements into this priority queue.
     * 
     * @param coll - the collection of elements to insert
     */
    public void insertAll(Collection<? extends E> coll) {
        for (E item : coll)
            insert(item);
    }

    /**
     * Indicates whether this priority queue contains the specified element.
     * 
     * @param item - the element to be checked for containment in this priority
     *             queue
     * @return true if the item is contained in this priority queue
     */
    public boolean contains(E item) {
        if (compare(item, arr[binarySearch(item)]) != 0)
            return false;
        return true;
    }

    /**
     * @return the number of elements in this priority queue
     */
    public int size() {
        return arrLength;
    }

    /**
     * @return true if this priority queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        if (arrLength == 0)
            return true;
        return false;
    }

    /**
     * Removes all of the elements from this priority queue. The queue will be
     * empty when this call returns.
     */
    public void clear() {
        Arrays.fill(arr, null);
        arrLength = 0;
    }

    /**
     * Searches through the array utilizing the binary search algorith to find the
     * target or closest element to it
     * 
     * @param target the desired element we are searching for
     * @return the index of either the target or the closest element
     */
    private int binarySearch(E target) {

        int lowerBound = 0;
        int upperBound = arrLength - 1;
        int midPoint = -1;

        while (lowerBound <= upperBound) {
            midPoint = lowerBound + (upperBound - lowerBound) / 2;
            if (compare(arr[midPoint], target) == 0)
                return midPoint;

            if (compare(arr[midPoint], target) < 0)
                lowerBound = midPoint + 1;
            else
                upperBound = midPoint - 1;

        }
        return midPoint;
    }

    /**
     * Helper method that compares two objects
     * 
     * @param lhs first object to compare
     * @param rhs second object to compare
     * @return the int value of the comparison of the two given objects. Negative if
     *         lhs is less than rhs, positive if lhs is greater than rhs, and equal
     *         if lhs is equal to rhs
     */
    @SuppressWarnings("unchecked")
    private int compare(E lhs, E rhs) {
        if (this.cmp != null) {
            return cmp.compare(lhs, rhs);
        }
        return ((Comparable<? super E>) lhs).compareTo(rhs);
    }

    /**
     * Doubles our backing array so that we always have enough room for additional
     * elements
     */
    @SuppressWarnings("unchecked")
    private void doubleArray() {
        if (this.arrLength == this.arr.length) {
            E[] newArr = (E[]) new Object[arrLength * 2];

            for (int i = 0; i < arrLength; i++)
                newArr[i] = this.arr[i];

            this.arr = newArr;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new SPQIterator();
    }

    private class SPQIterator implements Iterator<E> {
        private int nextIndex;
        private boolean calledNext;


        public SPQIterator() {
            calledNext = false;
            nextIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return arrLength > nextIndex;
           
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();

            E next = (E) arr[nextIndex++];
            calledNext = true;

            return next;
        }

        public void remove() {
            if (!calledNext)
                throw new IllegalStateException();
            
            calledNext = false;
            for (int i = nextIndex; i < arrLength; i++) {
                arr[i - 1] = arr[i];
            }
            arrLength--;

            nextIndex--;
        }
    }
}
