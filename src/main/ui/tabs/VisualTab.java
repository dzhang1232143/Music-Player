package ui.tabs;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ui.PlayerUI;

// A visual Tab that shows a picture
public class VisualTab extends Tab {

    private Image image;

    private static final String IMAGE_PATH = "src\\main\\ui\\img.jpg";

    // EFFECTS: creates a visual Tab 
    public VisualTab(PlayerUI controller) {
        super(controller);
        try {
            image = ImageIO.read(new File(IMAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: include the image as part of its being drawn
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
