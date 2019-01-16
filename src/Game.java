import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener {
    private final int DELAY = 1;
    private ArrayList<Player> players = new ArrayList<>();
    private CollisionHandler collisionHandler;
    private ArrayList<Platform> platforms = new ArrayList<>();

    public Game () {
        initGame();
    }

    private void initGame() {

        setFocusable(true);
        setBackground(Color.WHITE);

        initPlayers();
        initPlatform();
        collisionHandler = new CollisionHandler(players, platforms);

        Timer timer = new Timer(DELAY, this);
        timer.start();

    }

    private void initPlatform() {
        platforms.add(new Platform(0, 950, "floor.png"));
//        platforms.add(new Platform(400, 780, "short_floor.png"));

        platforms.add(new Platform(0, 0, "wall.png"));
        platforms.add(new Platform(1880, 0, "wall.png"));

        platforms.add(new Platform(220, 700, "small_platform.png"));
        platforms.add(new Platform(1450, 700, "small_platform.png"));
        platforms.add(new Platform(620, 830, "big_platform.png"));
    }

    private void initPlayers() {

        createPlayer("Player 1", 250, 800, "player_red.png",
                "missile_red.png",
                "A", "D", "W", "SPACE");

        createPlayer("Player 2", 1580, 800, "player_blue.png",
                "missile_blue.png",
                "LEFT", "RIGHT", "UP", "SLASH");

//        createPlayer("Player 2", 900, 800, "player_green.png",
//                "missile_green.png",
//                "F", "H", "T", "J");

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
        collisionHandler.handleCollisions();
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

    private void drawComponents(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Platform Platform : platforms) {
            g2d.drawImage(Platform.getImage(), Platform.getX(), Platform.getY(), this);
        }

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
