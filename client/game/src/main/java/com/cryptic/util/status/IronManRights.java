package com.cryptic.util.status;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum IronManRights {

    NONE(null),
    IRON_MAN(ChatCrown.IRON_MAN),

    HARDCORE_IRONMAN(ChatCrown.HARDCORE_IRONMAN),
    ULTIMATE_IRONMAN(ChatCrown.ULTIMATE_IRONMAN),
    GROUP_IRONMAN(ChatCrown.GROUP_IRONMAN)
    ;

    @Getter private final ChatCrown crown;

    IronManRights(ChatCrown crown) {
        this.crown = crown;
    }

    @Getter private static final Map<Integer, IronManRights> rights = new HashMap<>();
    static {
        for (IronManRights r : IronManRights.values()) {
            rights.put(r.ordinal(), r);
        }
    }
}
