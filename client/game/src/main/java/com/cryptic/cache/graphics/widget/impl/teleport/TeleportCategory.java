package com.cryptic.cache.graphics.widget.impl.teleport;

import java.util.List;

/**
 * @Author https://github.com/ophionb
 * @author Ophion
 */
public interface TeleportCategory {

    /**
     * Represents the category of the teleport.
     */
    String title();

    /**
     * @return Represents the state the list(Expanded = true, Collapsed = false).
     */
    boolean expanded();

    void setExpanded(boolean expanded);

    /**
     * Represents the teleport's favourites.
     */
    List<Teleport> getMembers();

    void setButtonId(int buttonId);

    int getButtonId();
}
