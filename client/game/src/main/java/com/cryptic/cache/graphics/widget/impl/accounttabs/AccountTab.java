package com.cryptic.cache.graphics.widget.impl.accounttabs;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;

public class AccountTab extends InterfaceBuilder {
    public AccountTab() {
        super(73000);
    }

    @Override
    public void build() {

        addConfigButton(nextInterface(), 73000, 2130, 2132, 59, 26, "Account", 0, 5, 1357);
        child(3, 3);
        addConfigButton(nextInterface(), 73000, 2136, 2138, 59, 26, "Useful Links", 1, 5, 1357);
        child(65, 3);
        addConfigButton(nextInterface(), 73000, 2133, 2135, 59, 26, "Community", 2, 5, 1357);
        child(127, 3);

        addText(nextInterface(), "Account", fonts, 2, 0xFF9800, true, true);
        child(94, 34);

        addText(nextInterface(), "Donator: Sapphire", fonts, 1, 0xFF9800, true, true);
        child(94, 60);

        addSprite(nextInterface(), 2141);
        child(27, 84);

        addClickableText(nextInterface(), "Open Store", "Open Store", fonts, 0, 0xF7F0DE, false, true, 132, 32);
        child(65, 92);
        addSprite(nextInterface(), 2143);//Side sprites left
        child(34, 88);
        addSprite(nextInterface(), 2143);//Side sprites right
        child(132, 88);

        addText(nextInterface(), "Choose Recovery Email", fonts, 1, 0xFF9800, true, true);
        child(94, 120);
        addSprite(nextInterface(), 2141);
        child(27, 140);
        addClickableText(nextInterface(), "Choose Email", "Open Recovery Email", fonts, 0, 0xF7F0DE, false, true, 132, 32);
        child(59, 148);
        addSprite(nextInterface(), 2145);//Side sprites left
        child(34, 144);
        addSprite(nextInterface(), 2145);//Side sprites right
        child(132, 144);

        addText(nextInterface(), "Name: Clive", fonts,1, 0xFF9800, true, true);
        child(94, 176);
        addSprite(nextInterface(), 2141);
        child(27, 196);
        addClickableText(nextInterface(), "Name Changer", "Change Name", fonts, 0, 0xF7F0DE, false, true, 132,  32);
        child(57, 204);
        addSprite(nextInterface(), 2139);//Side sprites left
        child(34, 200);
        addSprite(nextInterface(), 2139);//Side sprites right
        child(132, 200);

    }
}
