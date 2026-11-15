import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Responsive library search screen built with nested layout managers. */
public class LibrarySearchScreen extends JFrame {
    private final JTextField searchField = new JTextField(24);
    private final JComboBox<String> category =
            new JComboBox<>(new String[]{"All", "Books", "Journals", "Theses"});
    private final JTextArea results = new JTextArea();

    public LibrarySearchScreen() {
        super("Campus Library Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel("Campus Library Search");
        title.setBorder(BorderFactory.createEmptyBorder(10, 12, 4, 12));
        add(title, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Search:"));
        searchRow.add(searchField);
        searchRow.add(category);
        JButton searchButton = new JButton("Search");
        searchRow.add(searchButton);

        results.setEditable(false);
        results.setText("Search results will appear here.");
        JScrollPane resultScroll = new JScrollPane(results);
        resultScroll.setPreferredSize(new Dimension(600, 320));

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton reserve = new JButton("Reserve");
        bottomRow.add(prev);
        bottomRow.add(next);
        bottomRow.add(reserve);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        center.add(searchRow, BorderLayout.NORTH);
        center.add(resultScroll, BorderLayout.CENTER);
        center.add(bottomRow, BorderLayout.SOUTH);

        JLabel status = new JLabel("Ready");
        status.setBorder(BorderFactory.createEmptyBorder(4, 12, 8, 12));

        add(center, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                status.setText("Enter a search term.");
                return;
            }
            results.setText("Demo search\nQuery: " + query
                    + "\nCategory: " + category.getSelectedItem()
                    + "\n\nConnect this screen to the library catalogue service "
                    + "to retrieve live records.");
            status.setText("Search completed (demonstration data).");
        });

        prev.addActionListener(e -> status.setText("Previous page requested."));
        next.addActionListener(e -> status.setText("Next page requested."));
        reserve.addActionListener(e -> status.setText("Select a result before reserving."));

        setMinimumSize(new Dimension(480, 340));
        setSize(760, 520);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchScreen().setVisible(true));
    }
}
