package com.cryptic.cache.graphics.widget.impl.teleport;

import com.cryptic.cache.graphics.dropdown.Dropdown;
import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.ButtonAction;
import com.cryptic.cache.graphics.widget.Widget;
import com.cryptic.cache.graphics.widget.impl.teleport.impl.BossesCategory;
import com.cryptic.cache.graphics.widget.impl.teleport.impl.FavoritesCategory;
import com.cryptic.cache.graphics.widget.impl.teleport.impl.MonstersCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author https://github.com/ophionb
 * @author Ophion
 */
public class TeleportDashboardWidget extends Widget {

    /**
     * Represents all the available sidebar categories.
     */
    public static List<TeleportCategory> categories;

    static {
        categories = new ArrayList<>();
        categories.add(new FavoritesCategory());
        categories.add(new MonstersCategory());
        categories.add(new BossesCategory());
    }

    /**
     * INITIAL LOADING OF THE INTERFACE
     * @param font
     */
    public static void unpack(AdvancedFont[] font) {
        Widget frame = staticFrame(font);
        dynamicSideBar(frame, font);
    }

    public static void loadData(Widget frame, Teleport teleport) {
        if (teleport.getNpcId().isPresent()) {
//            frame.totalChildren(1);
//            frame.model_id = teleport.getNpcId().get();
//            addNPCWidget(79200);
//            frame.child(0, 79200, 20, 20);
        }


    }

    /**
     * Represents the static frame of the interface.
     * @param font
     */
        public static Widget staticFrame(AdvancedFont[] font) {
            Widget mode = addInterface(79000);
            mode.totalChildren(6);

            String[] options = {"Alphabetical A-Z", "Recently used"};
            //Background
            addSprite(79001, 1889);
            mode.child(0, 79001, 50, 20);
            //Close button
            closeButton(79002, 107, 108, false);
            mode.child(1, 79002, 446, 27);
            // Title
            addText(79003, "Teleport Dashboard", font, 2, 0xff9933, true, true);
            mode.child(2, 79003, 275, 29);

            // Drop down menu
            dropdownMenu(79004, 166, 0, options, Dropdown.PLAYER_ATTACK_OPTION_PRIORITY, font, 1);
            mode.child(3, 79004, 75, 60);
            addText(79005, "106M", font, 1, 0xfe971e, false, true);
            mode.child(4, 79005, 360, 70);

            /** Child for sub-categories */
            mode.child(5, 79100, 66, 100);

            /** child for selected data*/
          //  mode.child(6, 79200, 66, 100);
            return mode;
        }

    /**
     * Represents a sidebar loading a list of collapsable
     * @link{#TeleportCategory} objects, containing teleport members.
     * @param font
     */

    public static void dynamicSideBar(Widget parent, AdvancedFont[] font) {
        Widget sideBar = addTabInterface(79100);
        AtomicInteger totalItems = new AtomicInteger();
        AtomicInteger id = new AtomicInteger(0);
        int yPos = 0;
        Teleport selected = null;

        sideBar.width = 150;
        sideBar.height = 175;
        sideBar.scrollMax = sideBar.height + 1;
        categories.forEach(category -> {
            totalItems.addAndGet(1 + (category.expanded() ? category.getMembers().size() : 0));
        });
        sideBar.totalChildren(totalItems.get());
        for (TeleportCategory collapsible : categories) {
            //  Category collapsable name
            ButtonAction.addAction(79101 + id.get(), () -> {
                collapsible.setExpanded(!collapsible.expanded());
                dynamicSideBar(parent, font);
            });
            addClickableText(79101 + id.get(), collapsible.title(), "Select" + collapsible.title(), font, 0, 0xff9933, false, true, 150);
            sideBar.child(id.get(), 79101 + id.get(), 10, yPos);
            id.getAndIncrement();

            // Category items / sub-titles
            if (collapsible.expanded()){
                for (Teleport option : collapsible.getMembers()) {
                    yPos += 14;
                    addClickableText(79101 + id.get(), option.getName(), "Select" + option.getName(), font, 0, 0xff9933, false, true, 150);
                    sideBar.child(id.get(), 79101 + id.get(), 15, yPos);
                    textClicked(79101 + id.get(), 1137, 1, 2);
                    ButtonAction.addAction(79101 + id.get(), () -> {
                        loadData(parent, option);
                    });
                    id.getAndIncrement();
                }
            }
            yPos += 20;
        }
        sideBar.scrollMax = yPos;
    }
}
