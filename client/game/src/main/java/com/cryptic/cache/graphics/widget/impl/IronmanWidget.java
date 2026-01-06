package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;

/**
 * The class which represents functionality for the ironman interface.
 *
 * @author Patrick van Elderen | March, 06, 2021, 14:37
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class IronmanWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        ironman_widget_new(font);
        ironmanLeaderboard(font);
    }

    public static void ironmanLeaderboard(AdvancedFont[] font) {
        Widget widget = addInterface(67000);
        addSprite(67001, 1781);
        closeButton(67002, 24, 25, false);
        addText(67005, "<img=1770> Group Ironman Leaderboards", font, 2, 0xff981f, false, true);
        addText(67006, "Top 10 Groups", font, 2, 0xff981f, false, true);
        addText(67007, "Recent Groups", font, 2, 0xff981f, false, true);
        addText(67008, "Team:", font, 2, 0xff981f, false, true);
        addText(67009, "Status:", font, 2, 0xff981f, false, true);
        addText(67010, "Avg. Combat:", font, 2, 0xff981f, false, true);
        addText(67011, "Avg. Total And EXP:", font, 2, 0xff981f, false, true);
        addButton(67012, 1786, "Invite");
        addButton(67013, 1786, "Set name");
        addButton(67014, 1786, "Delete group");
        addText(67015, "Invite", font, 2, 0xff981f, false, true);
        addText(67016, "Set name", font, 2, 0xff981f, false, true);
        addText(67017, "Delete", font, 2, 0xff981f, false, true);
        addButton(67018, 1786, "Create");
        addText(67019, "Create", font, 2, 0xff981f, false, true);
        setChildren(19, widget);
        setBounds(67001, 20, 24, 0, widget);
        setBounds(67002, 479, 31, 1, widget);
        setBounds(67005, 150, 30, 2, widget);
        setBounds(67006, 50, 55, 3, widget);
        setBounds(67007, 50, 217, 4, widget);
        setBounds(67008, 40, 77, 5, widget);
        setBounds(67009, 163, 77, 6, widget);
        setBounds(67010, 240, 77, 7, widget);
        setBounds(67011, 350, 77, 8, widget);
        setBounds(67012, 365, 214, 9, widget);
        setBounds(67013, 365, 241, 10, widget);
        setBounds(67014, 365, 268, 11, widget);
        setBounds(67015, 390, 218, 12, widget);
        setBounds(67016, 379, 245, 13, widget);
        setBounds(67017, 388, 273, 14, widget);
        setBounds(67125, 35, 94, 15, widget);
        setBounds(67225, 35, 235, 16, widget);
        setBounds(67018, 365, 295, 17, widget);
        setBounds(67019, 388, 299, 18, widget);

        //Top 10
        Widget main = addTabInterface(67125);
        main.scrollPosition = 0;
        main.contentType = 0;
        main.width = 435;
        main.height = 117;
        main.scrollMax = 302;

        int y = 2;
        final int CHILD_LENGTH = 10 * 6;
        int child = 0;
        main.totalChildren(CHILD_LENGTH);
        int section = 0;
        for (int i = 67130; i < 67130 + CHILD_LENGTH; i += 6) {
            section++;
            addSprite(i, section % 2 == 0 ? 1784 : 1785);
            addText(i + 1, "Grootte bois040", font, 1, 16750623);
            addText(i + 2, "Offline", font, 1, 16750623);
            addText(i + 3, "Level: 126", font, 1, 16750623);
            addText(i + 4, "Total Level: 1202", font, 1, 16750623);
            addText(i + 5, "Total Exp: 200M", font, 1, 16750623);

            main.child(child++, i, 0, y);
            main.child(child++, i + 1, 3, y + 6);
            main.child(child++, i + 2, 130, y + 6);
            main.child(child++, i + 3, 215, y + 6);
            main.child(child++, i + 4, 328, y);
            main.child(child++, i + 5, 328, y + 14);
            y += 30;
        }

        //Recent
        Widget recent = addTabInterface(67225);
        recent.scrollPosition = 0;
        recent.contentType = 0;
        recent.width = 278;
        recent.height = 59;
        recent.scrollMax = 182;

        int recent_y = 2;
        final int RECENT_CHILD_LENGTH = 6 * 3;
        int recent_child = 0;
        recent.totalChildren(RECENT_CHILD_LENGTH);
        int recent_section = 0;
        for (int i = 67230; i < 67230 + RECENT_CHILD_LENGTH; i += 3) {
            recent_section++;
            addSprite(i, recent_section % 2 == 0 ? 1782 : 1783);
            addText(i + 1, "Group Name", font, 2, 0xff0000);
            addText(i + 2, "Henkie, Patrick,<br>Malefique, Wezel", font, 0, 16750623);

            recent.child(recent_child++, i, 0, recent_y);
            recent.child(recent_child++, i + 1, 3, recent_y + 6);
            recent.child(recent_child++, i + 2, 130, recent_y + 2);
            recent_y += 30;
        }
    }

    private static void ironman_widget_new(AdvancedFont[] font) {
        Widget widget = addInterface(42400);
        addSprite(42401, 1766);

        addText(42417, "Select your game mode", font, 1, 0xFFFFFF, false, true);
        hoverButton(42419, "Confirm Gamemode", 1777, 1776);

        setChildren(6, widget);

        addText(42421, "Account Selection", font, 2, 0xFD851A, false, true);
        addText(42422, "Confirm", font, 2, 0xFD851A, false, true);

        setBounds(42421, 200, 37, 4, widget);
        setBounds(42422, 422, 274, 5, widget);

        setBounds(42401, 15, 28, 0, widget);
        setBounds(42417, 150, 69, 1, widget);
        setBounds(42419, 412, 268, 2, widget);


        /**Scroll bounds**/
        setBounds(42425, 22, 87, 3, widget);

        int recentY = 5;


        /**Scroll Bar Widget**/
        Widget scroll = addTabInterface(42425);

        scroll.totalChildren(31);

        scroll.child(0, 42801, 0, 0);

        /**Scroll Bounds**/
        scroll.scrollPosition = 0;
        scroll.contentType = 0;
        scroll.width = 365;
        scroll.height = 208;
        scroll.scrollMax = 225;

        int x1 = 65;
        int x2 = 28;
        addText(42802, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42803, "Description", font, 0, 0xFD851A);
        scroll.child(1, 42802, 30, recentY);
        scroll.child(2, 42803, x2 + 2, recentY + 15);

        addText(42804, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42805, "Description", font, 0, 0xFD851A);
        scroll.child(3, 42804, 30, recentY + 45);
        scroll.child(4, 42805, x2 + 2, recentY + 60);

        addText(42806, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42807, "Description", font, 0, 0xFD851A);
        scroll.child(5, 42806, 30, recentY + 90);
        scroll.child(6, 42807, x2 + 2, recentY + 105);

        addText(42808, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42809, "Description", font, 0, 0xFD851A);
        scroll.child(7, 42808, 30, recentY + 135);
        scroll.child(8, 42809, x2 + 2, recentY + 150);

        addText(42810, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42811, "Description", font, 0, 0xFD851A);
        scroll.child(9, 42810, 30, recentY + 180);
        scroll.child(10, 42811, x2 + 2, recentY + 195);

        addText(42817, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42818, "Description", font, 0, 0xFD851A);
        scroll.child(11, 42817, 30, recentY + 225);
        scroll.child(12, 42818, x2 + 2, recentY + 240);

        addText(42819, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42820, "Description", font, 0, 0xFD851A);
        scroll.child(13, 42819, 30, recentY + 270);
        scroll.child(14, 42820, x2 + 2, recentY + 285);

        addText(42821, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42822, "Description", font, 0, 0xFD851A);
        scroll.child(15, 42821, 30, recentY + 315);
        scroll.child(16, 42822, x2 + 2, recentY + 330);

        addText(42823, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42824, "Description", font, 0, 0xFD851A);
        scroll.child(17, 42823, 30, recentY + 360);
        scroll.child(18, 42824, x2 + 2, recentY + 375);

        addText(42825, "Normal account", font, 2, 0xFFFFFF, false);
        addText(42826, "Description", font, 0, 0xFD851A);
        scroll.child(19, 42825, 30, recentY + 405);
        scroll.child(20, 42826, x2 + 2, recentY + 420);

        addClickableSprites(42812, "Toggle", 490, 491, 547);
        addClickableSprites(42813, "Toggle", 490, 491, 547);
        addClickableSprites(42814, "Toggle", 490, 491, 547);
        addClickableSprites(42815, "Toggle", 490, 491, 547);
        addClickableSprites(42816, "Toggle", 490, 491, 547);
        addClickableSprites(42827, "Toggle", 490, 491, 547);
        addClickableSprites(42828, "Toggle", 490, 491, 547);
        addClickableSprites(42829, "Toggle", 490, 491, 547);
        addClickableSprites(42830, "Toggle", 490, 491, 547);
        addClickableSprites(42831, "Toggle", 490, 491, 547);

        scroll.child(21, 42812, 8, recentY + 12);
        scroll.child(22, 42813, 8, recentY + 57);
        scroll.child(23, 42814, 8, recentY + 102);
        scroll.child(24, 42815, 8, recentY + 147);
        scroll.child(25, 42816, 8, recentY + 192);
        scroll.child(26, 42827, 8, recentY + 237);
        scroll.child(27, 42828, 8, recentY + 282);
        scroll.child(28, 42829, 8, recentY + 327);
        scroll.child(29, 42830, 8, recentY + 372);
        scroll.child(30, 42831, 8, recentY + 417);

    }
}