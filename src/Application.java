import java.awt.EventQueue;
import javax.swing.JFrame;

public class Application extends JFrame {
    private Application() { InitUI(); }

    private void InitUI() {

        add(new Game());

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("The Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Application app = new Application();
            app.setVisible(true);
        });
    }
}
