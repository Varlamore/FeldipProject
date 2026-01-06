package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

public class ItemStatementContainer extends InterfaceBuilder {
    public ItemStatementContainer() {
        super(4900);
    }

    @Override
    public void build() {
        Widget parent = cache[4900];
        setChildren(3, parent);
        setBounds(4901, 32, 55, 0, parent);
        setBounds(4902, 110, 25, 1, parent);
        setBounds(4907, 110, 65, 2, parent);
    }
}
