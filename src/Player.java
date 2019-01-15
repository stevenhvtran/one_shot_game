import javax.swing.*;
import java.awt.event.ActionEvent;

public class Player extends PhysicsSprite {
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private String missileImagePath;
    private Missile missile;
    private boolean onFloor = false;
    private int initialX, initialY;
    
    private static final String JUMP = "jump";
    private static final String MOVE_LEFT = "left";
    private static final String MOVE_RIGHT = "right";
    private static final String STOP_LEFT = "stop left";
    private static final String STOP_RIGHT = "stop right";
    private static final String SHOOT = "shoot";

    private String name;
    private int score = 0;

    public Player(String name, int x, int y, String imagePath, String missileImagePath, String leftKey, String rightKey,
                  String jumpKey, String shootKey) {
        super(x, y, imagePath, 3, 5);
        this.name = name;
        initialX = x;
        initialY = y;

        bindKeys(leftKey, rightKey, jumpKey, shootKey);

        this.missileImagePath = missileImagePath;
    }

    public enum Keys {
        LEFT, RIGHT, JUMP, SHOOT
    }

    public void incrementScore() {
        score += 1;
        System.out.println(name + "'s Score is: " + score);
    }
    
    private void bindKeys(String leftKey, String rightKey, String jumpKey, String shootKey) {

        // Bind movement keys
        getInputMap(IFW).put(KeyStroke.getKeyStroke(jumpKey), JUMP);
        getInputMap(IFW).put(KeyStroke.getKeyStroke(leftKey), MOVE_LEFT);
        getInputMap(IFW).put(KeyStroke.getKeyStroke(rightKey), MOVE_RIGHT);
        getActionMap().put(JUMP, new MoveAction(Keys.JUMP));
        getActionMap().put(MOVE_LEFT, new MoveAction(Keys.LEFT));
        getActionMap().put(MOVE_RIGHT, new MoveAction(Keys.RIGHT));

        // Bind release of movement keys
        getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + leftKey), STOP_LEFT);
        getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + rightKey), STOP_RIGHT);
        getActionMap().put(STOP_LEFT, new StopAction(Keys.LEFT));
        getActionMap().put(STOP_RIGHT, new StopAction(Keys.RIGHT));

        // Bind shoot key
        getInputMap(IFW).put(KeyStroke.getKeyStroke(shootKey), SHOOT);
        getActionMap().put(SHOOT, new MoveAction(Keys.SHOOT));
    }

    private class MoveAction extends AbstractAction {
        private Player.Keys key;

        public MoveAction(Player.Keys key) {
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            handleKeyPress(key);
        }
    }

    private class StopAction extends AbstractAction {
        private Player.Keys key;

        public StopAction(Player.Keys key) {
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            handleKeyRelease(key);
        }
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
                    setVerticalVelocity(-5);
                }
                onFloor = false;
                break;
            case SHOOT:
                if (missile == null) {
                    shootMissile();
                }
                break;
        }
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

    public void setOnFloor() {
        setVerticalVelocity(0);
        setY(getY() - 1);
        onFloor = true;
    }

    public void resetSpawn() {
        setX(initialX);
        setY(initialY);
    }

    private void shootMissile() {
        if (getDirection() == Directions.LEFT) {
            missile = new Missile(getX() - 20, getY() + getHeight()/3 , missileImagePath,
                    getDirection());

        } else {
            missile = new Missile( getX() + getWidth(), getY() + getHeight()/3, missileImagePath,
                    getDirection());
        }
    }

    public void deleteMissile() {
        missile = null;
    }

    public Missile getMissile() {
        return missile;
    }

    @Override
    public void applyGravity() {
        if ( getVerticalVelocity() < getTerminalVelocity()) {
            float verticalVelocity = getVerticalVelocity() + (float) 0.05;
            setVerticalVelocity(verticalVelocity);
        }
    }

    @Override
    public void move() {
        applyGravity();
        setX( getX() + getHorizontalVelocity() );
        setY( getY() + Math.round(getVerticalVelocity()) );
        setDirection(getDirection());
    }

}
