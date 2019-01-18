import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Sprite extends JComponent {
    private int x, y;
    private int height, width;
    private Image image;

    Sprite( int x, int y, String imagePath ) {
        this.x = x;
        this.y = y;

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

    void setX( int x ) { this.x = x; }

    void setY( int y ) { this.y = y; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    Image getImage() { return image; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
