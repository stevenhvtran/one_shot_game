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
            for (Platform platform : platforms) {
                if (player.getBounds().intersects(platform.getBounds())) {
                    if (player.intersectsPlatform(Player.Direction.FULL_LEFT, platform)) {
                        int delta = player.getX() + player.getWidth() - platform.getX();
                        player.setX(player.getX() - delta);
                    } else if (player.intersectsPlatform(Player.Direction.FULL_RIGHT, platform)) {
                        int delta = platform.getX() + platform.getWidth() - player.getX();
                        player.setX(player.getX() + delta);
                    } else if (player.intersectsPlatform(Player.Direction.TOP_LEFT, platform)) {
                        int deltaX = player.getX() + player.getWidth() - platform.getX();
                        int deltaY = player.getY() + player.getHeight() - platform.getY();
                        if (player.getOnFloor()) {
                            player.setY(player.getY() - deltaY);
                            player.setVerticalVelocity(0);
                            player.setOnFloor();
                        } else if (deltaX > deltaY) {
                            player.setX(player.getX() - deltaX);
                        } else {
                            player.setY(player.getY() - deltaY);
                            player.setVerticalVelocity(0);
                            player.setOnFloor();
                        }
                    } else if (player.intersectsPlatform(Player.Direction.TOP_RIGHT, platform)) {
                        int deltaX = platform.getX() + platform.getWidth() - player.getX();
                        int deltaY = player.getY() + player.getHeight() - platform.getY();
                        if (player.getOnFloor()) {
                            player.setY(player.getY() - deltaY);
                            player.setVerticalVelocity(0);
                            player.setOnFloor();
                        } else if (deltaX > deltaY) {
                            player.setX(player.getX() + deltaX);
                        } else {
                            player.setY(player.getY() - deltaY);
                            player.setVerticalVelocity(0);
                            player.setOnFloor();
                        }
                    } else if (player.intersectsPlatform(Player.Direction.BOTTOM_LEFT, platform)) {
                        int deltaX = player.getX() + player.getWidth() - platform.getX();
                        int deltaY = platform.getY() + platform.getHeight() - player.getY();
                        if (deltaX > deltaY) {
                            player.setX(player.getX() - deltaX);
                        } else {
                            player.setY(player.getY() + deltaY);
                            player.setVerticalVelocity(0);
                        }
                    } else if (player.intersectsPlatform(Player.Direction.BOTTOM_RIGHT, platform)) {
                        int deltaX = platform.getX() + platform.getWidth() - player.getX();
                        int deltaY = platform.getY() + platform.getHeight() - player.getY();
                        if (deltaX > deltaY) {
                            player.setX(player.getX() + deltaX);
                        } else {
                            player.setY(player.getY() + deltaY);
                            player.setVerticalVelocity(0);
                        }
                    } else if (player.intersectsPlatform(Player.Direction.LEFT, platform)) {
                        int delta = player.getX() + player.getWidth() - platform.getX();
                        player.setX(player.getX() - delta);
                    } else if (player.intersectsPlatform(Player.Direction.RIGHT, platform)) {
                        int delta = platform.getX() + platform.getWidth() - player.getX();
                        player.setX(player.getX() + delta);
                    } else if (player.intersectsPlatform(Player.Direction.TOP, platform)) {
                        int delta = player.getY() + player.getHeight() - platform.getY();
                        player.setY(player.getY() - delta);
                        player.setVerticalVelocity(0);
                        player.setOnFloor();
                    } else if (player.intersectsPlatform(Player.Direction.BOTTOM, platform)) {
                        int delta = platform.getY() + platform.getHeight() - player.getY();
                        player.setY(player.getY() + delta);
                        player.setVerticalVelocity(0);
                    }
                }
            }
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
