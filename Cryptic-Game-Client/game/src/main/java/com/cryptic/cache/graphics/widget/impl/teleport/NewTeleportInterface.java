package com.cryptic.cache.graphics.widget.impl.teleport;

import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;

public class NewTeleportInterface extends Widget {
    static int teleportbutton = 88101;
    static   int textbutton = 88101 + 30 ;
    static int descriptionbutton = 88101 + 30 + 30;
    static int favoritebutton = 88101 + 30 + 30 + 30;
    static int hoverinterface = 2080;
    public static void unpack(AdvancedFont[] font) {
        teleportInterface(font);
    }
    public static String[] CATEGORY_NAMES = new String[]{"Favorites", "Training", "Slayer", "Bossing", "Skilling", "Minigames", "Wilderness", "Cities", "Miscellaneous"};
    private static void teleportInterface(AdvancedFont[] font) {

        Widget main = addInterface(88000);
        addSprite(88001, 2061);
        addText(88002,  "Teleports Selection", font, 2, 0xFF981F, true, true);
        // addInputField(88003, 12, 0xFF981F, "*", 140, 22, false, false);
        addText(88003, "Categories", font, 2, 0xFF981F, false, true);

        closeButton(88004, 107, 108, false);

        main.totalChildren(33);

        main.child(0, 88001, 0, 15);//main interface
        main.child(32, 88002, 310, 58);//dropdown
        main.child(2, 88003, 35, 58);//addinputfield
        main.child(3, 88004, 482, 22);//close button


        int childStart = 4;
        int yPos = 81;//80
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {

            hoverButton10(88005 + i, "Select", 2064, 2065, "", font, 1, 0xdb9c22, 0xdb9c22, true);
            addSprite(88014 + i, 2066 + i);
            addText(88023 + i, CATEGORY_NAMES[i], font, 2, 0xFF981F, false, true);

            main.child(i + 4, 88005 + i, 13, yPos);
            main.child(i + 4 + CATEGORY_NAMES.length, 88014 + i, 18, yPos + 6);
            main.child(i + 4 + (CATEGORY_NAMES.length*2), 88023 + i, 40, yPos + 5);
            childStart++;
            yPos += 25;
        }
        addText(88032, "Teleporter", font, 2, 0xFF981F, false, true);
        main.child(31, 88032, 205, 25);

        main.child(1, 88050, 142,82); // scroll

        Widget scroll = addInterface(88050);
        scroll.scrollMax = 1040;
        scroll.width = 340;
        scroll.height = 227;

        setChildren(120, scroll);

        int xpos = -10;
        int ypos = 0;

        int start = 0;

        for(int i = 0 ; i < 30; i ++){
            hoverButton10(teleportbutton + i, "Select", i % 2 == 0 ? 2075 : 2076, hoverinterface, "", font, 1, 0xdb9c22, 0xdb9c22, true);
            addText(textbutton+i, "Agility: Gnome Course", font, 2, 0xFF981F, false, true);
            addText(descriptionbutton+i, "Beginner course.", font, 1, 0xffffff, false, true);
            addInteractiveButton(favoritebutton+i, 2078, 2077, 25, 25, "Toggle");


            setBounds(teleportbutton + i, xpos, ypos + 1, i + start, scroll);
            setBounds(textbutton+i, xpos+18, ypos + 6, i + 30 + start, scroll);
            setBounds(descriptionbutton+i, xpos+18, ypos + 19, i + 30 + 30 + start, scroll);
            setBounds(favoritebutton+i, xpos+330, ypos + 10, i + 30 + 30 + 30 + start, scroll);

            ypos += 36;
        }

    }
}

