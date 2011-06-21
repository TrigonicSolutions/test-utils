package com.trigonic.utils.test.junit;

import java.util.ArrayList;
import java.util.List;

public class ListCartesianProduct<T> extends AbstractCartesianProduct<List<T>, List<T>> {
    public ListCartesianProduct(List<List<T>> head, List<List<T>> tail) {
        super(head, tail);
    }
    
    @Override
    protected List<T> combine(List<T> head, List<T> tail) {
        List<T> result = new ArrayList<T>(head.size() + tail.size());
        result.addAll(head);
        result.addAll(tail);
        return result;
    }

}
