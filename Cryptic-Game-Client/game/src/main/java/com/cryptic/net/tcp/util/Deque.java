package com.cryptic.net.tcp.util;

import com.cryptic.entity.model.HashLink;

import java.util.Collection;
import java.util.Iterator;

public class Deque<T extends HashLink> implements Iterable<T>, Collection<T> {
    HashLink head = new HashLink();
    HashLink current;

    public Deque() {
        this.head.previous = this.head;
        this.head.next = this.head;
    }
    public void push_front(T arg0) {
        if (arg0.next != null) {
            arg0.unlink();
        }

        arg0.next = this.head.next;
        arg0.previous = this.head;
        arg0.next.previous = arg0;
        arg0.previous.next = arg0;
    }

    public void push_back(T arg0) {
        if (arg0.next != null) {
            arg0.unlink();
        }

        arg0.next = this.head;
        arg0.previous = this.head.previous;
        arg0.next.previous = arg0;
        arg0.previous.next = arg0;
    }

    public static void add_before(HashLink arg0, HashLink second) {
        if (arg0.next != null) {
            arg0.unlink();
        }

        arg0.next = second;
        arg0.previous = second.previous;
        arg0.next.previous = arg0;
        arg0.previous.next = arg0;
    }

    public T last() {
        return this.next(null);
    }

    T next(T arg0) {
        HashLink var2;
        if (arg0 == null) {
            var2 = this.head.previous;
        } else {
            var2 = arg0;
        }

        if (var2 == this.head) {
            this.current = null;
            return null;
        } else {
            this.current = var2.previous;
            return (T) var2;
        }
    }

    public T previous() {
        HashLink var1 = this.current;
        if (var1 == this.head) {
            this.current = null;
            return null;
        } else {
            this.current = var1.previous;
            return (T) var1;
        }
    }

    @Override
    public int size() {
        int var1 = 0;

        for(HashLink var2 = this.head.previous; var2 != this.head; var2 = var2.previous) {
            ++var1;
        }

        return var1;
    }

    public boolean is_empty() {
        return this.head.previous == this.head;
    }

    @Override
    public Iterator<T> iterator() {
        return new DequeIterator<>(this);
    }

    @Override
    public boolean isEmpty() {
        return this.is_empty();
    }

    @Override
    public boolean contains(Object arg0) {
        throw new RuntimeException();
    }

    @Override
    public Object[] toArray() {
        return this.get_elements();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray( E[] arr) {
        int idx = 0;

        for(HashLink elem = this.head.previous; elem != this.head; elem = elem.previous) {
            arr[idx++] = (E) elem;
        }

        return arr;
    }

    @SuppressWarnings("unchecked")
    T[] get_elements() {
        HashLink[] arr = new HashLink[this.size()];
        int idx = 0;

        for(HashLink elem = this.head.previous; elem != this.head; elem = elem.previous) {
            arr[idx++] = elem;
        }

        return (T[]) arr;
    }

    boolean add_first(T arg0) {
        this.push_front(arg0);
        return true;
    }

    @Override
    public boolean remove(Object arg0) {
        throw new RuntimeException();
    }

    @Override
    public boolean containsAll(Collection arg0) {
        throw new RuntimeException();
    }

    @Override
    public boolean addAll(Collection arg0) {
        throw new RuntimeException();
    }

    @Override
    public boolean removeAll(Collection arg0) {
        throw new RuntimeException();
    }

    @Override
    public boolean retainAll(Collection arg0) {
        throw new RuntimeException();
    }

    @Override
    public void clear() {
        while(this.head.previous != this.head) {
            this.head.previous.unlink();
        }
    }

    @Override
    public boolean add(T arg0) {
        return this.add_first(arg0);
    }

    @Override
    public boolean equals(Object arg0) {
        return super.equals(arg0);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
