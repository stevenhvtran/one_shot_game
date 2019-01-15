import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Sprite extends JComponent {
    private int x, y;
    private boolean visible;
    private int height, width;
    private Image image;

    public Sprite( int x, int y, String imagePath ) {
        this.x = x;
        this.y = y;
        visible = true;

        loadImage(imagePath);
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    private void loadImage( String imageName ) {
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(imageName));
        } catch (IOException e) {
            // TODO: Make this method throw the Exception and handle it further up
            System.out.println("Could not load image from: " + imageName);
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX( int x ) { this.x = x; }

    public void setY( int y ) { this.y = y; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Image getImage() { return image; }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
