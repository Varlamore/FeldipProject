package com.cryptic.net.tcp.util;

import com.cryptic.entity.model.HashLink;

import java.util.Iterator;

public class DequeIterator<T extends HashLink> implements Iterator<T> {
    Deque<T> deque;
    HashLink field4310;
    HashLink last = null;

    DequeIterator(Deque<T> arg0) {
        this.setDeque(arg0);
    }

    void setDeque(Deque<T> arg0) {
        this.deque = arg0;
        this.start();
    }

    void start() {
        this.field4310 = this.deque != null ? this.deque.head.previous : null;
        this.last = null;
    }

    public T next() {
        HashLink var1 = this.field4310;
        if (var1 == this.deque.head) {
            var1 = null;
            this.field4310 = null;
        } else {
            this.field4310 = var1.previous;
        }

        this.last = var1;
        return (T) var1;
    }

    public boolean hasNext() {
        return this.field4310 != this.deque.head && this.field4310 != null;
    }

    public void remove() {
        if (this.last == null) {
            throw new IllegalStateException();
        } else {
            this.last.unlink();
            this.last = null;
        }
    }
}

