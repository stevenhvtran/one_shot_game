import java.awt.geom.Line2D;

class Platform extends Sprite {
    Platform(int x, int y, String imagePath) {
        super(x, y, imagePath);
    }

    Line2D.Float getLeftBound() {
        return new Line2D.Float(getX(), getY() + 1, getX(), getY() + getHeight() - 1);
    }

    Line2D.Float getRightBound() {
        return new Line2D.Float(getX() + getWidth(), getY() + 1, getX() + getWidth(),
                getY() + getHeight() - 1);
    }

    Line2D.Float getTopBound() {
        return new Line2D.Float(getX() + 1, getY(),getX() + getWidth() - 1, getY());
    }

    Line2D.Float getBottomBound() {
        return new Line2D.Float(getX() + 1, getY() + getHeight(), getX() + getWidth() - 1,
                getY() + getHeight());
    }
}
