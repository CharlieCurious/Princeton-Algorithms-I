package deques_and_randomized_queues;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

	private class Node {
		Item item;
		Node next;
		Node front;
	}
	private Node first, last;
	private int size;

	private class DequeIterator implements Iterator<Item> {

		private Node current = first;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			if(!hasNext())
				throw new java.util.NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}

	}

	// Construct an empty deque.
	public Deque() {
		this.size = 0;
		this.first = null;
		this.last = null;
	}

	// Is the deque empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// Return the number of items on the deque.
	public int size() {
		return size;
	}

	// Add the item to the front.
	public void addFirst(Item item) {
		if(item == null)
			throw new IllegalArgumentException("item == null");
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		first.front = null;
		if(isEmpty())
			last = first;
		else
			oldfirst.front = first;
		size++;
	}

	// Add the item to the back.
	public void addLast(Item item) {
		if(item == null)
			throw new IllegalArgumentException("item == null");
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.next = null;
		last.front = oldlast;
		if(isEmpty())
			first = last;
		else
			oldlast.next = last;
		size++;
	}

	// Remove and return the item from the front.
	public Item removeFirst() {
		if(isEmpty())
			throw new java.util.NoSuchElementException();
		Item item = first.item;
		first = first.next;
		size--;
		if(isEmpty())
			last = first;
		else
			first.front = null;
		return item;
	}

	// Remove and return the item from the back.
	public Item removeLast() {
		if(isEmpty())
			throw new java.util.NoSuchElementException();
		Item item = last.item;
		last = last.front;
		size--;
		if(isEmpty())
			first = last;
		else
			last.next = null;
		return item;
	}

	@Override
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	// Unit testing (required).
	public static void main(String[] args) {
		Deque<String> deque = new Deque<>();
		deque.addFirst("Ola");
		deque.addLast("eu");
		deque.addLast("o");
		deque.addLast("Daniel");
		for(String s : deque)
			StdOut.println(s);
		StdOut.println();
		deque.addFirst("aslkhaskjsfh");
		deque.removeLast();
		deque.addLast("POTATO MoNsTeR!");
		for(String s : deque)
			StdOut.println(s);
		StdOut.println();
		deque.removeFirst();
		for(String s : deque)
			StdOut.println(s);
		StdOut.println("................." + deque.size());
		deque.removeFirst();
		deque.removeFirst();
		deque.removeFirst();
		deque.removeFirst();
		StdOut.println(deque.isEmpty());
		for(String s : deque)
			StdOut.println(s);
		StdOut.println();
		deque.removeFirst();
		for(String s : deque)
			StdOut.println(s);
		StdOut.println();



	}


}


