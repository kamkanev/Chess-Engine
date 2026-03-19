package org.example.toolbox;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Fonts {

    public static Font loadFont(String name, int style, float size) throws FontFormatException, IOException {
        Font baseFont = loadBaseFont(name);
        return baseFont.deriveFont(style, size);
    }

    public static Font loadFont(String name, float size) throws FontFormatException, IOException {
        Font baseFont = loadBaseFont(name);
        return baseFont.deriveFont(Font.PLAIN, size);
    }

    public static Font loadFont(String name) throws FontFormatException, IOException {
        Font baseFont = loadBaseFont(name);
        return baseFont.deriveFont(12f);
    }

    public static Font loadFont(String name, int style) throws FontFormatException, IOException {
        Font baseFont = loadBaseFont(name);
        return baseFont.deriveFont(style, 12f);
    }

    public static float getFontSizeByScreen(JFrame jf, int devider) {
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = jf.getWidth();
        double height = jf.getHeight();

        float size = (float) (width / devider);

        return size;
    }

    public static float getFontSizeByComponetSize(JComponent jc, int devider) {
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = jc.getWidth();
        double height = jc.getHeight();

        float size = (float) (width / devider);

        return size;
    }

    private static Font loadBaseFont(String name) throws FontFormatException, IOException {
        String resourceName = "fonts/" + name + ".ttf";
        try (InputStream input = Fonts.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IOException("Font resource not found: " + resourceName);
            }
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, input);
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(baseFont);
            return baseFont;
        }
    }

}
