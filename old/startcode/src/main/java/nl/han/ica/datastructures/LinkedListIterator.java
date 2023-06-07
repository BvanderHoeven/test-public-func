package nl.han.ica.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListIterator<T> implements Iterator<T> {
    private HANLinkedListNode<T> current;

    public LinkedListIterator(HANLinkedListNode<T> first) {
        current = first;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        HANLinkedListNode<T> tempo = current;
        current = current.getNext();
        return tempo.getValue();
    }
}
