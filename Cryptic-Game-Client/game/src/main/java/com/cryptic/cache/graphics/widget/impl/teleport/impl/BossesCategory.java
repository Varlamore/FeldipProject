package com.cryptic.cache.graphics.widget.impl.teleport.impl;

import com.cryptic.cache.graphics.widget.impl.teleport.Teleport;
import com.cryptic.cache.graphics.widget.impl.teleport.TeleportCategory;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author https://github.com/ophionb
 * @author Ophion
 */
public class BossesCategory implements TeleportCategory {
    private enum Entries {
        GOD_WARS(new Teleport("God Wars", 220)),
        RAIDS(new Teleport("Raids", 112));

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
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String title() {
        return "Bosses";
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
