package com.cryptic.collection.node;

import net.runelite.rs.api.RSNode;

public class Node implements RSNode {

    public long key;

    public Node previous;

    public Node next;

    public void remove() {
        if (this.next != null) {
            this.next.previous = this.previous;
            this.previous.next = this.next;
            this.previous = null;
            this.next = null;
            onUnlink();
        }
    }

    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public RSNode getNext() {
        return next;
    }

    @Override
    public void setNext(RSNode var1) {
        next = (Node) var1;
    }

    @Override
    public RSNode getPrevious() {
        return previous;
    }

    @Override
    public void setPrevious(RSNode var1) {
        previous = (Node) var1;
    }

    @Override
    public long getHash() {
        return key;
    }

    @Override
    public void unlink() {
        remove();
    }

    @Override
    public void onUnlink() {

    }
}
