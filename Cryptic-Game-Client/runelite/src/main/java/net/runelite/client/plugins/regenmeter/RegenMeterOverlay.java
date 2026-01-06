/*
 * Copyright (c) 2018 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.regenmeter;

import net.runelite.api.Client;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.minimap.MinimapConfig;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.Arc2D;

class RegenMeterOverlay extends Overlay
{
	private static final Color HITPOINTS_COLOR = brighter(0x9B0703);
	private static final Color SPECIAL_COLOR = brighter(0x1E95B0);
	private static final Color OVERLAY_COLOR = new Color(255, 255, 255, 60);
	private static final double DIAMETER = 26D;
	private static final int OFFSET = 27;

	private final Client client;
	private final RegenMeterPlugin plugin;
	private final RegenMeterConfig config;

	private MinimapConfig minimapConfig;

	private static Color brighter(int color)
	{
		float[] hsv = new float[3];
		Color.RGBtoHSB(color >>> 16, (color >> 8) & 0xFF, color & 0xFF, hsv);
		return Color.getHSBColor(hsv[0], 1.f, 1.f);
	}

	@Inject
	private ConfigManager configManager;

	@Inject
	public RegenMeterOverlay(Client client, RegenMeterPlugin plugin, RegenMeterConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D g)
	{


		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		if (config.showHitpoints())
		{
			renderRegen(g, WidgetInfo.MINIMAP_HEALTH_ORB, plugin.getHitpointsPercentage(), HITPOINTS_COLOR);
		}

		if (config.showSpecial())
		{
			if (false)
			{
				final boolean fixed = !client.isResized();
				final int xOffset = fixed ? 516 + 33 : client.getCanvasWidth() - 211;
				final int yOffset = fixed ? 135 : 5;
				final Rectangle bounds = new Rectangle(xOffset, yOffset, 30, 30);
				g.setColor(RegenMeterOverlay.OVERLAY_COLOR);
				g.fillOval(
					bounds.x + OFFSET,
					bounds.y + (int) (bounds.height / 2 - (DIAMETER) / 2),
					(int) DIAMETER, (int) DIAMETER);
			}

			renderRegen(g, WidgetInfo.MINIMAP_SPEC_ORB, plugin.getSpecialPercentage(), SPECIAL_COLOR);
		}

		return null;
	}

	private void renderRegen(Graphics2D g, WidgetInfo widgetInfo, double percent, Color color)
	{

		final boolean fixed = !client.isResized();
		final int xOffset = fixed ? 515 : client.getCanvasWidth() - 218;
		final int yOffset = -1;
		Rectangle bounds = new Rectangle(xOffset + (widgetInfo == WidgetInfo.MINIMAP_HEALTH_ORB ? 1 : 33)
				, yOffset + (widgetInfo == WidgetInfo.MINIMAP_HEALTH_ORB ? 44 : 135), 30, 30);

		Arc2D.Double arc = new Arc2D.Double(bounds.x + OFFSET, bounds.y + (bounds.height / 2 - DIAMETER / 2), DIAMETER, DIAMETER, 90.d, -360.d * percent, Arc2D.OPEN);
		final Stroke STROKE = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
		g.setStroke(STROKE);
		g.setColor(color);
		g.draw(arc);
	}
}
