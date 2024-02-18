package lab06;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the use of iterators in Assignment 3's 
 * SimplePriorityQueue, for Lab6.
 * 
 * @author CS 2420 course staff
 * @version February 18, 2022
 */
public class IteratorTester {
	
	private SimplePriorityQueue<Integer> smallSetOfEvens;
	private SimplePriorityQueue<Integer> largeSet;
	private Iterator<Integer> smallIterator;

	@BeforeEach
	void setUp() throws Exception {
		smallSetOfEvens = new SimplePriorityQueue<Integer>();
		for(int half = 1; half <= 10; half++) 
			smallSetOfEvens.insert(half*2); 
		// priority queue's backing array should be { 2, 4, 6, ..., 16, 18, 20 }
		
		smallIterator = smallSetOfEvens.iterator();
		
		largeSet = new SimplePriorityQueue<Integer>();
		for(int i = 1; i <= 100; i++)
			largeSet.insert(i);
		// priority queue's backing array should be { 1, 2, 3, ..., 98, 99, 100 }
	}

	@Test
	public void iterateOverEvenSet() {
		for(int expected = 2; expected <= 20; expected += 2) 
			assertEquals(expected, (int)smallIterator.next());
	}
	
	@Test
	public void expectedNoSuchElementThrown() {
		for(int count = 0; count < 10; count++) {
			smallIterator.next();
		}
		assertThrows(NoSuchElementException.class, () -> { smallIterator.next(); });
	}
	
	@Test
	public void hasNextReturnsTrue() {
		assertTrue(smallIterator.hasNext());
	}
	
	@Test
	public void hasNextReturnsFalseAtEnd() {
		for(int count = 0; count < 10; count++) {
			smallIterator.next();
		}
		assertFalse(smallIterator.hasNext());
	}
	
	@Test
	public void removeMaxElement() {
		for(int i = 0; i < 10; i++) 
			smallIterator.next();
		smallIterator.remove();  // removes 20
		assertEquals(18, (int)smallSetOfEvens.findMax());  // now max is 18
	}
	
	@Test
	public void removeWithoutCallToNext() {
		assertThrows(IllegalStateException.class, () -> { smallIterator.remove(); });
	}
	
	@Test
	public void removeEverything() {
		while(smallIterator.hasNext()) {
			smallIterator.next();
			smallIterator.remove();
		}
		assertEquals(0, smallSetOfEvens.size());
	}
	
	@Test
	public void removeEveryOtherElement() {
		Iterator<Integer> iterator = largeSet.iterator();
		while(iterator.hasNext()) {
			iterator.next();
			iterator.remove();
			if(iterator.hasNext()) {
				iterator.next();
			} 
		}
		assertEquals(50, largeSet.size());
		
		// priority queue's backing array should now be { 2, 4, 6, ..., 96, 98, 100 }
		for(int half = 50; half > 0; half--) {
			assertEquals(half*2, (int)largeSet.deleteMax());
		}
	}
}