package com.cryptic.engine.impl;

import com.cryptic.Client;

import com.cryptic.cache.graphics.widget.Widget;
import com.cryptic.cache.graphics.widget.impl.OptionTabWidget;
import com.cryptic.util.ConfigUtility;
import net.runelite.rs.api.RSMouseWheelHandler;


import java.awt.*;
import java.awt.event.*;

import static com.cryptic.engine.impl.MouseHandler.mouseX;
import static com.cryptic.engine.impl.MouseHandler.mouseY;

public class MouseWheelHandler implements MouseWheel,MouseWheelListener, RSMouseWheelHandler {

    int mouseWheelRotation;
    public static boolean mouseWheelDown;
    public static int mouseWheelX;
    public static int mouseWheelY;


    public MouseWheelHandler() {
        mouseWheelRotation = 0;
    }

    public synchronized int useRotation() {
        int rotation = this.mouseWheelRotation;
        this.mouseWheelRotation = 0;
        return rotation;
    }

    public void addTo(Component component) {
        component.addMouseWheelListener(this);
    }

    public void removeFrom(Component component) {
        component.removeMouseWheelListener(this);
    }

    public boolean canZoom = true;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.mouseWheelRotation += e.getWheelRotation();
        MouseWheelEvent event = Client.instance.getCallbacks().mouseWheelMoved(e);
        if (!event.isConsumed()) {
            int rotation = event.getWheelRotation();
            handleInterfaceScrolling(event);
            if (mouseX > 0 && mouseX < 512 && mouseY > Client.canvasHeight - 165 && mouseY < Client.canvasHeight - 25) {
                int scrollPos = Client.chatScrollAmount;
                scrollPos -= rotation * 30;
                if (scrollPos < 0)
                    scrollPos = 0;
                if (scrollPos > Client.chatScrollHeight - 110)
                    scrollPos = Client.chatScrollHeight - 110;
                if (Client.chatScrollAmount != scrollPos) {
                    Client.chatScrollAmount = scrollPos;
                    Client.update_chat_producer = true;
                }
            } else if (Client.loggedIn) {
                // Admin shift scrollwheel height changing. Do not send packets from the client to the server here, send packets from the main do while loop inside run method instead.
                if ((Client.instance.getMyPrivilege() == 5) && Client.isShiftPressed) {
                    this.mouseWheelRotation = rotation;
                    MouseHandler.shiftTeleport = true;
                } else {
                    MouseHandler.shiftTeleport = false;
                }

                /** ZOOMING **/
                boolean zoom = !Client.instance.isResized() ? (mouseX < 512) : (mouseX < Client.canvasWidth - 200);
                if (zoom && Client.widget_overlay_id == -1 && Client.instance.settings[ConfigUtility.ZOOM_TOGGLE_ID] == 0) {
                    int zoomValue = Client.field625;

                    if (event.getWheelRotation() != -1) {
                        zoomValue -= 45;
                    } else {
                        zoomValue += 45;
                    }

                    Client.setZoom(zoomValue);

                    //Widget.cache[OptionTabWidget.ZOOM_SLIDER].slider.setValue(Client.zoom_distance);
                    Client.instance.setting.save();
                }
                Client.update_chat_producer = true;
            }
        }
    }

    public void handleInterfaceScrolling(MouseWheelEvent event) {
        int rotation = event.getWheelRotation();
        int tabInterfaceId = Client.tabInterfaceIDs[Client.instance.sidebarId];
        if (tabInterfaceId != -1) {
            handleScrolling(rotation, tabInterfaceId, !Client.instance.isResized() ? Client.instance.getViewportWidth() - 218
                    : (!Client.instance.isResized() ? 28
                    : Client.instance.getViewportWidth() - 197), !Client.instance.isResized() ? Client.instance.getViewportHeight() - 298
                    : (!Client.instance.isResized() ? 37
                    : Client.instance.getViewportHeight()
                    - (Client.instance.getViewportWidth() >= 1000 ? 37 : 74) - 267));
        }
        if (Client.instance.widget_overlay_id != -1) {
            handleScrolling(rotation, Client.instance.widget_overlay_id, !Client.instance.isResized() ? 4
                    : (Client.instance.getViewportWidth() / 2) - 356, !Client.instance.isResized() ? 4
                    : (Client.instance.getViewportHeight() / 2) - 230);
        }
    }

    private void handleScrolling(int rotation, int interfaceId, int offsetX, int offsetY) {
        try {
            Widget widget = Widget.cache[interfaceId];
            if(widget == null)
                return;

            for (int index = 0; index < widget.children.length; index++) {
                Widget child = Widget.cache[widget.children[index]];
                if (child != null && child.scrollMax > child.height) {
                    int positionX = widget.child_x[index] + child.x;
                    int positionY = widget.child_y[index] + child.y;
                    int width = child.width;
                    int height = child.height;
                    if (MouseHandler.mouseX >= offsetX + positionX && MouseHandler.mouseY >= offsetY + positionY
                            && MouseHandler.mouseX < offsetX + positionX + width
                            && MouseHandler.mouseY < offsetY + positionY + height) {
                        int newRotation = rotation * 30;
                        if (newRotation > child.scrollMax - child.height - child.scrollPosition) {
                            newRotation = child.scrollMax - child.height - child.scrollPosition;
                        } else if (newRotation < -child.scrollPosition) {
                            newRotation = -child.scrollPosition;
                        }
                        if (Client.instance.getActiveInterfaceType() != 0) {
                            Client.instance.setMouseDragY(Client.instance.getMouseDragY() - newRotation);
                        }
                        child.scrollPosition += newRotation;
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
