package com.cryptic.engine.impl;

import com.cryptic.Client;
import com.cryptic.engine.keys.KeyEventProcessor;
import com.cryptic.engine.keys.KeyEventWrapper;
import com.cryptic.model.content.Keybinding;
import com.cryptic.util.ConfigUtility;
import net.runelite.rs.api.RSKeyHandler;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;

import static com.cryptic.engine.GameEngine.keyCodes;
import static com.cryptic.engine.impl.MouseHandler.shiftTeleport;


public class KeyHandler implements KeyListener, FocusListener, RSKeyHandler {

    public static boolean hasFocus;

    Collection<KeyEventWrapper> keyEventQueue;
    Collection<KeyEventWrapper> keyEventProcessingQueue;

    KeyEventProcessor[] keyEventProcessors;

    public boolean[] pressedKeys;

    public static volatile int idleCycles;

    public void setupComponent(Component component) {
        component.setFocusTraversalKeysEnabled(false);
        component.addKeyListener(this);
        component.addFocusListener(this);
    }

    synchronized void method362(Component component) {
        component.removeKeyListener(this);
        component.removeFocusListener(this);
        synchronized(this) {
            this.keyEventQueue.add(new KeyEventWrapper(4, 0));
        }
    }

    public KeyHandler() {
        this.keyEventProcessors = new KeyEventProcessor[3];
        this.pressedKeys = new boolean[112];
        this.idleCycles = 0;
        this.keyEventQueue = new ArrayList(100);
        this.keyEventProcessingQueue = new ArrayList(100);
    }


    public void processKeyEvents() {
        ++this.idleCycles;
        swapKeyEventQueues();
        for (KeyEventWrapper eventWrapper : keyEventProcessingQueue) {
            for (int i = 0; i < this.keyEventProcessors.length; i++) {
                if (eventWrapper.processWith(this.keyEventProcessors[i])) {
                    break;
                }
            }
        }
        this.keyEventProcessingQueue.clear();
    }

    synchronized void swapKeyEventQueues() {
        Collection<KeyEventWrapper> temp = this.keyEventProcessingQueue;
        this.keyEventProcessingQueue = this.keyEventQueue;
        this.keyEventQueue = temp;
    }

    public final synchronized void keyTyped(KeyEvent keyEvent) {
        Client.instance.getCallbacks().keyTyped(keyEvent);
        if (!keyEvent.isConsumed())
        {

            char var2 = keyEvent.getKeyChar();
            if (var2 != 0 && var2 != '\uffff' && method4577(var2)) {
                this.keyEventQueue.add(new KeyEventWrapper(3, var2));
            }

            keyEvent.consume();
        }
    }

    static int method2337(int var0) {
        return keyCodes[var0];
    }


    public final synchronized void keyReleased(KeyEvent keyEvent) {
        Client.instance.getCallbacks().keyReleased(keyEvent);
        if (!keyEvent.isConsumed())
        {
            int keyCode = keyEvent.getKeyCode();

            if (keyCode == KeyEvent.VK_SHIFT) {
                Client.isShiftPressed = false;
            }

            if (keyCode == KeyEvent.VK_CONTROL) {
                Client.isCtrlPressed = false;
                Client.instance.prayerGrabbed = null;
            }


            if (keyCode >= 0 && keyCode < keyCodes.length) {
                int keyHandlerKeyCode = keyCodes[keyCode];
                keyCode = keyHandlerKeyCode & -129;
            } else {
                keyCode = -1;
            }

            if (keyCode >= 0) {
                this.pressedKeys[keyCode] = false;
                this.keyEventQueue.add(new KeyEventWrapper(2, keyCode));
            }
            
            keyEvent.consume();
        }

    }

    public void assignProcessor(KeyEventProcessor processor, int index) {
        this.keyEventProcessors[index] = processor;
    }

    public final synchronized void focusGained(FocusEvent var1) {
        this.keyEventQueue.add(new KeyEventWrapper(4, 1));
    }

    public final synchronized void focusLost(FocusEvent var1) {
        Client.isShiftPressed = false;
        Client.isCtrlPressed = false;

        for (int var2 = 0; var2 < 112; ++var2) {
            if (this.pressedKeys[var2]) {
                this.pressedKeys[var2] = false;
                this.keyEventQueue.add(new KeyEventWrapper(2, var2));
            }
        }

        this.keyEventQueue.add(new KeyEventWrapper(4, 0));
    }

    public final synchronized void keyPressed(KeyEvent keyEvent) {
        Client.instance.getCallbacks().keyPressed(keyEvent);
        if (!keyEvent.isConsumed())
        {

            int keyCode = keyEvent.getKeyCode();

            if (Client.loggedIn) {
                Client.packetSender.sendKeystroke(keyCode);
            }

            if (Keybinding.isBound(keyCode)) {

                return;
            }

            if (shiftTeleport) {
                int newHeight = Client.instance.plane - Client.mouseWheelRotation;
                if (newHeight < 0) {
                    newHeight = 0;
                }
                if (newHeight > 3) {
                    newHeight = 3;
                }
                Client.teleport(Client.local_player.pathX[0] + Client.instance.next_region_start, Client.local_player.pathY[0] + Client.instance.next_region_end, newHeight);
                shiftTeleport = false;
            }

            if (keyCode == KeyEvent.VK_ESCAPE && Client.instance.settings[ConfigUtility.ESC_CLOSE_ID] == 1) {
                //Close any open interfaces.
                if (Client.loggedIn && Client.widget_overlay_id != -1) {
                    if(Client.widget_overlay_id == 48700) {
                        Client.tabInterfaceIDs[3] = 3213;
                    }
                    if (Client.widget_overlay_id == 16200) {
                        //Queue the task to run on the main Client thread to prevent a race condition.
                        //We use addSyncTask to send the packet from the main Client thread to prevent racing.
                        Client.packetSender.sendButtonClick(16202);

                    }
                    Client.instance.clearTopInterfaces();
                    return;
                }

                //Close the Client settings menu if it is open.
                if (Client.loggedIn && Client.tabInterfaceIDs[Client.sidebarId] == 50290) {
                    //Queue the task to run on the main Client thread to prevent a race condition.
                    //We use addSyncTask to send the packet from the main Client thread to prevent racing.
                    Client.packetSender.sendButtonClick(50293);
                    return;
                }
            }

            if (keyCode == KeyEvent.VK_SHIFT) {
                Client.isShiftPressed = true;
            }
            if (keyCode == KeyEvent.VK_CONTROL) {
                Client.isCtrlPressed = true;
            }
            if (keyCode == KeyEvent.VK_B && Client.isCtrlPressed) {
                Client.instance.packetSender.sendCommand("bank");
            }

            if (keyCode >= 0 && keyCode < keyCodes.length) {
                int keyHandlerKeyCode = keyCodes[keyCode];
                keyCode = keyHandlerKeyCode;
                boolean var4 = (keyHandlerKeyCode & 128) != 0;
                if (var4) {
                    keyCode = -1;
                }
            } else {
                keyCode = -1;
            }

            if (keyCode >= 0) {
                if (!this.pressedKeys[keyCode]) {
                    this.idleCycles = 0;
                }

                this.pressedKeys[keyCode] = true;
                this.keyEventQueue.add(new KeyEventWrapper(1, keyCode));
            }


            keyEvent.consume();
        }


    }

    public static final char[] cp1252AsciiExtension = new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'};


    public static boolean method4577(char var0) {
        if ((var0 <= 0 || var0 >= 128) && (var0 < 160 || var0 > 255)) {
            if (var0 != 0) {
                char[] var1 = cp1252AsciiExtension;

                for (int var2 = 0; var2 < var1.length; ++var2) {
                    char var3 = var1[var2];
                    if (var0 == var3) {
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }


}

