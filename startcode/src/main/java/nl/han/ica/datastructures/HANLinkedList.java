package nl.han.ica.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

import nl.han.ica.datastructures.interfaces.IHANLinkedList;

public class HANLinkedList<T> implements IHANLinkedList<T>, Iterable<T> {

    private HANLinkedListNode<T> first = null;

    @Override
    public void addFirst(T value) {
        if (first == null) {
            first = new HANLinkedListNode<>(value);
        } else {
            HANLinkedListNode<T> toAdd = new HANLinkedListNode<>(value);
            toAdd.setNext(first);
            first = toAdd;
        }
    }

    @Override
    public void clear() {
        first = null;
    }

    @Override
    public void insert(int index, T value) {
        HANLinkedListNode<T> tmp = new HANLinkedListNode<>(value);
        HANLinkedListNode<T> current = first;

        if (first == null) {
            first = tmp;
            return;
        }

        if (index == 0) {
            tmp.setNext(current);
            first = tmp;
            return;
        }

        if (index > getSize()) {
            return;
        }

        int count = 0;

        while (count != index - 1) {
            current = current.getNext();
            count++;
        }

        tmp.setNext(current.getNext());
        current.setNext(tmp);
    }

    @Override
    public void delete(int pos) {

        if (pos == 0) {
            removeFirst();
            return;
        }

        if (pos > getSize()) {
            return;
        }

        HANLinkedListNode<T> current = first;

        int count = 0;

        while (count != pos - 1) {
            current = current.getNext();
            count++;
        }

        HANLinkedListNode<T> toRemove = current.getNext();
        current.setNext(toRemove.getNext());
    }

    @Override
    public T get(int pos) {
        Iterator<T> iterator = iterator();

        for (int i = 0; i < pos; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    @Override
    public void removeFirst() {
        first = first.getNext();
    }

    @Override
    public T getFirst() {
        return first.getValue();
    }

    @Override
    public int getSize() {
        int size = 0;
        Iterator<T> iterator = iterator();

        while (iterator.hasNext()) {
            size++;
            iterator.next();
        }

        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<>(first);
    }

    private static class LinkedListIterator<T> implements Iterator<T> {
        private HANLinkedListNode<T> current;

        public LinkedListIterator(HANLinkedListNode<T> first) {
            this.current = first;
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
            T value = current.getValue();
            current = current.getNext();
            return value;
        }
    }
}
