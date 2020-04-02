package dev.raghav.flood.gfx;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {
	public static Font loadFont(String fontFileName, float size) {
		try {
			InputStream fontStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fontFileName);
			return Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
