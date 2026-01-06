package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.Client;
import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;

/**
 * @author Ynneh
 */
public class WildernessWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        boolean isToggled = Client.counterOn;
        /** The interfaceId **/
        Widget widget = addInterface(53720);
        /** Total Children to the Parent Interface **/
        widget.totalChildren(15);
        /** Background of the interface **/
        addTransparentSprite(53721, 1984, 50);
        widget.child(0, 53721, 317, isToggled ? 37 : 10);
        /** Target Hint **/
        addText(53722, "Target:", font, 0, 0xf0f320, true, true);
        widget.child(1, 53722, 446, isToggled ? 40 : 13);
        /** Target Name **/
        addText(53723, "None", font, 1, 0xffffff, true, true);
        widget.child(2, 53723, 446, isToggled ? 52  : 25);
        /** Target Location & Combat Level **/
        addText(53724, "Level: -----", font, 0, 0xf0f320, false, true);
        widget.child(3, 53724, 415, isToggled ? 68 : 41);
        /** Targets Wealth Risk **/
        addText(53725, "---", font, 0, 0xf0f320, false, true);//used for wealth
        widget.child(5, 53725, 323, isToggled ? 70 : 43);//under emblem
        /** Targets Wealth as a sprite **/
        addSprite(53726, 1985);
        widget.child(6, 53726, 355, isToggled ? 47 : 20);//used for risk skull
        /** The emblem of the target as a sprite **/
        addTransparentSprite(53727, 1983, 50);//TODO packet for transparacy
        widget.child(7, 53727, 315, isToggled ? 38 : 11);//level 1 emblem
        /** The emblem tier as a # **/
        addText(53728, "", font, 0, 0xf0f320, true, true);//used for emblem tier
        widget.child(8, 53728, 331, isToggled ? 57 : 30);//under emblem
        /** Skip target button **/
        //hoverButton(53729, "Skip Target", 1969, 1970);
        //widget.child(9, 53729, 483, isToggled ? 40 : 13);

        addHoverButton(53734, 1971, 25, 25, "Settings", -1, 53735, 1);
        widget.child(9, 53734, 483, isToggled ? 40 : 13);
        addHoveredButton(53735, 1972, 25, 25, 53729);
        widget.child(14, 53735, 483, isToggled ? 40 : 13);

        //Skull icon
        //Be careful with positioning the skull icon especially the y position,
        //we have hardcoded checks for position for this interface and
        //moving the skull icon breaks the checks
        /** Wilderness Skull Icon **/
        widget.child(4, 197, 0, 1);
        /** Wilderness Level **/
        addText(53730, "Level: 1", font, 1, 0xf0f320, true, true);//used for emblem tier
        widget.child(10, 53730, 485, 282);
        /** Statistics **/
        addText(53731, "Kills: X", font, 0, 0xf0f320, false, true);//used for wealth
        widget.child(11, 53731, 5, 68);
        addText(53732, "Deaths: X", font, 0, 0xf0f320, false, true);//used for wealth
        widget.child(12, 53732, 5, 78);
        addText(53733, "K/D Ratio: X", font, 0, 0xf0f320, false, true);//used for wealth
        widget.child(13, 53733, 5, 88);


    }
}