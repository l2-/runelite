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

package net.runelite.client.ui;

import java.awt.Font;

public enum Fonts
{
	PLAIN(FontManager.getDialogPlain()),
	TAHOMA_BOLD(FontManager.getTahomaBold()),
	LUCIDA_SANS_DEMIBOLD_ROMAN(FontManager.getLucidaSansDemiboldRoman()),
	ARIAL_BOLD(FontManager.getArialBold()),
	RUNESCAPE_SMALL(FontManager.getRunescapeSmallFont()),
	RUNESCAPE_DEFAULT(FontManager.getRunescapeFont()),
	RUNESCAPE_BOLD(FontManager.getRunescapeBoldFont()),
	TRECHBUCHET_MS_BOLD(FontManager.getTrebuchetMsBold()),
	LATO_BOLD(FontManager.getLatoBold());

	private final Font font;

	public Font getFont()
	{
		return font;
	}

	public Font getFont(int size)
	{
		return font.deriveFont((float)size);
	}

	Fonts(final Font font)
	{
		this.font = font;
	}
}