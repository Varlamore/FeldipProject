package com.cryptic.cache.graphics.widget.impl.questtabs;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.DEFAULT_TEXT_COLOR;

public class ModernAchievementsTab extends InterfaceBuilder {
    public ModernAchievementsTab() {
        super(80200);
    }

    @Override
    public void build() {

        int x = 0;
        int y = 3;

        addConfigButton(nextInterface(), 80000, 2125, 2124, 34, 21, "Character summary", 0, 5, 1306);
        child(40 + x, y);
        addConfigButton(nextInterface(), 80000, 2127, 2126, 34, 21, "Quests", 1, 5, 1306);
        child(80 + x, y);
        addConfigButton(nextInterface(), 80000, 2129, 2128, 34, 21, "Achievements", 2, 5, 1306);
        child(120 + x, y);

        drawPixels(nextInterface(), 192, 1, 0x544b40, 0x544b40, 100);
        child(0, 23);

        addTitleText(nextInterface(), "Achievements");
        child(94 + x, 25 + y);

        //addSprite(nextInterface(), 1281);
        addBorder(nextInterface(), 180, 211);
        child(6, 45);

        //1028 normal and 1029 hovered
        Widget scroll = addTabInterface(nextInterface());
        child(13, 51);
        scroll.width = 151;
        scroll.height = 202;
        scroll.scrollMax = 2810;
        int childid = 0;
        scroll.totalChildren(400);

        // Progress bars
        int barY = 28;
        for (int index = 0; index < 100; index++) {
            //Select button
            hoverButton(nextInterface(), 1028, 1029, "View Achievement", 0, 0, "", false);
            scroll.child(childid++, lastInterface(), 0, (index * barY));
            //Name of task
            addText(nextInterface(), 0, DEFAULT_TEXT_COLOR, false, "Fetch 25 Yew logs");
            System.out.println("Task name begins at: " + lastInterface());
            scroll.child(childid++, lastInterface(),3, 4 + (index * barY));
            //% of task
            addText(nextInterface(), 0, DEFAULT_TEXT_COLOR, true, "100%");
            System.out.println("Task % begins at: " + lastInterface());
            scroll.child(childid++, lastInterface(),132, 4 + (index * barY));
            //Progress bars for achievement
            drawProgressBar(nextInterface(),145, 7,100,0,0);
            System.out.println("Progress bar begins at: " + lastInterface());
            scroll.child(childid++, lastInterface(),3, 17 + (index * barY));
        }
    }
}
