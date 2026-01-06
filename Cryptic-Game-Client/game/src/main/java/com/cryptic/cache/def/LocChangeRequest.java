package com.cryptic.cache.def;

import com.cryptic.entity.model.HashLink;
import net.runelite.api.MenuAction;

public final class LocChangeRequest extends HashLink {
    public static MenuAction tempMenuAction;
    public static int oculusOrbFocalPointY;
    public int new_shape;
    int filter_ops = 31;
    public int delay2 = -1;
    public int delay = 0;
    public int new_rotation;
    public int type;
    public int plane;
    public int y;
    public int objectId;
    public int rotation;
    public int shape;
    public int new_id;
    public int x;

    public LocChangeRequest() {
    }

    public void set_filter_op(int arg0) {
        this.filter_ops = arg0;
    }

    public boolean show_op(int arg0) {
        if (arg0 >= 0 && arg0 <= 4) {
            return (this.filter_ops & 1 << arg0) != 0;
        } else {
            return true;
        }
    }
}
