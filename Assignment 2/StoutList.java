package HW2_template.edu.iastate.cs228.hw2;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author raghavk
 * 
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	/**
	 * returns the size of the list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * adds an element to the end of the list throws a NullPointerException if item
	 * is null
	 */
	@Override
	public boolean add(E item) {
		if (item == null) {
			throw new NullPointerException();
		}
		if (size == 0) {

			Node newNode = new Node();
			newNode.addItem(item);
			newNode.next = tail;
			newNode.previous = head;
			head.next = newNode;
			tail.previous = newNode;
		}

		else {
			Node current = tail.previous;

			if (current.count == nodeSize) {
				Node newNode = new Node();
				newNode.addItem(item);
				newNode.previous = current;
				newNode.next = tail;
				current.next = newNode;
				tail.previous = newNode;
			} else {
				current.addItem(item);
			}
		}
		size++;
		return true;
	}

	/**
	 * throws NullPointerException if item is null throws IndexOutOfBoundsException
	 * if pos<0 or pos>size adds element to the specified index
	 */
	@Override
	public void add(int pos, E item) {
		if (item == null) {
			throw new NullPointerException();
		}

		if (pos < 0 || pos > size) {
			throw new IndexOutOfBoundsException();
		}

		if (size == 0) {
			Node newNode = new Node();
			newNode.addItem(item);
			newNode.next = tail;
			newNode.previous = head;
			head.next = newNode;
			tail.previous = newNode;
		} else if (pos == size) {
			add(tail.previous, tail.previous.count, item);
		} else {
			NodeInfo info = find(pos);
			add(info.node, info.offset, item);
		}
		size++;
	}

	/**
	 * removes element at index pos throws IndexOutOfBoundsException if pos<0 or
	 * pos>size
	 */
	@Override
	public E remove(int pos) {
		if (pos < 0 || pos > size) {
			throw new IndexOutOfBoundsException();
		}
		NodeInfo info = find(pos);
		Node newNode = info.node;
		int off = info.offset;
		E val = newNode.data[off];
		newNode.removeItem(off);

		if (newNode.count < nodeSize / 2 && newNode.next != tail) {
			Node nextN = newNode.next;
			if (nextN.count > nodeSize / 2) {
				newNode.addItem(nextN.data[0]);
				nextN.removeItem(0);
			} else {
				for (int i = 0; i < nextN.count; i++) {
					newNode.addItem(nextN.data[i]);
				}
				newNode.next = nextN.next;
				nextN.next.previous = newNode;
			}
		}
		size--;
		return val;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		E[] arr = (E[]) new Comparable[size];
		int ind = 0;
		Node current = head.next;
		while (current != tail) {
			for (int i = 0; i < current.count; i++) {
				arr[ind] = current.data[i];
				ind++;
			}
			current = current.next;
		}
		head.next = tail;
		tail.previous = head;
		size = 0;
		insertionSort(arr, Comparator.naturalOrder());
		for (int i = 0; i < arr.length; i++) {
			add(arr[i]);
		}
	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		E[] arr = (E[]) new Comparable[size];
		int ind = 0;
		Node current = head.next;
		while (current != tail) {
			for (int i = 0; i < current.count; i++) {
				arr[ind] = current.data[i];
				ind++;
			}
			current = current.next;
		}
		head.next = tail;
		tail.previous = head;
		size = 0;
		bubbleSort(arr);
		for (int i = 0; i < arr.length; i++) {
			add(arr[i]);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new StoutIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + offset + " to
			// node: " + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	/**
	 * Helper class to store node and offset information.
	 */
	private class NodeInfo {
		/**
		 * reference to node object
		 */
		public Node node;
		/**
		 * value of offset
		 */
		public int offset;

		/**
		 * @param node
		 * @param offset constructs a NodeInfo object that stores a node and offset
		 */
		public NodeInfo(Node node, int offset) {
			this.node = node;
			this.offset = offset;
		}
	}

	/**
	 * @param pos
	 * @return NodeInfo object returns a NodeInfo onject of the element at index pos
	 *         throws IndexOutOfBoundsException
	 */
	private NodeInfo find(int pos) {
		int index = 0;
		Node current = head.next;
		while (current != tail) {
			if (index + current.count > pos) {
				return new NodeInfo(current, pos - index);
			}
			index += current.count;
			current = current.next;
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * @param n
	 * @param offset
	 * @param item
	 * @return NodeInfo object after adding item at given node and offset
	 */
	private NodeInfo add(Node n, int offset, E item) {
		if (n.count < nodeSize) {
			n.addItem(offset, item);
			return new NodeInfo(n, offset);
		}
		Node newNode = new Node();
		int mid = nodeSize / 2;
		for (int i = mid; i < nodeSize; i++) {
			newNode.addItem(n.data[i]);
			n.data[i] = null;
		}
		n.count = mid;
		newNode.next = n.next;
		newNode.previous = n;
		n.next.previous = newNode;
		n.next = newNode;

		if (offset <= mid) {
			n.addItem(offset, item);
			return new NodeInfo(n, offset);
		} else {
			newNode.addItem(offset - mid, item);
			return new NodeInfo(newNode, offset - mid);
		}
	}

	/**
	 * Class that models a StoutIterator object Implements the Iterator<E> interface
	 */
	private class StoutIterator implements Iterator<E> {

		/**
		 * cursor node
		 */
		private Node cursor;
		/**
		 * stores value of the offset
		 */
		private int offset;

		/**
		 * default constructor
		 */
		public StoutIterator() {
			cursor = head.next;
			offset = 0;
		}

		/**
		 * returns true if the list has another element
		 */
		@Override
		public boolean hasNext() {
			return cursor != tail && (offset < cursor.count || cursor.next != tail);
		}

		/**
		 * returns the next element in the list throws NoSuchElementException if value
		 * returned by hasNext() is false
		 */
		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			E val = cursor.data[offset];
			offset++;
			if (offset == cursor.count) {
				cursor = cursor.next;
				offset = 0;
			}
			return val;
		}
	}

	/**
	 * Models a StoutListIterator object and implements the ListIterator<E>
	 * interface
	 */
	private class StoutListIterator implements ListIterator<E> {
		// constants you possibly use ...

		private static final int BEHIND = -1;
		private static final int AHEAD = 1;
		private static final int NONE = 0;

		// instance variables ...
		/**
		 * cursor node
		 */
		private Node cursor;
		/**
		 * value of index
		 */
		private int index;
		/**
		 * stores the direction of the cursor
		 */
		private int direction;
		/**
		 * value of the offset
		 */
		private int offset;

		/**
		 * Default constructor
		 */
		public StoutListIterator() {
			this(0);
		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			if (pos < 0 || pos > size) {
				throw new IndexOutOfBoundsException();
			}
			cursor = head.next;
			offset = 0;
			index = 0;
			direction = NONE;
			while (index < pos) {
				next();
			}
		}

		/**
		 * returns true if the list has another element
		 */
		@Override
		public boolean hasNext() {
			return cursor != tail && (cursor.next != tail || offset < cursor.count);
		}

		/**
		 * returns the next element from the list
		 */
		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			E val = cursor.data[offset];
			offset++;
			if (offset == cursor.count) {
				cursor = cursor.next;
				offset = 0;
			}
			direction = BEHIND;
			index++;
			return val;
		}

		/**
		 * removes the element last returned by the cursor from the list
		 */
		@Override
		public void remove() {

			if (direction == NONE) {
				throw new IllegalStateException();
			} else {
				if (direction == BEHIND) {
					if (offset == 0) {
						cursor = cursor.previous;
						offset = cursor.count;
					}
					cursor.removeItem(offset - 1);
					offset--;
					index--;
				}
				if (direction == AHEAD) {
					cursor.removeItem(offset);
					if (offset == cursor.count) {
						cursor = cursor.next;
						offset = 0;
					}
					index--;
				}
				if (cursor.count < nodeSize / 2 && cursor.next != tail) {
					Node nextN = cursor.next;
					if (nextN.count > nodeSize / 2) {
						cursor.addItem(nextN.data[0]);
						nextN.removeItem(0);
					} else {
						for (int i = 0; i < nextN.count; i++) {
							cursor.addItem(nextN.data[i]);
						}
						cursor.next = nextN.next;
						nextN.next.previous = cursor;
					}
				}
			}
			size--;
			direction = NONE;
		}

		/**
		 * returns true if the list has a previous element
		 */
		@Override
		public boolean hasPrevious() {
			return cursor != head && (cursor.previous != head || offset > 0);
		}

		/**
		 * returns the previous element from the list
		 */
		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (offset == 0) {
				cursor = cursor.previous;
				offset = cursor.count;
			}
			offset--;
			E val = cursor.data[offset];
			direction = AHEAD;
			index--;
			return val;
		}

		/**
		 * returns the index of the next element in the list
		 */
		@Override
		public int nextIndex() {
			return index;
		}

		/**
		 * returns the index of the previous element in the list
		 */
		@Override
		public int previousIndex() {
			return index - 1;
		}

		/**
		 * sets the value of the last element returned by the cursor to e throws
		 * IllegalStateException if direction is none throws NullPointerException if e
		 * is null
		 */
		@Override
		public void set(E e) {
			if (e == null) {
				throw new NullPointerException();
			}
			if (direction == NONE) {
				throw new IllegalStateException();
			}
			if (direction == AHEAD) {
				cursor.data[offset] = e;
			}
			if (direction == BEHIND) {
				int set;
				Node setNode;
				if (offset == 0) {
					set = cursor.previous.count - 1;
					setNode = cursor.previous;
				} else {
					set = offset - 1;
					setNode = cursor;
				}
				setNode.data[set] = e;
			}
		}

		/**
		 * adds e to the left of the cursor throws NullPointerException if e is null
		 */
		@Override
		public void add(E e) {
			if (e == null) {
				throw new NullPointerException();
			}

			NodeInfo info = StoutList.this.add(cursor, offset, e);
			cursor = info.node;
			offset = info.offset + 1;
			size++;
			index++;
			direction = NONE;
		}
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		for (int i = 1; i < arr.length; i++) {
			E val = arr[i];
			int j = i - 1;
			while (j >= 0 && comp.compare(arr[j], val) > 0) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = val;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		int length = arr.length;
		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length - i - 1; j++) {
				if (arr[j].compareTo(arr[j + 1]) < 0) {
					E temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}

}