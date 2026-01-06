package com.cryptic.cache.graphics.dropdown;

import com.cryptic.Client;
import com.cryptic.cache.graphics.widget.Widget;
import com.cryptic.model.content.Keybinding;

public enum Dropdown {

    KEYBIND_SELECTION() {
        @Override
        public void selectOption(int selected, Widget dropdown) {
            Keybinding.bind((dropdown.id - Keybinding.MIN_FRAME) / 3, selected);
        }
    },

    TELEPORTINTERFACE_SELECTION() {
        @Override
        public void selectOption(int selected, Widget r) {
            switch (selected){
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
        }
    },

    PLAYER_ATTACK_OPTION_PRIORITY() {
        @Override
        public void selectOption(int selected, Widget r) {
            Client.instance.setting.player_attack_priority = selected;
        }
    },

    NPC_ATTACK_OPTION_PRIORITY() {
        @Override
        public void selectOption(int selected, Widget r) {
            Client.instance.setting.npc_attack_priority = selected;
        }
    };

    private Dropdown() {
    }

    public abstract void selectOption(int selected, Widget r);
}
