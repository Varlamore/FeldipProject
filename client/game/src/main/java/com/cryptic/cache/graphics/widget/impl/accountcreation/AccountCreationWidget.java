package com.cryptic.cache.graphics.widget.impl.accountcreation;

import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;
import static com.cryptic.util.ConfigUtility.DIFFICULTY_CONFIG_ID;

public class AccountCreationWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        /** id **/
        Widget widget = addInterface(54000);

        widget.totalChildren(21);

        /** Background **/
        addSprite(54001, 1991);
        widget.child(0, 54001, 50, 10);
        /** Character Bounds Box **/
        addTransparentSprite(54002, 2000, 80);
        widget.child(1, 54002, 302, 70);
        /** Character Models **/
        addCharacterToInterface(54003, 800);
        widget.child(2, 54003, 281, 190);
        /** Next Option Buttons **/
        hoverButton(54004, "Next Option", 1996, 1995);
        widget.child(3, 54004, 385, 49);
        hoverButton(54005, "Previous Option", 1994, 1993);
        widget.child(4, 54005, 292, 49);

        addConfigButton(54006, 904, 397, 393, "Select", DIFFICULTY_CONFIG_ID, 1, 5);
        addConfigButton(54007, 904, 398, 394, "Select", DIFFICULTY_CONFIG_ID, 2, 5);
        addConfigButton(54008, 904, 399, 395,  "Select", DIFFICULTY_CONFIG_ID, 3, 5);
        addConfigButton(54009, 904, 400, 396, "Select", DIFFICULTY_CONFIG_ID, 4, 5);

        /** Close Button **/
        hoverButton(54010, "Close", 1997, 1998);
        widget.child(9, 54010, 440, 20);

        /** Text **/
        addText(54011, "Gamemode", font, 0, 0xff9933, false, true);
        widget.child(5, 54011, 323, 50);
        addText(54012, "Account Creation", font, 2, 0xff9933, false, true);
        widget.child(6, 54012, 200, 20);
        addText(54013, "Regular", font, 0, 0xff9933, false, true);
        widget.child(7, 54013, 329, 75);

        /**Selection Type Icon**/
        addSprite(54014, 770);
        widget.child(8, 54014, 340, 107);

        /** Text **/
        addText(54015, "Set your difficulty level<br>  using the slider below.", font, 1, 0xff9933, false, true);
        widget.child(9, 54015, 79, 220);
        addText(54016, "100x", font, 0, 0x9FF00, false, true);
        widget.child(10, 54016, 115, 275);
        addText(54017, "50x", font, 0, 0x9FF00, false, true);
        widget.child(11, 54017, 115, 292);
        addText(54018, "Combat", font, 0, 0xff9933, false, true);
        widget.child(12, 54018, 145, 275);
        addText(54019, "Skilling", font, 0, 0xff9933, false, true);
        widget.child(13, 54019, 145, 292);
        addText(54020, "Welcome too Aelous<br> Choose your path!", font, 1, 0xff9933, false, true);
        widget.child(14, 54020, 91, 67);

        setBounds(54006, 85, 250, 15, widget);
        setBounds(54007, 117, 250, 16, widget);
        setBounds(54008, 149, 250, 17, widget);
        setBounds(54009, 181, 250, 18, widget);

        /**Bounds for slider bar**/
        widget.child(19, 54500, 213, 122);

        addHoverText(54021, "Continue", "Continue", font, 1, 0xff9933, false, true, 150, 11, 0xFFBD7D);
        widget.child(20, 54021, 400, 20);

        /**Scroll Bar Widget**/
        Widget scroll = addTabInterface(54500);
        scroll.totalChildren(1);

        scroll.child(0, 54501, 0, 0);

        /**Scroll Bounds**/
        scroll.scrollPosition = 0;
        scroll.contentType = 0;
        scroll.width = 5;
        scroll.height = 95;
        scroll.scrollMax = 150;
    }
}
