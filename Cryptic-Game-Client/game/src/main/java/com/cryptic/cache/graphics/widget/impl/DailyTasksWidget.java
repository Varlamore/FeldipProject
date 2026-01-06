package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;

/**
 * @author Patrick van Elderen | April, 13, 2021, 10:39
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class DailyTasksWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        Widget widget = addTabInterface(41400);
        addSprite(41401, 1903);
        closeButton(41402, 107, 108, false);
        addText(41403, "Daily Money Activities", font, 2, 0xF7AA25, true, true);
        addText(41404, "Activities", font, 2, 0xF7AA25, true, true);
        addText(41405, "Activity (Expires: <col=ffff00>13h 52m 53s)", font, 1, 0xF7AA25, true, true);
        drawProgressBar(41406,290,15,50,0,0);
        addText(41407, "0% (0/3)", font, 0, 0xffffff, true, true);
        addText(41408, "Vote for us on 3 top list sites", font, 2, 0xF7AA25, true, true);addText(41404, "Activities", font, 2, 0xF7AA25, true, true);
        addText(41409, "Fill up the bar for rewards! (2X bonus!)", font, 1, 0xF7AA25, false, true);
        addText(41410, "Information", font, 2, 0xF7AA25, true, true);
        addText(41411, "Recommended Combat<br>Level:", font, 1, 0xF7AA25, true, true);
        addText(41412, "Any!", font, 1, 65280, true, true);
        addText(41413, "<u=ffff00>Location:", font, 1, 0xF7AA25, true, true);
        addText(41414, "Safe zone", font, 1, 65280, true, true);
        hoverButton(41415, "Claim", 1844, 1845);
        addText(41418, "Claim", font, 2, 0xff981f, false, true);
        addContainer(41419, TYPE_CONTAINER, 7, 1, 13, 0, 0, false, true, true);
        addConfigButton(41420, 1163, 1165, 1166, 115, 24, "Easy", 1, 5, 1160, false);
        addConfigButton(41421, 1164, 1165, 1166, 115, 24, "Med", 0, 5, 1161, false);
        addConfigButton(41422, 1165, 1165, 1166, 115, 24, "Hard", 0, 5, 1162, false);
        addText(41423, "Easy", font, 1, 0xffffff, true, true);
        addText(41424, "Med", font, 1, 0xffffff, true, true);
        addText(41425, "Hard", font, 1, 0xffffff, true, true);
        hoverButton(41426, "Teleport", 1904, 1905);
        addText(41429, "Teleport", font, 2, 0xff981f, false, true);

        Widget scrollInterface = addTabInterface(41430);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 138;
        scrollInterface.height = 240;
        scrollInterface.scrollMax = 300;
        int x = 5, y = 3;
        int amountOfLines = 20;
        scrollInterface.totalChildren(amountOfLines);
        for (int index = 0; index < amountOfLines; index++) {
            addText(41431 + index, "Voting", font, 1, 0xff981f, false, false, 0xffffff, "Select achievement", 150);
            textClicked(41431 + index, 1137, 1, 0);
            scrollInterface.child(index, 41431 + index, x, y);
            y += 18;
        }

        widget.totalChildren(26);
        widget.child(0, 41401, 6, 10);
        widget.child(1, 41402, 480, 17);
        widget.child(2, 41403, 275, 19);
        widget.child(3, 41404, 88, 46);
        widget.child(4, 41405, 295, 157);
        widget.child(5, 41406, 193, 181);
        widget.child(6, 41407, 340, 183);
        widget.child(7, 41408, 340, 203);
        widget.child(8, 41409, 209, 232);
        widget.child(9, 41410, 248, 53);
        widget.child(10, 41411, 253, 72);
        widget.child(11, 41412, 250, 100);
        widget.child(12, 41413, 255, 117);
        widget.child(13, 41414, 255, 131);
        widget.child(14, 41415, 380, 110);
        widget.child(15, 41418, 399, 129);
        widget.child(16, 41419, 185, 255);
        widget.child(17, 41420, 11, 18);
        widget.child(18, 41421, 64, 18);
        widget.child(19, 41422, 117, 18);
        widget.child(20, 41423, 31, 21);
        widget.child(21, 41424, 84, 21);
        widget.child(22, 41425, 137, 21);
        widget.child(23, 41426, 380, 50);
        widget.child(24, 41429, 390, 69);
        widget.child(25, 41430, 14, 65);
    }
}
