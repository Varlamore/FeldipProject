package com.cryptic.cache.graphics.widget.impl.accounttabs;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;

public class LinksTab extends InterfaceBuilder {
    public LinksTab() {
        super(73050);
    }

    @Override
    public void build() {

        addConfigButton(nextInterface(), 73000, 2130, 2132, 59, 26, "Account", 0, 5, 1357);
        child(3, 3);
        addConfigButton(nextInterface(), 73000, 2136, 2138, 59, 26, "Useful Links", 1, 5, 1357);
        child(65, 3);
        addConfigButton(nextInterface(), 73000, 2133, 2135, 59, 26, "Community", 2, 5, 1357);
        child(127, 3);

        addText(nextInterface(), "Useful Links", fonts, 2, 0xFF9800, true, true);
        child(94, 34);

        String[] linkNames = { "Discord", "Website", "Donate", "Vote", "Hiscores" };
        int x = 12;
        int y = 52;
        for (int i = 0; i < linkNames.length; i++, x += 91) {
            if (i == 2 || i == 4) {
                x = 12;
                y += 43;
            } // Next row
            addSprite(nextInterface(), 2140);
            child(x, y);
            addClickableText(nextInterface(), linkNames[i], linkNames[i], fonts, 0, 0xF7F0DE, true, true, 75,  22);
            child(x, y + 12);
        }

    }
}
