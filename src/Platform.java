import java.awt.geom.Line2D;

public class Platform extends Sprite {
    public Platform(int x, int y, String imagePath) {
        super(x, y, imagePath);
    }

    public Line2D.Float getLeftBound() {
        return new Line2D.Float(getX(), getY(), getX(), getY() + getHeight());
    }

    public Line2D.Float getRightBound() {
        return new Line2D.Float(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight());
    }

    public Line2D.Float getTopBound() {
        return new Line2D.Float(getX(), getY(), getX() + getWidth(), getY());
    }

    public Line2D.Float getBottomBound() {
        return new Line2D.Float(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight());
    }
}
