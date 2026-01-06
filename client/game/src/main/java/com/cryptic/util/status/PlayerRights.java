package com.cryptic.util.status;

import java.util.HashMap;
import java.util.Map;

public enum PlayerRights {
    PLAYER(null),
    SUPPORT(ChatCrown.SUPPORT),
    MODERATOR(ChatCrown.MODERATOR),
    ADMINISTRATOR(ChatCrown.ADMINISTRATOR),
    DEVELOPER(ChatCrown.DEVELOPER),
    OWNER(ChatCrown.OWNER);

    private final ChatCrown crown;

    PlayerRights(ChatCrown crown) {
        this.crown = crown;
    }

    public ChatCrown getCrown() {
        return crown;
    }
    private static final Map<Integer, PlayerRights> rights = new HashMap<>();
    static {
        for (PlayerRights r : PlayerRights.values()) {
            rights.put(r.ordinal(), r);
        }
    }

    public static PlayerRights get(int ordinal) {
        return rights.getOrDefault(ordinal, PlayerRights.PLAYER);
    }
}
