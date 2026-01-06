package com.cryptic.cache.graphics.widget.impl.lootkey;

import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.util.ConfigUtility.WITHDRAW_AS_ITEM_LOOT_KEY;
import static com.cryptic.util.ConfigUtility.WITHDRAW_AS_NOTE_LOOT_KEY;

/**
 * @author Patrick van Elderen <<a href="https://github.com/PVE95">...</a>>
 * @Since March 29, 2022
 */
public class LootKeyWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        Widget widget = addInterface(69500);

        addSprite(69501, 2004);
        closeButton(69502, 107,108,false);
        addText(69503, "Wilderness Loot Key", font, 2, 0xff9933, true, true);
        addText(69504, "Value in chest:", font, 1, 0xffffff, true, true);
        addText(69505, "49,614gp", font, 1, 0xffffff, false, true);
        addText(69506, "Withdraw as:", font, 1, 0xff9933, true, true);
        addText(69507, "%1", font, 0, 0xFE9624, true);
        cache[69507].valueIndexArray = new int[][] { { 22, 5382, 0 } };
        addText(69508, "size", font, 0, 0xFE9624, true, true);

        addConfigButton(69509, 69500, 893, 894, 115, 24, "Item", 0, 5, WITHDRAW_AS_ITEM_LOOT_KEY, true);
        addConfigButton(69510, 69500, 893, 894, 115, 24, "Note", 0, 5, WITHDRAW_AS_NOTE_LOOT_KEY, true);

        addText(69511, "Item", font, 1, 0xff981f, true, true);
        addText(69512, "Note", font, 1, 0xff981f, true, true);

        adjustableConfig(69513, "Deposit inventory", 2001, 180, 728, 727);
        adjustableConfig(69514, "Deposit bank", 2002, 180, 728, 727);
        adjustableConfig(69515, "Clear", 2003, 180, 728, 727);

        addContainer(69516, TYPE_CONTAINER, 7, 4, 8, 3, 0, false, true, true);

        widget.totalChildren(16);
        widget.child(0, 69501, 100, 25);
        widget.child(1, 69502, 380, 32);
        widget.child(2, 69503, 250, 34);
        widget.child(3, 69504, 225, 63);
        widget.child(4, 69505, 268, 63);
        widget.child(5, 69506, 190, 262);
        widget.child(6, 69509, 130, 277);
        widget.child(7, 69510, 182, 277);
        widget.child(8, 69511, 156, 279);
        widget.child(9, 69512, 207, 279);
        widget.child(10, 69513, 235, 264);
        widget.child(11, 69507, 295, 270);
        widget.child(12, 69508, 295, 280);
        widget.child(13, 69514, 312, 264);
        widget.child(14, 69515, 361, 264);
        widget.child(15, 69516, 117, 77);
    }
}

