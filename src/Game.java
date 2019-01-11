import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener {
    private final int DELAY = 1;
    private ArrayList<Player> players = new ArrayList<>();


    private Floor floor;

    public Game () {
        initGame();
    }

    private void initGame() {

        setFocusable(true);
        setBackground(Color.WHITE);

        initPlayers();
        initFloor();

        Timer timer = new Timer(DELAY, this);
        timer.start();

    }

    private void initFloor() {
        floor = new Floor(0, 950);
    }

    private void initPlayers() {

        createPlayer("Player 1", 50, 800, "src/resources/player_red.png",
                "src/resources/missile_red.png",
                "A", "D", "W", "F");

        createPlayer("Player 2", 1850, 800, "src/resources/player_blue.png",
                "src/resources/missile_blue.png",
                "LEFT", "RIGHT", "UP", "SLASH");

//        createPlayer("Player 2", 900, 800, "src/resources/player_green.png",
//                "src/resources/missile_green.png",
//                "G", "J", "Y", "K");

    }

    private void createPlayer(String playerName, int x, int y, String imagePath, String missileImagePath,
                              String leftKey, String rightKey, String jumpKey, String shootKey) {

        Player player = new Player(playerName, x, y, imagePath, missileImagePath, leftKey, rightKey, jumpKey, shootKey);
        players.add(player);
        add(player);  // add JComponent to JPanel
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // This block runs every tick
        updateMovement();
        checkCollisions();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawComponents(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void updateMovement() {
        for (Player player : players) {
            player.move();
            Missile missile = player.getMissile();
            if (missile != null) {
                missile.move();
            }
        }
    }

    private void checkCollisions() {
        for (Player player : players) {

            // Check player and floor collisions
            if (player.getBounds().intersects(floor.getBounds())) {
                player.setOnFloor();
            }

            // Reset missile if it exits map
            if (player.getMissile() != null) {
                if (player.getMissile().getX() < 0 || player.getMissile().getX() > 1900) {
                    player.deleteMissile();
                }
            }

            // Check missile collisions
            for (Player player2 : players) {
                if (player != player2) {
                    if (player2.getMissile() != null) {
                        if (player.getBounds().intersects(player2.getMissile().getBounds())) {
                            player2.deleteMissile();
                            player2.incrementScore();
                            player.resetSpawn();
                        }
                    }
                }
            }

        }
    }

    private void drawComponents(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(floor.getImage(), floor.getX(), floor.getY(), this);

        for (Player player: players) {
            drawPlayer(g2d, player);

            Missile missile = player.getMissile();
            if (missile != null) {
                drawMissile(g2d, missile);
            }
        }
    }

    private void drawMissile(Graphics2D g2d, Missile missile) {
        if (missile.getDirection() == PhysicsSprite.Directions.LEFT) {
            g2d.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
        } else {
            g2d.drawImage(missile.getImage(), missile.getX() + missile.getWidth(), missile.getY(),
                    -missile.getWidth(), missile.getHeight(), this);
        }
    }

    private void drawPlayer(Graphics2D g2d, Player player) {
        if (player.getDirection() == PhysicsSprite.Directions.LEFT) {
            g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
        } else {
            g2d.drawImage(player.getImage(), player.getX() + player.getWidth(), player.getY(),
                    -player.getWidth(), player.getHeight(), this);
        }
    }
}
