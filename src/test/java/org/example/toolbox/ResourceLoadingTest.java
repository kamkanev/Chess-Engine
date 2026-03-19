package org.example.toolbox;

import org.junit.jupiter.api.Test;

import java.awt.Font;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceLoadingTest {

    @Test
    void loadsFontFromResources() throws Exception {
        Font font = Fonts.loadFont("Algerian", Font.PLAIN, 12f);
        assertNotNull(font);
    }

    @Test
    void loadsImageFromResources() throws Exception {
        assertNotNull(Images.loadImageIcon("backgrounds/back1.png"));
    }

    @Test
    void missingImageThrowsIOException() {
        assertThrows(IOException.class, () -> Images.loadImageIcon("backgrounds/does-not-exist.png"));
    }
}
