package nl.han.ica.datastructures;

import nl.han.ica.datastructures.interfaces.IHANLinkedList;

public class HANLinkedList<T> implements IHANLinkedList<T> {

    private HANLinkedListNode<T> firstNode = null;
    private int size = 0;

    @Override
    public void addFirst(T value) {
        if (firstNode == null) {
            firstNode = new HANLinkedListNode<>(value);
        } else {
            HANLinkedListNode<T> newFirstNode = new HANLinkedListNode<>(value);
            newFirstNode.setNext(firstNode);
            firstNode = newFirstNode;
        }
        size++;
    }

    @Override
    public void clear() {
        firstNode = null;
        size = 0;
    }

    @Override
    public void insert(int index, T value) {
        HANLinkedListNode<T> nodeToInsert = new HANLinkedListNode<>(value);

        if (firstNode == null) {
            firstNode = nodeToInsert;
            size++;
            return;
        }
        if (index == 0) {
            nodeToInsert.setNext(firstNode);
            firstNode = nodeToInsert;
            size++;
            return;
        }
        if (index > getSize()) {
            return;
        }

        int count = 0;
        HANLinkedListNode<T> selectedNode = firstNode;
        while (count != index - 1) {
            selectedNode = selectedNode.getNext();
            count++;
        }
        nodeToInsert.setNext(selectedNode.getNext());
        selectedNode.setNext(nodeToInsert);
        size++;
    }

    @Override
    public void delete(int position) {
        if (position == 0) {
            removeFirst();
            return;
        }

        if (position >= size) {
            return;
        }

        HANLinkedListNode<T> current = firstNode;

        int count = 0;

        while (count != position - 1) {
            current = current.getNext();
            count++;
        }

        HANLinkedListNode<T> toRemove = current.getNext();
        current.setNext(toRemove.getNext());
        size--;
    }


    @Override
    public T get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }

        HANLinkedListNode<T> currentNode = firstNode;
        for (int i = 0; i < position; i++) {
            currentNode = currentNode.getNext();
        }

        return currentNode.getValue();
    }

    @Override
    public void removeFirst() {
        if (firstNode != null) {
            firstNode = firstNode.getNext();
            size--;
        }
    }

    @Override
    public T getFirst() {
        return firstNode != null ? firstNode.getValue() : null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
