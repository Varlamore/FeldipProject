package com.cryptic.cache.graphics.widget.impl.questtabs;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;

public class ModernOSRSQuestTab extends InterfaceBuilder {
    public ModernOSRSQuestTab() {
        super(80000);
    }

    @Override
    public void build() {

        int x = 0;
        int y = 3;

        int yIncrement = 1;

        addConfigButton(nextInterface(), 80000, 2125, 2124, 34, 21, "Character summary", 0, 5, 1306);
        child(40 + x, y);
        addConfigButton(nextInterface(), 80000, 2127, 2126, 34, 21, "Quests", 1, 5, 1306);
        child(80 + x, y);
        addConfigButton(nextInterface(), 80000, 2129, 2128, 34, 21, "Achievements", 2, 5, 1306);
        child(120 + x, y);

        drawPixels(nextInterface(), 192, 1, 0x544b40, 0x544b40, 100);
        child(0, 23);

        addTitleText(nextInterface(), "Clive");

        child(95 + x, 24 + y);

        addSprite(nextInterface(), 2157);
        child(x + 4, y + 41 + yIncrement);
        addText(nextInterface(), "Combat Level:", fonts, 0, 16750643, true, true);
        child(49, 47 + y + yIncrement);
        addText(nextInterface(), "0", fonts, 0, 0x0DC10D, false, true);

        child(x + 48, 63 + y + yIncrement);

        addSprite(nextInterface(), 2157);
        child(x + 97, y + 41 + yIncrement);
        addText(nextInterface(), "Total Level:", fonts, 0, 16750643, true, true);
        child(141, 47 + y + yIncrement);
        addText(nextInterface(), "0", fonts, 0, 0x0DC10D, false, true);

        child(139, 63 + y + yIncrement);

        addSprite(nextInterface(), 2150);
        child(x + 4, y + 85 + yIncrement);
        addSprite(nextInterface(), 2153);
        child(x + 30, y + 91 + yIncrement);
        addText(nextInterface(), "Total XP: <col=0DC10D>46,827,168", fonts, 0, 16750643, false, true);
        child(51, 94 + y + yIncrement);

        addSprite(nextInterface(), 2152);
        child((int) (x + 4.5), y + 115 + yIncrement);
        addText(nextInterface(), " Completed Daily\\n Tasks:", fonts, 0, 16750643, true, true);
        child(49, 122 + y + yIncrement);
        addClickableText(nextInterface(), "0/5", "Display daily tasks", fonts, 0, 0x0DC10D, false, true, 50);
        child(45, 149 + y + yIncrement);
        addSprite(nextInterface(), 543);
        child(x + 22, y + 145 + yIncrement);

        addSprite(nextInterface(), 2152);
        child((int) (x + 97.5), y + 115 + yIncrement);
        addText(nextInterface(), "Achievements\\nCompleted:", fonts, 0, 16750643, true, true);
        child(143, 122 + y + yIncrement);
        addClickableText(nextInterface(), "204/492", "Select", fonts, 0, 0x0DC10D, false, true, 50);
        child(130, 149 + y + yIncrement);
        addSprite(nextInterface(), 542);
        child(x + 109, y + 145 + yIncrement);

        addSprite(nextInterface(), 2119);
        child((int) (x + 4.2), y + 171 + yIncrement);
        addText(nextInterface(), "Collections\\n Logged:", fonts, 0, 16750643, true, true);
        child(95, 177 + y + yIncrement);
        addSprite(nextInterface(), 2120);
        child(63, 203 + yIncrement);
        addClickableText(nextInterface(), "47/1476", "Open collection log", fonts, 0, 0xdc10d, false, true, 50);
        child(85, 203 + y + yIncrement);

        addSprite(nextInterface(), 2151);
        child(x + 4, y + 227 + yIncrement);
        addClickableText(nextInterface(), "Time Played - Click to reveal:", "Reveal", fonts, 0, 16750643, true, true, 143);
        child(25, 235 + y + yIncrement);

        addSprite(nextInterface(), 2154);
        child(x + 26, 63 + yIncrement);
        addSprite(nextInterface(), 2155);
        child(x + 118, 63 + yIncrement);
    }
}
