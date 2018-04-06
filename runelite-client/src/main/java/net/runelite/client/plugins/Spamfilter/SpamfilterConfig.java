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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(
	keyName = "spamfilter",
	name = "Spam Filter",
	description = "Configuration for spam filter"
)
public interface SpamfilterConfig extends Config
{
	@ConfigItem(
		keyName = "blacklistedWords",
		name = "Blacklisted words",
		description = "Configures specifically blacklisted words. Format: (item), (item)",
		position = 1
	)
	default String getBlacklistedWords()
	{
		return "";
	}

	@ConfigItem(
		keyName = "blacklistedWords",
		name = "",
		description = ""
	)
	void setBlacklistedWords(String key);

	@ConfigItem(
		keyName = "replaceSpam",
		name = "Replace the spam",
		description = "Replace the spam message with one a different one before deleting",
		position = 2
	)
	default boolean isReplaceSpam()
	{
		return true;
	}

	@ConfigItem(
		keyName = "replacementMessage",
		name = "Replacement Message",
		description = "Message to replace the spam message before it is cleared",
		position = 3
	)
	default String replacementMessage()
	{
		return "<Message deleted>";
	}
	
	@ConfigItem(
		keyName = "filterPublic",
		name = "Filter public chat",
		description = "",
		position = 4
	)
	default boolean isFilterPublic()
	{
		return true;
	}

	@ConfigItem(
		keyName = "filterPrivate",
		name = "Filter Private chat",
		description = "",
		position = 5
	)
	default boolean isFilterPrivate()
	{
		return true;
	}

	@ConfigItem(
		keyName = "filterClanchat",
		name = "Filter Clanchat chat",
		description = "",
		position = 6
	)
	default boolean isFilterClanchat()
	{
		return true;
	}

	@ConfigItem(
		keyName = "clearSpam",
		name = "Clear spam",
		description = "Clear the spam message after a delay",
		position = 7
	)
	default boolean isClearSpam()
	{
		return true;
	}

	@ConfigItem(
		keyName = "clearSpamDelay",
		name = "Clear delay",
		description = "Delay between changing and clear the spam message",
		position = 8
	)
	default int clearSpamDelay()
	{
		return 3;
	}
}
