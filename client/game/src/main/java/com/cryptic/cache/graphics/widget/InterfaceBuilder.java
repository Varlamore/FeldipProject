package com.cryptic.cache.graphics.widget;

/**
 * @author Sharky
 * @Since May 23, 2023
 */
public abstract class InterfaceBuilder extends Widget {

    private final int baseInterfaceId;
    private final Widget root;
    private int nextChildId;
    private int nextInterfaceId;

    public abstract void build();

    public InterfaceBuilder(int baseInterfaceId) {
        this.baseInterfaceId = baseInterfaceId;
        root = Widget.addInterface(baseInterfaceId);
        root.totalChildren(0);
        init();
    }

    public void init() {
        nextChildId = 0;
        nextInterfaceId = baseInterfaceId + 1;
    }

    public void child(int x, int y) {
        if (Widget.PRINT_INFO) {
            var toSet = Widget.cache[lastInterface()];
            if (toSet != null)
                toSet.print();
        }
        root.child(nextChild(), lastInterface(), x, y);
    }

    public void child(int interfaceId, int x, int y) {
        root.child(nextChild(), interfaceId, x, y);
    }

    public int getBaseInterfaceId() {
        return baseInterfaceId;
    }

    public int nextChild() {
        Widget.expandChildren(1, root);
        return nextChildId++;
    }

    public int lastInterface() {
        return nextInterfaceId - 1;
    }

    public int nextInterface() {
        return nextInterfaceId++;
    }

    public int getCurrentInterfaceId() {
        return nextInterfaceId;
    }

    public void setNextInterfaceId(int nextInterfaceId) {
        this.nextInterfaceId = nextInterfaceId;
    }

    public Widget getLastChild() {
        return Widget.get(nextInterfaceId - 1);
    }

    public Widget getRoot() {
        return root;
    }
}
