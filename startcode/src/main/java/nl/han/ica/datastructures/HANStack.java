package nl.han.ica.datastructures;

import nl.han.ica.datastructures.interfaces.IHANLinkedList;
import nl.han.ica.datastructures.interfaces.IHANStack;

public class HANStack<T> implements IHANStack<T> {

    IHANLinkedList<T> list;

    public HANStack() {
        list = new HANLinkedList<>();
    }

    @Override
    public void push(T value) {
        list.addFirst(value);
    }

    @Override
    public T pop() {
        T firstElement = list.getFirst();
        list.removeFirst();
        return firstElement;
    }

    @Override
    public T peek() {
        return list.getFirst();
    }
}
