package com.cryptic.instruction;

import java.util.Arrays;

/**
 * Represents a set of arguments that are to be passed off to an {@link VoidInstruction} for processing.
 * @author Heaven
 */
public class InstructionArgs {

    /**
     * The int stack to push and pop values off as needed.
     */
    private int[] intStack;

    /**
     * The string stack to push and pop values off as needed.
     */
    private String[] stringStack;

    /**
     * The position we are at within the int stack.
     */
    private int intStackPos;

    /**
     * The position we are at within the string stack.
     */
    private int stringStackPos;

    /**
     * Creates an empty set of instruction arguments.
     * @return
     */
    public static InstructionArgs createStack() {
        return new InstructionArgs(new int[0], new String[0]);
    }

    /**
     * Creates a new set of arguments consisting of the elements within the array passed through the constructor.
     * @param intArgs
     * @param stringArgs
     * @return
     */
    public static InstructionArgs createFrom(int[] intArgs, String[] stringArgs) {
        return new InstructionArgs(intArgs, stringArgs);
    }

    /**
     * Creates a new set of instructions consisting of the underlying stack elements of the specified argument.
     * @param from
     * @return
     */
    public static InstructionArgs createFrom(InstructionArgs from) {
        int[] is = Arrays.copyOf(from.intStack, from.intStack.length);
        String[] ss = Arrays.copyOf(from.stringStack, from.stringStack.length);
        return createFrom(is, ss);
    }

    /**
     * Private construction to avoid any undesirable instantiation.
     * @param intArgs
     * @param stringArgs
     */
    private InstructionArgs(int[] intArgs, String[] stringArgs) {
        this.intStack = intArgs;
        this.stringStack = stringArgs;
        this.intStackPos = intStack.length;
        this.stringStackPos = stringStack.length;
    }

    /**
     * Gets the next int-type argument in the array.
     * @return
     */
    public int getNextInt() {
        return intStack[--intStackPos];
    }

    /**
     * Adds a boolean value onto the stack by evaluating the input and setting a 0-1 logical value respectively.
     * @param bool
     */
    public void addNextBool(boolean bool) {
        addNextInt(bool ? 1 : 0);
    }

    /**
     * Adds an int value onto the stack to be returned on the next calling of {@link #getNextInt()}.
     */
    public InstructionArgs addNextInt(int value) {
        if (intStackPos >= intStack.length) {
            int oldSize = intStack.length;
            int[] newStack = new int[oldSize + 1];
            System.arraycopy(intStack, 0, newStack, 0, oldSize);
            intStack = null;
            intStack = newStack;
        }
        intStack[intStackPos++] = value;
        return this;
    }

    /**
     * Gets the next string-type argument in the array.
     * @return
     */
    public String getNextString() {
        return stringStack[--stringStackPos];
    }

    /**
     * Adds an string value onto the stack to be returned on the next calling of {@link #getNextString()}.
     */
    public InstructionArgs addNextString(String value) {
        if (stringStackPos >= stringStack.length) {
            int oldSize = stringStack.length;
            String[] newStack = new String[oldSize + 1];
            System.arraycopy(stringStack, 0, newStack, 0, oldSize);
            stringStack = null;
            stringStack = newStack;
        }
        stringStack[stringStackPos++] = value;
        return this;
    }

    public int getIntArg(int position) {
        return intStack[position];
    }

    public String getStringArg(int position) {
        return stringStack[this.stringStackPos - position];
    }

    public InstructionArgs intStackOffset(int offset) {
        this.intStackPos -= offset;
        return this;
    }

    public InstructionArgs stringStackOffset(int offset) {
        this.stringStackPos -= offset;
        return this;
    }
}
