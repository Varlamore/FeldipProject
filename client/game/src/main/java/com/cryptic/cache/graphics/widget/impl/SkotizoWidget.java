package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.widget.Widget;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since December 19, 2021
 */
public class SkotizoWidget extends Widget {

    public static void unpack() {
        Widget tab = addInterface(29230);
        tab.totalChildren(5);
        addSprite(29231, 1932);
        tab.child(0, 29231, 26, 21);
        addSprites(29232, 1936, 1933);
        tab.child(1, 29232, 46, 8);
        addSprites(29233, 1936, 1933);
        tab.child(2, 29233, 46, 73);
        addSprites(29234, 1936, 1933);
        tab.child(3, 29234, 13, 40);
        addSprites(29235, 1936, 1933);
        tab.child(4, 29235, 78, 40);
    }
}
