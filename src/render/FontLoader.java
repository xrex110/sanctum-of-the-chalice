package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

public class FontLoader {
	private static final String DIR_PATH = "../res/fonts";
	private static final String FONT_PATH = "../res/fonts/";

	public void loadFont(String fontName) {
		try {
			File fontFile = new File(FONT_PATH + fontName + ".ttf");	//Always use ttfs here
			Font newFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(newFont);

			//Can be used to inspect the font name for a newly loaded font
			/* String[] fontNames = ge.getAvailableFontFamilyNames();
			for(String s : fontNames) {
				System.out.println(s);
			}*/
		} catch(IOException e) {
			System.out.println("Fond load fail D:");
			e.printStackTrace();
		} catch(FontFormatException e) {
			System.out.println("Fond load fail D:");
			e.printStackTrace();
		}
	}

	//TODO: Finish writing this function to load and register all fonts 
	//within DIR_PATH
	
	/*public void loadAllFonts() {
		try {
			File fontDir = new File(DIR_PATH);
			File[] files = fontDir.listFiles();
			for(File f : files) System.out.println(f.getName());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}*/
}
