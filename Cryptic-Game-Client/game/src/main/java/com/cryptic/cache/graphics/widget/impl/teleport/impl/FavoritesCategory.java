package com.cryptic.cache.graphics.widget.impl.teleport.impl;

import com.cryptic.cache.graphics.widget.impl.teleport.Teleport;
import com.cryptic.cache.graphics.widget.impl.teleport.TeleportCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author https://github.com/ophionb
 * @author Ophion
 */
public class FavoritesCategory implements TeleportCategory {

    private static List<Teleport> teleports = new ArrayList<Teleport>(20);

    private boolean expanded = false;

    private int buttonId;

    @Override
    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }

    @Override
    public int getButtonId() {
        return buttonId;
    }

    @Override
    public String title() {
        return "Favourites";
    }

    @Override
    public boolean expanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public List<Teleport> getMembers() {
        return teleports;
    }

    public static void setMembers(List<Teleport> teleports) {
        FavoritesCategory.teleports = teleports;
    }
}
