package com.trigonic.utils.test.junit;

import java.util.AbstractList;
import java.util.List;

public abstract class AbstractCartesianProduct<T, E> extends AbstractList<E> {
    private List<T> head;
    private List<T> tail;
    
    public AbstractCartesianProduct(List<T> head, List<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        int headIndex = index / tail.size();
        int tailIndex = index % tail.size();
        return combine(head.get(headIndex), tail.get(tailIndex));
    }
    
    @SuppressWarnings("hiding")
    protected abstract E combine(T head, T tail); 

    @Override
    public int size() {
        return head.size() * tail.size();
    }
}
