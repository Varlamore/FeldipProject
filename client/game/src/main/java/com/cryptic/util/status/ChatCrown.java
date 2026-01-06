package com.cryptic.util.status;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum ChatCrown {

    SUPPORT("<img=14</img>", 2170),
    MODERATOR("<img=1</img>", 2171),
    ADMINISTRATOR("<img=2</img>", 2172),
    OWNER("<img=3</img>", 2175),
    DEVELOPER("<img=4</img>", 2174),
    IRON_MAN("<img=9</img>", 502),
    ULTIMATE_IRONMAN("<img=10</img>", 503),
    HARDCORE_IRONMAN("<img=11</img>", 504),
    GROUP_IRONMAN("<img=19</img>", 1770),
    //Members
    RUBY("<img=5</img>", 1427),
    SAPPHIRE("<img=6</img>", 500),
    EMERALD("<img=22</img>", 1077),
    DIAMOND("<img=7</img>", 1078),
    DRAGONSTONE("<img=24</img>", 1425),
    ONYX("<img=15</img>", 1426),
    ZENYTE("<img=23</img>", 1048);

    private final String identifier;
    private final int spriteId;

    ChatCrown(String identifier, int spriteId) {
        this.identifier = identifier;
        this.spriteId = spriteId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getSpriteId() {
        return spriteId;
    }

    private static final Set<ChatCrown> STAFF = EnumSet.of(SUPPORT, MODERATOR, ADMINISTRATOR, DEVELOPER, OWNER);

    public boolean isStaff() {
        return STAFF.contains(this);
    }

    public static List<ChatCrown> get(int rights, int donatorRights, int ironmanStatus) {
        List<ChatCrown> crowns = new ArrayList<>();

        PlayerRights playerRights = PlayerRights.get(rights);
        if (playerRights != PlayerRights.PLAYER
                && playerRights.getCrown() != null) {
            crowns.add(playerRights.getCrown());
        }

        MemberRights donorRights = MemberRights.get(donatorRights);
        if (donorRights != MemberRights.NONE
                && donorRights.getCrown() != null) {
            crowns.add(donorRights.getCrown());
        }

        IronManRights ironManRights = IronManRights.getRights().get(ironmanStatus);
        if (ironManRights != IronManRights.NONE
                && ironManRights.getCrown() != null) {
            crowns.add(ironManRights.getCrown());
        }

        return crowns;
    }

    @Override
    public String toString() {
        return "ChatCrown{" +
                "identifier='" + identifier + '\'' +
                ", spriteId=" + spriteId +
                '}';
    }
}
