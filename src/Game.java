import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener {
    private final int DELAY = 1;
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private ArrayList<Player> players = new ArrayList<>();

    private static final String JUMP = "jump";
    private static final String MOVE_LEFT = "left";
    private static final String MOVE_RIGHT = "right";
    private static final String STOP_LEFT = "stop left";
    private static final String STOP_RIGHT = "stop right";
    private static final String SHOOT = "shoot";

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

        createPlayer(20, 20, "src/resources/player_red.png",
                "src/resources/missile_red.png",
                "A", "D", "W", "G");
        createPlayer(1000, 20, "src/resources/player_blue.png",
                "src/resources/missile_blue.png",
                "LEFT", "RIGHT", "UP", "SLASH");

    }

    private void createPlayer(int x, int y, String imagePath, String missileImagePath, String leftKey, String rightKey,
                              String jumpKey, String shootKey) {
        Player player = new Player(x, y, imagePath, missileImagePath);
        player.getInputMap(IFW).put(KeyStroke.getKeyStroke(jumpKey), JUMP);
        player.getInputMap(IFW).put(KeyStroke.getKeyStroke(leftKey), MOVE_LEFT);
        player.getInputMap(IFW).put(KeyStroke.getKeyStroke(rightKey), MOVE_RIGHT);

        player.getActionMap().put(JUMP, new MoveAction(player, Player.Keys.JUMP));
        player.getActionMap().put(MOVE_LEFT, new MoveAction(player, Player.Keys.LEFT));
        player.getActionMap().put(MOVE_RIGHT, new MoveAction(player, Player.Keys.RIGHT));


        player.getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + leftKey), STOP_LEFT);
        player.getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + rightKey), STOP_RIGHT);

        player.getActionMap().put(STOP_LEFT, new StopAction(player, Player.Keys.LEFT));
        player.getActionMap().put(STOP_RIGHT, new StopAction(player, Player.Keys.RIGHT));

        player.getInputMap(IFW).put(KeyStroke.getKeyStroke(shootKey), SHOOT);
        player.getActionMap().put(SHOOT, new MoveAction(player, Player.Keys.SHOOT));

        players.add(player);
        add(player);  // add JComponent to JPanel
    }

    private class MoveAction extends AbstractAction {
        private Player player;
        private Player.Keys key;

        public MoveAction(Player player, Player.Keys key) {
            this.player = player;
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            player.handleKeyPress(key);
        }
    }

    private class StopAction extends AbstractAction {
        private Player player;
        private Player.Keys key;

        public StopAction(Player player, Player.Keys key) {

            this.player = player;
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            player.handleKeyRelease(key);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
            for (Missile missile : player.getMissiles()) {
                // check missile visibility and delete if it is visible
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
//            if (player.getBounds().intersects())
        }
    }

    private void drawComponents(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(floor.getImage(), floor.getX(), floor.getY(), this);

        for (Player player: players) {
            drawPlayer(g2d, player);

            for (Missile missile: player.getMissiles()) {
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
