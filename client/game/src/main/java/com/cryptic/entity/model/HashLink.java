package com.cryptic.entity.model;

public class HashLink {
    public HashLink previous;
    public HashLink next;
    public long key;

    public void unlink() {
        if (this.next != null) {
            this.next.previous = this.previous;
            this.previous.next = this.next;
            this.previous = null;
            this.next = null;
        }
    }

    public boolean hasNext() {
        return this.next != null;
    }
}