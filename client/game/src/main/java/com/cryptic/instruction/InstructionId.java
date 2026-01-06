package com.cryptic.instruction;

import com.cryptic.instruction.impl.SetHealthHud;
import com.cryptic.instruction.impl.SetVarp;

/**
 * A list of instruction identifiers mapped to their respective instruction.
 * @author Heaven
 */
@SuppressWarnings("unchecked")
public enum InstructionId {
    NOTHING(-1),
    SET_VARP(8) {
        @Override
        public VoidInstruction getInstruction() {
            return new SetVarp();
        }
    },
    HEALTH_HUD(13) {
        @Override
        public VoidInstruction getInstruction() { return new SetHealthHud(); }
    },

    ; // End of enum

    public static final InstructionId[] VALUES = values();

    /**
     * The identifier value in the form of a int literal.
     */
    public final int uid;

    /**
     * A fallback instruction if no other is specified.
     */
    public <T> Instruction<T> getInstruction() {
        return null;
    }

    /**
     * A shorthand method to invoking an instruction without
     * @param args
     */
    public void invoke(InstructionArgs args) {
        getInstruction().invoke(args);
    }

    InstructionId(int uid) {
        this.uid = uid;
    }

    public static InstructionId fromId(int id) {
        for (InstructionId insId : VALUES) {
            if (insId.uid == id)
                return insId;
        }
        return NOTHING;
    }
}