package nl.han.ica.datastructures;

public class HANLinkedListNode<T> {
    private T value;
    private HANLinkedListNode<T> next;

    public HANLinkedListNode(T value) {
        this.value = value;
    }

    public HANLinkedListNode(T value, HANLinkedListNode<T> next) {
        this.value = value;
        this.next = next;
    }

    public void setNext(HANLinkedListNode<T> next) {
        this.next = next;
    }

    public HANLinkedListNode<T> getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }
}

