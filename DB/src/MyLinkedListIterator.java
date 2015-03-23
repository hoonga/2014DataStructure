import java.util.Iterator;

public class MyLinkedListIterator<T extends Comparable<T>> implements Iterator<T> {
	// FIXME implement this
	// Implement the iterator for MyLinkedList.
	// You have to maintain the current position of the iterator.

	// Remove below annotation(@SuppressWarnings("unused")) after you implement this iterator.
	// This annotation tells compiler that "Do not warn me about this variable not being used".
	@SuppressWarnings("unused")
	private final MyLinkedList<T> l;
	private Node<T> CurrentNode = null;

	public MyLinkedListIterator(MyLinkedList<T> myLinkedList) {
		this.l = myLinkedList;
	}

	@Override
	public boolean hasNext() {
		if (this.CurrentNode != null)
			return (CurrentNode.next!=null);
		else {
			return l.head != null;
		}
	}

	@Override
	public T next() {
		if (CurrentNode == null)
			CurrentNode = l.head;
		else 
			this.CurrentNode = this.CurrentNode.next;
		return CurrentNode.item;
	}

	@Override
	public void remove() {
		// This code does not have to be modified.
		throw new UnsupportedOperationException();
	}
}
