package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.SpriteCache;
import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;

/**
 * The class which represents functionality for the equipment widget.
 *
 * @author Patrick van Elderen | 07:20 : dinsdag 2 juli 2019 (CEST)
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public class EquipmentWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        equipment_tab_widget();
    }

    public static void buildEquipmentTab() {
        final Widget parent = cache[1644];
        final Widget newTab = createSprite(parent.id, 1835, 1835);
        final Widget inventory = cache[1688];
        inventory.inventoryOffsetX[14] = -97;
        inventory.inventoryOffsetY[14] = -198;
        inventory.sprites[14] = SpriteCache.get(1836);
        addChild(parent.id, newTab.id, 37, 4, getIndexOfChild(parent.id, inventory.id));
    }

    private static void equipment_tab_widget() {
        removeConfig(21338);
        removeConfig(21344);
        removeConfig(21342);
        removeConfig(21341);
        removeConfig(21340);
        removeConfig(15103);
        removeConfig(15104);
        Widget main_widget = cache[1644];
        main_widget.children[26] = 27650;
        main_widget.child_x[26] = 0;
        main_widget.child_y[26] = 0;

        //Move equipment widget
        main_widget.child_x[23] = 23;
        main_widget.child_y[23] = 42;

        main_widget = addInterface(27650);

        hoverButton(27651, "View guide prices", 146, 147);
        hoverButton(27653, "View equipment stats", 144, 145);
        hoverButton(27654, "View items kept on death", 148, 149);
        hoverButton(27668, "Call follower", 872, 873);

        setChildren(4, main_widget);

        // Prices
        setBounds(27651, 52, 205, 0, main_widget);
        // Death
        setBounds(27654, 98, 205, 1, main_widget);
        // Follower
        setBounds(27668, 143, 205, 2, main_widget);
        // Equip
        setBounds(27653, 6, 205, 3, main_widget);
    }

}
