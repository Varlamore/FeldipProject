package com.cryptic.cache.graphics.widget.option_menu;

import com.cryptic.cache.graphics.SimpleImage;
import com.cryptic.cache.graphics.widget.Widget;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Widget type that draws a custom option menu interface. (Examples:
 * Teleportation + favorite interfaces, achievement interface)
 *
 * @author Lennard
 */
public class OptionMenuInterface extends Widget {

    /**
     * The maximum amount of options that this Option Menu Interface can hold.
     */
    private final int maxOptions;

    /**
     * A {@link Collection} holding all the {@link OptionMenu}s for this Option
     * Menu Interface.
     */
    private Collection<OptionMenu> optionMenus = new ArrayList<OptionMenu>();

    /**
     * Additional {@link SimpleImage}s that are loaded into the cache and can be
     * displayed on the interface.
     */
    private final SimpleImage[] sprites;

    public OptionMenuInterface(int identifier, int width, int height, int maxOptions, SimpleImage[] sprites, String backgroundMessage) {
        super(identifier, width, height, 29, 29);
        this.maxOptions = maxOptions;
        this.sprites = sprites;
        defaultText = backgroundMessage;
        tooltips = new String[] { "Teleport" };
    }

    public int getMaxOptions() {
        return maxOptions;
    }

    public Collection<OptionMenu> getOptionMenus() {
        return optionMenus;
    }

    public void setOptionMenus(Collection<OptionMenu> optionMenus) {
        this.optionMenus = optionMenus;
    }

    public SimpleImage[] getSprites() {
        return sprites;
    }

}
