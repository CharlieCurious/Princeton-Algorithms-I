package deques_and_randomized_queues;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private int size;
	private Item[] array;

	// Construct an empty randomized queue.
	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		this.size = 0;
		array = (Item[]) new Object[1];
	}

	private class RandomizedQueueIterator implements Iterator<Item> {

		private int count = size;
		private int[] order;

		public RandomizedQueueIterator() {
			order = new int[count];
			for(int i = 0; i < count; i++)
				order[i] = i;
			StdRandom.shuffle(order);
		}

		@Override
		public boolean hasNext() {
			return count > 0;
		}

		@Override
		public Item next() {
			if(!hasNext())
				throw new java.util.NoSuchElementException();
			return array[order[--count]];
		}

	}

	@SuppressWarnings("unchecked")
	private void resize(int factor) {
		Item[] copy = (Item[]) new Object[factor];
		for(int i = 0; i < size; i++)
			copy[i] = array[i];
		array = copy;
	}

	// Is the randomized queue empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// Return the number of items on the randomized queue.
	public int size() {
		return size;
	}

	// Add the item.
	public void enqueue(Item item) {
		if(item == null)
			throw new IllegalArgumentException("item == null");
		array[size++] = item;
		if(size == array.length)
			resize(array.length * 2);
	}

	// Remove and return a random item.
	public Item dequeue() {
		if(size == 0)
			throw new java.util.NoSuchElementException();
		int rand = StdRandom.uniform(size);
		Item item = array[rand];
		array[rand] = array[size - 1];
		array[--size] = null;
		if(size > 0 && size == array.length / 4)
			resize(array.length / 2);
		return item;
	}

	// Return a random item (but do not remove it).
	public Item sample() {
		if(size == 0)
			throw new java.util.NoSuchElementException();
		int rand = StdRandom.uniform(size);
		return array[rand];
	}

	@Override
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	// Unit testing (required).
	public static void main(String[] args) {
		RandomizedQueue<String> queue = new RandomizedQueue<>();
		StdOut.println(queue.isEmpty());

		queue.enqueue("ola");
		queue.enqueue("Eu");
		queue.enqueue("Sou");
		queue.enqueue("o");
		queue.enqueue("Daniel");

		StdOut.println(queue.isEmpty() + " " + queue.size());
		StdOut.println("......1.......");

		for(String s : queue)
			StdOut.println(s);
		StdOut.println("......2.......");

		queue.dequeue();
		for(String s : queue)
			StdOut.println(s);
		StdOut.println("......3.......");

		StdOut.println(queue.sample());
	}

}