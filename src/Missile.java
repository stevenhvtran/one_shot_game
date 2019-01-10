public class Missile extends PhysicsSprite {
    private Directions direction;

    public Missile(int x, int y, String imagePath, Directions direction) {
        super(x, y, imagePath, 5, 0);
        this.direction = direction;
        initVelocity();
    }

    private void initVelocity() {
        if (direction == Directions.LEFT) {
            setLeftVelocity(getMaxHorizontalVelocity());
        } else {
            setRightVelocity(getMaxHorizontalVelocity());
        }
    }
}
