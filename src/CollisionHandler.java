import java.util.ArrayList;

public class CollisionHandler {
    private ArrayList<Player> players;
    private ArrayList<Platform> platforms;

    public CollisionHandler(ArrayList<Player> players, ArrayList<Platform> platforms) {
        this.players = players;
        this.platforms = platforms;
    }

    public void handleCollisions() {
        handlePlayerPlatformCollisions();
        handleMissilePlatformCollisions();
        handlePlayerMissileCollisions();
    }

    private void handlePlayerPlatformCollisions() {
        for (Player player : players) {
            int horizontalVelocity = player.getHorizontalVelocity();
            int verticalVelocity = (int) Math.ceil(player.getVerticalVelocity());
            int deltaX, deltaY;
            int timeX, timeY;

            for (Platform platform : platforms) {
                switch(player.getCollisionDirection(platform)) {
                    case TOP_LEFT:
                        deltaX = getDelta(Player.Direction.LEFT, player, platform);
                        deltaY = getDelta(Player.Direction.TOP, player, platform);
                        if (horizontalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.TOP, player, deltaY);
                            break;
                        } else if (verticalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.LEFT, player, deltaX);
                            break;
                        }

                        // Resolve collision time based
                        timeX = deltaX / horizontalVelocity;
                        timeY = deltaY / verticalVelocity;
                        if (timeX < timeY) {
                            resolvePlayerCollision(Player.Direction.LEFT, player, deltaX);
                        } else {
                            resolvePlayerCollision(Player.Direction.TOP, player, deltaY);
                        }
                        break;
                    case TOP_RIGHT:
                        deltaX = getDelta(Player.Direction.RIGHT, player, platform);
                        deltaY = getDelta(Player.Direction.TOP, player, platform);
                        if (-horizontalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.TOP, player, deltaY);
                            break;
                        } else if (verticalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.RIGHT, player, deltaX);
                            break;
                        }

                        // Resolve collision time based
                        timeX = deltaX / -horizontalVelocity;
                        timeY = deltaY / verticalVelocity;
                        if (timeX < timeY) {
                            resolvePlayerCollision(Player.Direction.RIGHT, player, deltaX);
                        } else {
                            resolvePlayerCollision(Player.Direction.TOP, player, deltaY);
                        }
                        break;
                    case BOTTOM_LEFT:
                        deltaX = getDelta(Player.Direction.LEFT, player, platform);
                        deltaY = getDelta(Player.Direction.BOTTOM, player, platform);
                        if (horizontalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.BOTTOM, player, deltaY);
                            break;
                        } else if (-verticalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.LEFT, player, deltaX);
                            break;
                        }

                        // Resolve collision time based
                        timeX = deltaX / horizontalVelocity;
                        timeY = deltaY / -verticalVelocity;
                        if (timeX < timeY) {
                            resolvePlayerCollision(Player.Direction.LEFT, player, deltaX);
                        } else {
                            resolvePlayerCollision(Player.Direction.BOTTOM, player, deltaY);
                        }
                        break;
                    case BOTTOM_RIGHT:
                        deltaX = getDelta(Player.Direction.RIGHT, player, platform);
                        deltaY = getDelta(Player.Direction.BOTTOM, player, platform);
                        if (-horizontalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.BOTTOM, player, deltaY);
                            break;
                        } else if (-verticalVelocity <= 0) {
                            resolvePlayerCollision(Player.Direction.RIGHT, player, deltaX);
                            break;
                        }

                        // Resolve collision time based
                        timeX = deltaX / -horizontalVelocity;
                        timeY = deltaY / -verticalVelocity;
                        if (timeX < timeY) {
                            resolvePlayerCollision(Player.Direction.RIGHT, player, deltaX);
                        } else {
                            resolvePlayerCollision(Player.Direction.BOTTOM, player, deltaY);
                        }
                        break;
                    case LEFT:
                        deltaX = getDelta(Player.Direction.LEFT, player, platform);
                        resolvePlayerCollision(Player.Direction.LEFT, player, deltaX);
                        break;
                    case RIGHT:
                        deltaX = getDelta(Player.Direction.RIGHT, player, platform);
                        resolvePlayerCollision(Player.Direction.RIGHT, player, deltaX);
                        break;
                    case TOP:
                        deltaY = getDelta(Player.Direction.TOP, player, platform);
                        resolvePlayerCollision(Player.Direction.TOP, player, deltaY);
                        break;
                    case BOTTOM:
                        deltaY = getDelta(Player.Direction.BOTTOM, player, platform);
                        resolvePlayerCollision(Player.Direction.BOTTOM, player, deltaY);
                        break;
                    case NONE:
                        break;
                }
            }
        }
    }

    private int getDelta(Player.Direction direction, Player player, Platform platform) {
        switch(direction) {
            case LEFT:
                return player.getX() + player.getWidth() - platform.getX();
            case RIGHT:
                return platform.getX() + platform.getWidth() - player.getX();
            case TOP:
                return player.getY() + player.getHeight() - platform.getY();
            case BOTTOM:
                return platform.getY() + platform.getHeight() - player.getY();
            default:
                return 0;
        }
    }

    private void resolvePlayerCollision(Player.Direction direction, Player player, int delta) {
        switch(direction) {
            case LEFT:
                player.setX(player.getX() - delta);
                break;
            case RIGHT:
                player.setX(player.getX() + delta);
                break;
            case TOP:
                if (player.getVerticalVelocity() >= 0) {
                    player.setY(player.getY() - delta);
                    player.setVerticalVelocity(0);
                    player.setOnFloor();
                }
                break;
            case BOTTOM:
                if (player.getVerticalVelocity() <= 0) {
                    player.setY(player.getY() + delta);
                    player.setVerticalVelocity(0);
                }
                break;
        }
    }

    private void handleMissilePlatformCollisions() {
        for (Player player : players) {
            Missile missile = player.getMissile();
            if (missile != null) {
                for (Platform platform : platforms) {
                    if (missile.getBounds().intersects(platform.getBounds())) {
                        player.deleteMissile();
                    }
                }
            }
        }
    }

    private void handlePlayerMissileCollisions() {
        // Check if player got shot by other player
        for (Player player : players) {
            for (Player otherPlayer : players) {
                if (player != otherPlayer) {
                    if (otherPlayer.getMissile() != null) {
                        if (player.getBounds().intersects(otherPlayer.getMissile().getBounds())) {
                            otherPlayer.deleteMissile();
                            otherPlayer.incrementScore();
                            player.resetSpawn();
                        }
                    }
                }
            }
        }

    }
}
