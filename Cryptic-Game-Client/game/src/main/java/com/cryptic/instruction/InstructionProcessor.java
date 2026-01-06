package com.cryptic.instruction;

/**
 * A class that serves to process any valid instruction that is passed along with the specified arguments.
 * @author Heaven
 */
public class InstructionProcessor {

    /**
     * Attempts to invoke the instruction from the id value and arguments passed through.
     * @param instructionId the int-literal value of the identifier. This value must match one of the identifier values
     *                      within the {@link InstructionId} list.
     * @param args The arguments to be passed along to the respective instruction, if any.
     */
    public static void invoke(int instructionId, InstructionArgs args) {
        try {
            InstructionId identifier = InstructionId.fromId(instructionId);
            if (identifier == InstructionId.NOTHING) {
                System.err.println("Could not invoke missing instruction with id "+ instructionId +".");
                return;
            }

            Instruction<Object> instruction = identifier.getInstruction();
            if (instruction == null) {
                System.err.println("No valid instruction mapped to id "+ identifier +".");
                return;
            }

            instruction.invoke(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
