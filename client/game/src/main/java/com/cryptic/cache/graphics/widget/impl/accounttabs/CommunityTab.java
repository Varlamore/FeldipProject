package com.cryptic.cache.graphics.widget.impl.accounttabs;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;

public class CommunityTab extends InterfaceBuilder {
    public CommunityTab() {
        super(73100);
    }

    @Override
    public void build() {

        addConfigButton(nextInterface(), 73000, 2130, 2132, 59, 26, "Account", 0, 5, 1357);
        child(3, 3);
        addConfigButton(nextInterface(), 73000, 2136, 2138, 59, 26, "Useful Links", 1, 5, 1357);
        child(65, 3);
        addConfigButton(nextInterface(), 73000, 2133, 2135, 59, 26, "Community", 2, 5, 1357);
        child(127, 3);

        addText(nextInterface(), "Community", fonts, 2, 0xFF9800, true, true);
        child(94, 34);

        addText(nextInterface(), "Current Poll: @red@Inactive", fonts,1, 0xFF9800, true, true);
        child(118, 60);

        addSprite(nextInterface(), 2141);
        child(27, 84);
        addClickableText(nextInterface(), "View Poll", "View Poll", fonts, 0, 0xF7F0DE, true, true, 132,  32);
        child(29, 93);
        addSprite(nextInterface(), 2149);//Side sprites left
        child(34, 88);
        addSprite(nextInterface(), 2149);//Side sprites right
        child(132, 88);

        addSprite(nextInterface(), 2141);
        child(27, 119);
        addClickableText(nextInterface(), "View History", "View History", fonts, 0, 0xF7F0DE, true, true, 132,  32);
        child(29, 127);
        addSprite(nextInterface(), 2147);//Side sprites left
        child(34, 126);
        addSprite(nextInterface(), 2147);//Side sprites right
        child(132, 126);



        addText(nextInterface(), "Latest Update", fonts,2, 0xFF9800, true, true);
        child(94, 157);

        addSprite(nextInterface(), 2141);
        child(27, 177);
        addClickableText(nextInterface(), "View Newspost", "View Newspost", fonts, 0, 0xF7F0DE, true, true, 132,  32);
        child(29, 185);
        addSprite(nextInterface(), 2146);//Side sprites left
        child(34, 181);
        addSprite(nextInterface(), 2146);//Side sprites right
        child(132, 181);

        addSprite(nextInterface(), 2141);
        child(27, 212);
        addClickableText(nextInterface(), "View Archive", "View Archive", fonts, 0, 0xF7F0DE, true, true, 132,  32);
        child(29, 220);
        addSprite(nextInterface(), 2148);//Side sprites left
        child(34, 216);
        addSprite(nextInterface(), 2148);//Side sprites right
        child(132, 216);

    }
}
