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

package net.runelite.client.plugins.Spamfilter;

import com.google.common.base.Splitter;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Provides;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.SetMessage;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;

@PluginDescriptor(
	name = "Spam Filter"
)
@Slf4j
public class Spamfilter extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private SpamfilterConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ScheduledExecutorService executor;

	@Provides
	SpamfilterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SpamfilterConfig.class);
	}

	private List<String> blacklistedWords;
	private ArrayList<SpamfilterMessage> detectedMessages;
	private String replacementMessage;

	@Override
	protected void startUp()
	{
		reset();
		detectedMessages = new ArrayList<>();
	}

	private void reset()
	{
		Splitter COMMA_SPLITTER = Splitter.on(Pattern.compile("\\s*,\\s*"));

		// gets the blacklisted words from the text box in the config
		blacklistedWords = COMMA_SPLITTER.splitToList(config.getBlacklistedWords().trim());

		replacementMessage = config.replacementMessage();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("spamfilter"))
		{
			reset();
		}
	}

	@Schedule(
		period = 40,
		unit = ChronoUnit.MILLIS
	)
	public void checkDetectedMessages()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}
		if (config.isClearSpam() && detectedMessages.size() > 0)
		{
			for (int i = 0; i < detectedMessages.size(); i++)
			{
				SpamfilterMessage spamfilterMessage = detectedMessages.get(i);
				if (System.nanoTime() >= spamfilterMessage.getNanoTimeSinceDetection() + ((long)config.clearSpamDelay() * 1000000000))
				{
					purgeMessage(spamfilterMessage);
				}
			}
			client.refreshChat();
		}
	}

	@Subscribe
	public void onSetMessage(SetMessage setMessage)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		switch (setMessage.getType())
		{
			case PUBLIC:
				if (!config.isFilterPublic())
				{
					return;
				}
			case CLANCHAT:
				if (!config.isFilterClanchat())
				{
					return;
				}
			case PRIVATE_MESSAGE_RECEIVED:
				if (!config.isFilterPrivate())
				{
					return;
				}
				break;
			default:
				return;
		}
		executor.submit(() -> ScanMessage(setMessage.getMessageNode(), blacklistedWords));
	}

	private void ScanMessage(MessageNode messageNode, List<String> blacklistedWords)
	{
		if (blacklistedWords.size() > 0 && blacklistedWords.parallelStream().filter(s -> s.length() > 0).anyMatch(messageNode.getValue().toLowerCase()::contains))
		{
			if (config.isClearSpam())
			{
				detectedMessages.add(new SpamfilterMessage(messageNode, System.nanoTime()));
			}
			if (config.isReplaceSpam() && replacementMessage != null)
			{
				messageNode.setRuneLiteFormatMessage(replacementMessage);
				chatMessageManager.update(messageNode);
				client.refreshChat();
			}
		}
	}

	private void purgeMessage(SpamfilterMessage spamfilterMessage)
	{
		chatMessageManager.remove(spamfilterMessage.getMessageNode());
		detectedMessages.remove(spamfilterMessage);
	}
}
