package com.cryptic.cache.graphics.widget.option_menu;

/**
 * Represents a single Option Menu field/box that is used in the
 * {@link OptionMenuInterface}.
 *
 * @author Lennard
 */
public class OptionMenu {

    /**
     * The identifier of this Option Menu.
     */
    private final int identifier;

    /**
     * The name of this Option Menu.
     */
    private final String optionName;

    /**
     * The tooltip of this Option Menu. Will be displayed under the name.
     */
    private final String optionTooltip;

    /**
     * Determines if this Option Menu is highlighted. Used for button hovering.
     */
    private boolean isHighlighted;

    public OptionMenu(int identifier, String optionName, String optionTooltip) {
        this.identifier = identifier;
        this.optionName = optionName;
        this.optionTooltip = optionTooltip;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getOptionTooltip() {
        return optionTooltip;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

}
