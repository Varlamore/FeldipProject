package com.cryptic.collection;

import com.cryptic.collection.node.DualNode;
import net.runelite.rs.api.RSObjectNode;

public class ObjectNode extends DualNode implements RSObjectNode {

    public final Object obj;

    public ObjectNode(Object var1) {
        this.obj = var1;
    }

    @Override
    public Object getValue() {
        return obj;
    }
}