package com.cryptic.instruction.impl;

import com.cryptic.Client;
import com.cryptic.instruction.InstructionArgs;
import com.cryptic.instruction.VoidInstruction;

public class SetVarp implements VoidInstruction {

    @Override
    public Void invoke(InstructionArgs args) {
        int idx = args.getNextInt();
        Client.instance.settings[idx] = args.getNextInt();
        return null;
    }
}
