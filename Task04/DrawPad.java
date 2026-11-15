import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/** Click positions are stored and redrawn whenever AWT repaints the frame. */
public class DrawPad extends Frame {
    private final List<Point> points = new ArrayList<>();
    private final Label status = new Label("Clicks: 0");

    public DrawPad() {
        super("Draw Pad");
        add(status, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                points.add(new Point(e.getX(), e.getY()));
                status.setText("Clicks: " + points.size());
                repaint();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(500, 360);
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Point point : points) {
            g.fillOval(point.x - 3, point.y - 3, 6, 6);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new DrawPad().setVisible(true));
    }
}
