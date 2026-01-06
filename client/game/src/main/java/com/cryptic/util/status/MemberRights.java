package com.cryptic.util.status;

import java.util.HashMap;
import java.util.Map;

public enum MemberRights {

    NONE(null),
    MEMBER(ChatCrown.RUBY),
    SUPER_MEMBER(ChatCrown.SAPPHIRE),
    ELITE_MEMBER(ChatCrown.EMERALD),
    EXTREME_MEMBER(ChatCrown.DIAMOND),
    LEGENDARY_MEMBER(ChatCrown.DRAGONSTONE),
    VIP(ChatCrown.ONYX),
    SPONSOR_MEMBER(ChatCrown.ZENYTE),
    ;

    private final ChatCrown crown;

    MemberRights(ChatCrown crown) {
        this.crown = crown;
    }

    public ChatCrown getCrown() {
        return crown;
    }

    private static final Map<Integer, MemberRights> rights = new HashMap<>();
    static {
        for (MemberRights r : MemberRights.values()) {
            rights.put(r.ordinal(), r);
        }
    }

    public static MemberRights get(int ordinal) {
        return rights.getOrDefault(ordinal, MemberRights.NONE);
    }
}
