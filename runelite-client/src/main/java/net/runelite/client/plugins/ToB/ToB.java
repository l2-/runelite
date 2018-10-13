/*
 * Copyright (c) 2018. l2-
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
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

package net.runelite.client.plugins.ToB;

import com.google.common.eventbus.Subscribe;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Ignore;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "Theatre of BLood",
	description = "Theatre of blood assist",
	tags = {"tob", "theatre"}
)
public class ToB extends Plugin
{
	@Inject
	private Client client;

	private Widget widget = null;

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() != WidgetID.PERFORMERS_FOR_THE_THEATRE_GROUPS_GROUP_ID && event.getGroupId() != WidgetID.PERFORMERS_FOR_THE_THEATRE_PLAYERS_GROUP_ID)
		{
			return;
		}

		if (event.getGroupId() == WidgetID.PERFORMERS_FOR_THE_THEATRE_GROUPS_GROUP_ID)
		{
			widget = client.getWidget(WidgetID.PERFORMERS_FOR_THE_THEATRE_GROUPS_GROUP_ID, 0);
		}

		if (event.getGroupId() == WidgetID.PERFORMERS_FOR_THE_THEATRE_PLAYERS_GROUP_ID)
		{
			widget = client.getWidget(WidgetID.PERFORMERS_FOR_THE_THEATRE_PLAYERS_GROUP_ID, 0);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (widget == null)
		{
			return;
		}

		// recheck if the widget is still active
		int p_id = WidgetInfo.TO_GROUP(widget.getId());
		int c_id = WidgetInfo.TO_CHILD(widget.getId());

		List<Widget> widgetList = new LinkedList<>();

		if (p_id == WidgetID.PERFORMERS_FOR_THE_THEATRE_GROUPS_GROUP_ID)
		{
			Widget w = client.getWidget(p_id, 16);
			if (w == null)
			{
				return;
			}

			Widget[] ws = w.getStaticChildren();
			for (Widget widget : ws)
			{
				Widget[] widgets = widget.getDynamicChildren();
				if (widgets.length > 3)
				{
					widgetList.add(widgets[3]);
				}
			}

		}
		else if (p_id == WidgetID.PERFORMERS_FOR_THE_THEATRE_PLAYERS_GROUP_ID)
		{
			Widget w1 = client.getWidget(p_id, 26);

			if (w1 != null)
			{
				Widget[] dChildsAccepted = w1.getDynamicChildren();

				if (dChildsAccepted.length > 2)
				{
					for (int i = 1; i < dChildsAccepted.length; i += 11)
					{
						if (!dChildsAccepted[i].getText().equals("-"))
						{
							widgetList.add(dChildsAccepted[i]);
						}
					}
				}
			}

			Widget w2 = client.getWidget(p_id, 41);

			if (w2 != null)
			{
				Widget[] dChildsApplied = w2.getDynamicChildren();

				if (dChildsApplied.length > 2)
				{
					for (int i = 1; i < dChildsApplied.length; i+=11)
					{
						if (!dChildsApplied[i].getText().equals("-"))
						{
							widgetList.add(dChildsApplied[i]);
						}
					}
				}
			}
		}

		Ignore[] ignores = client.getIgnores();
		for (Widget w : widgetList)
		{
			String wtext = w.getText();
			if (client.isFriended(wtext, false))
			{
				w.setTextColor(Color.green.getRGB());
				continue;
			}
			for (int i = 0; i < client.getIgnoreCount(); i++)
			{
				String name = client.getIgnores()[i].getName();
				if (name.replace('\u00A0',' ').equals(wtext))
				{
					w.setTextColor(Color.red.getRGB());
					break;
				}
			}
		}

		widget = null;
	}
}
