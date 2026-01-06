package com.cryptic.instruction.impl;

import com.cryptic.cache.graphics.widget.HealthHud;
import com.cryptic.cache.graphics.widget.Widget;
import com.cryptic.instruction.InstructionArgs;
import com.cryptic.instruction.VoidInstruction;

public class SetHealthHud implements VoidInstruction {

    @Override
    public Void invoke(InstructionArgs args) {
        int type = args.getNextInt();
        HealthHud.setHudType(HealthHud.HudType.values()[type]);
        Widget header = Widget.cache[HealthHud.HEADER_ID];
        header.defaultText = args.getNextString();
        return null;
    }
}