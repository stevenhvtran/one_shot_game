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
    private Font f = new Font("Dialog", Font.BOLD, 4);

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
        platforms.add(new Platform(830, 550, "small_platform.png"));
    }

    private void initPlayers() {

        createPlayer("Eddie", 250, 800, "player_red.png",
                "missile_red.png",
                "G", "J", "Y", "SPACE");

        createPlayer("Stev", 1580, 800, "player_blue.png",
                "missile_blue.png",
                "LEFT", "RIGHT", "UP", "SLASH");
//
//        createPlayer("Ahbi", 950, 350, "player_green.png",
//                "missile_green.png",
//                "A", "D", "W", "Q");

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

        g.setFont(f);
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
            drawCooldown(g2d, player);

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

    private void drawCooldown(Graphics2D g2d, Player player) {
        int cooldownTicks = player.getMissileCooldown();
        String cooldown = "■■■■■■■■■■■■".substring(0, (int) Math.ceil((double) cooldownTicks / 25));
        g2d.drawString(cooldown, player.getX(), player.getY() - 5);
    }
}
