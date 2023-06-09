package nl.han.ica.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

import nl.han.ica.datastructures.interfaces.IHANLinkedList;

public class HANLinkedList<T> implements IHANLinkedList<T> {

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
        HANLinkedListNode<T> newNode = new HANLinkedListNode<>(value);
        HANLinkedListNode<T> firstNodeCopy = first;

        if (first == null) {
            first = newNode;
            return;
        }

        else if (index == 0) {
            newNode.setNext(firstNodeCopy);
            first = newNode;
            return;
        }

        else if (index > getSize()) {
            return;
        }

        int traverser = 0;

        while (traverser != index - 1) {
            firstNodeCopy = firstNodeCopy.getNext();
            traverser++;
        }

        newNode.setNext(firstNodeCopy.getNext());
        firstNodeCopy.setNext(newNode);
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

        HANLinkedListNode<T> firstNodeCopy = first;

        int traverser = 0;

        while (traverser != pos - 1) {
            firstNodeCopy = firstNodeCopy.getNext();
            traverser++;
        }

        HANLinkedListNode<T> toRemove = firstNodeCopy.getNext();
        firstNodeCopy.setNext(toRemove.getNext());
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
        private HANLinkedListNode<T> currentNode;

        public LinkedListIterator(HANLinkedListNode<T> first) {
            this.currentNode = first;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T value = currentNode.getValue();
            currentNode = currentNode.getNext();
            return value;
        }
    }
}
