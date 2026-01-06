package com.cryptic.instruction;

/**
 * A client instruction is comprised of an underlying operation or series of operations that are to be processed by the
 * client. See {@link InstructionProcessor} for more info on processing. The instruction has the ability to set a return
 * type based on required usage however, if no return type is expected then it is worth using {@link VoidInstruction}.
 * @param <T> The return type expected for this instruction.
 */
public interface Instruction<T> {
    T invoke(InstructionArgs args);
}