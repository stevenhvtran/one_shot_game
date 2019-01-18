public class PhysicsSprite extends Sprite {
    private int leftVelocity, rightVelocity;
    private float verticalVelocity;
    private int terminalVelocity;
    private int maxHorizontalVelocity;
    private Directions direction;

    public PhysicsSprite(int x, int y, String imagePath, int maxHorizontalVelocity, int terminalVelocity) {
        super(x, y, imagePath);
        this.terminalVelocity = terminalVelocity;
        this.maxHorizontalVelocity = maxHorizontalVelocity;
        direction = Directions.LEFT;
    }

    public enum Directions {
        LEFT, RIGHT
    }

    public void move() {
        applyGravity();
        setX( getX() + getHorizontalVelocity() );
        setY( getY() + Math.round(verticalVelocity) );
        direction = getDirection();
    }

    public Directions getDirection() {
        if ( getHorizontalVelocity() < 0 ) {
            return Directions.LEFT;
        } else if ( getHorizontalVelocity() > 0 ) {
            return Directions.RIGHT;
        }
        return direction;
    }

    public void applyGravity() {
        if ( verticalVelocity < terminalVelocity) {
            verticalVelocity += 0.05;
        }
    }

    void setDirection(Directions direction) { this.direction = direction; }

    void setLeftVelocity(int leftVelocity) {
        this.leftVelocity = -leftVelocity;
    }

    void setRightVelocity(int rightVelocity) {
        this.rightVelocity = rightVelocity;
    }

    int getMaxHorizontalVelocity() {
        return maxHorizontalVelocity;
    }

    float getVerticalVelocity() {
        return verticalVelocity;
    }

    void setVerticalVelocity(float verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }

    int getHorizontalVelocity() {
        return leftVelocity + rightVelocity;
    }

    int getTerminalVelocity() {
        return terminalVelocity;
    }
}
