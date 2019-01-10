import java.util.ArrayList;

public class Player extends PhysicsSprite {
    private String missileImagePath;
    private ArrayList<Missile> missiles = new ArrayList<>();
    private boolean onFloor = false;

    public Player(int x, int y, String imagePath, String missileImagePath) {
        super(x, y, imagePath, 3, 5);
        this.missileImagePath = missileImagePath;
    }

    public enum Keys {
        LEFT, RIGHT, JUMP, SHOOT
    }

    public void setOnFloor() {
        setVerticalVelocity(0);
        setY(getY() - 1);
        onFloor = true;
    }

    public void handleKeyPress(Keys key) {
        switch(key) {
            case LEFT:
                setLeftVelocity(getMaxHorizontalVelocity());
                break;
            case RIGHT:
                setRightVelocity(getMaxHorizontalVelocity());
                break;
            case JUMP:
                if (onFloor) {
                    setVerticalVelocity(getVerticalVelocity() - 5);
                }
                onFloor = false;
                break;
            case SHOOT:
                shootMissile();
                break;
        }
    }

    private void shootMissile() {
        Missile missile;
        if (getDirection() == Directions.LEFT) {
            missile = new Missile(getX() - 20, getY() + getHeight()/2 , missileImagePath,
                    getDirection());

        } else {
            missile = new Missile( getX() + getWidth(), getY() + getHeight()/2, missileImagePath,
                    getDirection());
        }
        missiles.add(missile);
    }


    public void handleKeyRelease(Keys key) {
        switch(key) {
            case LEFT:
                setLeftVelocity(0);
                break;
            case RIGHT:
                setRightVelocity(0);
                break;
        }
    }

    public ArrayList<Missile> getMissiles() {
        return missiles;
    }

    @Override
    public void applyGravity() {
        if ( getVerticalVelocity() < getTerminalVelocity()) {
            float verticalVelocity = getVerticalVelocity() + new Float(0.1);
            setVerticalVelocity(verticalVelocity);
        }
    }

    @Override
    public void move() {
        if (!onFloor) {
            applyGravity();
        }
        setX( getX() + getHorizontalVelocity() );
        setY( getY() + Math.round(getVerticalVelocity()) );
        setDirection(getDirection());
    }


}
