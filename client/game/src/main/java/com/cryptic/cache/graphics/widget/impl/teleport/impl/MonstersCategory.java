package com.cryptic.cache.graphics.widget.impl.teleport.impl;

import com.cryptic.cache.graphics.widget.impl.teleport.Teleport;
import com.cryptic.cache.graphics.widget.impl.teleport.TeleportCategory;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author https://github.com/ophionb
 * @author Ophion
 */
public class MonstersCategory implements TeleportCategory {

    private boolean expanded = false;
    private int buttonId;

    private enum Entries {
        YAKS(new Teleport("Yaks", 220)),
        ROCK_CRABS(new Teleport("Rock Crabs", 112));

        private final Teleport teleport;

        Entries(Teleport teleport) {
            this.teleport = teleport;
        }

        private static final List<Teleport> TELEPORTS = new ArrayList<Teleport>();

        static
        {
            for (final Entries member : Entries.values()) getTELEPORTS().add(member.teleport);
        }

        private static List<Teleport> getTELEPORTS() {
            return TELEPORTS;
        }

    }

    @Override
    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }

    @Override
    public int getButtonId() {
        return buttonId;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String title() {
        return "Monsters";
    }

    @Override
    public boolean expanded() {
        return expanded;
    }

    @Override
    public List<Teleport> getMembers() {
        return Entries.getTELEPORTS();
    }
}
